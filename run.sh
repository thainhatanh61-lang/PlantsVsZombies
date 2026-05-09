#!/bin/bash
# Get the directory where the script is located
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# If we are in the 'src' directory, move up to the root
if [[ "$DIR" == */src ]]; then
    cd "$DIR/.."
else
    cd "$DIR"
fi

# Compile and run the game
echo "Compiling from $(pwd)..."
javac -d out src/*.java
echo "Starting game..."
java -cp out Main
