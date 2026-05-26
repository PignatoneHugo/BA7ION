# Architecture MVC globale — BAS7ION

Document de cadrage à destination des 6 étudiants de l'équipe. Objectif : poser un squelette qui permette de paralléliser le travail, respecter strictement la convention MVC + `Observable/Observer` du cours, et limiter les ré-architectures en cours de projet.

---

## Contexte

**Pourquoi ce document**

L'équipe (6 étudiants ENSEEIHT) doit livrer pour le 19/06/2026 une simulation de gestion de royaume médiéval (`BAS7ION`) à partir d'un cahier des charges très ambitieux (**14 Epics, ~277 user stories**, volontairement non terminable). Le sujet impose Java + Swing + MVC strict. La méthodologie est SCRUM avec sprints.

Le répertoire `/home/n7student/Documents/Programmation objet/cpo/projet/` est actuellement vide. Avant de répartir les US entre les 6 développeurs, il faut figer une architecture qui :
- respecte la convention du cours (packages `Modele/Vue/Controleur` avec majuscule initiale, pattern `Observable/Observer` issu du TP Chat),
- permette à 6 personnes de coder en parallèle sans collisions,
- absorbe l'ampleur du périmètre (4 ressources, 9 bâtiments, 4 unités, 5 branches de décrets, 17 événements, IA à 4 personnalités, 3 difficultés, 9 phases de résolution par tour),
- soit testable (notamment combats et IA), sérialisable (5 slots de sauvegarde + autosave), et internationalisable (FR/EN obligatoires).

**Résultat attendu**

