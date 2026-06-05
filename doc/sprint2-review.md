# Sprint Review — Sprint 2

**Projet** : BAS7ION — Simulation de gestion et stratégie d'un royaume médiéval
**Cours** : CPO + Gestion de projet (SCRUM), 1A ENSEEIHT, 2025–2026
**Équipe** : 6 (Léna Charrière, Mathéo Depuydt, Hugo Pignatone, Benjamin Sarrat, Fabien Serres, Antoine Tuffery)
**Sprint** : 2 / 4
**Période** : `[29/05/2026 → 05/06/2026]` (sprint d'une semaine, vendredi → vendredi)
**Date du Sprint Review** : `[05/06/2026]`

---

## 1. Sprint Goal

> *Rendre le jeu **interactif** : le joueur peut désormais piloter sa population et ses bâtiments depuis l'UI, choisir son niveau de taxes, lancer/annuler des chantiers d'amélioration, et subir des événements aléatoires. En parallèle, les briques militaire et IA sont livrées en isolation (testées JUnit) pour préparer leur branchement au Sprint 3.*

**Critère d'acceptation principal** : le joueur lance une partie depuis le menu, joue 5 tours d'affilée en pilotant son économie via l'UI, voit le moral évoluer selon le niveau de taxes, déclenche au moins une amélioration de bâtiment, et reçoit au moins un événement Épidémie avec 3 choix de réponse. `ResolveurCombat` passe 5 tests JUnit déterministes.

---

## 2. Rôles SCRUM

| Rôle | Membre | Responsabilités |
|---|---|---|
| Scrum Master | `[À désigner]` | Anime les dailies, lève les blocages, garant des règles SCRUM |
| Product Owner | `[À désigner]` | Priorise le backlog, arbitre la portée du sprint |
| Dev Team | Tous | Réalisation technique |

---

## 3. User stories planifiées et statut

| US | Description | Assigné | Status |
|---|---|---|---|
| US-POP-05 / 06 | Affecter +/− un habitant à un rôle depuis l'UI | Antoine T. | ✅ Done |
| US-INFRA-14 à 22 | Implémenter les 8 bâtiments restants | Mathéo D. | ✅ Done |
| US-INFRA-10 / 12 | Mécanique d'amélioration (chantier en N tours) | Mathéo D. + Hugo P. | ✅ Done |
| US-INFRA-23 | Statut "Endommagé" sur les bâtiments (champ exposé) | Mathéo D. | ✅ Done (champ + API, pas encore déclenché par le combat) |
| US-COMBAT-06 à 09 | `ResolveurCombat` + Armée + Unité + tests JUnit | Benjamin S. | ✅ Done (5 tests, 100 % pass) |
| US-EVENT-01 / 04 / 06 | Premier événement aléatoire branché (Épidémie) | Fabien S. + Antoine T. | ✅ Done |
| US-UX-01 / 02 | Menu principal + écran "Nouvelle partie" | Antoine T. | ✅ Done |
| US-ECO-08 / 09 | Système de taxes et impact sur le moral | Léna C. | ✅ Done |
| US-IA-15 à 18 | Configuration de difficulté (Facile / Normal / Difficile) | Fabien S. | ✅ Done |
| US-TOUR-07 | Compléter les 9 phases du cycle | Hugo P. | ✅ Done |
| US-UX-04 | Feedback visuel sur les actions du joueur (status bar) | Hugo P. | ✅ Done |
| US-IA-04 | Production et croissance économique du bot | Fabien S. | ❌ Reporté Sprint 3 |
| US-COMBAT-X | Brancher ResolveurCombat dans le cycle | Benjamin S. + Hugo P. | ❌ Reporté Sprint 3 |

**Vélocité** : ~11 US complètes + 1 partielle + 2 reportées = ~11 US équivalent réalisées sur 13 planifiées (~85 %).

---

## 4. Contributions par membre

### 4.1 Hugo Pignatone — *Core & Cycle de tour (E1)*

**Périmètre** : Compléter les 9 phases du cycle, intégrer la résolution des événements modaux dans le contrôleur, ajouter une barre d'état permanente pour le feedback joueur.

**Fichiers livrés** :
- `src/Modele/partie/etat/EtatActionsDifferees.java` (vide la file d'actions des royaumes)
- `src/Modele/partie/etat/EtatCombatsSubis.java` (placeholder Sprint 3)
- `src/Modele/partie/etat/EtatCombatsOffensifs.java` (placeholder Sprint 3)
- `src/Modele/partie/etat/EtatTourIA.java` (placeholder Sprint 3)
- `src/Vue/VueStatusBar.java`
- Modifications de `EtatConsommation.java` (nouvelle chaîne), `ControleurPartie.java` (boucle de résolution avec gestion du dialogue modal), `README.md`

**Difficultés rencontrées** :
- Gestion de la pause modale Swing : `DialogueEvenement.afficher()` bloque l'EDT, mais comme on l'appelle dans le `invokeLater` de `terminerTour()`, le flux reste cohérent. La résolution du tour reprend après que le joueur ait choisi.

**Prochains pas Sprint 3** : Brancher `ResolveurCombat` dans `EtatCombatsSubis` / `EtatCombatsOffensifs`, implémenter le tour des bots (`EtatTourIA`).

---

### 4.2 Léna Charrière — *Royaume & Économie (E2)*

**Périmètre** : Système de taxes (3 niveaux) avec impact sur le moral et le revenu d'or, classe `Moral` bornée 0–100, intégration dans la phase de production.

**Fichiers livrés** :
- `src/Modele/economie/NiveauTaxes.java` (enum FAIBLE / NORMAL / ELEVE)
- `src/Modele/population/Moral.java`
- Modifications de `EtatProduction.java` (appel `appliquerTaxes()`), `Stock.java` (commentaire amélioré)
- `doc/sprint2-review.md`

**Difficultés rencontrées** :
- Cohérence du flux de notifications : `appliquerTaxes()` modifie le trésor ET le moral. Choix de notifier `TRESOR_CHANGE` puis `MORAL_CHANGE` séparément pour que chaque vue ne se rafraîchisse que sur ce qui l'intéresse.
- Équilibrage de l'impact moral : `-3 / tour` pour les taxes élevées peut sembler peu, mais cumulé sur 10 tours c'est `-30`, soit un effondrement notable du moral.

**Prochains pas Sprint 3** : Effets du moral sur la production (taux multiplicatif), gestion d'une crise (révolte si moral < seuil).

---

### 4.3 Mathéo Depuydt — *Bâtiments (E3)*

**Périmètre** : Les 8 bâtiments restants + mécanique d'amélioration (chantier en N tours) + intégration dans le `Royaume`.

**Fichiers livrés** :
- Les 8 bâtiments : `Mine.java`, `Scierie.java`, `Habitations.java`, `Caserne.java`, `Remparts.java`, `Marche.java`, `Bibliotheque.java`, `TourDeGuet.java`
- `src/Modele/action/ActionAmeliorer.java`
- Modifications de `Batiment.java` (champ `toursRestants`, méthodes `enChantier()`, `peutEtreAmeliore()`, `demarrerChantier()`)
- Modifications de `Royaume.java` (instanciation des 9 bâtiments, helpers de notification)
- Modifications de `Equilibrage.java` (table de coûts d'amélioration, niveau max, durée chantier)

**Difficultés rencontrées** :
- Les 4 bâtiments sans production directe (Caserne, Remparts, Marché, Tour de Guet) restent à `appliquerProduction()` vide pour Sprint 2 — leur logique métier viendra Sprint 3 (recrutement, échanges, défense, détection).
- Le pattern Template Method s'est révélé pratique : `Batiment.produire()` finale gère le chantier de manière centralisée, les sous-classes ne s'occupent que de leur production.

**Prochains pas Sprint 3** : Effet métier des 4 bâtiments restants (recrutement caserne, échanges marché, bonus remparts dans `ResolveurCombat`, détection tour de guet).

---

### 4.4 Benjamin Sarrat — *Militaire, Combat, Espionnage (E4)*

**Périmètre** : Moteur de combat complet en isolation, testé en JUnit avec graines fixes.

**Fichiers livrés** :
- Tout `src/Modele/militaire/` : `PostureCombat.java` (enum + multiplicateurs), `TypeUnite.java` (4 types + stats), `Unite.java`, `Armee.java`, `TableAvantages.java` (Pierre-Feuille-Ciseaux)
- `src/Modele/combat/RapportCombat.java` (DTO immuable)
- `src/Modele/combat/ResolveurCombat.java` (méthode statique pure, déterministe via seed)
- `tests/ResolveurCombatTest.java` (5 scénarios)
- Modification de `build.sh` (commande `./build.sh test`)

**Difficultés rencontrées** :
- Premier test (victoire écrasante) initialement échoué : l'assertion comparait les pertes en valeur absolue alors qu'il fallait comparer en pourcentage de l'effectif. Corrigé en calculant le `tauxPertes = pertes / effectif`.
- Équilibrage du Pierre-Feuille-Ciseaux : choix de +50 % d'attaque pour l'unité avantageuse. À ajuster en playtests au Sprint 4.

**Prochains pas Sprint 3** : Brancher le `ResolveurCombat` dans `EtatCombatsSubis` / `EtatCombatsOffensifs` (coord avec Hugo), ajouter les actions `ActionAttaquer` / `ActionMobiliser`, lecture du bonus des `Remparts`.

---

### 4.5 Fabien Serres — *IA, Événements, Conditions de fin (E5)*

**Périmètre** : Premier événement aléatoire (Épidémie) avec dialogue modal + difficulté de partie.

**Fichiers livrés** :
- Tout `src/Modele/evenement/` : `EffetEvenement.java` (interface), `EffetSimple.java` (implémentation générique or/morts/moral), `Choix.java`, `Evenement.java` (abstract), `Epidemie.java` (3 choix)
- `src/Modele/partie/etat/EtatEvenement.java` (tirage probabiliste 30 %)
- `src/Vue/dialogue/DialogueEvenement.java` (popup modal via `JOptionPane`)
- `src/config/Difficulte.java` (FACILE / NORMAL / DIFFICILE)
- Modifications de `Partie.java` (événement en attente, `Random` seedable), `PartieBuilder.java` (difficulté + graine)

**Difficultés rencontrées** :
- Coordination avec Hugo sur la pause modale du tour : décision retenue de stocker un `evenementEnAttente` dans `Partie`, et le `ControleurPartie` détecte cet état après chaque `passerEtape()` pour ouvrir le dialogue.
- Tirage aléatoire reproductible : passage par `partie.aleatoire()` (un `Random` seedable porté par la Partie) au lieu de `Math.random()`.

**Prochains pas Sprint 3** : Catalogue complet d'événements (10+ types : Sécheresse, Filon d'or, Réfugiés, Épidémie, Tempête...), tirage pondéré selon la saison et l'état du royaume, première `StrategieIA` pour les bots.

---

### 4.6 Antoine Tuffery — *UI & UX (E6)*

**Périmètre** : Menu principal + écran nouvelle partie, onglets Économie (boutons +/− et toggles taxes) et Infrastructures (amélioration/annulation), affichage du moral dans le HUD, internationalisation FR/EN du Sprint 2.

**Fichiers livrés** :
- `src/Vue/menu/VueMenuPrincipal.java`, `src/Vue/menu/VueNouvellePartie.java`
- `src/Controleur/ControleurMenu.java`
- `src/Controleur/ControleurOnglet.java` (abstract)
- `src/Controleur/ControleurEconomie.java`
- `src/Controleur/ControleurInfrastructures.java`
- `src/Vue/onglets/OngletInfrastructures.java`
- Modifications de `FenetreJeu.java` (CardLayout), `VueDashboard.java` (nouvel onglet), `VueHUD.java` (label moral), `OngletEconomie.java` (boutons +/− et toggles taxes), `Main.java` (simplifié, menu en entrée)
- Mise à jour `strings_fr.properties` et `strings_en.properties` (~30 nouvelles clés)

**Difficultés rencontrées** :
- Respect strict de la convention vues passives : tous les boutons (+/−, toggles taxes, améliorer/annuler) sont exposés via getters pour que les contrôleurs y attachent leurs listeners. Aucun listener dans le constructeur des vues.
- Refacto de `FenetreJeu` en `CardLayout` pour pouvoir switcher entre menu / nouvelle partie / jeu. Les composants HUD et Dashboard sont créés tardivement, après que le joueur ait cliqué "Démarrer".

**Prochains pas Sprint 3** : Onglets restants (Militaire, Espionnage, Diplomatie, Journal), écran d'options (langue, thème), écran de fin de partie.

---

## 5. Démo (à présenter en revue de sprint)

**Pré-requis** : un terminal dans `projet/`, Java 17+.

**Étape 1 — Build et lancement**
```bash
./build.sh run
```
La fenêtre BAS7ION s'ouvre sur le **menu principal**.

**Étape 2 — Nouvelle partie**
- Clic "Nouvelle partie" → écran de configuration.
- Saisir un nom (ex. "Royaume du Joueur"), 0 bot, difficulté "Normale".
- Clic "Démarrer".

**Étape 3 — État initial**
- HUD : `Or: 500/5000 | Nourriture: 100/1000 | Bois: 200/1000 | Pierre: 200/1000 | Savoir: 0/999 | Royaume du Joueur | Tour 1 | Population: 10/20 | Moral: 50/100 | [Fin de tour]`
- Onglet Économie : 6 fermiers, 4 inactifs, taxes Normales sélectionnées.
- Onglet Infrastructures : 9 bâtiments tous au niveau 1/5, statut Normal.

**Étape 4 — Pilotage de la population et des taxes**
- Onglet Économie : clic + sur Mineur (1 inactif → 1 mineur). Status bar : "+1 habitant affecté : Mineur".
- Changer les taxes en "Élevées". Status bar : "Taxes ajustées : Élevées".
- Clic "Fin de tour" : la pierre monte de +1 (1 mineur × 1), le moral baisse de −3, l'or monte du revenu des taxes (10 hab × 3 = +30).

**Étape 5 — Amélioration de bâtiment**
- Onglet Infrastructures : clic "Améliorer" sur Ferme. Status bar : "Amélioration planifiée : Ferme". L'or et le bois diminuent immédiatement (200 or, 100 bois). Statut Ferme : "Planifié". Bouton devient "Annuler".
- Clic "Annuler" : ressources remboursées, statut redevient "Normal".
- Re-planifier l'amélioration, clic "Fin de tour" 2 fois. Au 2e tour, la Ferme passe au niveau 2/5.

**Étape 6 — Événement Épidémie**
- Continuer "Fin de tour" jusqu'au déclenchement de l'événement (probabilité 30 % à partir du tour 2).
- Popup modal : "Épidémie ! Une maladie virulente se propage…"
- Choisir "Soigner" : −200 or, 2 habitants meurent, moral −2. Status bar : "Événement résolu : Épidémie - Soigner aux frais du royaume".

**Étape 7 — Validation tests**
```bash
./build.sh test
```
Sortie attendue : `Resultat : 5 reussis, 0 echoues.`

**Étape 8 — Validation architecture**
```bash
grep -r "import javax.swing" src/Modele/   # 0 résultat attendu
```

---

## 6. Definition of Done (DoD) du Sprint 2

- [x] Le code compile sans erreur (`./build.sh build`).
- [x] Le menu principal s'ouvre, "Nouvelle partie" permet de configurer et lancer une partie.
- [x] Le joueur peut affecter ses habitants via les boutons +/− dans l'UI.
- [x] Le joueur peut changer le niveau de taxes et voir l'impact sur l'or et le moral.
- [x] Le joueur peut planifier et annuler une amélioration de bâtiment ; le coût est réservé à la planification et remboursé à l'annulation.
- [x] Au moins un événement Épidémie se déclenche aléatoirement après le tour 1 et applique son effet après choix du joueur.
- [x] `ResolveurCombat` passe les 5 tests JUnit (graine fixe).
- [x] Aucun import croisé Vue ↔ Modèle (vérifié par `grep`).
- [x] Status bar affiche un feedback après chaque action utilisateur significative.
- [x] Cycle de tour complet à 9 phases (4 actives, 4 placeholders).
- [x] Documentation : `README.md` mis à jour, `doc/sprint2-review.md` rédigé.

---

## 7. Rétrospective d'équipe

> `[À compléter ensemble en réunion]`

**Ce qui a bien fonctionné**
- Le découpage E1–E6 du plan d'architecture est resté pertinent pour le Sprint 2 sans modification.
- La signature uniforme `(Partie, FenetreJeu)` pour les contrôleurs a évité les confusions sur ce qu'on passe à qui.
- Le pattern Command + file FIFO a permis d'implémenter l'amélioration **et son annulation** très facilement (4 lignes dans `ControleurInfrastructures.deplanifier()`).
- L'aléatoire centralisé dans `partie.aleatoire()` rend les tests reproductibles et préparera le terrain pour le combat aléatoire au Sprint 3.
- `[À ajouter]`

**Ce qui a moins bien fonctionné**
- La phase EtatEvenement et la gestion du modal ont demandé plusieurs allers-retours avant de trouver la bonne approche (pause modale dans la boucle `terminerTour` plutôt que dans une vraie suspension d'état).
- Le passage du coût d'amélioration à la planification (au lieu de l'exécution) a cassé l'idéal du pattern Command pur (`Action.executer` ne paie plus). C'est un compromis pragmatique mais à documenter clairement.
- Pas encore de PR review systématique : à institutionnaliser pour le Sprint 3.
- `[À ajouter]`

**Actions correctives pour Sprint 3**
- Mettre en place des PR obligatoires avec review par un binôme avant merge sur `main`.
- Coordination Benjamin ↔ Hugo dès le début du Sprint 3 pour le branchement de `ResolveurCombat` dans le cycle.
- Coordination Fabien ↔ Antoine pour la `StrategieIA` (interface partagée entre modèle et UI).
- Planifier 1h de playtest interne en fin de semaine 3 pour valider l'équilibrage.
- `[À ajouter]`

---

## 8. Objectifs Sprint 3

**Période prévue** : `[06/06/2026 → 12/06/2026]` (vendredi → vendredi)

### 8.1 Sprint Goal

> *Brancher toutes les briques métier restantes dans le cycle de tour. Le jeu devient un vrai jeu de stratégie multi-royaume : combats résolus, IA des bots active, catalogue d'événements riche, sauvegarde / chargement, conditions de victoire et défaite.*

### 8.2 User stories prioritaires

| Priorité | US | Description | Assigné |
|---|---|---|---|
| P0 | US-COMBAT-X | Brancher `ResolveurCombat` dans `EtatCombatsSubis` / `EtatCombatsOffensifs` | Benjamin S. + Hugo P. |
| P0 | US-IA-04 à 10 | 4 stratégies d'IA (Agressif, Défensif, Commerçant, Équilibré) + `EtatTourIA` actif | Fabien S. |
| P0 | US-MIL-01 à 05 | Recrutement de soldats via Caserne + actions militaires | Benjamin S. + Mathéo D. |
| P0 | US-EVENT-X | Catalogue de ~10 événements (Sécheresse, Filon d'or, Réfugiés, Tempête...) | Fabien S. |
| P1 | US-SAVE-01 à 03 | Sauvegarde / chargement (5 slots + autosave) | Antoine T. + Hugo P. |
| P1 | US-END-01 / 02 | Conditions de victoire et défaite + écran de fin | Hugo P. + Antoine T. |
| P1 | US-INFRA-X | Logique métier des 4 bâtiments restants (Marché échanges, Bibliothèque bonus recherche, Tour de Guet détection) | Mathéo D. |
| P2 | US-DIPLO-01 à 03 | Diplomatie minimale (paix / guerre / alliance) | Fabien S. |

**Estimation grossière** : ~25 US, à affiner en Sprint Planning.

### 8.3 Definition of Done du Sprint 3

- [ ] Une partie complète peut se dérouler du menu principal jusqu'à une condition de victoire ou défaite.
- [ ] Au moins 2 bots actifs avec stratégies différentes attaquent et produisent.
- [ ] Sauvegarde / chargement fonctionnel sur au moins 1 slot.
- [ ] Catalogue d'événements ≥ 8 types, tirage pondéré par saison/état.
- [ ] Tests JUnit étendus : couverture ≥ 50 % sur `Modele/combat/`, `Modele/economie/`, `Modele/ia/`.
- [ ] Aucune régression sur les fonctionnalités du Sprint 2.

### 8.4 Risques identifiés

| Risque | Impact | Mitigation |
|---|---|---|
| Bugs de combat lors du branchement dans le cycle | Régression du jeu | Tests JUnit étendus sur `ResolveurCombat` avant intégration |
| Performance de la phase `TourIA` avec 4 bots | Lag à la fin de tour | Profiler et optimiser si > 100 ms |
| Format de sauvegarde non décidé (JSON vs Java Serializable) | Décision à prendre | Trancher en début de Sprint 3 (cf. risque 4 du plan d'archi) |

---

## Annexe — Métriques objectives du Sprint 2

| Métrique | Valeur Sprint 1 | Valeur Sprint 2 | Évolution |
|---|---|---|---|
| Fichiers Java livrés | 29 | 66 | +37 |
| Lignes de code Java (incl. Javadoc) | ~1750 | ~4200 | +2450 |
| Packages créés | 11 | 14 | +3 (`evenement`, `militaire`, `combat`, `dialogue`, `menu`) |
| Patterns implémentés | 4 | 8 | +4 (Strategy futur, Factory futur, Template Method actif, Builder enrichi) |
| Locales supportées | 2 (FR par défaut, EN) | 2 | = |
| Phases du cycle de tour implémentées | 4 / 9 | 9 / 9 | +5 |
| Bâtiments implémentés | 1 / 9 | 9 / 9 | +8 |
| Types d'unités militaires | 0 / 4 | 4 / 4 | +4 |
| Stratégies IA | 0 / 4 | 0 / 4 | = (Sprint 3) |
| Événements aléatoires | 0 | 1 (Épidémie) | +1 |
| Tests automatisés | 0 | 5 (ResolveurCombat) | +5 |
| Clés i18n FR/EN | 30 | ~60 | +30 |
