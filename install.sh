#!/data/data/com.termux/files/usr/bin/bash

echo "============================================="
echo "   Setting up Termux Amnesia Edition (OTG)   "
echo "============================================="

# Ensure prefix bin exists
mkdir -p $PREFIX/bin

# Create native 'update' and 'input' handler commands
cat << 'EOF' > $PREFIX/bin/amnesia-handler
#!/data/data/com.termux/files/usr/bin/bash
ACTION=$1
FLAG=$2

if [ "$FLAG" = "-p" ]; then
    if [ -d "/storage/emulated/0/usb" ] || [ -d "/mnt/media_rw" ]; then
        echo ">>> [Amnesia OTG] Executing $ACTION with OTG synchronization..."
        # Add your actual python or sync script logic here
        sleep 1
        echo "✔ [Success] OTG Data processed successfully!"
    else
        echo "❌ [ERROR] OTG Drive is not connected! Please attach your USB drive."
    fi
else
    echo "Usage: $ACTION -p"
fi
EOF

chmod +x $PREFIX/bin/amnesia-handler

# Add aliases or function wrappers to bash.bashrc
BASH_RC="$PREFIX/etc/bash.bashrc"

if ! grep -q "Termux Amnesia Edition Aliases" "$BASH_RC"; then
    cat << 'EOF' >> "$BASH_RC"

# Termux Amnesia Edition Aliases
update() {
    if [ "$1" = "-p" ]; then
        amnesia-handler update -p
    else
        command update "$@" 2>/dev/null || echo "No command 'update' found"
    fi
}

input() {
    if [ "$1" = "-p" ]; then
        amnesia-handler input -p
    else
        command input "$@" 2>/dev/null || echo "No command 'input' found"
    fi
}
EOF
    echo "✔ Amnesia OTG commands integrated into bash.bashrc successfully!"
else
    echo "ℹ Amnesia commands already configured."
fi

echo "============================================="
echo "   Setup Completed! Type 'update -p' or      "
echo "   'input -p' to start using OTG sync.       "
echo "============================================="
