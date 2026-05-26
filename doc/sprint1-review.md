# Sprint Review — Sprint 1

**Projet** : BAS7ION — Simulation de gestion et stratégie d'un royaume médiéval
**Cours** : CPO + Gestion de projet (SCRUM), 1A ENSEEIHT, 2025–2026
**Équipe** : 6 (Léna Charrière, Mathéo Depuydt, Hugo Pignatone, Benjamin Sarrat, Fabien Serres, Antoine Tuffery)
**Sprint** : 1 / N
**Période** : `[À ajuster — ex. 12/05/2026 → 26/05/2026]`
**Date du Sprint Review** : `[À ajuster]`

---

## 1. Sprint Goal

> *Mettre en place le squelette MVC du projet et livrer un **vertical slice** qui valide l'architecture : un tour de jeu complet (Planification → Production → Consommation → Fin de tour), un royaume joueur, une ressource visible (nourriture) qui évolue selon le nombre de fermiers, un HUD permanent et un onglet économie. Le but n'est pas d'avoir une feature jouable mais de prouver que les flux MVC + Observer + State + Command fonctionnent et que l'équipe peut paralléliser le travail sur cette base.*

**Critère d'acceptation principal** : `java Main` ouvre une fenêtre 1280×720, affiche les ressources, et un clic sur "Fin de tour" fait monter la nourriture de +2/tour (production de 6 fermiers − consommation de 10 habitants).

---

## 2. Rôles SCRUM

| Rôle | Membre | Responsabilités |
|---|---|---|
| Scrum Master | `[À désigner]` | Anime les dailies, lève les blocages, garant des règles SCRUM |
| Product Owner | `[À désigner]` | Priorise le backlog, arbitre la portée du sprint |
| Dev Team | Tous | Réalisation technique |

> **Note d'équipe** : à 6 sur un projet étudiant, Scrum Master et Product Owner restent contributeurs Dev. Préciser dans le rapport final qui a tenu chaque rôle.

---

## 3. User stories planifiées et statut

| US | Description | Assigné | Status |
|---|---|---|---|
| US-TOUR-01 | Identifier le tour courant | Hugo P. | ✅ Done |
| US-TOUR-04 | Bouton "Fin de tour" | Hugo P. + Antoine T. | ✅ Done |
| US-TOUR-06 | Lancement de la phase de résolution | Hugo P. | ✅ Done |
| US-TOUR-07 | Ordre de résolution des étapes (partiel : 4/9 phases) | Hugo P. | 🟡 Partiel |
| US-ECO-01 | Consulter le stock des ressources de base | Léna C. + Antoine T. | ✅ Done |
| US-ECO-05 | Production de nourriture par les Fermiers | Mathéo D. | ✅ Done |
| US-ECO-12 | Consommation de nourriture par la population | Léna C. | ✅ Done |
| US-POP-01 | Consulter l'état démographique global | Léna C. + Antoine T. | ✅ Done |
| US-POP-02 | Consulter la répartition par rôle | Léna C. + Antoine T. | ✅ Done |
| US-INFRA-14 | Effets de la Ferme (production de base) | Mathéo D. | 🟡 Partiel (sans bonus de décrets/saison) |
| US-UX-01 | Écran d'accueil au lancement | Antoine T. | ❌ Non fait (reporté Sprint 2) |
| US-UX-17 | Langue de l'interface (FR/EN) | Antoine T. | ✅ Done (infra prête, EN non vérifié sur tout) |
| US-COMBAT-06 | Calcul automatique de l'issue du combat | Benjamin S. | 🟡 Préparation (pas de code livré) |
| US-EVENT-04 | Affichage d'un popup d'événement | Fabien S. | 🟡 Préparation (pas de code livré) |
| US-IA-04 | Production et croissance économique du bot | Fabien S. | ❌ Non commencé |

**Vélocité** : 9 US complètes + 3 partielles + 1 non commencée + 1 reportée = ~9 US équivalent réalisées sur 14 planifiées.

> `[À ajuster]` La liste ci-dessus est basée sur le découpage du plan d'architecture (`doc/architecture.md` section 7). Validez avec l'équipe quelles US chacun a réellement portées et ajustez les statuts.

---

## 4. Contributions par membre

### 4.1 Hugo Pignatone — *Core & Cycle de tour (E1)*

