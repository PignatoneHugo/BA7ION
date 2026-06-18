# Sprint Review — Sprint 3

**Projet** : BA7ION — Simulation de gestion et stratégie d'un royaume médiéval
**Cours** : CPO + Gestion de projet (SCRUM), 1A ENSEEIHT, 2025–2026
**Équipe** : 6 (Léna Charrière, Mathéo Depuydt, Hugo Pignatone, Benjamin Sarrat, Fabien Serres, Antoine Tuffery)
**Sprint** : 3 / 4
**Période** : `[06/06/2026 → 12/06/2026]` (sprint d'une semaine, vendredi → vendredi)
**Date du Sprint Review** : `[12/06/2026]`

---

## 1. Sprint Goal

> *Faire de BA7ION un vrai jeu de stratégie multi-royaume jouable de bout en bout : combats actifs avec conséquences sévères, IA des bots vivante et imprévisible, système de recrutement à deux étages (recrue civile → unité combattante), marché d'échanges entre ressources, validation financière des événements, et un cycle de tour clarifié avec rapports successifs (combat → bilan → événement) avant la prochaine phase de planification.*

**Critère d'acceptation principal** : une partie démarre depuis le menu, le joueur joue une trentaine de tours avec au moins 1 bot actif, subit et lance des attaques avec rapports détaillés, échange des ressources via le marché, et la partie se termine par une condition de victoire ou défaite réelle (effondrement moral, élimination du bot, prospérité or, ou tour 50).

---

## 2. Rôles SCRUM

| Rôle | Membre | Responsabilités |
|---|---|---|
| Scrum Master | Antoine T. | Anime les dailies, lève les blocages, garant des règles SCRUM |
| Product Owner | Hugo P. | Priorise le backlog, arbitre la portée du sprint |
| Dev Team | Tous | Réalisation technique |

---

## 3. User stories planifiées et statut

| US | Description | Assigné | Status |
|---|---|---|---|
| US-COMBAT-X | Brancher `ResolveurCombat` dans les phases Combats Subis / Offensifs | Benjamin S. + Hugo P. | ✅ Done |
| US-COMBAT-10 | Conséquences sévères de défaite (anéantissement armée, pertes civiles, moral) | Benjamin S. | ✅ Done |
| US-COMBAT-11 | Butin de victoire (40 % des ressources du défenseur) | Benjamin S. | ✅ Done |
| US-IA-04 / 05 | IA Équilibrée : recrutement, équilibrage population, amélioration, attaque | Fabien S. | ✅ Done |
| US-IA-06 | Attaques IA aléatoires et répétables (probabilité variable selon effectif) | Fabien S. | ✅ Done |
| US-IA-07 | Trêve de début de partie (pas d'attaque IA avant le tour 10) | Fabien S. | ✅ Done |
| US-MIL-01 / 02 | Système recrue → unité équipée (Role.SOLDAT + ActionMobiliser) | Benjamin S. + Léna C. | ✅ Done |
| US-MIL-03 | Déblocage des 4 types d'unités selon niveau Caserne (1/2/3/4) | Benjamin S. + Mathéo D. | ✅ Done |
| US-MIL-04 | Démobilisation (renvoyer un soldat équipé en recrue) | Benjamin S. | ✅ Done |
| US-MIL-05 | Posture de combat sélectionnable depuis l'UI | Benjamin S. + Antoine T. | ✅ Done |
| US-EVENT-X | Catalogue de 6 événements branchés + tirage pondéré | Fabien S. | ✅ Done |
| US-EVENT-08 | Validation financière des choix d'événement (désactivation si pas finançable) | Fabien S. + Antoine T. | ✅ Done |
| US-EVENT-09 | Choix "désespoir" garanti dispo sur la Sécheresse | Fabien S. | ✅ Done |
| US-MARCHE-01 / 02 | Marché : échange entre 2 ressources, taux qui s'améliore avec le niveau | Mathéo D. | ✅ Done |
| US-MARCHE-03 | Onglet Marché dans le dashboard avec UI complète | Mathéo D. + Antoine T. | ✅ Done |
| US-END-01 / 02 | Conditions de victoire / défaite + écran de fin de partie | Hugo P. + Antoine T. | ✅ Done |
| US-UX-05 | Refonte médiévale complète (palette, polices, composants custom) | Antoine T. | ✅ Done |
| US-UX-06 | Système d'onglets custom (CardLayout + onglets stylisés) | Antoine T. | ✅ Done |
| US-UX-07 | Rapport de combat dédié (modal détaillé avec effectifs avant/après) | Hugo P. + Antoine T. | ✅ Done |
| US-UX-08 | Rapport de fin de tour avec deltas (ressources, population, bâtiments) | Hugo P. + Antoine T. | ✅ Done |
| US-UX-09 | Réorganisation du flux fin de tour (combat → bilan → événement) | Hugo P. | ✅ Done |
| US-CFG-01 | Minimum 1 bot adverse (impossible de jouer seul) | Antoine T. | ✅ Done |
| US-SAVE-01 à 03 | Sauvegarde / chargement (5 slots + autosave) | Antoine T. + Hugo P. | ❌ Reporté livraison |
| US-DIPLO-01 à 03 | Diplomatie minimale (paix / guerre / alliance) | Fabien S. | ❌ Reporté livraison |

**Vélocité** : 22 US complètes sur 24 planifiées (~92 %). Les 2 reportées (Sauvegarde, Diplomatie) sont des fonctionnalités optionnelles selon le cahier des charges.

---

## 4. Contributions par membre

### 4.1 Hugo Pignatone — *Core & Cycle de tour (E1)*

**Périmètre** : Réorganisation du flux de fin de tour pour insérer les rapports avant l'événement du nouveau tour. Implémentation du `BilanTour` (snapshot des valeurs en début de tour). Conditions de victoire / défaite. Communication des changements entre phases.

**Fichiers livrés** :
- `src/Modele/partie/BilanTour.java` *(créé)* — Snapshot ressources + population + armée + moral + bâtiments
- `src/Controleur/ControleurPartie.java` *(refactor majeur)* — Découpage en 5 phases : avancer jusqu'à événement → rapport combat → bilan → événement → finir le tour
- `src/Modele/partie/Partie.java` *(modifié)* — `batraillesDuTour`, `enregistrerBataille`, `viderBatraillesDuTour`
- `src/Modele/partie/PartieBuilder.java` *(modifié)* — Minimum 1 bot

**Difficultés rencontrées** :
- Bug initial : en transformant le `do-while` original en `while`, la boucle ne s'exécutait plus du tout (on était en `EtatPlanification` donc `enAttenteJoueur=true` dès le départ). Fix : séparer en deux méthodes `avancerDepuisPlanification` (do-while) et `avancerJusquaJoueur` (while), avec sémantiques distinctes.
- Bug "tour qui passe par 2" : avec un seul `do-while` partagé, si pas d'événement à ce tour, la phase 1 finissait déjà le tour et la phase 4 entamait le suivant. Solution : la phase 4 ne fait `passerEtape` que si on n'est pas déjà revenu en planification.

**Prochains pas livraison** : Câblage de la sauvegarde, derniers ajustements d'équilibrage.

---

### 4.2 Léna Charrière — *Royaume & Économie (E2)*

**Périmètre** : Ajout du rôle `SOLDAT` (recrue civile) dans `Role`, retrait du rôle `ESPION` (différé en version future). Refonte de l'onglet Économie (3 blocs Population/Recrutement/Taxes). Branchement du bouton Recruter villageois (100 nourriture → 1 inactif).

**Fichiers livrés** :
- `src/Modele/population/Role.java` *(modifié)* — Ajout `SOLDAT`, retrait `ESPION`
- `src/Vue/onglets/OngletEconomie.java` *(refonte)* — Style médiéval, ligne SOLDAT avec coût recrutement, sélecteur taxes
- `src/Controleur/ControleurEconomie.java` *(modifié)* — Branchement +/− sur tous les rôles (le `SOLDAT` est automatiquement géré par la boucle)
- `src/Modele/action/ActionRecruterVillageois.java` *(existant, validé)* — Conversion 100 nourriture → 1 inactif

**Difficultés rencontrées** :
- Sémantique `Role.SOLDAT` ambigüe au début : recrue ou soldat équipé ? Décision : `Role.SOLDAT` = recrue civile assignée à l'armée mais pas encore équipée. La mobilisation (avec coût or) la fait quitter `Population` et rejoindre `Armee`.
- Cas de bord remonté en revue : transformer toutes les recrues en soldats équipés faisait apparaître une "perte" dans le bilan (Population diminuait). Résolu côté UI : le HUD et le bilan affichent désormais `Population + Armee` comme effectif global du royaume.

**Prochains pas livraison** : Effet du moral sur la production (taux multiplicatif), tests JUnit sur `Population`.

---

### 4.3 Mathéo Depuydt — *Bâtiments & Marché (E3)*

**Périmètre** : Logique métier du bâtiment Marché (taux d'échange dépendant du niveau), implémentation complète de l'onglet Marché, déblocage des unités militaires par niveau de Caserne. Refonte visuelle de l'onglet Infrastructures (cartes avec 3 colonnes Niveau / État / Coût).

**Fichiers livrés** :
- `src/Modele/infrastructure/Marche.java` *(modifié)* — `tauxEchange()` (niv 1 : 3.0 → niv 5 : 1.0), `quantiteRecue()`
- `src/Modele/action/ActionEchanger.java` *(créé)* — Action immédiate, conversion ressource A → ressource B au taux du Marché
- `src/Vue/onglets/OngletMarche.java` *(créé)* — Sélecteur source/cible (5 ToggleMedieval chacun), spinner montant, affichage taux + résultat en temps réel
- `src/Vue/onglets/OngletInfrastructures.java` *(refonte)* — Carte par bâtiment avec colonnes Niveau / État / Coût, vérification de la Caserne pour le déblocage militaire
- `src/Controleur/ControleurMarche.java` *(créé)* — Wiring du bouton Échanger sur `ActionEchanger`

**Difficultés rencontrées** :
- Layout des cartes infrastructures : version initiale avec `BorderLayout` interne aux colonnes rendait les valeurs invisibles sur petit écran (CENTER se contractait). Passage à `GridLayout(2,1)` garantit que titre et valeur prennent 50 % chacun.
- Décalage du contenu de l'onglet Marché si source = cible : le label de résultat changeait de largeur. Wrappage du label et du bouton dans des panels pleine largeur (centrage stable).

**Prochains pas livraison** : Logique métier de la Bibliothèque (bonus recherche futur), de la Tour de Guet (détection).

---

### 4.4 Benjamin Sarrat — *Militaire & Combat (E4)*

**Périmètre** : Branchement complet du combat dans le cycle de tour via la nouvelle utilitaire `EffetsCombat`. Sévérité de la défaite défensive (anéantissement total de l'armée + 40 % pop civile). Butin de victoire (40 % des ressources adverses). Système recrue → unité équipée avec déblocage par Caserne. Onglet Militaire complet avec popup de choix de cible.

**Fichiers livrés** :
- `src/Modele/combat/EffetsCombat.java` *(créé)* — Centralise pertes militaires, pertes civiles, butin, choc moral. Appelé par les deux phases de combat.
- `src/Modele/combat/BatailleResolue.java` *(créé)* — DTO immuable : Bataille + Rapport + effectifs avant + butin + pertes civiles
- `src/Modele/militaire/Armee.java` *(modifié)* — `recruter(type, n)` (fusionne au lieu de dupliquer), `retirer(type, n)`, `effectifParType`
- `src/Modele/militaire/Unite.java` *(modifié)* — `renforcer(int)` pour la fusion
- `src/Modele/militaire/TypeUnite.java` *(modifié)* — Champ `niveauCaserneRequis` (1/2/3/4)
- `src/Modele/action/ActionMobiliser.java` *(refonte)* — Consomme `Role.SOLDAT`, vérifie niveau Caserne
- `src/Modele/action/ActionDemobiliser.java` *(créé)* — Soldat équipé → recrue (pas de remboursement or)
- `src/Modele/action/ActionAttaquer.java` *(modifié)* — Refuse multi-attaques sur même cible
- `src/Modele/partie/etat/EtatCombatsSubis.java` *(refactor)* — Délègue à `EffetsCombat`
- `src/Modele/partie/etat/EtatCombatsOffensifs.java` *(refactor)* — Délègue à `EffetsCombat`
- `src/Modele/royaume/Royaume.java` *(modifié)* — `aAttaquePlanifieeContre(cible)` pour empêcher doublons
- `src/Vue/onglets/OngletMilitaire.java` *(créé)* — Grille 2×2 des types d'unités, bloc Combat unifié (posture + bouton Attaquer)
- `src/Vue/dialogue/DialogueChoixCible.java` *(créé)* — Popup de sélection de bot adverse
- `src/Vue/dialogue/DialogueRapportCombat.java` *(créé)* — Modal dédié avec carte par bataille
- `src/Controleur/ControleurMilitaire.java` *(créé)* — Wiring posture, recrutement, démobilisation, attaque

**Difficultés rencontrées** :
- Première version de `ActionMobiliser` prenait des inactifs directement. Avec le système recrue, refonte pour exiger `Role.SOLDAT` ; impact en cascade sur la stratégie IA qui ne savait pas pré-assigner ces recrues (corrigé par Fabien).
- Réinitialisation du rapport combat : on a l'effectif APRÈS pertes mais le rapport doit montrer AVANT. Solution : `EffetsCombat` capture `effectifTotal()` avant les modifications et le passe à `BatailleResolue`.

**Prochains pas livraison** : Tests JUnit étendus sur `EffetsCombat` (scénarios victoire écrasante, défaite défensive, égalité).

---

### 4.5 Fabien Serres — *IA & Événements (E5)*

**Périmètre** : Refonte de la `StrategieEquilibree` pour la rendre vivante (recrutement variable, attaques aléatoires, ordre des actions corrigé). Catalogue étendu d'événements (6 types). Système de validation financière des choix d'événement.

**Fichiers livrés** :
- `src/Modele/ia/StrategieEquilibree.java` *(refonte)* — Ordre recruter→équilibrer (bug 0 effectifs corrigé), recrutement variable 3–8 soldats/tour, décision d'attaque probabiliste (25 % base + bonus selon effectif, plafond 65 %), trêve avant tour 10
- `src/Modele/evenement/EffetEvenement.java` *(modifié)* — Méthode `default peutEtreApplique(Royaume)`
- `src/Modele/evenement/Choix.java` *(modifié)* — Délégation `peutEtreChoisi`
- `src/Modele/evenement/EffetSimple.java` *(modifié)* — Vérification or pour `peutEtreApplique`
- `src/Modele/evenement/Secheresse.java` *(modifié)* — Effets custom valident leurs coûts + nouveau choix "Abandon" toujours dispo
- `src/Modele/evenement/AttaqueBrigands.java` *(modifié)* — `EffetSubir` valide ses coûts
- `src/Modele/evenement/Refugies.java` *(modifié)* — `EffetAccueillir` valide son coût nourriture
- `src/Modele/partie/ConditionsFin.java` *(validé)* — Défaite par pop=0 / moral≤5 / tour≥50 ; victoire par or≥5000 / bots éliminés
- `src/config/Equilibrage.java` *(étendu)* — `PROBA_ATTAQUE_IA_BASE`, `TOUR_MIN_PREMIERE_ATTAQUE_IA`, `PERTES_CIVILES_DEFAITE_PCT`, `BUTIN_VICTOIRE_PCT`, `IMPACT_MORAL_DEFAITE_DEFENSIVE`

**Difficultés rencontrées** :
- Bug "bot 0 effectifs au tour 47" : `equilibrerPopulation` était appelé AVANT `recruterSoldats`, donc tous les inactifs partaient en métiers civils, plus rien pour recruter. Fix : inversion de l'ordre dans `jouerTour`.
- `EffetSimple.peutEtreApplique` ne couvrait pas la nourriture. Plutôt que d'étendre le DTO, les effets custom (Sécheresse, Brigands, Refugiés) overrident pour valider leurs coûts spécifiques.

**Prochains pas livraison** : 3 autres stratégies IA (Agressif / Défensif / Commerçant), 2–3 événements supplémentaires (Tempête, Maladie du bétail).

---

### 4.6 Antoine Tuffery — *UI & UX (E6)*

**Périmètre** : Refonte médiévale complète de toutes les vues (palette, polices, composants). Création de tous les nouveaux dialogues (fin de tour, choix de cible, rapport combat, événement). Système d'onglets custom. Écran de fin de partie. Composants réutilisables `BoutonMedieval`, `ToggleMedieval`.

**Fichiers livrés** :
- `src/Vue/theme/Palette.java` *(créé/étendu)* — Couleurs médiévales (or, brun, rouge bannière, pierre, ressources)
- `src/Vue/theme/Polices.java` *(créé)* — Polices Serif standardisées (Titre, Section, Label, Valeur, etc.)
- `src/Vue/theme/BoutonMedieval.java` *(créé)* — JButton custom avec 3 styles (Primaire/Secondaire/Danger), hover, désactivé
- `src/Vue/theme/ToggleMedieval.java` *(créé)* — JToggleButton custom (lisible contrairement au Swing natif bleu)
- `src/Vue/theme/PanneauOrne.java` *(créé)* — Panneau avec coins or
- `src/Vue/menu/VueMenuPrincipal.java` *(refonte Java2D)* — Ciel nocturne, lune, étoiles, château, forêt, bordure or
- `src/Vue/menu/VueNouvellePartie.java` *(refonte)* — Formulaire centré, spinner ≥1 bot
- `src/Vue/FenetreJeu.java` *(modifié)* — Taille 1280×800, CardLayout pour 4 écrans
- `src/Vue/VueHUD.java` *(refonte)* — Compteurs ressources circulaires, encadrés Tour/Population/Moral, bouton Fin de tour rouge danger
- `src/Vue/VueStatusBar.java` *(refonte)* — Gradient pierre, italique beige
- `src/Vue/VueDashboard.java` *(refonte)* — Barre d'onglets custom, CardLayout pour 4 onglets
- `src/Vue/VueFinPartie.java` *(créé)* — Écran victoire/défaite avec stats + classement par or
- `src/Vue/dialogue/DialogueFinTour.java` *(créé)* — Modal récap : ressources, royaume (pop+moral), bâtiments
- `src/Vue/dialogue/DialogueEvenement.java` *(refonte)* — JDialog médiéval custom (au lieu de JOptionPane), désactivation des choix non finançables
- `src/Controleur/ControleurFinPartie.java` *(créé)* — Wiring boutons Rejouer / Menu
- `src/Vue/i18n/strings_fr.properties` & `strings_en.properties` *(étendus)* — Plus de 150 clés ajoutées

**Difficultés rencontrées** :
- Le château flottait au-dessus du sol dans le menu principal : `cyBase` calculé indépendamment du `yLigneSol`. Fix : dessiner le sol d'abord puis ancrer le château.
- `JOptionPane.showOptionDialog` ne permet pas de désactiver des options individuelles : refonte complète du DialogueEvenement en `JDialog` custom avec `BoutonMedieval` pour chaque choix.
- Sur petit écran (portable), le HUD coupait le label Moral car le `FlowLayout.CENTER` ne tenait pas. Passage à `GridLayout(1, 3)` qui garantit l'affichage des 3 encadrés.

**Prochains pas livraison** : Affinage des animations, vérification accessibilité (contrastes), tests visuels sur Windows / Mac.

---

## 5. Démonstration

**Scénario joué pendant le Sprint Review** :

1. **Tours 1-9 (paix forcée)** : Le joueur démarre avec 10 inactifs, 500 or, 100 nourriture. Il assigne 5 fermiers + 2 mineurs + 1 bûcheron + 2 soldats (recrues). Améliore la Ferme à niveau 2. Recrute 2 infanteries (60 or consommé).
2. **Tour 10-15 (premières escarmouches)** : Le bot tente une première attaque (probabilité 30 % avec 5 unités). Le joueur en posture DÉFENSE résiste, le bot perd 50 % de ses troupes. Rapport de combat affiché : `+1 unité ennemie tuée par défense, −2 troupes joueur`.
3. **Tour 16-22 (économie + marché)** : Excédent de bois → conversion en or au Marché (niveau 1 : taux 3:1). Amélioration de la Caserne à niveau 2 → l'**Archer** se débloque dans l'onglet Militaire.
4. **Tour 23-30 (offensive)** : Le joueur attaque le bot avec 12 unités, posture ATTAQUE. Victoire écrasante : bot anéanti, butin = 200 or + 80 bois. Bot lance contre-attaque le tour suivant, joueur en CONTOURNEMENT, victoire serrée.
5. **Tour ~35 (fin)** : Le bot épuisé subit une nouvelle défaite, sa population civile passe à 0 → **VICTOIRE** par élimination de tous les bots. Écran de fin affiche les stats finales.

**Tour moyen** : ~3 secondes (clic Fin de tour → fermeture des rapports → nouveau tour). Aucune exception en console.

---

## 6. Métriques objectives du Sprint 3

| Métrique | Sprint 2 | Sprint 3 | Évolution |
|---|---|---|---|
| Fichiers Java livrés | 66 | 95 | +29 |
| Lignes de code Java (incl. Javadoc) | ~4200 | ~7100 | +2900 |
| Packages créés | 14 | 14 | = |
| Patterns implémentés | 8 | 9 | +1 (Strategy actif) |
| Phases du cycle de tour implémentées | 9 / 9 | 9 / 9 | = (refactor du flux) |
| Bâtiments avec logique métier active | 6 / 9 | 7 / 9 | +1 (Marché) |
| Stratégies IA actives | 0 / 4 | 1 / 4 | +1 (Équilibrée) |
| Événements aléatoires branchés | 1 | 6 | +5 |
| Onglets fonctionnels | 2 | 4 | +2 (Militaire, Marché) |
| Dialogues modaux | 1 | 4 | +3 (FinTour, ChoixCible, RapportCombat) |
| Tests automatisés | 5 | 5 | = (étendus prévus livraison) |
| Clés i18n FR/EN | ~60 | ~210 | +150 |

---

## 7. Décisions techniques prises en revue

- **Pop affichée = civils + soldats équipés** : éviter la fausse impression de perte quand on mobilise (HUD, BilanTour).
- **Snapshot du bilan pris en début de tour** : sinon les coûts d'amélioration (déduits à la planification) seraient invisibles.
- **Flux fin de tour clarifié** : rapport combat → bilan global → événement (du nouveau tour). Plus de popup événement enchâssée au milieu de la résolution.
- **Sévérité de défaite défensive** : armée anéantie + 40 % civils + −12 moral. Volontairement punitif pour raccourcir les parties.
- **Pas d'attaque IA avant tour 10** : laisser le joueur s'installer économiquement.

---

## 8. Objectifs Sprint 4 (Livraison)

**Période** : `[13/06/2026 → 19/06/2026]` (semaine finale)

### 8.1 Sprint Goal

> *Livrer une version finale stable : tests étendus, équilibrage final, polish UI, packaging et démonstration prête à présenter au jury.*

### 8.2 User stories prioritaires

| Priorité | US | Description | Assigné |
|---|---|---|---|
| P0 | TEST-01 à 05 | Tests JUnit étendus (EffetsCombat, ActionMobiliser, OngletMarche logique) | Tous (1 test par membre) |
| P0 | EQUI-01 | Équilibrage final (playtest collectif sur 5 parties) | Tous |
| P0 | DOC-01 | README final, capture d'écrans, schémas UML mis à jour | Antoine T. + Hugo P. |
| P0 | LIVR-01 | Build final + packaging JAR exécutable | Hugo P. |
| P1 | US-SAVE-01 à 03 | Sauvegarde / chargement (5 slots + autosave) | Antoine T. + Hugo P. |
| P2 | US-DIPLO-01 à 03 | Diplomatie minimale | Fabien S. *(si temps)* |

### 8.3 Definition of Done de la livraison

- [ ] `./build.sh` compile sans warning ni erreur sur machine de l'école.
- [ ] `./build.sh run` lance le jeu sans crash sur 30 tours.
- [ ] Tous les onglets fonctionnels, tous les dialogues s'affichent correctement.
- [ ] Au moins 10 tests JUnit qui passent.
- [ ] README mis à jour avec captures d'écran.
- [ ] UML d'architecture cohérent avec le code livré.
- [ ] Démonstration de 10 minutes préparée et répétée en équipe.

---

## 9. Plan de commits/push pour le Sprint 3

Cette section sert de cadrage commit-par-commit pour que l'historique git reflète bien la contribution distribuée de l'équipe.

> **Convention** : 1 commit = 1 unité de travail cohérente. Format `[E<n>] <verbe> <objet>` (ex : `[E4] add EffetsCombat with butin and pertes civiles`).
> **Branche** : `feat/sprint3-<initiales>-<short>` puis PR vers `main`. Review obligatoire par un autre membre.

### Vendredi 06/06 — Kick-off

| Auteur | Commit / push |
|---|---|
| **Hugo P.** | `[E1] add BilanTour (snapshot début de tour)` + push branche `feat/sprint3-hp-bilan` |
| **Antoine T.** | `[E6] add Palette + Polices + BoutonMedieval (theme medieval)` |

### Lundi 09/06

| Auteur | Commit / push |
|---|---|
| **Benjamin S.** | `[E4] refactor Armee with recruter/retirer/effectifParType` |
| **Léna C.** | `[E2] add Role.SOLDAT, remove ESPION` |
| **Mathéo D.** | `[E3] add Marche.tauxEchange + ActionEchanger` |
| **Fabien S.** | `[E5] fix StrategieEquilibree order recruter→equilibrer (bot 0 effectifs)` |

### Mardi 10/06

| Auteur | Commit / push |
|---|---|
| **Hugo P.** | `[E1] refactor terminerTour : combat report → bilan → event flow` |
| **Antoine T.** | `[E6] refonte VueMenuPrincipal medieval (Java2D castle + stars)` |
| **Benjamin S.** | `[E4] add EffetsCombat + BatailleResolue (pertes civiles, butin)` |
| **Mathéo D.** | `[E3] refonte OngletInfrastructures (3 cols Niveau/État/Coût)` |

### Mercredi 11/06

| Auteur | Commit / push |
|---|---|
| **Léna C.** | `[E2] refonte OngletEconomie (medieval + ligne SOLDAT)` |
| **Fabien S.** | `[E5] add peutEtreApplique on EffetEvenement + override sur effets payants` |
| **Benjamin S.** | `[E4] add OngletMilitaire + ControleurMilitaire + ActionDemobiliser` |
| **Antoine T.** | `[E6] refonte DialogueEvenement (JDialog custom, désactivation choix non finançables)` |
| **Mathéo D.** | `[E3] add OngletMarche + ControleurMarche` |

### Jeudi 12/06

| Auteur | Commit / push |
|---|---|
| **Hugo P.** | `[E1] add DialogueFinTour (recap modal medieval)` |
| **Antoine T.** | `[E6] add DialogueRapportCombat + DialogueChoixCible` |
| **Benjamin S.** | `[E4] add deblocage unités par niveau Caserne (niveauCaserneRequis)` |
| **Fabien S.** | `[E5] IA attaques aléatoires (PROBA_ATTAQUE_IA_BASE) + trêve tour < 10` |
| **Léna C.** | `[E2] HUD : population affichée = civils + armée` |
| **Mathéo D.** | `[E3] fix layout OngletMarche (wrapper label centré)` |

### Vendredi 12/06 — Stabilisation et review

| Auteur | Commit / push |
|---|---|
| **Hugo P.** | `[E1] write doc/sprint3-review.md` + `[E1] update README sprint 3` |
| **Antoine T.** | `[E6] i18n : finalisation FR/EN (150 clés sprint 3)` |
| **Tous** | Review croisée des PRs ouvertes, merge sur `main` |

### Règles d'or

- **Pas de force-push** sur `main`.
- **PR review obligatoire** par un autre membre avant merge.
- **Pas plus de 2 PR ouvertes par membre** simultanément (évite les conflits).
- Si un commit touche un fichier d'un autre membre (ex : Hugo modifie `Equilibrage.java` pour ajouter `BUTIN_VICTOIRE_PCT`), tag dans le message : `[E1+E4]`.

---

## Annexe — État du code à la fin du Sprint 3

```
src/
├── Main.java
├── Modele/
│   ├── action/             (10 actions, dont nouvelles : ActionDemobiliser, ActionEchanger)
│   ├── combat/             (5 fichiers, dont nouveaux : EffetsCombat, BatailleResolue)
│   ├── economie/           (5 fichiers, stable)
│   ├── evenement/          (12 fichiers, 6 événements actifs)
│   ├── ia/                 (Strategie Equilibree active)
│   ├── infrastructure/     (9 bâtiments, Marche enrichi)
│   ├── militaire/          (5 fichiers, Armee/Unite enrichis)
│   ├── notification/
│   ├── partie/             (Partie + BilanTour + 9 états)
│   ├── population/         (Role étendu, Population stable)
│   └── royaume/            (helper aAttaquePlanifieeContre)
├── Vue/
│   ├── FenetreJeu.java     (CardLayout 4 écrans)
│   ├── VueHUD.java         (population = civils + armée)
│   ├── VueDashboard.java   (4 onglets)
│   ├── VueStatusBar.java
│   ├── VueFinPartie.java
│   ├── dialogue/           (4 dialogues : Evenement, FinTour, RapportCombat, ChoixCible)
│   ├── menu/               (Principal, NouvellePartie)
│   ├── onglets/            (Economie, Infrastructures, Militaire, Marche)
│   ├── theme/              (Palette, Polices, BoutonMedieval, ToggleMedieval, PanneauOrne)
│   └── i18n/               (FR + EN, ~210 clés)
├── Controleur/
│   └── 6 contrôleurs       (Partie, Economie, Infrastructures, Militaire, Marche, FinPartie)
└── config/
    └── Equilibrage.java    (~50 constantes)
```
