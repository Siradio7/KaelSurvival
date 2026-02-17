# Analyse technique du code KaelSurvival

## Vue d’ensemble
Le projet est un jeu **libGDX** structuré par modules Gradle (`core`, `lwjgl3`) avec une séparation globalement cohérente entre logique de jeu (models/controllers), affichage (views/screens) et bootstrap desktop. L’architecture suit bien l’intention MVC annoncée dans le README.

## Points forts
- **Structure modulaire claire** : l’entrée (`Main`) délègue rapidement vers les écrans et le chargement audio.
- **Boucle de gameplay complète** : gestion joueur, IA ennemie, projectiles, narration contextuelle, HUD, transitions d’écran.
- **Système de carte exploitable** : chargement de maps via `MapManager` et extraction d’objets Tiled nommés.
- **Lisibilité correcte** : classes nommées explicitement, logique de collision et d’update compréhensible.

## Risques / anomalies identifiées

### 1) Double instanciation inutile de `GameWorld`
Dans `GameScreen#show`, `world` est créé deux fois de suite.
- Impact : léger gaspillage mémoire/CPU, code ambigu.
- Correctif conseillé : supprimer l’une des deux lignes.

### 2) `try/catch` très large dans la boucle de rendu
`GameScreen#render` encapsule toute la boucle dans un `try/catch(Exception)`.
- Impact : risque de masquer des erreurs structurelles, maintien d’un état partiellement corrompu.
- Correctif conseillé : limiter la capture à des zones ciblées, ou laisser remonter en dev.

### 3) Fuite/erreur potentielle de cycle de vie des textures
Dans `WorldRenderer#dispose`, la texture du joueur idle est disposée deux fois (`playerIdle.getTexture().dispose()` appelé 2 fois).
- Impact : risque d’exception selon backend graphique, comportement non déterministe.
- Correctif conseillé : supprimer le doublon et centraliser la politique d’ownership des assets (AssetManager/libGDX).

### 4) Chargement map dépendant d’un nom de layer en dur
`GameScreen#loadMapData` suppose l’existence de `"Object Layer 1"`.
- Impact : fragilité élevée si changement dans Tiled.
- Correctif conseillé : fallback + validation explicite + message d’erreur guidant le map designer.

### 5) Conditions de victoire/sortie couplées au contrôleur
`GameController` gère simultanément input, combat, narration, transitions d’écran.
- Impact : classe “god object”, testabilité limitée.
- Correctif conseillé : extraire des services dédiés (combat, progression de niveau, narration).

### 6) Auto-aim strict (pas de tir sans cible)
Le clic gauche sans orque à portée annule complètement le tir.
- Impact : UX potentiellement frustrante selon intention design.
- Correctif conseillé : documenter ce choix ou proposer un mode “tir libre” configurable.

## Dette technique priorisée
1. **P1 (fiabilité runtime)** : corriger le `dispose()` et la double instanciation de `GameWorld`.
2. **P1 (robustesse contenu)** : sécuriser le chargement du layer map.
3. **P2 (maintenabilité)** : découper `GameController` en sous-systèmes.
4. **P3 (UX/game design)** : clarifier auto-aim vs tir manuel.

## Plan d’amélioration pragmatique (court terme)
- Sprint 1 : correctifs sûrs (double init, double dispose, validation layer).
- Sprint 2 : extraction `CombatSystem` + `ProgressionSystem`.
- Sprint 3 : instrumentation (logs gameplay + métriques temps de frame).

## Validation exécutée dans cet environnement
- Tentative d’exécution de tests Gradle impossible sans accès de téléchargement externe (wrapper bloqué proxy).
- Analyse réalisée par revue statique des classes cœur du gameplay.
