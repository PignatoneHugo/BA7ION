#!/bin/bash
# Script de build simple pour BAS7ION (Sprint 1, sans Maven/Gradle).
# Compile les sources Java dans bin/ et copie les ressources .properties.
#
# Robuste aux chemins contenant des espaces : on se place dans le dossier
# projet et on utilise des chemins relatifs (src/ et bin/), donc aucun
# argument transmis a javac ne contient d'espace.
#
# Usage :
#   ./build.sh        # compile
#   ./build.sh run    # compile puis lance
#   ./build.sh clean  # nettoie bin/

set -e

# Se placer dans le dossier du script (= racine projet).
cd "$(dirname "$0")"

clean() {
    rm -rf bin
    echo "[clean] bin/ supprime."
}

build() {
    mkdir -p bin

    # 1. Compilation Java (chemins relatifs, pas d'espace).
    find src -name "*.java" -print0 | xargs -0 javac -d bin -encoding UTF-8
    echo "[build] compilation OK."

    # 2. Copie des ressources non-Java.
    (cd src && find . -name "*.properties" -exec cp --parents {} ../bin/ \;)
    echo "[build] ressources copiees."
}

run() {
    java -cp bin Main
}

case "${1:-build}" in
    clean) clean ;;
    build) build ;;
    run)   build; run ;;
    *) echo "Usage : $0 [clean|build|run]"; exit 1 ;;
esac
