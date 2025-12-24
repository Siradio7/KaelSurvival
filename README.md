# KaelSurvival 🏹

**KaelSurvival** est un jeu de survie 2D développé en Java avec le framework **libGDX**. Le joueur incarne Kael, le dernier héritier d'une lignée déchue, luttant pour sa survie contre des hordes d'orques dans un monde hostile.

## 📖 L'Histoire
Le ciel s'est assombri lorsque les premiers tambours orques ont résonné. Le campement de Kael a été réduit en cendres, et il est désormais le dernier gardien d'un secret ancestral.

Pour restaurer la lumière sur ses terres, Kael doit traverser les bois maudits et atteindre la **Grotte Mystique**. Mais les orques rôdent, et chaque pas vers le sanctuaire est une lutte pour la vie. Saurez-vous guider Kael vers son destin ou laisserez-vous les ténèbres l'emporter ?

## 🚀 Fonctionnalités
- **Système de Survie Top-Down** : Mouvement fluide à 360° et gestion des collisions.
- **IA des Orques** : Des ennemis intelligents qui patrouillent et traquent le joueur. Leur mouvement et leur rotation sont bridés sur 8 directions pour un style rétro authentique.
- **Caméra Dynamique** : Une caméra qui suit le joueur avec un système de **Zoom/Dézoom** intelligent qui ne dépasse jamais les limites de la carte.
- **Architecture MVC** : Code propre et modulaire séparant les Modèles, les Vues (Rendu) et les Contrôleurs (Logique).
- **Gestion Audio** : Ambiance sonore immersive avec musiques et effets sonores dédiés.

## 🎮 Commandes
| Action | Touche |
| :--- | :--- |
| **Déplacement** | Touches directionnelles ou ZQSD |
| **Zoomer** | Touche `P` |
| **Dézoomer** | Touche `M` |
| **Interagir** | Automatique (Zones cibles) |

## 🛠️ Architecture Technique
Le projet respecte strictement le design pattern **MVC** (Modèle-Vue-Contrôleur) :
- **Models** : Gèrent l'état des entités (`Player`, `Ork`, `GameWorld`).
- **Views** : Gèrent le rendu graphique (`WorldRenderer`) et les écrans (`MenuScreen`, `GameScreen`).
- **Controllers** : Gèrent la logique de jeu, les entrées et la physique (`GameController`, `PlayerController`).

## 📦 Installation et Lancement

### Prérequis
- Java JDK 17 ou supérieur.
- Gradle (inclus via le wrapper).

### Exécution
1. Clonez le dépôt :
   ```bash
   git clone https://github.com/Siradio7/KaelSurvival.git

## 1. Lancement du Projet avec Gradle
Le projet utilise Gradle pour automatiser la gestion des dépendances et l'exécution. Vous pouvez lancer le jeu en utilisant le wrapper Gradle inclus :

* **Sur Windows** :
    ```bash
    gradlew.bat lwjgl3:run
    ```
* **Sur Linux/Mac** :
    ```bash
    ./gradlew lwjgl3:run
    ```

### Commandes utiles
* `clean` : Supprime les dossiers de build pour repartir sur une installation propre.
* `build` : Compile les sources et prépare les archives du projet.
* `lwjgl3:jar` : Génère un fichier JAR exécutable dans le dossier `lwjgl3/build/libs`.

## Ressources (Assets)
L'univers de KaelSurvival repose sur les éléments suivants :

* **Framework** : Développé avec **libGDX**, un framework Java multiplateforme.
* **Cartes (Maps)** : Créées avec **Tiled Map Editor** et stockées au format `.tmx` dans `assets/maps/`.
* **Graphismes** : Sprites 2D personnalisés incluant Kael (joueur), les Orks et les éléments d'environnement (arbres, grottes) situés dans `assets/images/` et `assets/maps/`.
* **Audio** : Gestion centralisée via **AudioManager** pour les ambiances et effets :
    * Musiques au format **.mp3** (menu, introduction).
    * Effets sonores au format **.wav** (clics de souris).
* **Interface (UI)** : Utilisation de fichiers **JSON** pour les skins et de polices de caractères **.fnt** pour l'affichage du texte.
