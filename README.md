# BA7ION

Simulation de gestion et de strategie d'un royaume medieval. Projet long
CPO + Gestion de projet (SCRUM), 1A ENSEEIHT, equipe 6.

**Equipe :** Lena Charriere, Matheo Depuydt, Hugo Pignatone, Benjamin Sarrat,
Fabien Serres, Antoine Tuffery.

**Livraison :** 2026-06-19.

## Le jeu

Jeu au tour par tour : on gere un royaume face a 1 a 4 royaumes adverses tenus
par une IA. A chaque tour on affecte sa population a des metiers (fermier,
mineur, bucheron, erudit, soldat), on produit des ressources (or, nourriture,
bois, pierre, savoir), on ameliore ses 9 batiments, on recrute une armee et on
peut attaquer un voisin (a partir du tour 6). Des evenements aleatoires ou
scriptes viennent pimenter la partie.

- **Victoire :** accumuler 5000 or, ou eliminer tous les adversaires.
- **Defaite :** population a 0, moral trop bas, ou tour 50 atteint sans victoire.

## Structure du depot

```
projet/
├── src/                 Code source Java
│   ├── Main.java
│   ├── config/          Equilibrage (toutes les constantes), Difficulte
│   ├── Modele/          Logique metier (sans Swing)
│   │   ├── action/          Actions du joueur (pattern Command)
│   │   ├── combat/          ResolveurCombat, EffetsCombat, RapportCombat
│   │   ├── economie/        Ressources, stocks, tresor, taxes
│   │   ├── evenement/       Evenements (Epidemie, grenouille empoisonnee...)
│   │   ├── ia/              Strategie des bots (StrategieEquilibree)
│   │   ├── infrastructure/  Les 9 batiments
│   │   ├── militaire/       Armees, unites, postures de combat
│   │   ├── notification/    Notification + TypeNotification (Observer)
│   │   ├── partie/          Partie, Tour, machine a etats (9 phases)
│   │   ├── persistance/     Sauvegarde JSON (Gson) + checksum
│   │   ├── population/      Population, Role, Moral
│   │   └── royaume/         Agregat Royaume (seul Observable cote royaume)
│   ├── Vue/             Couche d'affichage Swing (passive)
│   │   ├── dialogue/        Popups modaux (evenement, combat, fin de tour)
│   │   ├── menu/            Menu principal + ecran Nouvelle partie
│   │   ├── onglets/         Economie, Infrastructures, Militaire, Marche
│   │   └── theme/           Couleurs, polices, composants stylises
│   └── Controleur/      Orchestration (branche les ecouteurs Swing)
├── tests/               Tests JUnit 4
├── lib/                 Dependances : gson, junit, hamcrest
├── doc/                 Doc interne (architecture, sprints, roadmap, uml)
├── saves/               Sauvegardes JSON (creees pendant le jeu)
├── DOCUMENTATION_TECHNIQUE.md   Doc technique complete du code
├── UML.md               Diagrammes UML (Mermaid)
└── livrables/           Documents finaux (rapport, manuel utilisateur...)
```

## Pre-requis

- **Java 17+** (developpe et teste sur Java 21). Le projet utilise
  `java.util.Observable`, deprecie depuis Java 9 mais toujours present
  (convention du cours).
- **Pas de Maven/Gradle** : les dependances sont des jars fournis dans `lib/` :
  - `gson-2.13.2.jar` : serialisation JSON des sauvegardes ;
  - `junit-4.13.2.jar` + `hamcrest-core-1.3.jar` : tests.
  Le script `build.sh` les met automatiquement au classpath.

## Compiler, lancer, tester

Depuis la racine `projet/`, utiliser le script `build.sh` :

```bash
./build.sh         # compile (bin/)
./build.sh run     # compile puis lance le jeu (Gson au classpath)
./build.sh test    # compile puis execute les tests JUnit
./build.sh clean   # nettoie bin/
```

**Manuellement** (si le script ne marche pas) :

```bash
mkdir -p bin
find src -name "*.java" -print0 | xargs -0 javac -d bin -cp lib/gson-2.13.2.jar -encoding UTF-8
java -cp "bin:lib/gson-2.13.2.jar" Main
```

Sous **VS Code / Eclipse** : ouvrir `projet/`. Le fichier `.classpath` declare
deja `src/` et `tests/` comme dossiers sources et les jars de `lib/` comme
bibliotheques. (Dans VS Code, si les imports ne se resolvent pas : commande
"Java: Reload Projects".)

## Sauvegarde

La partie est **sauvegardee automatiquement a chaque fin de tour** dans
`saves/<nom du royaume>.json`. Le fichier est en JSON lisible, avec une
empreinte **SHA-256** qui detecte toute modification manuelle ou corruption.
On recharge une partie via le bouton "Charger une sauvegarde" du menu.

## Architecture

- Architecture **MVC** stricte (`Modele` / `Vue` / `Controleur`) avec le pattern
  **Observer** (`Observable` / `Notification`).
- Voir `doc/architecture.md` pour le document de cadrage.
- Voir **`DOCUMENTATION_TECHNIQUE.md`** pour l'explication complete du code et
  **`UML.md`** pour les diagrammes.

## Conventions de code (extrait)

- Packages `Modele/`, `Vue/`, `Controleur/` avec **majuscule initiale**
  (convention imposee par le cours).
- Metier en francais (`Royaume`, `produire`), termes Java en anglais
  (`getX`, `Listener`).
- **Aucun nombre magique** : tout passe par `config/Equilibrage.java`.
- Les Vues n'appellent **jamais** un setter du modele ; tout passe par un
  Controleur.
- Un seul `Observable` par agregat (`Partie`, `Royaume`), argument toujours une
  `Notification`.
- Sauvegarde JSON via **Gson** (serialisation automatique).

## Etat des sprints

| Sprint | Statut | Contenu |
|---|---|---|
| 1 | Termine | Squelette MVC, cycle de tour, Ferme productrice |
| 2 | Termine | 9 batiments, ameliorations, taxes & moral, UI interactive, evenement Epidemie, ResolveurCombat |
| 3 | Termine | Combats branches (ouverts au tour 6), IA des bots, catalogue d'evenements + grenouille empoisonnee, sauvegarde JSON |
| 4 | En cours | Equilibrage, 66 tests JUnit, documentation technique + UML, rapport et presentation |

Voir les bilans dans `doc/sprint1-review.md`, `sprint2-review.md`,
`sprint3-review.md`.

## Tests

```bash
./build.sh test
```

**66 tests JUnit 4** repartis en 9 suites : economie, population, moral,
infrastructure, evenement, action, deroulement de partie, persistance et combat.
Tous doivent passer (`OK (66 tests)`).

## Demo rapide

1. `./build.sh run` ouvre la fenetre sur le menu principal.
2. "Nouvelle partie" : choisir un nom, le nombre d'adversaires, la difficulte.
3. "Demarrer" : l'ecran de jeu s'ouvre avec le HUD (5 ressources, tour,
   population, moral) et 4 onglets : Economie, Infrastructures, Militaire, Marche.
4. **Economie** : repartir les habitants par role (+/-), regler les taxes,
   recruter un villageois.
5. **Infrastructures** : ameliorer un batiment (chantier de 2 tours).
6. **Militaire** : recruter des unites, choisir une posture. Les combats ne
   s'ouvrent qu'a partir du tour 6.
7. "Fin de tour" : la chaine des 9 phases s'execute. Au tour 6, l'evenement
   "grenouille empoisonnee" annonce le debut des combats. La partie est
   sauvegardee automatiquement.
8. Quitter puis "Charger une sauvegarde" pour reprendre la partie.