À l'issue de l'approbation de ce plan, l'équipe dispose de :
- une structure de packages stable,
- une convention de communication M-V-C unique,
- une répartition initiale du travail entre les 6 membres,
- un objectif clair pour le Sprint 1 (vertical slice qui valide l'architecture).

Aucun fichier Java n'est créé par ce plan ; il s'agit du document de design qui sera utilisé pour démarrer l'implémentation.

---

## 1. Vue d'ensemble

L'application suit un MVC strict en 3 packages racine (`Modele`, `Vue`, `Controleur`), avec deux packages techniques transverses (`util`, `persistance`) pour ne pas polluer le métier. Le `Main` à la racine de `src/` instancie un seul `Partie` (modèle racine), une `FenetreJeu` qui héberge plusieurs `JPanel` vues (HUD permanent + onglets), et un `ControleurPartie` qui orchestre les actions utilisateur. Toute la logique métier (calculs de production, combats, IA, décrets, événements) reste dans `Modele`. Les vues sont passives, n'appellent jamais le modèle en écriture, et reçoivent toutes leurs mises à jour via `update(Observable o, Object arg)`.

Le moteur de tour est implémenté comme une **machine à états** (pattern State) qui enchaîne les 9 étapes de résolution. Les actions du joueur durant la phase de planification sont encapsulées en **objets Command** stockés dans une file, exécutés au moment de la résolution. L'IA des bots utilise **Strategy + Factory** (une stratégie par personnalité). La création d'une partie passe par un **Builder** (réutilisation du pattern du TP14).

---

## 2. Structure des packages

```
src/
├── Main.java
│
├── Modele/
│   ├── partie/                  # racine du modèle, cycle de tour
│   │   ├── Partie.java                 (extends Observable)
│   │   ├── Tour.java
│   │   ├── PartieBuilder.java
│   │   ├── ConditionsFin.java
│   │   └── etat/                # pattern State pour les 9 phases
│   │       ├── EtatTour.java                (interface)
│   │       ├── EtatPlanification.java
│   │       ├── EtatProduction.java
│   │       ├── EtatConsommation.java
│   │       ├── EtatActionsDifferees.java
│   │       ├── EtatCombatsSubis.java
│   │       ├── EtatCombatsOffensifs.java
│   │       ├── EtatTourIA.java
│   │       ├── EtatEvenement.java
│   │       └── EtatFinTour.java
│   │
│   ├── royaume/                 # un royaume = joueur OU bot
│   │   ├── Royaume.java                 (extends Observable)
│   │   ├── Tresor.java
│   │   └── ProfilRoyaume.java           (couleur, nom, IA ?)
│   │
│   ├── population/
│   │   ├── Population.java
│   │   ├── Habitant.java
│   │   ├── Role.java                    (enum : FERMIER, MINEUR, BUCHERON, ERUDIT, ESPION, SOLDAT, INACTIF)
│   │   ├── Moral.java
│   │   └── Demographie.java
│   │
│   ├── economie/
│   │   ├── Ressource.java               (enum : OR, NOURRITURE, BOIS, PIERRE, SAVOIR)
│   │   ├── Stock.java
│   │   ├── BilanRessources.java         (production - consommation par tour)
│   │   ├── Taxes.java                   (niveau FAIBLE/NORMAL/ELEVE)
│   │   ├── Marche.java                  (échanges entre ressources)
│   │   └── Saison.java                  (enum + modificateurs)
│   │
│   ├── infrastructure/
│   │   ├── Batiment.java                (abstract)
│   │   ├── TypeBatiment.java            (enum : FERME, MINE, SCIERIE, HABITATIONS, CASERNE, REMPARTS, MARCHE, BIBLIOTHEQUE, TOUR_GUET)
│   │   ├── Ferme.java, Mine.java, ... (9 sous-classes, ou 1 classe paramétrée par TypeBatiment)
│   │   └── FabriqueBatiment.java        (Factory)
│   │
│   ├── recherche/
│   │   ├── Decret.java
│   │   ├── BrancheDecret.java           (enum : AGRICULTURE, ECONOMIE, MILITAIRE, POPULATION, DIPLOMATIE)
│   │   ├── EffetDecret.java             (interface)
│   │   ├── CatalogueDecrets.java
│   │   └── GestionnaireDecrets.java     (2 actifs max, durée, expiration)
│   │
│   ├── militaire/
│   │   ├── Armee.java
│   │   ├── Unite.java                   (abstract)
│   │   ├── TypeUnite.java               (enum : INFANTERIE_LEG, ARCHER, LANCIER, CAVALERIE_LRD)
│   │   ├── TableAvantages.java          (Pierre-Feuille-Ciseaux)
│   │   └── PostureCombat.java           (enum : ATTAQUE, DEFENSE, CONTOURNEMENT)
│   │
│   ├── combat/
│   │   ├── Bataille.java
│   │   ├── ResolveurCombat.java         (algorithme déterministe + aléa borné)
│   │   ├── RapportCombat.java
│   │   └── Butin.java
│   │
│   ├── espionnage/
│   │   ├── MissionEspionnage.java
│   │   ├── TypeMission.java             (enum)
│   │   ├── RapportEspionnage.java
│   │   └── NiveauConfiance.java
│   │
│   ├── evenement/
│   │   ├── Evenement.java               (abstract, possède des Choix)
│   │   ├── Choix.java
│   │   ├── EffetEvenement.java
│   │   ├── CatalogueEvenements.java
│   │   └── TirageEvenement.java         (pondéré par saison/état)
│   │
│   ├── ia/
│   │   ├── StrategieIA.java             (interface)
│   │   ├── StrategieAgressif.java
│   │   ├── StrategieDefensif.java
│   │   ├── StrategieCommercant.java
│   │   ├── StrategieEquilibre.java
│   │   ├── NiveauDifficulte.java        (enum)
│   │   └── FabriqueIA.java
│   │
│   ├── action/                  # pattern Command
│   │   ├── Action.java                  (interface : estExecutable() + executer(Partie))
│   │   ├── FileActions.java
│   │   ├── ActionConstruire.java
│   │   ├── ActionAmeliorer.java
│   │   ├── ActionFormer.java
│   │   ├── ActionMobiliser.java
│   │   ├── ActionLancerDecret.java
│   │   ├── ActionEspionner.java
│   │   ├── ActionAttaquer.java
│   │   ├── ActionEchanger.java
│   │   └── ActionFixerTaxes.java
│   │
│   ├── effet/                   # effets temporaires/cumulés
│   │   ├── Effet.java                   (interface)
│   │   ├── EffetTemporaire.java         (durée en tours)
│   │   ├── EffetPermanent.java
│   │   ├── ModificateurMultiplicatif.java
│   │   └── GestionnaireEffets.java
│   │
│   ├── statistiques/
│   │   ├── JournalPartie.java
│   │   ├── EntreeJournal.java
│   │   ├── Statistiques.java
│   │   └── Records.java
│   │
│   └── notification/
│       ├── TypeNotification.java        (enum servant d'arg à notifyObservers)
│       └── Notification.java            (payload typé)
│
├── Vue/
│   ├── FenetreJeu.java                  (JFrame conteneur)
│   ├── VueHUD.java                      (JPanel, Observer, ressources + tour + nation)
│   ├── VueDashboard.java                (JPanel central avec onglets)
│   ├── onglets/
│   │   ├── OngletPopulation.java
│   │   ├── OngletEconomie.java
│   │   ├── OngletInfrastructures.java
│   │   ├── OngletRecherche.java
│   │   ├── OngletMilitaire.java
│   │   ├── OngletEspionnage.java
│   │   ├── OngletDiplomatie.java
│   │   └── OngletJournal.java
│   ├── dialogue/
│   │   ├── DialogueEvenement.java       (popup fin de tour)
│   │   ├── DialogueRapportCombat.java
│   │   ├── DialogueRapportEspionnage.java
│   │   └── DialogueSauvegarde.java
│   ├── menu/
│   │   ├── VueMenuPrincipal.java
│   │   ├── VueNouvellePartie.java
│   │   ├── VueOptions.java
│   │   ├── VueTutoriel.java
│   │   └── VueCodex.java
│   ├── composant/                       # widgets réutilisables
│   │   ├── BarreRessource.java
│   │   ├── BarreMoral.java
│   │   ├── PanneauStat.java
│   │   └── GraphiqueEvolution.java
│   ├── i18n/
│   │   ├── Traducteur.java
│   │   └── strings_fr.properties, strings_en.properties
│   └── theme/
│       ├── Palette.java                 (mode normal + mode daltonien)
│       └── Icones.java
│
├── Controleur/
│   ├── ControleurPartie.java            (chef d'orchestre, écoute les vues)
│   ├── ControleurMenu.java
│   ├── ControleurOnglet.java            (abstract, un par onglet métier)
│   ├── ControleurPopulation.java
│   ├── ControleurEconomie.java
│   ├── ControleurInfrastructures.java
│   ├── ControleurRecherche.java
│   ├── ControleurMilitaire.java
│   ├── ControleurEspionnage.java
│   ├── ControleurEvenement.java         (lecture choix dans popup)
│   ├── ControleurRaccourcisClavier.java
│   └── ControleurSauvegarde.java
│
├── persistance/
│   ├── Sauvegarde.java                  (DTO sérialisable)
│   ├── GestionnaireSauvegardes.java     (5 slots + autosave)
│   ├── SerialisateurJson.java           (ou Java natif Serializable)
│   └── RecordsStore.java                (persistant entre parties)
│
├── config/
│   ├── Equilibrage.java                 (toutes les constantes numériques)
│   ├── ConstantesJeu.java               (limites, seuils)
│   └── Difficulte.java
│
└── util/
    ├── Aleatoire.java                   (Random encapsulé, seedable pour les tests)
    ├── Probabilite.java
    └── Console.java                     (réutiliser celle des TPs, debug uniquement)
```

---

## 3. Classes pivots et responsabilités

### Modèle
- `Partie` (`extends Observable`) : racine du modèle, contient le `Royaume` joueur, la liste des bots, le `Tour` courant, le `JournalPartie`. Émet les notifications globales.
- `Tour` : compteur de tour, saison courante, état courant de la machine à états, transition `passerEtape()`.
- `Royaume` (`extends Observable`) : agrège `Population`, `Tresor` (ressources), `liste<Batiment>`, `Armee`, `GestionnaireDecrets`, `GestionnaireEffets`. Une instance par participant (joueur + bots).
- `EtatTour` (interface) : `executer(Partie p)` + `EtatTour suivant()`. Une implémentation par phase. Le `Tour` délègue à son état courant.
- `Action` (interface, dérivée de `Commande` du TP10) : `boolean estExecutable(Royaume)` + `void executer(Royaume, Partie)`. Stockée dans `FileActions` durant la planification, exécutée pendant `EtatActionsDifferees`.
- `StrategieIA` (interface) : `void jouerTour(Royaume bot, Partie p)`. 4 implémentations + Factory.
- `ResolveurCombat` : fonction pure (sans état) qui prend deux `Armee` + posture + remparts et renvoie un `RapportCombat`. Critique pour la testabilité.
- `Evenement` : possède une liste de `Choix`, chacun portant un ou plusieurs `EffetEvenement`. Tiré par `TirageEvenement` selon saison + indicateurs du royaume.
- `Effet` / `GestionnaireEffets` : abstraction de tout modificateur temporaire (décret, événement, posture). Le calcul de production demande au gestionnaire la somme des modificateurs applicables ; centralise les "effets cumulés".
- `PartieBuilder` : configure nom du joueur, nombre de bots, personnalités, difficulté, seed aléatoire, puis `build()` retourne la `Partie` prête.

### Vue
- `FenetreJeu` (`JFrame`) : conteneur unique, swap entre `VueMenuPrincipal` et `VueDashboard`.
- `VueHUD` : permanent en haut, observe `Royaume` du joueur, met à jour ressources + moral + tour + saison.
- `VueDashboard` : `JTabbedPane` qui contient les 8 onglets ; chaque onglet est lui-même un `Observer` du `Royaume`.
- Onglets : passifs, ne contiennent que des widgets Swing et la méthode `update()`. Tout ActionListener est branché par le contrôleur correspondant.
- `DialogueEvenement` : modal, bloque la fin de tour tant que le joueur n'a pas choisi.
- `Traducteur` : singleton, lit les `.properties`, fournit `t("cle")`. Toutes les chaînes affichées passent par lui.

### Contrôleur
- `ControleurPartie` : possède la `Partie`, expose `terminerTour()`, `nouvellePartie()`, `charger()`, `sauvegarder()`. Branché sur le bouton "Fin de tour" du HUD.
- `ControleurOnglet` (abstract) : référence vers `Partie` + vers `FileActions` du joueur. Sous-classé par un contrôleur par onglet métier. Chaque sous-contrôleur expose des méthodes "haut niveau" (`construire(TypeBatiment)`, `mobiliser(TypeUnite, int)`) qui empilent une `Action` dans la file ou exécutent immédiatement (selon US).
- `ControleurEvenement` : lit le `Choix` retenu dans le `DialogueEvenement`, applique les effets, débloque la transition d'état.
- `ControleurSauvegarde` : médiateur entre `GestionnaireSauvegardes` (persistance) et l'UI ; jamais appelé en dehors d'actions explicites de l'utilisateur, sauf pour l'autosave déclenchée en fin de tour.

---

## 4. Flux de communication

**Cas 1 — Action utilisateur (ex : améliorer un bâtiment)**

1. Clic sur un bouton dans `OngletInfrastructures`.
2. `ActionListener` attaché par `ControleurInfrastructures` se déclenche.
3. Le contrôleur crée une `ActionAmeliorer(typeBatiment)` et l'empile via `royaumeJoueur.getFileActions().ajouter(action)`. (Ou exécute immédiatement si l'US le précise — cf. taxes, échanges.)
4. `Royaume.notifyObservers(TypeNotification.FILE_ACTIONS_CHANGEE)`.
5. `OngletInfrastructures.update()` rafraîchit la liste des actions planifiées ; `VueHUD.update()` met à jour le coût prévisionnel.

