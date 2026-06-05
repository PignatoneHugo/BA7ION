# BAS7ION

Simulation de gestion et de strategie d'un royaume medieval. Projet long
CPO + Gestion de projet (SCRUM), 1A ENSEEIHT, equipe 6.

**Equipe :** Lena Charriere, Matheo Depuydt, Hugo Pignatone, Benjamin Sarrat,
Fabien Serres, Antoine Tuffery.

**Livraison :** 2026-06-19.

## Structure du depot

```
projet/
├── src/                Code source Java
│   ├── Main.java
│   ├── Modele/         Logique metier (cf. doc/architecture.md)
│   │   ├── action/         Actions du joueur (pattern Command)
│   │   ├── combat/         ResolveurCombat + RapportCombat
│   │   ├── economie/       Ressources, stocks, taxes
│   │   ├── evenement/      Evenements aleatoires (Epidemie...)
│   │   ├── infrastructure/ Les 9 batiments
│   │   ├── militaire/      Armees, unites, postures
│   │   ├── notification/   Notification + TypeNotification
│   │   ├── partie/         Partie, Tour, machine a etats (9 phases)
│   │   ├── population/     Population, Role, Moral
│   │   └── royaume/        Agregat Royaume (seul Observable cote royaume)
│   ├── Vue/            Couche d'affichage Swing (passive)
│   │   ├── dialogue/       Popups modaux (DialogueEvenement)
│   │   ├── i18n/           Traducteur + strings_fr/en.properties
│   │   ├── menu/           Menu principal + ecran Nouvelle partie
│   │   └── onglets/        OngletEconomie, OngletInfrastructures
│   ├── Controleur/     Couche d'orchestration
│   └── config/         Constantes d'equilibrage, Difficulte
├── tests/              Tests automatises (executables sans JUnit)
├── doc/                Documentation interne (architecture, sprints, roadmap)
└── livrables/          Documents finaux (rapport.pdf, utilisateur.pdf, ...)
```

## Pre-requis

- **Java 17+** (le projet utilise `java.util.Observable`, deprecie depuis
  Java 9 mais toujours present ; convention du cours).
- Pas de dependance externe (les tests sont actuellement executes sans JUnit ;
  migration vers JUnit 5 prevue au Sprint 3 quand le projet aura Maven/Gradle).

## Compiler, lancer, tester

Depuis la racine `projet/`, le plus simple est d'utiliser le script `build.sh` :

```bash
./build.sh build   # compile (bin/)
./build.sh run     # compile puis lance le jeu
./build.sh test    # compile puis execute les tests automatises
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

Sous Eclipse : importer `projet/` comme projet Java, marquer `src/` comme
source folder. Eclipse copie automatiquement les `.properties` lors du build.

## Architecture

Voir `doc/architecture.md` pour le document de cadrage complet (MVC +
Observer/Observable, decoupage en sous-equipes, conventions de code,
risques techniques) et `doc/roadmap.md` pour la roadmap des 4 sprints.

## Conventions de code (extrait)

- Packages `Modele/`, `Vue/`, `Controleur/` avec **majuscule initiale**
  (convention imposee par le cours).
- Metier en francais (`Royaume`, `produire`), termes Java en anglais
  (`getX`, `Listener`).
- **Aucun nombre magique** : tout passe par `config/Equilibrage.java`.
- Les Vues n'appellent **jamais** un setter du modele ; toutes les actions
  passent par un Controleur.
- Pattern Observer : un seul `Observable` par agregat (`Partie`, `Royaume`),
  arg toujours une `Notification`.

## Etat des sprints

| Sprint | Statut | Contenu |
|---|---|---|
| 1 | Termine | Squelette MVC, cycle de tour a 4 phases, Ferme productrice |
| 2 | Termine | 9 batiments, ameliorations, taxes & moral, UI interactive, evenement Epidemie, ResolveurCombat + tests, difficulte, 9 phases completes |
| 3 | A venir | Combats branches, IA des bots, catalogue d'evenements, sauvegarde |
| 4 | A venir | Tests etendus, equilibrage, redaction du rapport et de la presentation |

Voir `doc/sprint1-review.md` pour le bilan du Sprint 1, `doc/roadmap.md`
pour la vision long terme.

## Demo rapide (Sprint 2)

1. `./build.sh run` ouvre la fenetre sur le menu principal.
2. "Nouvelle partie", choisir un nom, le nombre d'adversaires, la difficulte.
3. "Demarrer" : l'ecran de jeu s'ouvre avec le HUD (5 ressources, moral, tour)
   et deux onglets (Economie, Infrastructures).
4. Dans Economie : ajuster la repartition des habitants par role avec les
   boutons +/-, choisir un niveau de taxes.
5. Dans Infrastructures : cliquer "Ameliorer" sur un batiment pour empiler
   l'action dans la file.
6. "Fin de tour" : la chaine des 9 phases s'execute (production, consommation,
   actions planifiees...). Au tour 2, un evenement Epidemie ouvre un popup
   modal avec 3 choix.

## Tests automatises

```bash
./build.sh test
```

Lance la suite `ResolveurCombatTest` (5 scenarios deterministes : victoire
ecrasante, defaite face aux remparts, combat equilibre, avantage Pierre-
Feuille-Ciseaux, determinisme via graine). Tous les tests doivent passer.
