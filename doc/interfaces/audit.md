# Audit des maquettes UI vs code réel

Ce document liste, pour chacune des 4 maquettes SVG fournies par l'équipe,
ce qui doit être **retiré ou modifié** pour que l'interface ne représente
**que ce qui existe vraiment dans le code**.

C'est la référence pour le Sprint 4 (intégration Swing) : on n'implémente
que ce qui est dans la colonne "À garder", et on retire de la maquette
ce qui est listé "À supprimer".

---

## Ce qui existe dans le code (référence)

### Ressources (5)
`OR`, `NOURRITURE`, `BOIS`, `PIERRE`, `SAVOIR`

### Rôles d'habitants (6)
`INACTIF`, `FERMIER`, `MINEUR`, `BUCHERON`, `ERUDIT`, `ESPION`

### Bâtiments (9)
`FERME`, `MINE`, `SCIERIE`, `HABITATIONS`, `CASERNE`, `REMPARTS`,
`MARCHE`, `BIBLIOTHEQUE`, `TOUR_GUET`

### Unités militaires (4)
- `INFANTERIE_LEGERE` (ATK 10, DEF 8)
- `ARCHER` (ATK 12, DEF 6)
- `LANCIER` (ATK 8, DEF 12)
- `CAVALERIE_LOURDE` (ATK 15, DEF 10)

### Postures de combat (3)
`ATTAQUE`, `DEFENSE`, `CONTOURNEMENT`

### Niveaux de taxes (3)
`FAIBLE`, `NORMAL`, `ELEVE`

### Difficulté de partie (3)
`FACILE`, `NORMAL`, `DIFFICILE`

### IA des bots (1 seule)
`StrategieEquilibree` — une seule stratégie, qu'on appelle simplement « Bot ».

### Événements aléatoires (6)
`Epidemie`, `Secheresse`, `FilonDor`, `Refugies`, `BonneRecolte`,
`AttaqueBrigands`

### Actions du joueur (4)
- `ActionAmeliorer` — amélioration de bâtiment (cout en or, bois, pierre)
- `ActionMobiliser` — recrutement de soldats (30 or + 1 inactif par soldat)
- `ActionAttaquer` — attaque d'un autre royaume
- `ActionRecruterVillageois` — +1 inactif contre 100 nourriture

### Conditions de fin
- **Victoire** : 5000 or atteint OU tous les bots éliminés
- **Défaite** : population à 0 OU moral ≤ 5 OU tour 50 atteint

---

## 1. `menu_principal_v2.svg` / `menu_ba7ion_v3.svg`

### À garder
- Décor médiéval : château, chevaliers, lune, étoiles, torches, ambiance
- Titre **BA7ION** doré + sous-titre « Gestion et stratégie d'un royaume médiéval »
- Bouton **Nouvelle partie**
- Bouton **Options** (déjà implémenté avec popup "non disponible")
- Bouton **Quitter**
- Footer crédits / version

### À supprimer
- ❌ Bouton **« CONTINUER »** : pas de système de save/load fonctionnel
  pour charger une partie depuis le menu. `GestionnaireSauvegardes` existe
  mais aucun écran ne sait lister les slots et charger.

---

## 2. `game_screen_ba7ion.svg` (écran de jeu principal)

### Barre du haut — Ressources et tour

**À garder** :
- Or, Nourriture, Bois, Pierre — avec quantité, capacité max, et solde par tour
- Indicateur **Tour courant** (numéro)
- Bouton **Fin de tour**

**À ajouter** :
- ➕ Ressource **Savoir** (5ème ressource — manquante dans la maquette)

**À supprimer** :
- ❌ Indicateur **« Saison »** (Automne 🍂) — les saisons ne sont pas
  modélisées dans le code. Pas de `Saison.java`.

### Panneau gauche — Royaume et population

**À garder** :
- Nom du royaume
- Total population / capacité de logement
- Effectifs par rôle avec boutons +/− (réaffectation entre rôles)
- Moral 0-100
- Sélecteur de taxes (FAIBLE / NORMALES / ELEVÉES)