**Cas 2 — Fin de tour**

1. Clic sur "Fin de tour" dans le HUD, écouté par `ControleurPartie`.
2. `ControleurPartie.terminerTour()` appelle `partie.passerEtape()` en boucle jusqu'au retour à `EtatPlanification`.
3. Chaque `EtatTour.executer(partie)` modifie le modèle et appelle `partie.setChanged(); partie.notifyObservers(new Notification(phase, donnees))`.
4. Si `EtatEvenement` se déclenche, il bloque sur un `EvenementEnAttente` exposé par `Partie` ; `DialogueEvenement` est ouvert par `ControleurPartie` qui repère le changement et attend la réponse de `ControleurEvenement` avant de continuer.
5. À la fin : `EtatFinTour` déclenche autosauvegarde et notifie une éventuelle condition de victoire/défaite ; le HUD affiche le tour suivant.

**Cas 3 — Tour des bots**

1. `EtatTourIA.executer(partie)` boucle sur les bots, demande à chacun `bot.getStrategieIA().jouerTour(bot, partie)`.
2. La stratégie empile des `Action` dans la file du bot puis les exécute immédiatement (les bots n'ont pas de phase de planification séparée).
3. Les conséquences (attaque sur le joueur) sont stockées dans `partie.getCombatsAResoudre()`, traitées par `EtatCombatsSubis` au tour **suivant** (cohérence avec l'ordre des 9 phases).

**Règle d'or** : aucune vue n'appelle un setter du modèle. Aucun contrôleur ne crée de widgets Swing (sauf widgets contextuels comme `JOptionPane` de confirmation). Aucun modèle n'importe `javax.swing.*`.

---

## 5. Patterns de conception

| Pattern | Localisation | Justification |
|---|---|---|
| **Observer** (`Observable`/`Observer`) | `Partie`, `Royaume`, vues | Convention du cours, multi-vues sur même modèle |
| **State** | `EtatTour` et sous-classes | 9 phases ordonnées, transitions strictes, facile à tester phase par phase |
| **Command** | `Action` et sous-classes | Actions différées entre planification et résolution, undo non requis mais file inspectable, journalisation triviale |
| **Strategy** | `StrategieIA` | 4 personnalités interchangeables sans toucher au moteur |
| **Factory** | `FabriqueIA`, `FabriqueBatiment` | Encapsuler la création conditionnelle (difficulté, niveau) |
| **Builder** | `PartieBuilder` | Création progressive d'une partie avec beaucoup de paramètres optionnels (cf. TP14) |
| **Template Method** | `Batiment.produire()` qui appelle `quantiteBase()` + `modificateurs()` | Mutualiser le squelette de calcul de production entre les 9 bâtiments |
| **Singleton** | `Equilibrage`, `Traducteur`, `Aleatoire` | Configuration globale en lecture seule (à utiliser avec parcimonie, jamais pour le modèle métier) |
| **DTO** | `Sauvegarde`, `RapportCombat`, `RapportEspionnage` | Découpler la sérialisation du modèle vivant |

---

## 6. Classes racines

La hiérarchie d'instanciation depuis `Main` :

```
Partie (racine)
 ├── Royaume joueur
 │    ├── Population
 │    ├── Tresor
 │    ├── List<Batiment>
 │    ├── Armee
 │    ├── GestionnaireDecrets
 │    ├── GestionnaireEffets
 │    └── FileActions
 ├── List<Royaume> bots (chacun a en plus une StrategieIA)
 ├── Tour
 │    └── EtatTour courant
 ├── CatalogueEvenements
 ├── CatalogueDecrets
 └── JournalPartie
```

Un seul objet `Partie` existe à la fois en mémoire (sauf transition de chargement). Les vues observent à la fois `Partie` (pour les changements globaux : phase de tour, fin de partie) et `royaume.joueur` (pour les changements locaux : ressources, bâtiments).

---

## 7. Découpage en sous-équipes (6 étudiants)

| Étudiant | Périmètre principal | Périmètre secondaire | Interfaces critiques à fournir tôt |
|---|---|---|---|
| **E1 — Core** | `Modele/partie/` (Partie, Tour, EtatTour et 9 états), `Main.java`, `PartieBuilder` | `config/Equilibrage` | `EtatTour`, `Action`, `Notification` |
| **E2 — Royaume & Économie** | `Modele/royaume/`, `Modele/economie/`, `Modele/population/` | `Modele/effet/` | API de `Royaume`, `Stock`, `BilanRessources` |
| **E3 — Bâtiments & Recherche** | `Modele/infrastructure/`, `Modele/recherche/` | actions associées | `Batiment.produire()`, `Decret.appliquer()` |
| **E4 — Militaire, Combat, Espionnage** | `Modele/militaire/`, `Modele/combat/`, `Modele/espionnage/` | actions associées | `ResolveurCombat.resoudre()`, `RapportCombat` |
| **E5 — IA, Événements, Victoire** | `Modele/ia/`, `Modele/evenement/`, `Modele/partie/ConditionsFin` | équilibrage IA | `StrategieIA`, `Evenement`, `TirageEvenement` |
| **E6 — UI & UX** | tout `Vue/` + tout `Controleur/` + `i18n` + `persistance/` | journal, statistiques | `ControleurOnglet` (abstract), gabarit d'onglet |

**Convergence** : E1 livre les interfaces `Action`, `EtatTour`, `Notification` au Sprint 1. E2-E5 implémentent leur métier en respectant ces interfaces. E6 démarre en parallèle avec des stubs (`Royaume` minimal) puis se câble progressivement.

**Pairing recommandé** : E2+E3 (économie + bâtiments très couplés), E4 isolé (peu de dépendances), E5+E1 (IA pilote la machine à états), E6 transverse à toute l'équipe.

---

## 8. Sprint 1 — Vertical slice qui valide l'architecture

Objectif : un mini-tour complet, mais avec une seule ressource, un seul bâtiment, aucun bot, aucun combat. Si ce slice marche, l'architecture tient.

1. **E1** : `Partie`, `Tour`, `EtatPlanification`, `EtatProduction`, `EtatFinTour`. La machine à états fait au moins ces 3 phases.
2. **E2** : `Royaume` avec uniquement `Tresor` (Or + Nourriture), `Population` réduite à un compteur global, `Stock.ajouter()/retirer()`.
3. **E3** : `Ferme` qui produit de la nourriture, sans niveau, sans modificateur.
4. **E1+E2** : interface `Action` + `FileActions` + `ActionConstruire` fictive (pour tester le pattern Command même si le bâtiment existe déjà).
5. **E6** : `FenetreJeu`, `VueHUD` qui affiche Or/Nourriture/Tour, `VueDashboard` avec un seul onglet `OngletEconomie`, `ControleurPartie` avec un bouton "Fin de tour". `Traducteur` opérationnel sur 5 chaînes.
6. **E5** : un `Evenement` codé en dur qui se déclenche au tour 2 et offre 2 choix, juste pour câbler `DialogueEvenement` + `EtatEvenement` + `ControleurEvenement`.
7. **E4** : commence le `ResolveurCombat` en pur unitaire (tests JUnit), sans encore le brancher.

**Critère de fin de Sprint 1** : `Main` lance le jeu, on voit ses ressources, on clique "Fin de tour" 3 fois, on voit la production s'appliquer, l'événement popper, et le HUD se rafraîchir via `notifyObservers`. Aucune US métier complète n'est implémentée, mais les **flux** sont validés.

---

## 9. Risques techniques majeurs

1. **Cycles d'observation et notifications cascade**. Avec `Partie` qui observe ses `Royaume` qui contiennent eux-mêmes des `Observable` (`Population`, `Tresor`), un changement de stock peut potentiellement provoquer une cascade de `update()` redondants. **Mitigation** : un seul `Observable` par "agrégat métier" (`Royaume`, `Partie`) ; les sous-composants exposent des getters mais n'héritent pas d'`Observable`. Le `Royaume` notifie après chaque action atomique cohérente, jamais à chaque setter interne.

2. **Effets cumulés et ordre d'application**. La production d'un bâtiment dépend de décrets, événements, postures, saison. L'ordre (`(base * multAgriculture) + bonusEvenement` vs `base * (multAgriculture + bonusEvenement)`) change les résultats. **Mitigation** : un seul `GestionnaireEffets` centralise tous les modificateurs ; une formule de combinaison documentée dans `config/Equilibrage` (additifs séparés des multiplicatifs, appliqués dans l'ordre `base → additifs → multiplicatifs → clamp`). Tests unitaires obligatoires sur cette formule.

3. **Modal blocking de l'événement de fin de tour**. Si `EtatEvenement.executer()` est appelé sur l'EDT Swing, il ne peut pas attendre la réponse du joueur (deadlock). **Mitigation** : la résolution du tour n'est **pas** synchrone bout en bout. `EtatEvenement` stocke un `EvenementEnAttente` dans `Partie`, rend la main, et `ControleurPartie` reprend le tour après que `ControleurEvenement` a poussé la réponse. Documenter ce point dans `EtatTour.javadoc`.

4. **Sauvegarde et sérialisation du graphe d'objets**. Les `Observable` ne sont pas sérialisables tels quels, et les `Observer` non plus (vues Swing). **Mitigation** : sérialiser via des DTO (`Sauvegarde`) construits depuis le modèle, jamais le graphe d'objets directement. Choisir tôt entre JSON (lisible, debuggable) et `Serializable` Java (rapide, fragile au refactor). Recommandation : JSON via Gson/Jackson en dépendance, sinon écriture maison sur des `Map<String, Object>` car les libs externes peuvent être interdites par le cours — **à confirmer avec l'enseignant en première semaine**.

5. **Déterminisme et tests de l'aléa**. Les combats, événements, IA reposent sur du `Math.random()`. Cela rend les bugs irreproductibles. **Mitigation** : tout l'aléa passe par `util.Aleatoire` qui encapsule un `Random` seedable. `PartieBuilder.withSeed(long)` permet aux tests de fixer la graine. En prod, seed = `System.nanoTime()`.

---

## 10. Conventions de code à fixer dès le Sprint 0

**Langue**
- **Métier en français** (`Royaume`, `produire`, `Decret`) pour rester aligné avec le cahier des charges et le cours.
- **Termes Java standards en anglais** (`getX`, `setX`, `Listener`, `Observer`, `equals`, `toString`).
- **Pas de mélange dans un même identifiant** : `getRessource` oui, `getResource` non.

**Packages**
- Majuscule initiale `Modele/Vue/Controleur` (convention du cours, contre-Java mais imposée). Sous-packages en minuscules.
- Une classe par fichier, nom de fichier = nom de classe.

**Constantes d'équilibrage**
- Toutes dans `config/Equilibrage.java`, `public static final`, regroupées par section avec commentaire d'en-tête.
- **Aucun nombre magique dans le code métier** — référencer `Equilibrage.COUT_FERME_NIVEAU_2` plutôt que `150`.
- Les bornes (max population, niveaux max) dans `ConstantesJeu.java`.

**Effets cumulés**
- Toujours via `GestionnaireEffets.appliquer(valeurDeBase, contexte)`. Pas de `* 1.1` éparpillés dans le code.
- Formule documentée : `final = clamp(base * produit(mult) + somme(additif), min, max)`.

**Observer**
- `setChanged()` + `notifyObservers(arg)` toujours appelés ensemble en fin de méthode publique modificatrice.
- L'`arg` est **toujours** une `Notification` (jamais `null`, jamais un type primitif), pour que les vues filtrent : `if (arg instanceof Notification n && n.type() == TRESOR_CHANGE)`.

**Vues passives**
- Une `Vue*.java` n'a pas le droit d'importer un setter du modèle. Lint manuel en revue de code.
- Tout listener Swing est attaché par un contrôleur, **jamais** dans le constructeur de la vue.

**Tests**
- JUnit 5. Tests obligatoires (minimum) pour : `ResolveurCombat`, `GestionnaireEffets`, chaque `EtatTour`, chaque `StrategieIA` (au moins un scénario nominal).
- Tests à graine fixe pour tout ce qui utilise `Aleatoire`.

**Git / SCRUM**
- Une branche par US (`feat/us-3.2-ameliorer-batiment`), PR obligatoire, review par un autre étudiant.
- Pas de commit direct sur `main`.
- Le `Main.java` ne doit pas être touché sans accord, c'est un fichier de câblage stabilisé après Sprint 1.

**Style**
- Indentation 4 espaces. Lignes ≤ 120 caractères.
- Javadoc obligatoire sur toutes les méthodes publiques des classes pivots (`Partie`, `Royaume`, `Action`, `EtatTour`, `StrategieIA`).
- Pas d'emoji, pas d'accent dans les identifiants Java (réserver les accents aux chaînes affichées, qui passeront de toute façon par `Traducteur`).

---

## 11. Ce qui n'est volontairement **pas** dans ce document

- Le détail des US (cahier des charges fait foi).
- Les signatures précises des méthodes (à figer par chaque sous-équipe en Sprint 1).
- L'ergonomie fine de l'UI (maquettes à produire séparément par E6).
- Le choix exact de la lib de sérialisation (point ouvert, cf. risque 4).
- Les règles d'équilibrage chiffrées (à itérer en playtests, ne pas figer maintenant).

---

## 12. Fichiers les plus structurants à créer en premier

Par ordre de dépendance (à attaquer en Sprint 1, par E1 puis E2/E6) :

1. `src/Modele/notification/TypeNotification.java` + `Notification.java`
2. `src/Modele/action/Action.java` + `FileActions.java`
3. `src/Modele/partie/etat/EtatTour.java`
4. `src/Modele/royaume/Royaume.java` (squelette `extends Observable`)
5. `src/Modele/partie/Partie.java` (squelette `extends Observable`)
6. `src/Modele/partie/Tour.java`
7. `src/Modele/partie/PartieBuilder.java`
8. `src/Vue/FenetreJeu.java`
9. `src/Vue/VueHUD.java`
10. `src/Controleur/ControleurPartie.java`
11. `src/Main.java` (câblage final du slice)

---

## 13. Vérification (Sprint 1)

Pour valider que l'architecture tient, le Sprint 1 doit produire les preuves suivantes :

**Tests automatisés (JUnit 5)**
- `ResolveurCombatTest` : au moins 3 scénarios (victoire écrasante, défaite, match serré) avec aléa fixé par seed.
- `GestionnaireEffetsTest` : combinaison additifs + multiplicatifs + clamp, ordre vérifié.
- `EtatProductionTest` : un tour avec 5 fermiers + ferme produit la quantité attendue.
- `FileActionsTest` : empilement, exécution dans l'ordre, action non exécutable ignorée.

**Test manuel end-to-end**
1. `java Main` ouvre la fenêtre principale (menu).
2. "Nouvelle Partie" lance une partie minimale (1 joueur, 0 bot, 1 ressource visible : Nourriture).
3. Le HUD affiche `Tour 1`, `Nourriture: 100`, `Population: 10` (valeurs initiales).
4. Cliquer "Fin de tour" → `Tour 2` affiché, `Nourriture` change selon la production des fermiers.
5. Au `Tour 2`, le `DialogueEvenement` codé en dur s'affiche, propose 2 choix ; cliquer un choix applique son effet et débloque le tour suivant.
6. Aucune exception en console, aucun freeze de l'EDT.

**Critères architecturaux**
- `grep -r "import javax.swing" src/Modele/` retourne 0 lignes.
- `grep -r "import.*Modele\." src/Vue/` ne contient que des imports de getters (jamais de setters).
- Tout `Vue*.java` est un `Observer` ; tout `*Controleur.java` ne `extends` pas `JFrame`/`JPanel`.

Si ces 3 niveaux passent, l'architecture est validée pour les Sprints 2+.
