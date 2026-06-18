# BA7ION — UML complet du projet

Diagrammes UML de **tout** le code (classes, interfaces, énumérations,
relations), regroupés par sous-système pour rester lisibles. Format **Mermaid**
(rendu automatique dans l'aperçu Markdown de VS Code / IntelliJ / GitHub).

**Légende des relations :**
`<|--` héritage (extends) · `<|..` implémentation (implements) ·
`*--` composition · `o--` agrégation · `-->` association · `..>` dépendance.
`$` = membre statique · `*` = méthode abstraite · `~T~` = générique.

---

## 0. Vue d'ensemble des packages

```mermaid
flowchart LR
    Main --> Controleur
    Main --> Vue
    Controleur --> Vue
    Controleur --> Modele
    Vue -. observe / lecture seule .-> Modele
    Modele --> config
    Modele --> Gson[(Gson lib)]

    subgraph Modele
      direction TB
      partie --> royaume
      partie --> etat[partie.etat]
      royaume --> economie
      royaume --> population
      royaume --> infrastructure
      royaume --> militaire
      royaume --> action
      royaume --> ia
      combat --> militaire
      partie --> evenement
      partie --> combat
      persistance --> partie
      tous[. . .] --> notification
    end
```

> Règle d'or MVC : **Vue → Modèle en lecture seule** (Observer), **Contrôleur**
> branche les écouteurs. Aucun package `Modele` n'importe Swing.

---

## 1. Cœur d'une partie (machine à états)

```mermaid
classDiagram
    class Observable
    class Partie {
      -Royaume joueur
      -List~Royaume~ bots
      -Tour tour
      -List~BatailleResolue~ batraillesDuTour
      -Evenement evenementEnAttente
      -boolean grenouilleEmpoisonneeDeclenchee
      -Random aleatoire
      +passerEtape()
      +numeroTour() int
      +combatsAutorises() boolean
      +enAttenteJoueur() boolean
      +notifier(Notification)
      +declencherEvenement(Evenement)
      +resoudreEvenement(Choix)
      +tousLesRoyaumes() List~Royaume~
    }
    class Tour {
      -int numero
      -EtatTour etat
      +executerPhaseCourante(Partie)
      +enAttenteJoueur() boolean
      +definirNumero(int)
    }
    class EtatTour {
      <<interface>>
      +executer(Partie)
      +suivant() EtatTour
      +nomCle() String
    }
    class EtatPlanification
    class EtatProduction
    class EtatConsommation
    class EtatActionsDifferees
    class EtatCombatsSubis
    class EtatCombatsOffensifs
    class EtatTourIA
    class EtatEvenement
    class EtatFinTour
    class ConditionsFin {
      +evaluer(Partie)$ Etat
    }
    class Etat {
      <<enumeration>>
      EN_COURS
      VICTOIRE
      DEFAITE
    }
    class BilanTour {
      +numeroTour() int
      +ressource(Ressource) int
      +populationTotale() int
      +effectifArmee() int
      +moral() int
      +niveau(TypeBatiment) int
      +enChantier(TypeBatiment) boolean
    }
    class PartieBuilder {
      -String nomJoueur
      -int nombreBots
      -long graineAleatoire
      -Difficulte difficulte
      +build() Partie
      +depuisSauvegarde(Sauvegarde)$ Partie
    }

    Observable <|-- Partie
    Partie *-- Tour
    Partie o-- "1..*" Royaume
    Tour o-- EtatTour
    EtatTour <|.. EtatPlanification
    EtatTour <|.. EtatProduction
    EtatTour <|.. EtatConsommation
    EtatTour <|.. EtatActionsDifferees
    EtatTour <|.. EtatCombatsSubis
    EtatTour <|.. EtatCombatsOffensifs
    EtatTour <|.. EtatTourIA
    EtatTour <|.. EtatEvenement
    EtatTour <|.. EtatFinTour
    ConditionsFin ..> Partie
    ConditionsFin --> Etat
    PartieBuilder ..> Partie
```

Enchaînement des états (chaîne `suivant()`) :
`Planification → Production → Consommation → ActionsDifferees → CombatsSubis →
CombatsOffensifs → TourIA → Evenement → FinTour → (Planification)`.

---

## 2. Royaume, économie et population

```mermaid
classDiagram
    class Observable
    class Royaume {
      -String nom
      -Tresor tresor
      -Population population
      -Moral moral
      -List~Batiment~ batiments
      -Armee armee
      -FileActions fileActions
      -List~Bataille~ bataillesOffensives
      -NiveauTaxes niveauTaxes
      -StrategieIA strategieIA
      +reaffecter(Role, Role, int) boolean
      +appliquerTaxes()
      +appliquerConsommationCivile(Random)
      +batiment(TypeBatiment) Batiment
      +estBot() boolean
    }
    class Tresor {
      -Map~Ressource,Stock~ stocks
      +quantite(Ressource) int
      +ajouter(Ressource, int) int
      +retirer(Ressource, int) int
      +definirQuantite(Ressource, int)
      +contient(Ressource, int) boolean
    }
    class Stock {
      -int quantite
      -int capaciteMax
      +ajouter(int) int
      +retirer(int) int
      +definirQuantite(int)
    }
    class Ressource {
      <<enumeration>>
      OR
      NOURRITURE
      BOIS
      PIERRE
      SAVOIR
    }
    class NiveauTaxes {
      <<enumeration>>
      FAIBLE
      NORMAL
      ELEVE
      +orParHabitant() int
      +impactMoralParTour() int
    }
    class Population {
      -Map~Role,Integer~ effectifs
      -int capaciteLogement
      +total() int
      +effectif(Role) int
      +reaffecter(Role, Role, int) boolean
      +ajouterInactifs(int) int
      +retirerHabitants(int, Random) int
      +definirEffectif(Role, int)
    }
    class Role {
      <<enumeration>>
      INACTIF
      FERMIER
      MINEUR
      BUCHERON
      ERUDIT
      SOLDAT
    }
    class Moral {
      -int valeur
      +valeur() int
      +ajuster(int) int
      +definir(int)
    }

    Observable <|-- Royaume
    Royaume *-- Tresor
    Royaume *-- Population
    Royaume *-- Moral
    Royaume *-- Armee
    Royaume *-- FileActions
    Royaume o-- "9" Batiment
    Royaume --> NiveauTaxes
    Royaume o-- StrategieIA
    Tresor *-- "5" Stock
    Stock --> Ressource
    Population --> Role
```

---

## 3. Infrastructure (les bâtiments)

```mermaid
classDiagram
    class Batiment {
      <<abstract>>
      #int niveau
      #boolean endommage
      #int toursRestants
      +produire(Royaume)
      +niveau() int
      +enChantier() boolean
      +peutEtreAmeliore() boolean
      +demarrerChantier()
      +restaurer(int, int)
      +type()* TypeBatiment
      #appliquerProduction(Royaume)*
    }
    class TypeBatiment {
      <<enumeration>>
      FERME
      MINE
      SCIERIE
      HABITATIONS
      CASERNE
      REMPARTS
      MARCHE
      BIBLIOTHEQUE
      TOUR_GUET
    }
    class Ferme
    class Mine
    class Scierie
    class Bibliotheque
    class Habitations
    class Caserne
    class Remparts {
      +bonusDefensif() int
    }
    class TourDeGuet {
      +porteeDetection() int
    }
    class Marche {
      +tauxEchange() double
      +quantiteRecue(int) int
    }

    Batiment <|-- Ferme
    Batiment <|-- Mine
    Batiment <|-- Scierie
    Batiment <|-- Bibliotheque
    Batiment <|-- Habitations
    Batiment <|-- Caserne
    Batiment <|-- Remparts
    Batiment <|-- TourDeGuet
    Batiment <|-- Marche
    Batiment --> TypeBatiment
```

---

## 4. Militaire et moteur de combat

```mermaid
classDiagram
    class Armee {
      -List~Unite~ unites
      -PostureCombat posture
      +effectifTotal() int
      +effectifParType(TypeUnite) int
      +recruter(TypeUnite, int)
      +retirer(TypeUnite, int) int
      +definirPosture(PostureCombat)
    }
    class Unite {
      -TypeUnite type
      -int effectif
      +subirPertes(int) int
      +renforcer(int)
    }
    class TypeUnite {
      <<enumeration>>
      INFANTERIE_LEGERE
      ARCHER
      LANCIER
      CAVALERIE_LOURDE
      +attaqueBase() int
      +defenseBase() int
      +niveauCaserneRequis() int
    }
    class PostureCombat {
      <<enumeration>>
      ATTAQUE
      DEFENSE
      CONTOURNEMENT
      +multAttaque() double
      +multDefense() double
      +utiliseRemparts() boolean
    }
    class TableAvantages {
      +BONUS_AVANTAGE$ double
      +bonusContre(TypeUnite, TypeUnite)$ double
    }
    class Bataille {
      -Royaume attaquant
      -Royaume defenseur
      -PostureCombat posture
    }
    class ResolveurCombat {
      +resoudre(Armee, Armee, PostureCombat, int, long)$ RapportCombat
    }
    class RapportCombat {
      -Vainqueur vainqueur
      -int pertesAttaquant
      -int pertesDefenseur
      +puissanceAttaquant() double
      +puissanceDefenseur() double
    }
    class Vainqueur {
      <<enumeration>>
      ATTAQUANT
      DEFENSEUR
      EGALITE
    }
    class BatailleResolue {
      -int effectifAvantAttaquant
      -int effectifAvantDefenseur
      -int pertesCivilesDefenseur
      -Map~Ressource,Integer~ butin
    }
    class EffetsCombat {
      +appliquer(Bataille, Partie)$ BatailleResolue
    }

    Armee o-- "*" Unite
    Armee --> PostureCombat
    Unite --> TypeUnite
    Bataille --> Royaume
    Bataille --> PostureCombat
    ResolveurCombat ..> Armee
    ResolveurCombat ..> TableAvantages
    ResolveurCombat ..> RapportCombat
    RapportCombat --> Vainqueur
    EffetsCombat ..> Bataille
    EffetsCombat ..> ResolveurCombat
    EffetsCombat ..> BatailleResolue
```

---

## 5. Actions (patron Commande)

```mermaid
classDiagram
    class Action {
      <<interface>>
      +estExecutable(Royaume) boolean
      +executer(Royaume)
      +description() String
    }
    class FileActions {
      -List~Action~ actions
      +ajouter(Action)
      +retirer(Action) boolean
      +executerToutes(Royaume) int
    }
    class ActionAmeliorer {
      -TypeBatiment type
    }
    class ActionMobiliser {
      -TypeUnite type
      -int effectif
    }
    class ActionDemobiliser {
      -TypeUnite type
      -int effectif
    }
    class ActionAttaquer {
      -Royaume cible
      -PostureCombat posture
    }
    class ActionEchanger {
      -Ressource source
      -Ressource cible
      -int montantSource
    }
    class ActionRecruterVillageois

    Action <|.. ActionAmeliorer
    Action <|.. ActionMobiliser
    Action <|.. ActionDemobiliser
    Action <|.. ActionAttaquer
    Action <|.. ActionEchanger
    Action <|.. ActionRecruterVillageois
    FileActions o-- "*" Action
```

---

## 6. Intelligence artificielle (Strategy + Factory)

```mermaid
classDiagram
    class StrategieIA {
      <<interface>>
      +jouerTour(Royaume, Partie)
    }
    class StrategieEquilibree {
      +jouerTour(Royaume, Partie)
    }
    class FabriqueIA {
      +creerEquilibree()$ StrategieIA
    }
    StrategieIA <|.. StrategieEquilibree
    FabriqueIA ..> StrategieEquilibree
```

---

## 7. Événements

```mermaid
classDiagram
    class Evenement {
      <<abstract>>
      -String titre
      -String description
      -List~Choix~ choix
      +titre() String
      +choix() List~Choix~
      #ajouterChoix(Choix)
    }
    class Choix {
      -String libelle
      -EffetEvenement effet
      +peutEtreChoisi(Royaume) boolean
    }
    class EffetEvenement {
      <<interface>>
      +appliquer(Royaume, Random)
      +peutEtreApplique(Royaume) boolean
    }
    class EffetSimple {
      -int deltaOr
      -int habitantsPerdus
      -int deltaMoral
    }
    class CatalogueEvenements {
      +ENTREES$ Entree[]
      +POIDS_TOTAL$ int
    }
    class TirageEvenement {
      +tirer(Random)$ Evenement
    }
    class Epidemie
    class Secheresse
    class FilonDor
    class Refugies
    class BonneRecolte
    class AttaqueBrigands
    class GrenouilleEmpoisonnee

    Evenement *-- "*" Choix
    Choix --> EffetEvenement
    EffetEvenement <|.. EffetSimple
    Choix ..> EffetSimple
    Evenement <|-- Epidemie
    Evenement <|-- Secheresse
    Evenement <|-- FilonDor
    Evenement <|-- Refugies
    Evenement <|-- BonneRecolte
    Evenement <|-- AttaqueBrigands
    Evenement <|-- GrenouilleEmpoisonnee
    CatalogueEvenements ..> Evenement
    TirageEvenement ..> CatalogueEvenements
```

---

## 8. Notifications (patron Observateur)

```mermaid
classDiagram
    class Notification {
      -TypeNotification type
      -Object donnee
      +type() TypeNotification
      +donnee() Object
    }
    class TypeNotification {
      <<enumeration>>
      TOUR_DEMARRE
      PHASE_CHANGEE
      TOUR_TERMINE
      TRESOR_CHANGE
      POPULATION_CHANGEE
      MORAL_CHANGE
      BATIMENTS_CHANGES
      FILE_ACTIONS_CHANGEE
      EVENEMENT_EN_ATTENTE
      EVENEMENT_RESOLU
      PARTIE_GAGNEE
      PARTIE_PERDUE
    }
    Notification --> TypeNotification
```

---

## 9. Persistance (sauvegarde JSON via Gson)

```mermaid
classDiagram
    class Sauvegarde {
      +VERSION$ int
      +int numeroTour
      +long graineAleatoire
      +boolean grenouilleEmpoisonneeDeclenchee
      +EtatRoyaume joueur
      +List~EtatRoyaume~ bots
      +versJson() String
      +depuisJson(String)$ Sauvegarde
    }
    class EtatRoyaume {
      +String nom
      +boolean estBot
      +int moral
      +Map~Ressource,Integer~ ressources
      +Map~Role,Integer~ populationParRole
      +Map~TypeBatiment,Integer~ niveauxBatiments
      +Map~TypeUnite,Integer~ armee
      +appliquerA(Royaume)
    }
    class Integrite {
      +checksum(String)$ String
    }
    class GestionnaireSauvegardes {
      +sauvegarderAuto(Partie)$
      +chargerDepuis(File)$ Sauvegarde
      +fichierAuto(Partie)$ File
    }
    class Gson {
      <<library>>
    }

    Sauvegarde *-- "1+bots" EtatRoyaume
    Sauvegarde ..> Integrite
    Sauvegarde ..> Gson
    GestionnaireSauvegardes ..> Sauvegarde
    EtatRoyaume ..> Royaume
```

---

## 10. Configuration

```mermaid
classDiagram
    class Equilibrage {
      <<utility>>
      +POPULATION_INITIALE$ int
      +PRODUCTION_NOURRITURE_PAR_FERMIER$ int
      +BONUS_FERME_PAR_NIVEAU$ double
      +TOUR_DEBUT_COMBATS$ int
      +OR_VICTOIRE_PROSPERITE$ int
      +coutAmelioration(TypeBatiment, int)$ Map
    }
    class Difficulte {
      <<enumeration>>
      FACILE
      NORMAL
      DIFFICILE
      +bonusOrInitial() int
    }
```

---

## 11. Couche Vue (Swing)

```mermaid
classDiagram
    class JFrame
    class FenetreJeu {
      +afficherMenu()
      +afficherJeu(Partie)
      +afficherFinPartie(Partie, Etat)
      +hud() VueHUD
      +dashboard() VueDashboard
    }
    class Observer {
      <<interface>>
    }
    class VueHUD {
      +boutonFinTour() BoutonMedieval
      +update(Observable, Object)
    }
    class VueDashboard {
      +ongletEconomie() OngletEconomie
      +ongletMilitaire() OngletMilitaire
    }
    class VueStatusBar {
      +setMessage(String)
    }
    class VueFinPartie
    class VueMenuPrincipal
    class VueNouvellePartie
    class OngletEconomie
    class OngletInfrastructures
    class OngletMarche
    class OngletMilitaire
    class DialogueEvenement {
      +afficher(Component, Evenement, Royaume)$ Choix
    }
    class DialogueFinTour
    class DialogueRapportCombat
    class DialogueChoixCible

    JFrame <|-- FenetreJeu
    FenetreJeu *-- VueHUD
    FenetreJeu *-- VueDashboard
    FenetreJeu *-- VueStatusBar
    FenetreJeu *-- VueMenuPrincipal
    FenetreJeu *-- VueNouvellePartie
    FenetreJeu ..> VueFinPartie
    VueDashboard *-- OngletEconomie
    VueDashboard *-- OngletInfrastructures
    VueDashboard *-- OngletMarche
    VueDashboard *-- OngletMilitaire
    Observer <|.. VueHUD
    Observer <|.. OngletEconomie
    Observer <|.. OngletInfrastructures
    Observer <|.. OngletMarche
    Observer <|.. OngletMilitaire
    VueHUD ..> Partie
```

**Thème** (`Vue/theme/`) : `Palette` (couleurs), `Polices` (polices),
`BoutonMedieval` / `ToggleMedieval` (extends `JButton`/`JToggleButton`),
`PanneauOrne` (extends `JPanel`), `ChampsMedievaux` (helpers statiques de style).

---

## 12. Contrôleurs

```mermaid
classDiagram
    class ControleurMenu {
      +ControleurMenu(FenetreJeu)
    }
    class ControleurPartie {
      +ControleurPartie(Partie, FenetreJeu)
      +terminerTour()
    }
    class ControleurOnglet {
      <<abstract>>
      #Partie partie
    }
    class ControleurEconomie
    class ControleurInfrastructures
    class ControleurMarche
    class ControleurMilitaire
    class ControleurFinPartie

    ControleurOnglet <|-- ControleurEconomie
    ControleurOnglet <|-- ControleurInfrastructures
    ControleurOnglet <|-- ControleurMarche
    ControleurOnglet <|-- ControleurMilitaire
    ControleurMenu ..> Partie
    ControleurMenu ..> FenetreJeu
    ControleurPartie ..> Partie
    ControleurPartie ..> FenetreJeu
    ControleurPartie ..> ControleurEconomie
    ControleurPartie ..> ControleurInfrastructures
    ControleurPartie ..> ControleurMarche
    ControleurPartie ..> ControleurMilitaire
```

---

## Synthèse des patrons (où les retrouver dans l'UML)

| Patron | Classes |
|---|---|
| **State** | `EtatTour` + 9 `Etat*` (diag. 1) |
| **Observer** | `Observable`→`Partie`/`Royaume`, `Notification`, vues `Observer` (diag. 1, 2, 8, 11) |
| **Command** | `Action` + 6 actions, `FileActions` (diag. 5) |
| **Strategy** | `StrategieIA` → `StrategieEquilibree` (diag. 6) |
| **Factory** | `FabriqueIA` (diag. 6) |
| **Builder** | `PartieBuilder` (diag. 1) |
| **Template Method** | `Batiment.produire()` / `appliquerProduction()` (diag. 3) |
| **MVC** | packages `Modele` / `Vue` / `Controleur` (diag. 0) |
