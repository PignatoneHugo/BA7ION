# Roadmap — BAS7ION

**Projet** : BAS7ION — Simulation de gestion et stratégie d'un royaume médiéval
**Équipe** : 6 (Léna Charrière, Mathéo Depuydt, Hugo Pignatone, Benjamin Sarrat, Fabien Serres, Antoine Tuffery)
**Cours** : CPO + Gestion de projet (SCRUM), 1A ENSEEIHT, 2025–2026
**Livraison finale** : 19/06/2026

---

## Vue d'ensemble

Le projet est découpé en **3 sprints** courts suivis d'une **remise du livrable**. Chaque sprint produit un incrément démontrable et un `sprintN-review.md` associé.

```
Sprint 1 ────► Sprint 2 ────► Sprint 3 ────► Livrable
Squelette    Interaction    Système        19/06/2026
MVC          joueur         complet
                            & finalisation
```

| Étape | Période | Thème | Objectif majeur |
|---|---|---|---|
| Sprint 1 | `[12/05/2026 → 28/05/2026]` | **Squelette MVC** | Vertical slice prouvant l'architecture |
| Sprint 2 | `[29/05/2026 → 08/06/2026]` | **Interaction joueur** | Le joueur pilote son économie via l'UI |
| Sprint 3 | `[09/06/2026 → 18/06/2026]` | **Système complet & finalisation** | Combat + IA + événements branchés, polish, rapport |
| Livrable | 19/06/2026 | **Remise** | `rapport.pdf`, `utilisateur.pdf`, `presentation.pdf`, `src/` |

> Les dates sont à ajuster en équipe selon la cadence réelle.

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

**Objectif** : Rendre le jeu interactif. Le joueur peut piloter sa population et ses bâtiments depuis l'UI, et subir des événements aléatoires.

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

## Sprint 3 — Système complet & finalisation

**Objectif** : Brancher toutes les briques métier restantes dans le cycle de tour **et** finaliser le projet pour la livraison. C'est le sprint le plus dense : pas d'ajout de feature après son terme.

**Livrables fonctionnels** :
- 5 phases restantes du cycle (`EtatActionsDifferees`, `EtatCombatsSubis`, `EtatCombatsOffensifs`, `EtatTourIA`, `EtatEvenement`)
- Système militaire complet branché : `Armee`, 4 types d'unités, postures de combat, recrutement via la Caserne
- IA des bots (4 stratégies : Agressif, Défensif, Commerçant, Équilibré) via Strategy + Factory
- Catalogue de ~10 événements aléatoires
- Système de décrets (2 décrets actifs max, effets temporaires)
- Conditions de victoire / défaite
- Sauvegarde / chargement (5 slots + autosave en fin de tour) via DTO sérialisable
- Journal de partie + écran de statistiques de fin

**Livrables qualité & documentation** :
- Tests JUnit ≥ 50 % de couverture sur `Modele/combat/`, `Modele/economie/`, `Modele/ia/`
- Équilibrage final basé sur 2–3 playtests internes
- Rédaction de `rapport.pdf`, `utilisateur.pdf`, `presentation.pdf`
- Revue de code finale + nettoyage des `TODO`

**Démo cible** : présentation orale prête, jeu jouable du menu principal jusqu'à la fin de partie sans crash, avec combats vs bots et événements.

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

| Métrique | Sprint 1 | Sprint 2 | Sprint 3 |
|---|---|---|---|
| Fichiers Java | 29 | `[à mesurer]` | `[à mesurer]` |
| Phases du tour implémentées | 4 / 9 | 4 / 9 | 9 / 9 |
| Bâtiments implémentés | 1 / 9 | 9 / 9 | 9 / 9 |
| Stratégies IA | 0 / 4 | 0 / 4 | 4 / 4 |
| Couverture JUnit | 0 % | ~30 % | ≥ 50 % |
| US closes | 9 | ~31 | 100 % de l'engagement |

---

## Règles de gestion de projet

- **PR obligatoires** sur `main`, review par un binôme avant merge (à partir du Sprint 2).
- **Daily stand-up** de 10 min en début de séance pendant chaque sprint.
- **Sprint Review + Rétrospective** à la fin de chaque sprint, formalisé dans `doc/sprintN-review.md`.
- **Conventions de code** : cf. `doc/architecture.md` section 10 (langue, packages, nombres magiques, Observer, tests).
- **Branche par US** : `feat/us-X.Y-courte-description`. Pas de commit direct sur `main`.
