# Roadmap — BAS7ION

**Projet** : BAS7ION — Simulation de gestion et stratégie d'un royaume médiéval
**Équipe** : 6 (Léna Charrière, Mathéo Depuydt, Hugo Pignatone, Benjamin Sarrat, Fabien Serres, Antoine Tuffery)
**Cours** : CPO + Gestion de projet (SCRUM), 1A ENSEEIHT, 2025–2026
**Cadence** : sprints d'une semaine, du vendredi au vendredi
**Livraison finale** : 19/06/2026

---

## Vue d'ensemble

Le projet est découpé en **4 sprints d'une semaine** (vendredi → vendredi) qui mènent directement à la remise du livrable. Chaque sprint produit un incrément démontrable et un `sprintN-review.md` associé.

```
Sprint 1 ───► Sprint 2 ───► Sprint 3 ───► Sprint 4 ───► Livrable
Squelette   Interaction   Système       Tests &       19/06/2026
MVC         joueur        complet       finalisation
```

| Étape | Période | Thème | Objectif majeur |
|---|---|---|---|
| Sprint 1 | `[22/05/2026 → 29/05/2026]` | **Squelette MVC** | Vertical slice prouvant l'architecture |
| Sprint 2 | `[29/05/2026 → 05/06/2026]` | **Interaction joueur** | Le joueur pilote son économie via l'UI |
| Sprint 3 | `[05/06/2026 → 12/06/2026]` | **Système complet** | Combat + IA + événements branchés |
| Sprint 4 | `[12/06/2026 → 19/06/2026]` | **Tests & finalisation** | Polish, équilibrage, rédaction des livrables |
| Livrable | 19/06/2026 | **Remise** | `rapport.pdf`, `utilisateur.pdf`, `presentation.pdf`, `src/` |

---

## Sprint 1 — Squelette MVC ✅

**Statut** : Terminé (cf. [sprint1-review.md](sprint1-review.md))

**Objectif** : Mettre en place le squelette MVC + Observer + State + Command et prouver que 6 développeurs peuvent paralléliser sur cette base.

**Livré** :
- Cycle de tour à 4 phases sur 9 (Planification → Production → Consommation → Fin de tour)
- Royaume joueur avec 5 ressources, population par rôle, 1 bâtiment (Ferme)
- HUD permanent + onglet Économie
- Internationalisation FR/EN
- 29 fichiers Java, ~1750 lignes

---

## Sprint 2 — Interaction joueur

**Période** : 29/05/2026 → 05/06/2026

**Objectif** : Rendre le jeu interactif. Le joueur peut piloter sa population et ses bâtiments depuis l'UI, et subir un premier événement aléatoire.

**Livrables clés** :
- Boutons +/− sur les rôles dans l'onglet Économie
- Les 8 bâtiments restants (Mine, Scierie, Habitations, Caserne, Remparts, Marché, Bibliothèque, Tour de Guet)
- Système d'amélioration de bâtiment (chantier en N tours via `ActionAmeliorer`)
- Premier événement aléatoire branché (`DialogueEvenement` modal)
- Menu principal + écran "Nouvelle partie"
- `ResolveurCombat` testé en JUnit (3 scénarios, graine fixe) — pas encore branché dans le cycle
- Système de taxes et impact sur le moral

**Démo cible** : le joueur joue 5 tours d'affilée, déplace ses habitants, construit, et un événement aléatoire popper entre les tours 3 et 5.

---

## Sprint 3 — Système complet

**Période** : 05/06/2026 → 12/06/2026

**Objectif** : Brancher toutes les briques métier restantes dans le cycle de tour. Le jeu devient un vrai jeu de stratégie multi-royaume jouable de bout en bout.

