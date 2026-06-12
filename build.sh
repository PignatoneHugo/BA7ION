#!/bin/bash
# Script de build simple pour BAS7ION (sans Maven/Gradle).
# Compile les sources Java dans bin/ et copie les ressources (images).
#
# Robuste aux chemins contenant des espaces : on se place dans le dossier
# projet et on utilise des chemins relatifs (src/ et bin/), donc aucun
# argument transmis a javac ne contient d'espace.
#
# Usage :
#   ./build.sh        # compile
#   ./build.sh run    # compile puis lance
#   ./build.sh test   # compile puis execute les tests
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

    # 1. Compilation Java des sources de production.
    find src -name "*.java" -print0 | xargs -0 javac -d bin -encoding UTF-8
    echo "[build] compilation OK."

    # 2. Copie des ressources non-Java (images).
    (cd src && find . \( -name "*.png" -o -name "*.jpg" -o -name "*.jpeg" \) \
            -exec cp --parents {} ../bin/ \; 2>/dev/null) || true
    echo "[build] ressources copiees."
}

run() {
    java -cp bin Main
}

test() {
    mkdir -p bin
    # Compile les tests par-dessus les classes de production deja dans bin/.
    find tests -name "*.java" -print0 | xargs -0 javac -d bin -cp bin -encoding UTF-8
    echo "[test] compilation des tests OK."
    java -cp bin ResolveurCombatTest
}

case "${1:-build}" in
    clean) clean ;;
    build) build ;;
    run)   build; run ;;
    test)  build; test ;;
    *) echo "Usage : $0 [clean|build|run|test]"; exit 1 ;;
esac
