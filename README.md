# Pac-Max - Projet T-JAV-501 (2DGame)

## 📋 Description

Pac-Max est un jeu 2D inspiré de Pac-Man, développé en Java avec JavaFX dans le cadre du projet T-JAV-501 d'Epitech. Le jeu met l'accent sur une architecture orientée objet solide, l'utilisation de design patterns et la qualité du code.

## 🎮 Fonctionnalités

### Gameplay
- **Joueur** : Contrôlez Pac-Man avec les flèches directionnelles ou ZQSD
- **Fantômes** : 4 fantômes avec des comportements IA différents
- **Collectibles** : Pac-gums normales et power-ups
- **Power-ups** : Permet de manger les fantômes temporairement
- **Score** : Système de points avec sauvegarde des meilleurs scores
- **Vies** : 3 vies par partie
- **Niveaux** : Système de niveaux progressifs

### Techniques
- Architecture POO complète (héritage, polymorphisme, encapsulation)
- Design Patterns : Singleton, Strategy, State, Factory
- Gestion des collisions AABB
- Boucle de jeu avec delta time
- Interface JavaFX moderne
- Sauvegarde JSON des scores

## 🏗️ Architecture

```
src/
├── main/java/com/epitech/pacmax/
│   ├── Main.java                    # Point d'entrée
│   ├── engine/                      # Moteur de jeu
│   │   ├── GameEngine.java          # Singleton - Boucle principale
│   │   └── GameState.java           # États du jeu
│   ├── entities/                    # Entités du jeu
│   │   ├── Entity.java              # Classe abstraite de base
│   │   ├── Player.java              # Joueur (Pac-Man)
│   │   ├── Ghost.java               # Fantômes
│   │   ├── PacGum.java              # Pac-gums
│   │   ├── Wall.java                # Murs
│   │   ├── Direction.java           # Énumération des directions
│   │   ├── Movable.java             # Interface pour entités mobiles
│   │   ├── Collidable.java          # Interface pour collisions
│   │   └── Renderable.java          # Interface pour rendu
│   ├── ai/                          # Intelligence artificielle
│   │   ├── GhostAI.java             # Interface Strategy
│   │   ├── AggressiveAI.java        # IA agressive (Blinky)
│   │   ├── AmbushAI.java            # IA embuscade (Pinky)
│   │   ├── RandomAI.java            # IA aléatoire (Clyde)
│   │   └── FleeAI.java              # IA de fuite (mode scared)
│   ├── levels/                      # Gestion des niveaux
│   │   └── GameMap.java             # Chargement de cartes JSON
│   ├── ui/                          # Interface utilisateur
│   │   └── GameUI.java              # Rendu JavaFX
│   └── utils/                       # Utilitaires
│       ├── CollisionManager.java    # Gestion des collisions
│       ├── InputHandler.java        # Gestion des entrées
│       ├── ScoreManager.java        # Gestion des scores
│       └── SoundManager.java        # Gestion des sons
└── test/java/                       # Tests unitaires (à implémenter)
```

## 🚀 Installation et Lancement

### Prérequis
- Java 17 ou supérieur
- Maven 3.6+
- JavaFX 21+

### Compilation
```bash
mvn clean compile
```

### Lancement
```bash
mvn javafx:run
```

### Création du JAR exécutable
```bash
mvn clean package
java -jar target/pacmax-1.0-SNAPSHOT.jar
```

### Génération de la Javadoc
```bash
mvn javadoc:javadoc
```
La documentation sera disponible dans `target/site/apidocs/`

## 🎯 Contrôles

- **Flèches directionnelles** ou **ZQSD** : Déplacer Pac-Man
- **P** ou **ÉCHAP** : Pause
- **ENTRÉE** : Démarrer / Rejouer
- **ESPACE** : Démarrer (depuis le menu)

## 🧩 Design Patterns Utilisés

### 1. Singleton
- **Classe** : `GameEngine`
- **Justification** : Une seule instance du moteur de jeu doit exister

### 2. Strategy
- **Interface** : `GhostAI`
- **Implémentations** : `AggressiveAI`, `AmbushAI`, `RandomAI`, `FleeAI`
- **Justification** : Permet de changer dynamiquement le comportement des fantômes

### 3. State
- **Énumération** : `GameState`
- **États** : MENU, RUNNING, PAUSED, GAME_OVER, LEVEL_COMPLETE
- **Justification** : Gère les différents états du jeu de manière claire

### 4. Factory (à implémenter)
- **Utilisation future** : Création d'entités depuis fichiers JSON
- **Justification** : Centralise la création d'objets complexes

## 📊 Principes POO Appliqués

### Encapsulation
- Tous les attributs sont privés ou protégés
- Accès via getters/setters appropriés
- Validation des données dans les setters

### Héritage
- `Entity` : Classe abstraite de base
- `Player`, `Ghost`, `PacGum`, `Wall` : Héritent de `Entity`

### Polymorphisme
- Interfaces : `Movable`, `Collidable`, `Renderable`
- Méthodes abstraites : `update()`, `render()`

### Abstraction
- Interfaces pour séparer les responsabilités
- Classes abstraites pour le code commun

## 🧪 Tests Unitaires (À Implémenter)

### Tests suggérés avec JUnit 5

```java
// Exemple de tests à créer
- PlayerTest : test des déplacements, collisions, score
- GhostTest : test des comportements IA
- CollisionManagerTest : test des détections de collision
- ScoreManagerTest : test de sauvegarde/chargement JSON
- GameEngineTest : test de la boucle de jeu
```

### Lancer les tests
```bash
mvn test
```

## 📝 TODO / Améliorations Futures

- [ ] Chargement de niveaux depuis fichiers JSON
- [ ] Ajout de sons et musiques
- [ ] Animation des sprites
- [ ] Menus graphiques avancés
- [ ] Système de bonus supplémentaires
- [ ] Mode multijoueur local
- [ ] Éditeur de niveaux
- [ ] Achievements/Trophées
- [ ] Difficulté progressive
- [ ] Pathfinding A* pour les fantômes

## 📚 Documentation

### UML
Un diagramme de classes UML est disponible dans le dossier `docs/` (à générer).

### Javadoc
Toutes les classes publiques sont documentées avec Javadoc. Générez la documentation avec :
```bash
mvn javadoc:javadoc
```

## 👥 Équipe

Projet réalisé par une équipe de 3 personnes dans le cadre du T-JAV-501.

## 📄 Licence

Projet éducatif - Epitech 2025

## 🔗 Ressources

- [JavaFX Documentation](https://openjfx.io/)
- [Maven Documentation](https://maven.apache.org/)
- [JUnit 5 Documentation](https://junit.org/junit5/)
- [Design Patterns](https://refactoring.guru/design-patterns)

---

**Note** : Ce projet est une base fonctionnelle. Il est conçu pour être étendu et amélioré par l'équipe de développement.