**Livrables clés** :
- 5 phases restantes du cycle (`EtatActionsDifferees`, `EtatCombatsSubis`, `EtatCombatsOffensifs`, `EtatTourIA`, `EtatEvenement`)
- Système militaire complet branché : `Armee`, 4 types d'unités, postures de combat, recrutement via la Caserne
- IA des bots (4 stratégies : Agressif, Défensif, Commerçant, Équilibré) via Strategy + Factory
- Catalogue de ~10 événements aléatoires
- Système de décrets (2 décrets actifs max, effets temporaires)
- Conditions de victoire / défaite
- Sauvegarde / chargement (5 slots + autosave en fin de tour) via DTO sérialisable
- Journal de partie

**Démo cible** : une partie complète vs 2 bots, avec combats, événements et fin de partie déterminée (victoire ou défaite).

---

## Sprint 4 — Tests & finalisation

**Période** : 12/06/2026 → 19/06/2026

**Objectif** : Finaliser le jeu pour la livraison. **Pas d'ajout de feature** : focus sur la qualité, l'équilibrage et la rédaction des livrables. Phase critique car directement adossée à la remise.

**Livrables qualité** :
- Tests JUnit étendus : ≥ 50 % de couverture sur `Modele/combat/`, `Modele/economie/`, `Modele/ia/`
- Équilibrage final basé sur 2–3 playtests internes (ajustement des constantes dans `Equilibrage.java`)
- Correction des bugs remontés en Sprint 3
- Revue de code finale + nettoyage des `TODO`
- Mode daltonien (US-UX-15) si temps disponible
- Écran de statistiques de fin de partie

**Livrables documentation** :
- `rapport.pdf` : rapport technique (architecture, choix de conception, patterns, métriques)
- `utilisateur.pdf` : manuel utilisateur (règles, captures d'écran, raccourcis)
- `presentation.pdf` : slides de la soutenance orale
- `doc/uml/` complété pour tous les modules
- README final et instructions de build

**Démo cible** : présentation orale prête, jeu jouable du menu principal jusqu'à la fin de partie sans crash, livrables PDF finalisés et validés en relecture croisée.

---

## Livrable final — 19/06/2026

**Contenu de la remise** :

| Fichier | Description |
|---|---|
| `rapport.pdf` | Rapport technique : architecture, choix de conception, patterns, métriques |
| `utilisateur.pdf` | Manuel utilisateur : règles, captures d'écran, raccourcis |
| `presentation.pdf` | Slides de la soutenance orale |
| `src/` | Code source Java complet, compilable via `./build.sh` |
| `doc/uml/` | Diagrammes UML des modules principaux |
| `tests/` | Suite JUnit 5 |

**Soutenance** : présentation orale en équipe, démonstration en direct du jeu, questions sur l'architecture et les choix de conception.

---

## Indicateurs de suivi

À mettre à jour à chaque fin de sprint dans le `sprintN-review.md` correspondant.

| Métrique | Sprint 1 | Sprint 2 | Sprint 3 | Sprint 4 |
|---|---|---|---|---|
| Fichiers Java | 29 | `[à mesurer]` | `[à mesurer]` | `[à mesurer]` |
| Phases du tour implémentées | 4 / 9 | 4 / 9 | 9 / 9 | 9 / 9 |
| Bâtiments implémentés | 1 / 9 | 9 / 9 | 9 / 9 | 9 / 9 |
| Stratégies IA | 0 / 4 | 0 / 4 | 4 / 4 | 4 / 4 |
| Couverture JUnit | 0 % | ~30 % | ~40 % | ≥ 50 % |
| Livrables PDF | — | — | — | 3 / 3 |

---

## Règles de gestion de projet

- **PR obligatoires** sur `main`, review par un binôme avant merge (à partir du Sprint 2).
- **Daily stand-up** de 10 min en début de séance pendant chaque sprint.
- **Sprint Review + Rétrospective** chaque vendredi à la fin du sprint, formalisé dans `doc/sprintN-review.md`.
- **Conventions de code** : cf. `doc/architecture.md` section 10 (langue, packages, nombres magiques, Observer, tests).
- **Branche par US** : `feat/us-X.Y-courte-description`. Pas de commit direct sur `main`.
- **Gel des features** au démarrage du Sprint 4 : aucune nouvelle fonctionnalité ne sera intégrée, uniquement corrections de bugs et finalisation.