**Périmètre** : Machine à états du tour, interface `Action` (Command), classes racines `Partie` / `Tour`, `Main`, fichier de constantes d'équilibrage.

**Fichiers livrés** (14) :
- `src/Main.java`
- `src/Modele/partie/Partie.java` (`extends Observable`)
- `src/Modele/partie/Tour.java`
- `src/Modele/partie/PartieBuilder.java`
- `src/Modele/partie/etat/EtatTour.java` (interface)
- `src/Modele/partie/etat/EtatPlanification.java`
- `src/Modele/partie/etat/EtatProduction.java`
- `src/Modele/partie/etat/EtatConsommation.java`
- `src/Modele/partie/etat/EtatFinTour.java`
- `src/Modele/action/Action.java` (interface)
- `src/Modele/action/FileActions.java`
- `src/Modele/notification/Notification.java`
- `src/Modele/notification/TypeNotification.java`
- `src/config/Equilibrage.java`

**Difficultés rencontrées** :
- `Observable.setChanged()` étant `protected`, impossible à appeler depuis les `EtatXxx` situés dans un sous-package. Résolu en exposant `Partie.notifier(Notification)` comme point d'entrée unique pour les états.

**Prochains pas Sprint 2** : Implémenter les 5 phases manquantes (`EtatActionsDifferees`, `EtatCombatsSubis`, `EtatCombatsOffensifs`, `EtatTourIA`, `EtatEvenement`). Coordonner avec Benjamin et Fabien pour les contrats d'interface.

---

### 4.2 Léna Charrière — *Royaume & Économie (E2)*

