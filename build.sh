#!/bin/bash
# Script de build simple pour BA7ION (sans Maven/Gradle).
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

# Gson (serialisation JSON des sauvegardes) : jar fourni dans lib/.
GSON="lib/gson-2.13.2.jar"

build() {
    mkdir -p bin

    # 1. Compilation Java des sources de production (Gson au classpath).
    find src -name "*.java" -print0 | xargs -0 javac -d bin -cp "$GSON" -encoding UTF-8
    echo "[build] compilation OK."

    # 2. Copie des ressources non-Java (images).
    (cd src && find . \( -name "*.png" -o -name "*.jpg" -o -name "*.jpeg" \) \
            -exec cp --parents {} ../bin/ \; 2>/dev/null) || true
    echo "[build] ressources copiees."
}

run() {
    # Gson est requis a l'execution (sauvegarde / chargement de partie).
    java -cp "bin:$GSON" Main
}

test() {
    mkdir -p bin
    # JUnit 4 (+ hamcrest) et Gson sont fournis dans lib/ : aucune dependance reseau.
    JUNIT="lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar"
    CP="bin:$JUNIT:$GSON"
    # Compile les tests par-dessus les classes de production deja dans bin/.
    find tests -name "*.java" -print0 | xargs -0 javac -d bin -cp "$CP" -encoding UTF-8
    echo "[test] compilation des tests OK."
    # Lance toutes les classes de test via le runner console de JUnit.
    java -cp "$CP" org.junit.runner.JUnitCore \
        EconomieTest \
        PopulationTest \
        MoralTest \
        InfrastructureTest \
        EvenementTest \
        ActionTest \
        PartieTest \
        PersistanceTest \
        ResolveurCombatTest \
        EffetsCombatTest
}

case "${1:-build}" in
    clean) clean ;;
    build) build ;;
    run)   build; run ;;
    test)  build; test ;;
    *) echo "Usage : $0 [clean|build|run|test]"; exit 1 ;;
esac
