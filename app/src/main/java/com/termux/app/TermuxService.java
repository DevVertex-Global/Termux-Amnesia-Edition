package com.termux.app;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.termux.R;
import com.termux.app.event.SystemEventReceiver;
import com.termux.app.terminal.TermuxTerminalSessionActivityClient;
import com.termux.app.terminal.TermuxTerminalSessionServiceClient;
import com.termux.shared.termux.plugins.TermuxPluginUtils;
import com.termux.shared.data.IntentUtils;
import com.termux.shared.net.uri.UriUtils;
import com.termux.shared.errors.Errno;
import com.termux.shared.shell.ShellUtils;
import com.termux.shared.shell.command.runner.app.AppShell;
import com.termux.shared.termux.settings.properties.TermuxAppSharedProperties;
import com.termux.shared.termux.shell.command.environment.TermuxShellEnvironment;
import com.termux.shared.termux.shell.TermuxShellUtils;
import com.termux.shared.termux.TermuxConstants;
import com.termux.shared.termux.TermuxConstants.TERMUX_APP.TERMUX_ACTIVITY;
import com.termux.shared.termux.TermuxConstants.TERMUX_APP.TERMUX_SERVICE;
import com.termux.shared.termux.settings.preferences.TermuxAppSharedPreferences;
import com.termux.shared.termux.shell.TermuxShellManager;
import com.termux.shared.termux.shell.command.runner.terminal.TermuxSession;
import com.termux.shared.termux.terminal.TermuxTerminalSessionClientBase;
import com.termux.shared.logger.Logger;
import com.termux.shared.notification.NotificationUtils;
import com.termux.shared.android.PermissionUtils;
import com.termux.shared.data.DataUtils;
import com.termux.shared.shell.command.ExecutionCommand;
import com.termux.shared.shell.command.ExecutionCommand.Runner;
import com.termux.shared.shell.command.ExecutionCommand.ShellCreateMode;
import com.termux.terminal.TerminalEmulator;
import com.termux.terminal.TerminalSession;
import com.termux.terminal.TerminalSessionClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class TermuxService extends Service implements AppShell.AppShellClient, TermuxSession.TermuxSessionClient {

    class LocalBinder extends Binder {
        public final TermuxService service = TermuxService.this;
    }

    private final IBinder mBinder = new LocalBinder();
    private final Handler mHandler = new Handler();

    private TermuxTerminalSessionActivityClient mTermuxTerminalSessionActivityClient;
    private final TermuxTerminalSessionServiceClient mTermuxTerminalSessionServiceClient = new TermuxTerminalSessionServiceClient(this);

    private TermuxAppSharedProperties mProperties;
    private TermuxShellManager mShellManager;

    private PowerManager.WakeLock mWakeLock;
    private WifiManager.WifiLock mWifiLock;

    boolean mWantsToStop = false;
    private static final String LOG_TAG = "TermuxService";

    @Override
    public void onCreate() {
        Logger.logVerbose(LOG_TAG, "onCreate");
        mProperties = TermuxAppSharedProperties.getProperties();
        mShellManager = TermuxShellManager.getShellManager();
        runStartForeground();
        SystemEventReceiver.registerPackageUpdateEvents(this);
    }

    @SuppressLint("Wakelock")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.logDebug(LOG_TAG, "onStartCommand");
        runStartForeground();

        String action = null;
        if (intent != null) {
            action = intent.getAction();
        }

        if (action != null) {
            switch (action) {
                case TERMUX_SERVICE.ACTION_STOP_SERVICE:
                    actionStopService();
                    break;
                case TERMUX_SERVICE.ACTION_WAKE_LOCK:
                    actionAcquireWakeLock();
                    break;
                case TERMUX_SERVICE.ACTION_WAKE_UNLOCK:
                    actionReleaseWakeLock(true);
                    break;
                case TERMUX_SERVICE.ACTION_SERVICE_EXECUTE:
                    actionServiceExecute(intent);
                    break;
            }
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Logger.logVerbose(LOG_TAG, "onDestroy");
        TermuxShellUtils.clearTermuxTMPDIR(true);
        actionReleaseWakeLock(false);
        if (!mWantsToStop)
            killAllTermuxExecutionCommands();
        TermuxShellManager.onAppExit(this);
        SystemEventReceiver.unregisterPackageUpdateEvents(this);
        runStopForeground();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (mTermuxTerminalSessionActivityClient != null)
            unsetTermuxTerminalSessionClient();
        return false;
    }

    private void runStartForeground() {
        setupNotificationChannel();
        startForeground(TermuxConstants.TERMUX_APP_NOTIFICATION_ID, buildNotification());
    }

    private void runStopForeground() {
        stopForeground(true);
    }

    private void requestStopService() {
        runStopForeground();
        stopSelf();
    }

    private void actionStopService() {
        mWantsToStop = true;
        killAllTermuxExecutionCommands();
        requestStopService();
    }

    private synchronized void killAllTermuxExecutionCommands() {
        boolean processResult;
        List<TermuxSession> termuxSessions = new ArrayList<>(mShellManager.mTermuxSessions);
        List<AppShell> termuxTasks = new ArrayList<>(mShellManager.mTermuxTasks);
        List<ExecutionCommand> pendingPluginExecutionCommands = new ArrayList<>(mShellManager.mPendingPluginExecutionCommands);

        for (int i = 0; i < termuxSessions.size(); i++) {
            ExecutionCommand executionCommand = termuxSessions.get(i).getExecutionCommand();
            processResult = mWantsToStop || executionCommand.isPluginExecutionCommandWithPendingResult();
            termuxSessions.get(i).killIfExecuting(this, processResult);
            if (!processResult)
                mShellManager.mTermuxSessions.remove(termuxSessions.get(i));
        }

        for (int i = 0; i < termuxTasks.size(); i++) {
            ExecutionCommand executionCommand = termuxTasks.get(i).getExecutionCommand();
            if (executionCommand.isPluginExecutionCommandWithPendingResult())
                termuxTasks.get(i).killIfExecuting(this, true);
            else
                mShellManager.mTermuxTasks.remove(termuxTasks.get(i));
        }

        for (int i = 0; i < pendingPluginExecutionCommands.size(); i++) {
            ExecutionCommand executionCommand = pendingPluginExecutionCommands.get(i);
            if (!executionCommand.shouldNotProcessResults() && executionCommand.isPluginExecutionCommandWithPendingResult()) {
                if (executionCommand.setStateFailed(Errno.ERRNO_CANCELLED.getCode(), this.getString(com.termux.shared.R.string.error_execution_cancelled))) {
                    TermuxPluginUtils.processPluginExecutionCommandResult(this, LOG_TAG, executionCommand);
                }
            }
        }
    }

    @SuppressLint({"WakelockTimeout", "BatteryLife"})
    private void actionAcquireWakeLock() {
        if (mWakeLock != null) return;
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TermuxConstants.TERMUX_APP_NAME.toLowerCase() + ":service-wakelock");
        mWakeLock.acquire();

        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mWifiLock = wm.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, TermuxConstants.TERMUX_APP_NAME.toLowerCase());
        mWifiLock.acquire();

        if (!PermissionUtils.checkIfBatteryOptimizationsDisabled(this)) {
            PermissionUtils.requestDisableBatteryOptimizations(this);
        }
        updateNotification();
    }

    private void actionReleaseWakeLock(boolean updateNotification) {
        if (mWakeLock == null && mWifiLock == null) return;
        if (mWakeLock != null) {
            mWakeLock.release();
            mWakeLock = null;
        }
        if (mWifiLock != null) {
            mWifiLock.release();
            mWifiLock = null;
        }
        if (updateNotification) updateNotification();
    }

    private void actionServiceExecute(Intent intent) {
        if (intent == null) return;
        ExecutionCommand executionCommand = new ExecutionCommand(TermuxShellManager.getNextShellId());
        executionCommand.executableUri = intent.getData();
        executionCommand.isPluginExecutionCommand = true;
        executionCommand.runner = IntentUtils.getStringExtraIfSet(intent, TERMUX_SERVICE.EXTRA_RUNNER,
            (intent.getBooleanExtra(TERMUX_SERVICE.EXTRA_BACKGROUND, false) ? Runner.APP_SHELL.getName() : Runner.TERMINAL_SESSION.getName()));
        
        if (Runner.runnerOf(executionCommand.runner) == null) {
            String errmsg = this.getString(R.string.error_termux_service_invalid_execution_command_runner, executionCommand.runner);
            executionCommand.setStateFailed(Errno.ERRNO_FAILED.getCode(), errmsg);
            TermuxPluginUtils.processPluginExecutionCommandError(this, LOG_TAG, executionCommand, false);
            return;
        }

        if (executionCommand.executableUri != null) {
            executionCommand.executable = UriUtils.getUriFilePathWithFragment(executionCommand.executableUri);
            executionCommand.arguments = IntentUtils.getStringArrayExtraIfSet(intent, TERMUX_SERVICE.EXTRA_ARGUMENTS, null);
        }

        mShellManager.mPendingPluginExecutionCommands.add(executionCommand);

        if (Runner.APP_SHELL.equalsRunner(executionCommand.runner))
            executeTermuxTaskCommand(executionCommand);
        else if (Runner.TERMINAL_SESSION.equalsRunner(executionCommand.runner))
            executeTermuxSessionCommand(executionCommand);
    }

    private void executeTermuxTaskCommand(ExecutionCommand executionCommand) {
        if (executionCommand == null) return;
        AppShell newTermuxTask = createTermuxTask(executionCommand);
    }

    @Nullable
    public synchronized AppShell createTermuxTask(ExecutionCommand executionCommand) {
        if (executionCommand == null) return null;
        executionCommand.setShellCommandShellEnvironment = true;
        AppShell newTermuxTask = AppShell.execute(this, executionCommand, this, new TermuxShellEnvironment(), null, false);
        if (newTermuxTask != null) {
            mShellManager.mTermuxTasks.add(newTermuxTask);
            if (executionCommand.isPluginExecutionCommand)
                mShellManager.mPendingPluginExecutionCommands.remove(executionCommand);
            updateNotification();
        }
        return newTermuxTask;
    }

    @Override
    public void onAppShellExited(final AppShell termuxTask) {
        mHandler.post(() -> {
            if (termuxTask != null) {
                ExecutionCommand executionCommand = termuxTask.getExecutionCommand();
                if (executionCommand != null && executionCommand.isPluginExecutionCommand)
                    TermuxPluginUtils.processPluginExecutionCommandResult(this, LOG_TAG, executionCommand);
                mShellManager.mTermuxTasks.remove(termuxTask);
            }
            updateNotification();
        });
    }

    private void executeTermuxSessionCommand(ExecutionCommand executionCommand) {
        if (executionCommand == null) return;
        TermuxSession newTermuxSession = createTermuxSession(executionCommand);
        if (newTermuxSession == null) return;
        handleSessionAction(DataUtils.getIntFromString(executionCommand.sessionAction,
            TERMUX_SERVICE.VALUE_EXTRA_SESSION_ACTION_SWITCH_TO_NEW_SESSION_AND_OPEN_ACTIVITY),
            newTermuxSession.getTerminalSession());
    }

    @Nullable
    public synchronized TermuxSession createTermuxSession(ExecutionCommand executionCommand) {
        if (executionCommand == null) return null;
        executionCommand.setShellCommandShellEnvironment = true;
        executionCommand.terminalTranscriptRows = mProperties.getTerminalTranscriptRows();

        TermuxSession newTermuxSession = TermuxSession.execute(this, executionCommand, getTermuxTerminalSessionClient(),
            this, new TermuxShellEnvironment(), null, executionCommand.isPluginExecutionCommand);
        
        if (newTermuxSession == null) return null;

        mShellManager.mTermuxSessions.add(newTermuxSession);
        if (executionCommand.isPluginExecutionCommand)
            mShellManager.mPendingPluginExecutionCommands.remove(executionCommand);

        if (mTermuxTerminalSessionActivityClient != null)
            mTermuxTerminalSessionActivityClient.termuxSessionListNotifyUpdated();

        updateNotification();
        return newTermuxSession;
    }

    public synchronized int removeTermuxSession(TerminalSession sessionToRemove) {
        int index = getIndexOfSession(sessionToRemove);
        if (index >= 0)
            mShellManager.mTermuxSessions.get(index).finish();
        return index;
    }

    @Override
    public void onTermuxSessionExited(final TermuxSession termuxSession) {
        if (termuxSession != null) {
            ExecutionCommand executionCommand = termuxSession.getExecutionCommand();
            if (executionCommand != null && executionCommand.isPluginExecutionCommand)
                TermuxPluginUtils.processPluginExecutionCommandResult(this, LOG_TAG, executionCommand);
            mShellManager.mTermuxSessions.remove(termuxSession);
            if (mTermuxTerminalSessionActivityClient != null)
                mTermuxTerminalSessionActivityClient.termuxSessionListNotifyUpdated();
        }
        updateNotification();
    }

    private ShellCreateMode processShellCreateMode(@NonNull ExecutionCommand executionCommand) {
        return ShellCreateMode.ALWAYS;
    }

    private void handleSessionAction(int sessionAction, TerminalSession newTerminalSession) {
        switch (sessionAction) {
            case TERMUX_SERVICE.VALUE_EXTRA_SESSION_ACTION_SWITCH_TO_NEW_SESSION_AND_OPEN_ACTIVITY:
                setCurrentStoredTerminalSession(newTerminalSession);
                if (mTermuxTerminalSessionActivityClient != null)
                    mTermuxTerminalSessionActivityClient.setCurrentSession(newTerminalSession);
                startTermuxActivity();
                break;
            default:
                setCurrentStoredTerminalSession(newTerminalSession);
                if (mTermuxTerminalSessionActivityClient != null)
                    mTermuxTerminalSessionActivityClient.setCurrentSession(newTerminalSession);
                break;
        }
    }

    private void startTermuxActivity() {
        if (PermissionUtils.validateDisplayOverOtherAppsPermissionForPostAndroid10(this, true)) {
            TermuxActivity.startTermuxActivity(this);
        }
    }

    public synchronized TermuxTerminalSessionClientBase getTermuxTerminalSessionClient() {
        if (mTermuxTerminalSessionActivityClient != null)
            return mTermuxTerminalSessionActivityClient;
        else
            return mTermuxTerminalSessionServiceClient;
    }

    public synchronized void setTermuxTerminalSessionClient(TermuxTerminalSessionActivityClient termuxTerminalSessionActivityClient) {
        mTermuxTerminalSessionActivityClient = termuxTerminalSessionActivityClient;
        for (int i = 0; i < mShellManager.mTermuxSessions.size(); i++)
            mShellManager.mTermuxSessions.get(i).getTerminalSession().updateTerminalSessionClient(mTermuxTerminalSessionActivityClient);
    }

    public synchronized void unsetTermuxTerminalSessionClient() {
        for (int i = 0; i < mShellManager.mTermuxSessions.size(); i++)
            mShellManager.mTermuxSessions.get(i).getTerminalSession().updateTerminalSessionClient(mTermuxTerminalSessionServiceClient);
        mTermuxTerminalSessionActivityClient = null;
    }

    private Notification buildNotification() {
        Resources res = getResources();
        Intent notificationIntent = TermuxActivity.newInstance(this);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        int sessionCount = getTermuxSessionsSize();
        String notificationText = sessionCount + " session" + (sessionCount == 1 ? "" : "s");

        Notification.Builder builder = NotificationUtils.geNotificationBuilder(this,
            TermuxConstants.TERMUX_APP_NOTIFICATION_CHANNEL_ID, Notification.PRIORITY_LOW,
            TermuxConstants.TERMUX_APP_NAME, notificationText, null,
            contentIntent, null, NotificationUtils.NOTIFICATION_MODE_SILENT);
        if (builder == null) return null;

        builder.setShowWhen(false);
        builder.setSmallIcon(R.drawable.ic_service_notification);
        builder.setColor(0xFF607D8B);
        builder.setOngoing(true);
        return builder.build();
    }

    private void setupNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return;
        NotificationUtils.setupNotificationChannel(this, TermuxConstants.TERMUX_APP_NOTIFICATION_CHANNEL_ID,
            TermuxConstants.TERMUX_APP_NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
    }

    private synchronized void updateNotification() {
        if (mWakeLock == null && mShellManager.mTermuxSessions.isEmpty() && mShellManager.mTermuxTasks.isEmpty()) {
            requestStopService();
        } else {
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(TermuxConstants.TERMUX_APP_NOTIFICATION_ID, buildNotification());
        }
    }

    private void setCurrentStoredTerminalSession(TerminalSession terminalSession) {
        if (terminalSession == null) return;
        TermuxAppSharedPreferences preferences = TermuxAppSharedPreferences.build(this);
        if (preferences == null) return;
        preferences.setCurrentSession(terminalSession.mHandle);
    }

    public synchronized boolean isTermuxSessionsEmpty() {
        return mShellManager.mTermuxSessions.isEmpty();
    }

    public synchronized int getTermuxSessionsSize() {
        return mShellManager.mTermuxSessions.size();
    }

    public synchronized List<TermuxSession> getTermuxSessions() {
        return mShellManager.mTermuxSessions;
    }

    @Nullable
    public synchronized TermuxSession getTermuxSession(int index) {
        if (index >= 0 && index < mShellManager.mTermuxSessions.size())
            return mShellManager.mTermuxSessions.get(index);
        else
            return null;
    }

    @Nullable
    public synchronized TermuxSession getTermuxSessionForTerminalSession(TerminalSession terminalSession) {
        if (terminalSession == null) return null;
        for (int i = 0; i < mShellManager.mTermuxSessions.size(); i++) {
            if (mShellManager.mTermuxSessions.get(i).getTerminalSession().equals(terminalSession))
                return mShellManager.mTermuxSessions.get(i);
        }
        return null;
    }

    public synchronized TermuxSession getLastTermuxSession() {
        return mShellManager.mTermuxSessions.isEmpty() ? null : mShellManager.mTermuxSessions.get(mShellManager.mTermuxSessions.size() - 1);
    }

    public synchronized int getIndexOfSession(TerminalSession terminalSession) {
        if (terminalSession == null) return -1;
        for (int i = 0; i < mShellManager.mTermuxSessions.size(); i++) {
            if (mShellManager.mTermuxSessions.get(i).getTerminalSession().equals(terminalSession))
                return i;
        }
        return -1;
    }

    public synchronized TerminalSession getTerminalSessionForHandle(String sessionHandle) {
        TerminalSession terminalSession;
        for (int i = 0, len = mShellManager.mTermuxSessions.size(); i < len; i++) {
            terminalSession = mShellManager.mTermuxSessions.get(i).getTerminalSession();
            if (terminalSession.mHandle.equals(sessionHandle))
                return terminalSession;
        }
        return null;
    }

    public synchronized AppShell getTermuxTaskForShellName(String name) {
        if (DataUtils.isNullOrEmpty(name)) return null;
        AppShell appShell;
        for (int i = 0, len = mShellManager.mTermuxTasks.size(); i < len; i++) {
            appShell = mShellManager.mTermuxTasks.get(i);
            String shellName = appShell.getExecutionCommand().shellName;
            if (shellName != null && shellName.equals(name))
                return appShell;
        }
        return null;
    }

    public synchronized TermuxSession getTermuxSessionForShellName(String name) {
        if (DataUtils.isNullOrEmpty(name)) return null;
        TermuxSession termuxSession;
        for (int i = 0, len = mShellManager.mTermuxSessions.size(); i < len; i++) {
            termuxSession = mShellManager.mTermuxSessions.get(i);
            String shellName = termuxSession.getExecutionCommand().shellName;
            if (shellName != null && shellName.equals(name))
                return termuxSession;
        }
        return null;
    }

    public boolean wantsToStop() {
        return mWantsToStop;
    }

    /**
     * ==========================================
     * OTG AMNESIA EDITION - NATIVE COMMAND INTERCEPTOR
     * ==========================================
     * ইউজার যখন টার্মিনালে 'input -p' বা 'update -p' টাইপ করবে,
     * তখন এটি সরাসরি OtgSyncHelper এর মেথড ট্রিগার করবে।
     */
    public void handleOtgCommand(String command, TerminalSession session) {
        if (command == null || session == null) return;
        String trimmed = command.trim();

        if (trimmed.equals("input -p")) {
            File otgDir = new File("/storage/emulated/0/usb"); // ওটিজি মাউন্ট পাথ
            File localDir = new File(session.getCwd());

            session.write(">>> Executing Amnesia OTG Import...\n");
            OtgSyncHelper.importFromOtg(otgDir, localDir, new OtgSyncHelper.SyncCallback() {
                @Override
                public void onProgress(int percent) {
                    // প্রোগ্রেস আপডেট
                }
                @Override
                public void onSuccess(String message) {
                    session.write("\n[✔] " + message + "\n");
                }
                @Override
                public void onError(String error) {
                    session.write("\n[✘] " + error + " (OTG Drive not connected?)\n");
                }
            });
        } 
        else if (trimmed.equals("update -p")) {
            File localDir = new File(session.getCwd());
            File otgDir = new File("/storage/emulated/0/usb");

            session.write(">>> Executing Amnesia OTG Update...\n");
            OtgSyncHelper.updateToOtg(localDir, otgDir, new OtgSyncHelper.SyncCallback() {
                @Override
                public void onProgress(int percent) {
                    // প্রোগ্রেস আপডেট
                }
                @Override
                public void onSuccess(String message) {
                    session.write("\n[✔] " + message + "\n");
                }
                @Override
                public void onError(String error) {
                    session.write("\n[✘] " + error + " (OTG Drive not connected?)\n");
                }
            });
        }
    }
}