**Périmètre** : Agrégat `Royaume` (un seul Observable côté royaume, règle d'or du plan), modèle économique (5 ressources, stocks bornés), population minimale par rôle.

**Fichiers livrés** (6) :
- `src/Modele/royaume/Royaume.java` (`extends Observable`)
- `src/Modele/economie/Ressource.java` (enum)
- `src/Modele/economie/Stock.java`
- `src/Modele/economie/Tresor.java`
- `src/Modele/population/Role.java` (enum)
- `src/Modele/population/Population.java`

**Difficultés rencontrées** :
- Choix de ne **pas** rendre `Tresor` et `Population` observables individuellement (sinon cascade de notifications, cf. risque 1 du plan). C'est `Royaume` qui notifie après chaque action atomique cohérente.
- Famine simplifiée pour le Sprint 1 (1 mort tous les 5 unités de nourriture manquantes) : à rééquilibrer Sprint 2.

**Prochains pas Sprint 2** : Implémenter `Taxes`, `Marche`, `Moral`, `Demographie` (croissance/déclin), formation des spécialistes (Érudit, Espion).

---

### 4.3 Mathéo Depuydt — *Bâtiments (E3)*

**Périmètre** : Classe abstraite `Batiment` avec pattern Template Method (`produire()` → `appliquerProduction()`), enum des 9 types, première implémentation concrète (`Ferme`).

**Fichiers livrés** (3) :
- `src/Modele/infrastructure/Batiment.java` (abstract, Template Method)
- `src/Modele/infrastructure/TypeBatiment.java` (enum 9 valeurs)
- `src/Modele/infrastructure/Ferme.java`

**Difficultés rencontrées** :
- Choix entre **une classe par bâtiment** (verbeux mais clair) ou **une classe paramétrée par TypeBatiment** (concis mais switch partout). Décision : une classe par bâtiment, pour autoriser des formules de production très différentes (un Marché ne produit pas comme une Ferme).

**Prochains pas Sprint 2** : Implémenter les 8 autres bâtiments (`Mine`, `Scierie`, `Habitations`, `Caserne`, `Remparts`, `Marche`, `Bibliotheque`, `TourDeGuet`), ajouter la mécanique d'amélioration de niveau (chantier en N tours) et de réparation.

---

### 4.4 Benjamin Sarrat — *Militaire, Combat, Espionnage (E4)*

**Périmètre Sprint 1** : Préparation. Pas de code livré, le militaire/combat est volontairement hors du vertical slice.

**Travail effectué** :
- Lecture approfondie des Epics 5 (Système Militaire), 6 (Combat) et 7 (Espionnage) du cahier des charges.
- Première modélisation UML des classes `Armee`, `Unite`, `TypeUnite`, `Bataille`, `ResolveurCombat` (`[À déposer dans doc/uml/militaire.png]`).
- Choix d'algorithme pour la résolution calculatoire : prise en compte du Pierre-Feuille-Ciseaux (Cavalerie > Archers > Infanterie > Lanciers > Cavalerie), du bonus de Remparts, des postures et d'un aléa borné à ±10 %.
- Définition de l'interface `ResolveurCombat.resoudre(Armee, Armee, Posture, int bonusRemparts)` qu'il fournira en début de Sprint 2.

**Difficultés rencontrées** :
- Équilibrage du système Pierre-Feuille-Ciseaux : les coefficients du cahier des charges (+50% pour Cavalerie contre Archers, etc.) seront à vérifier en playtests.

**Prochains pas Sprint 2** : Livrer `Armee`, `Unite`, `TypeUnite`, `Bataille`, `ResolveurCombat` testé en JUnit (3 scénarios minimum : victoire écrasante, défaite serrée, match nul). Pas encore brancher dans le cycle de tour : isolation totale par JUnit en premier (cf. plan d'archi section 8 point 7).

---

### 4.5 Fabien Serres — *IA, Événements, Conditions de fin (E5)*

**Périmètre Sprint 1** : Préparation. Idem Benjamin, pas dans le vertical slice.

**Travail effectué** :
- Lecture approfondie des Epics 8 (Événements), 10 (IA des Bots) et 11 (Conditions de Victoire/Défaite).
- Définition de l'interface `StrategieIA` (`void jouerTour(Royaume bot, Partie p)`) qui sera implémentée en Sprint 2 par 4 classes (Agressif, Défensif, Commerçant, Équilibré).
- Premier catalogue d'événements aléatoires rédigé (~10 événements types : Sécheresse, Filon d'or, Réfugiés, Épidémie, Tempête, etc.).
- Discussion avec Hugo sur l'**ordre de résolution** : un événement détecté en `EtatEvenement` doit bloquer la résolution du tour jusqu'à la réponse du joueur, sans deadlock sur l'EDT Swing (cf. risque 3 du plan).

**Difficultés rencontrées** :
- Coordination avec Antoine (UI) sur le format du `DialogueEvenement` : modal vs intégré au HUD. Choix retenu : modal bloquant.

**Prochains pas Sprint 2** : Livrer `Evenement`, `Choix`, `EffetEvenement`, `CatalogueEvenements`, `TirageEvenement`, `EtatEvenement` (à brancher dans le cycle de tour), et la première `StrategieIA` (Équilibré).

---

### 4.6 Antoine Tuffery — *UI & UX (E6)*

**Périmètre** : Toute la couche `Vue/` + `Controleur/` + internationalisation FR/EN.

**Fichiers livrés** (8 + 2 properties) :
- `src/Vue/FenetreJeu.java` (JFrame conteneur)
- `src/Vue/VueHUD.java` (JPanel `Observer`, bandeau permanent en haut)
- `src/Vue/VueDashboard.java` (JTabbedPane central)
- `src/Vue/onglets/OngletEconomie.java` (JPanel `Observer`)
- `src/Vue/i18n/Traducteur.java` (singleton i18n)
- `src/Vue/i18n/strings_fr.properties`
- `src/Vue/i18n/strings_en.properties`
- `src/Controleur/ControleurPartie.java`

**Difficultés rencontrées** :
- Respect strict de la règle "vues passives" : le bouton "Fin de tour" du `VueHUD` est exposé via un getter pour que `ControleurPartie` y attache son `ActionListener` (interdiction de brancher dans le constructeur de la vue).
- Vérification qu'aucun `import javax.swing.*` ne pollue le package `Modele/` : OK.

**Prochains pas Sprint 2** : Boutons +/− sur les rôles dans `OngletEconomie` (US-POP-05/06), onglet Infrastructures (US-INFRA-01/02/10), écran d'accueil + menu principal (US-UX-01), modal `DialogueEvenement` (coordination avec Fabien).

---

## 5. Démo (à présenter en revue de sprint)

**Pré-requis** : un terminal dans `projet/`, Java 21 installé.

**Étape 1 — Build et lancement**
```bash
./build.sh run
```
La fenêtre BAS7ION s'ouvre en 1280×720, centrée.

**Étape 2 — État initial**
- HUD : `Or: 500/5000  Nourriture: 100/1000  Bois: 200/1000  Pierre: 200/1000  Savoir: 0/999  |  Royaume du Joueur  |  Population: 10/20  Tour 1  |  [Fin de tour]`
- Onglet Économie : `Inactif: 4 | Fermier: 6 | Mineur: 0 | Bûcheron: 0 | Érudit: 0 | Espion: 0`

**Étape 3 — Cycle de tour**
Cliquer "Fin de tour" 3 fois. Observer :
- Le compteur de tour passe à 2, 3, 4.
- La nourriture monte de +2 par tour (production 12 − consommation 10).
- Aucune exception en console.

**Étape 4 — Validation architecture**
```bash
grep -r "import javax.swing" src/Modele/   # 0 résultat attendu
```

---

## 6. Définition of Done (DoD) du Sprint 1

- [x] Le code compile sans erreur (`./build.sh build`).
- [x] L'application se lance et la fenêtre s'affiche.
- [x] Le cycle de tour boucle correctement (4 phases sur 9, suffisant pour le vertical slice).
- [x] Aucun import croisé Vue ↔ Modèle.
- [x] Pattern Observer fonctionnel (le HUD se rafraîchit après `notifyObservers`).
- [x] Fichiers `.properties` chargés par `Traducteur`.
- [x] `.gitignore` en place, fichiers Eclipse `.project`/`.classpath` non commités (chacun les régénère).
- [x] Documentation : `README.md`, `doc/architecture.md`, `doc/sprint1-review.md`.
- [ ] Tests JUnit `[Décidé en équipe : reportés au Sprint 2 — premier test sera `ResolveurCombatTest` par Benjamin]`.

---

## 7. Rétrospective d'équipe

> `[À compléter ensemble en réunion]`

**Ce qui a bien fonctionné**
- Le découpage E1–E6 du plan d'architecture a permis à chacun de savoir précisément son périmètre dès le début du Sprint.
- Les conventions de code (langue, packages, pas de nombres magiques, Observable centralisé) ont été suivies sans débat — gain de temps en review.
- `[À ajouter]`

**Ce qui a moins bien fonctionné**
- E4 et E5 n'ont pas livré de code au Sprint 1 ; à arbitrer en équipe : était-ce le bon choix de les laisser en préparation, ou aurait-il fallu leur confier des tâches transverses (tests, documentation, équilibrage) ?
- Pas encore de tests automatisés.
- `[À ajouter]`

**Actions correctives pour Sprint 2**
- Adopter JUnit 5 dès le début du Sprint 2 (Benjamin pilote la mise en place).
- Mettre en place des PR obligatoires + review par un binôme avant merge sur `main` (cf. convention Git du plan d'architecture).
- Faire un point de coordination Hugo ↔ Fabien ↔ Antoine dès le démarrage pour le `DialogueEvenement` (interface entre 3 couches).
- `[À ajouter]`

---

## 8. Backlog Sprint 2 envisagé

Priorisation à valider avec le Product Owner :

1. **Affecter des habitants depuis l'UI** (US-POP-05, US-POP-06, US-POP-07) — Antoine + Léna
2. **Compléter les 8 bâtiments restants** (US-INFRA-14 à US-INFRA-22) — Mathéo
3. **Système d'amélioration de bâtiments** (US-INFRA-10, US-INFRA-12) — Mathéo + Hugo (pour les chantiers en N tours = nouvelle Action différée)
4. **`ResolveurCombat` testé en JUnit** (US-COMBAT-06 à US-COMBAT-09) — Benjamin
5. **Premier événement aléatoire branché** (US-EVENT-01, US-EVENT-04, US-EVENT-06) — Fabien + Antoine
6. **Menu principal + écran nouvelle partie** (US-UX-01, US-UX-02) — Antoine
7. **Configuration de difficulté** (US-IA-15 à US-IA-18) — Fabien

**Estimation grossière** : ~20 US, à affiner en Sprint Planning.

---

## Annexe — Métriques objectives du Sprint 1

| Métrique | Valeur |
|---|---|
| Fichiers Java livrés | 29 |
| Lignes de code Java (incl. Javadoc) | ~1750 |
| Packages créés | 11 |
| Patterns implémentés | 4 (Observer, State, Command, Builder) |
| Locale supportées | 2 (FR par défaut, EN) |
| Phases du cycle de tour implémentées | 4 / 9 |
| Bâtiments implémentés | 1 / 9 |
| Types d'unités militaires | 0 / 4 |
| Tests automatisés | 0 *(reportés Sprint 2)* |
