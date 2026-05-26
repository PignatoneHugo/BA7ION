# BAS7ION

Simulation de gestion et de strategie d'un royaume medieval. Projet long CPO + Gestion de projet (SCRUM), 1A ENSEEIHT, equipe 6.

**Equipe :** Lena Charriere, Matheo Depuydt, Hugo Pignatone, Benjamin Sarrat, Fabien Serres, Antoine Tuffery.

**Livraison :** 2026-06-19.

## Structure du depot

```
projet/
├── src/                Code source Java
│   ├── Main.java
│   ├── Modele/         Logique metier (cf. doc/architecture.md)
│   ├── Vue/            Couche d'affichage Swing (passive)
│   ├── Controleur/     Couche d'orchestration
│   ├── config/         Constantes d'equilibrage
│   └── util/           Utilitaires transverses
├── doc/                Documentation interne (architecture, diagrammes...)
└── livrables/          Documents finaux (rapport.pdf, utilisateur.pdf, ...)
```

## Pre-requis

- **Java 11+** (le projet utilise `java.util.Observable`, deprecie depuis Java 9 mais conserve ; convention du cours).
- Pas de dependance externe au Sprint 1 (JUnit ajoute en Sprint 2 pour les tests).

## Compiler et lancer

Depuis la racine `projet/`, le plus simple est d'utiliser le script `build.sh` :

```bash
./build.sh build   # compile (bin/)
./build.sh run     # compile puis lance
./build.sh clean   # nettoie bin/
```

Le script s'occupe aussi de copier les fichiers `.properties` (utilises par
`Traducteur` pour l'internationalisation) dans `bin/Vue/i18n/`, ce que
`javac` ne fait pas tout seul.

**Manuellement** (si le script ne marche pas) :

```bash
mkdir -p bin
find src -name "*.java" -print0 | xargs -0 javac -d bin -encoding UTF-8
(cd src && find . -name "*.properties" -exec cp --parents {} ../bin/ \;)
java -cp bin Main
```

Sous Eclipse : importer `projet/` comme projet Java, marquer `src/` comme source folder. Eclipse copie automatiquement les `.properties` lors du build.

## Architecture

Voir `doc/architecture.md` pour le document de cadrage complet (MVC + Observer/Observable, decoupage en sous-equipes, conventions de code, Sprint 1, risques techniques).

## Conventions de code (extrait)

- Packages `Modele/`, `Vue/`, `Controleur/` avec **majuscule initiale** (convention imposee par le cours).
- Metier en francais (`Royaume`, `produire`), termes Java en anglais (`getX`, `Listener`).
- **Aucun nombre magique** : tout passe par `config/Equilibrage.java`.
- Les Vues n'appellent **jamais** un setter du modele ; toutes les actions passent par un Controleur.
- Pattern Observer : un seul `Observable` par agregat (`Partie`, `Royaume`), arg toujours une `Notification`.

## Sprint 1 - Vertical slice

Le code actuel est le squelette du Sprint 1 : tour cycle (Planification -> Production -> Consommation -> FinTour), un seul royaume joueur, une Ferme productrice, HUD permanent, un onglet Economie. Si vous lancez `Main` et cliquez "Fin de tour" plusieurs fois, vous devez voir la nourriture augmenter (production des 5 fermiers de demarrage) puis diminuer (consommation des 10 habitants).

Voir `doc/architecture.md` section 8 pour les criteres de fin de Sprint 1.