**À modifier** :
- Liste des rôles : remplacer **« Forgerons »** par rien (il n'y a que
  6 rôles dans le code). Les rôles à afficher sont exactement :
  - 🌾 **Fermier** (produit nourriture)
  - ⛏️ **Mineur** (produit pierre + or)
  - 🌲 **Bûcheron** (produit bois)
  - 📖 **Érudit** (produit savoir)
  - 🕵️ **Espion** (pas d'effet métier en Sprint 3, prévu pour espionnage)
  - 💤 **Inactif** (par défaut, pas de production)

**À supprimer** :
- ❌ **Forgerons** : pas dans `Role.java`

**À ajouter** :
- ➕ Bouton **« Recruter villageois (100 🌾) »** sous la liste des rôles
  — appelle `ActionRecruterVillageois` : −100 nourriture, +1 inactif

### Panneau droit — Armée et actions

**À garder** :
- Force totale de l'armée
- Liste des 4 types d'unités avec effectif et stats (ATK/DEF correctes ci-dessus)
- Indicateur « en garnison » (bonus défensif des Remparts)

**À modifier** :
- Stats des unités : les valeurs dans la maquette (ATK 8/12/18/10, DEF 4/2/12/14)
  ne correspondent pas au code. Utiliser les vraies valeurs :
  - Infanterie légère : ATK **10** DEF **8**
  - Archer : ATK **12** DEF **6**
  - Lancier : ATK **8** DEF **12**
  - Cavalerie lourde : ATK **15** DEF **10**

**Actions à garder** :
- ✅ **Recruter** (ActionMobiliser, recrute infanterie légère)
- ✅ **Construire/Améliorer** (ActionAmeliorer)
- ✅ **Attaquer** (ActionAttaquer)
- ✅ **Marché** : pas vraiment d'action métier mais Marché est un bâtiment
  qui existe (action future)

**À supprimer** :
- ❌ **Espionner** : `Role.ESPION` existe mais aucune `ActionEspionner`
  n'est implémentée
- ❌ **Décrets** : `Decret`, `BrancheDecret`, `GestionnaireDecrets`
  n'existent pas dans le code. À reporter à un futur sprint.

### Barre du bas — Journal, infrastructures, mini-carte

**À garder** :
- **Journal** : flux des derniers événements (les messages du `VueStatusBar`
  actuel peuvent y être routés)
- **Infrastructures** : grille des 9 bâtiments avec niveau et bouton
  Améliorer (déjà implémentée dans `OngletInfrastructures.java`)

**À modifier** :
- Liste des bâtiments : remplacer **« Forge »** par les bâtiments réels
  manquants (Habitations, Marché, Scierie, Tour de Guet)
- Les 9 bâtiments à afficher sont exactement :
  - 🌾 **Ferme**
  - ⛏️ **Mine**
  - 🌲 **Scierie**
  - 🏘️ **Habitations**
  - ⚔️ **Caserne**
  - 🏰 **Remparts**
  - 🪙 **Marché**
  - 📖 **Bibliothèque**
  - 👁️ **Tour de Guet**

**À supprimer** :
- ❌ **Forge** : pas dans `TypeBatiment.java`
- ❌ **Mini-carte avec territoires** : le jeu n'a pas de carte ni de
  positions géographiques. Les royaumes ne sont pas placés sur une carte.
  À retirer entièrement de l'écran.

### Popup événement
**À garder** :
- Structure générale (titre événement, description, boutons de choix)
- L'événement peut être une `Epidemie`, `Secheresse`, `FilonDor`,
  `Refugies`, `BonneRecolte` ou `AttaqueBrigands`
- Nombre de choix : 2 ou 3 selon l'événement (cf. classes événements)

**Pas de modification structurelle** — les contenus changent selon
l'événement tiré, ce qui est déjà géré par `DialogueEvenement`.

---

## 3. `end_of_turn_ba7ion.svg` (rapport de fin de tour)

### Section ressources

**À modifier** :
- Tableau des ressources : ajouter une 5ème ligne **Savoir** (manquante)
- Les soldes doivent être calculés depuis les vraies productions :
  - Or : taxes (NORMAL = 2 × population) + Mine (1 par mineur) + événements
  - Nourriture : Ferme (2 par fermier) − consommation (1 par habitant)
  - Bois : Scierie (2 par bûcheron)
  - Pierre : Mine (1 par mineur)
  - Savoir : Bibliothèque (1 par érudit)

### Section population

**À modifier** :
- « Naissances » : remplacer par **« Nouveaux villageois recrutés »** (les
  villageois ne naissent pas automatiquement, ils sont recrutés à 100 nourriture)
- « Décès » : ✅ OK (vient de la famine ou des événements négatifs)

**À supprimer** :
- ❌ Pas de fermiers automatiquement assignés (le joueur le fait via +/−)

### Section militaire
- ✅ OK : recrutement + combats subis/offensifs + force totale

### Section événement
- ✅ OK : afficher l'événement déclenché ce tour avec la décision prise.
- **À modifier** : retirer la mention « durée X tours » — dans le code,
  les événements sont **ponctuels** (un seul tour d'effet), pas persistants.

### Section bâtiments
- ✅ OK : progression des bâtiments en chantier
- Mettre Bibliothèque / Tour de Guet / Marché si applicable

**À supprimer** :
- ❌ **Section « Décret actif »** : pas dans le code. À retirer entièrement.

---

## 4. `end_of_game_ba7ion.svg` (écran de fin de partie)

### En-tête victoire/défaite
- ✅ OK : badge VICTOIRE ou DÉFAITE selon `ConditionsFin.Etat`
- Titre : utiliser **« VICTOIRE »** ou **« DÉFAITE »**
- Raison de fin :
  - Victoire 5000 or : « Vous avez accumulé une fortune immense ! »
  - Victoire bots éliminés : « Vous avez conquis tous les royaumes adverses ! »
  - Défaite population 0 : « Votre royaume est dépeuplé. »
  - Défaite moral ≤ 5 : « Votre peuple s'est révolté. »
  - Défaite tour 50 : « Le temps imparti à votre règne est écoulé. »

### Statistiques

**À garder** :
- Tours survécus
- Or total amassé (Tresor.quantite(OR) final)
- Population finale
- Bâtiments améliorés (compter ceux avec niveau > 1)
- Victoires/Défaites en combat (compter via les rapports)

**À supprimer** :
- ❌ « Troupes perdues » : on peut le calculer, mais c'est gadget si pas tracké
- ❌ « **Décrets promulgués** » : pas de décrets dans le code
- ❌ « **Score final** en points » : pas de système de score dans le code.
  À supprimer ou à implémenter en Sprint 4 avec une formule simple
  (ex: `or + 10*population + 100*tours_survécus`)

### Classement

**À supprimer ou simplifier** :
- ❌ « **Rang en ligne #142 / 8 450 joueurs** » : pas de backend en ligne,
  le jeu est solo local.
- ❌ « **Meilleur score précédent** » : pas de système de persistance des
  high scores entre parties. À implémenter ou retirer.
- ❌ Stratégies « Agressif / Défensif / Équilibré » distinctes : il n'y a
  qu'**une seule stratégie d'IA** (`StrategieEquilibree`). Tous les bots
  ont la même.
- ❌ Citation médiévale en italique : décoratif OK, mais à retirer si on
  veut rester épuré

**À garder/modifier** :
- Classement des royaumes par or final (ou par population)
- Maximum 5 entrées : Joueur + jusqu'à 4 bots
- Indiquer **« Bot »** générique au lieu de « Aldric », « Morvaine », etc.
  (les bots s'appellent juste « Bot 1 », « Bot 2 », ... dans `PartieBuilder`)

### Boutons
**À garder** :
- ✅ **Rejouer** (relance avec même configuration)
- ✅ **Nouvelle partie** (retour écran de config)
- ✅ **Menu principal**

### Pied de page

**À garder** :
- Durée de partie

**À supprimer** :
- ❌ « **Partie sauvegardée automatiquement** » : pas d'autosave fonctionnel
- ❌ « **Rang en ligne** » : pas de online

---

## Récapitulatif global des éléments à supprimer

| Élément | Présent dans | Raison |
|---|---|---|
| Forgeron (rôle) | Écran de jeu | Pas dans `Role.java` |
| Forge (bâtiment) | Écran de jeu | Pas dans `TypeBatiment.java` |
| Saison / Météo | Écran de jeu | Pas de `Saison.java` |
| Mini-carte | Écran de jeu | Pas de carte / positions dans le code |
| Décrets | Écran de jeu, fin de tour, fin de partie | Pas dans le code |
| Espionner (action) | Écran de jeu | Pas d'`ActionEspionner` |
| Bouton Continuer | Menu principal | Pas d'écran de chargement |
| Stratégies multiples (Agressif, Défensif…) | Fin de partie | 1 seule IA (`StrategieEquilibree`) |
| Score / Rang en ligne / High score | Fin de partie | Pas de système de score |
| Sauvegarde automatique mention | Fin de partie | Autosave pas branchée |
| Effet « durée X tours » des événements | Écran de jeu, fin de tour | Événements ponctuels |

## Récapitulatif des éléments à ajouter / corriger

| Élément | Où | Détail |
|---|---|---|
| Savoir (ressource) | Barre du haut, fin de tour | 5ème ressource manquante |
| Bouton « Recruter villageois » | Onglet population | 100 nourriture → 1 inactif |
| Stats correctes des unités | Panneau droit | ATK 10/12/8/15, DEF 8/6/12/10 |
| 9 bâtiments du code (sans Forge) | Onglet bâtiments | Voir liste ci-dessus |
| Raison de fin selon `ConditionsFin` | Fin de partie | 5 raisons possibles |
| Nommer les bots « Bot 1 / Bot 2... » | Fin de partie | C'est ce que fait `PartieBuilder` |
