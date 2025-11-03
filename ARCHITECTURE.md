# Architecture Technique - Pac-Max

## 🏛️ Vue d'ensemble

Pac-Max utilise une architecture modulaire basée sur les principes SOLID et les design patterns classiques. Le projet est organisé en couches distinctes pour faciliter la maintenance et l'évolution.

## 📦 Modules Principaux

### 1. Engine (Moteur de Jeu)

**Responsabilité** : Gestion de la boucle de jeu et de l'état global

#### GameEngine (Singleton)
```java
- Instance unique du moteur
- Boucle de jeu principale (AnimationTimer)
- Gestion du delta time
- Coordination des mises à jour
- Gestion des niveaux
```

**Pattern Singleton** : Garantit une seule instance du moteur dans toute l'application.

#### GameState (State Pattern)
```java
enum GameState {
    MENU,           // Menu principal
    RUNNING,        // Jeu en cours
    PAUSED,         // Pause
    GAME_OVER,      // Fin de partie
    LEVEL_COMPLETE, // Niveau terminé
    TRANSITION      // Transition entre niveaux
}
```

**Pattern State** : Simplifie la gestion des différents états du jeu.

### 2. Entities (Entités)

**Responsabilité** : Représentation des objets du jeu

#### Hiérarchie de classes
```
Entity (abstract)
├── Player
├── Ghost
├── PacGum
└── Wall
```

#### Interfaces
```
Movable      : Entités qui se déplacent
Collidable   : Entités qui peuvent entrer en collision
Renderable   : Entités qui peuvent être affichées
```

**Principe d'Interface Segregation** : Chaque interface a une responsabilité unique.

### 3. AI (Intelligence Artificielle)

**Responsabilité** : Comportements des fantômes

#### GhostAI (Strategy Pattern)
```java
interface GhostAI {
    Direction chooseDirection(Ghost ghost);
    void setPlayerPosition(double x, double y);
}
```

#### Implémentations
- **AggressiveAI** : Poursuit directement le joueur (Blinky)
- **AmbushAI** : Anticipe la position du joueur (Pinky)
- **RandomAI** : Mouvements aléatoires (Clyde, Inky)
- **FleeAI** : Fuit le joueur (mode scared)

**Pattern Strategy** : Permet de changer le comportement des fantômes à l'exécution.

### 4. Levels (Niveaux)

**Responsabilité** : Gestion des cartes et niveaux

#### GameMap
```java
- Chargement depuis JSON
- Grille de tuiles (tiles)
- Points d'apparition (spawn points)
- Métadonnées du niveau
```

**Format JSON** :
```json
{
  "name": "Level 1",
  "width": 28,
  "height": 31,
  "tileSize": 30,
  "tiles": [[...], [...]],
  "playerSpawn": {"x": 14, "y": 23},
  "ghostSpawns": [...]
}
```

### 5. UI (Interface Utilisateur)

**Responsabilité** : Affichage graphique

#### GameUI
```java
- Canvas JavaFX
- Rendu des entités
- HUD (score, vies, niveau)
- Écrans de menu/pause/game over
```

**Séparation des responsabilités** : L'UI ne contient que la logique d'affichage.

### 6. Utils (Utilitaires)

**Responsabilité** : Services transversaux

#### CollisionManager
```java
- Détection AABB (Axis-Aligned Bounding Box)
- Résolution des collisions avec les murs
- Calcul de distances
```

#### InputHandler
```java
- Capture des événements clavier
- Traduction en actions de jeu
- Gestion contextuelle selon l'état
```

#### ScoreManager
```java
- Sauvegarde/chargement JSON
- Top 10 des meilleurs scores
- Métadonnées (date, niveau)
```

#### SoundManager
```java
- Effets sonores (AudioClip)
- Musique de fond (MediaPlayer)
- Contrôle du volume
```

## 🔄 Flux de Données

### Boucle de Jeu

```
1. AnimationTimer (JavaFX)
   ↓
2. GameEngine.update(deltaTime)
   ↓
3. Player.update(deltaTime)
   Ghost.update(deltaTime) × 4
   ↓
4. CollisionManager.checkCollisions()
   ↓
5. GameEngine.checkGameConditions()
   ↓
6. GameUI.render()
   ↓
7. Entity.render(gc) pour chaque entité
```

### Gestion des Entrées

```
1. KeyEvent (JavaFX)
   ↓
2. InputHandler.handleKeyPressed()
   ↓
3. Selon GameState :
   - RUNNING → Player.setNextDirection()
   - PAUSED → GameEngine.setState(RUNNING)
   - MENU → GameEngine.initialize()
```

### Système de Collision

```
1. GameEngine.checkCollisions()
   ↓
2. Pour chaque paire d'entités :
   CollisionManager.checkCollision()
   ↓
3. Si collision détectée :
   - Player ↔ Wall → resolveWallCollision()
   - Player ↔ PacGum → collect() + addScore()
   - Player ↔ Ghost → loseLife() ou addScore()
```

## 🎨 Design Patterns - Justifications

### 1. Singleton (GameEngine)
**Problème** : Besoin d'un point d'accès global au moteur de jeu.
**Solution** : Une seule instance accessible via `getInstance()`.
**Avantages** : 
- Évite les instances multiples
- Accès facile depuis n'importe où
- Contrôle de l'initialisation

### 2. Strategy (GhostAI)
**Problème** : Différents comportements pour les fantômes.
**Solution** : Interface commune avec implémentations variées.
**Avantages** :
- Changement de comportement à l'exécution
- Facilite l'ajout de nouvelles IA
- Respect du principe Open/Closed

### 3. State (GameState)
**Problème** : Logique complexe selon l'état du jeu.
**Solution** : Énumération des états avec gestion centralisée.
**Avantages** :
- Code plus lisible
- Transitions d'état claires
- Évite les if/else imbriqués

### 4. Template Method (Entity.update)
**Problème** : Structure commune mais détails spécifiques.
**Solution** : Méthode abstraite dans la classe de base.
**Avantages** :
- Réutilisation du code
- Polymorphisme
- Extension facile

## 🧪 Points d'Extension

### Ajout d'une nouvelle entité
```java
1. Créer une classe héritant de Entity
2. Implémenter update() et render()
3. Ajouter dans GameEngine si nécessaire
```

### Ajout d'une nouvelle IA
```java
1. Créer une classe implémentant GhostAI
2. Implémenter chooseDirection()
3. Assigner à un Ghost via setAI()
```

### Ajout d'un nouveau niveau
```java
1. Créer un fichier JSON avec le format GameMap
2. Charger via GameMap.loadFromFile()
3. Utiliser dans GameEngine.loadLevel()
```

## 📊 Diagramme de Classes (Simplifié)

```
┌─────────────┐
│ GameEngine  │ (Singleton)
├─────────────┤
│ - instance  │
│ - player    │
│ - ghosts[]  │
│ - state     │
├─────────────┤
│ + update()  │
│ + start()   │
└─────────────┘
      │
      │ uses
      ▼
┌─────────────┐         ┌──────────────┐
│   Entity    │◄────────│  Movable     │
│  (abstract) │         │ (interface)  │
├─────────────┤         └──────────────┘
│ # x, y      │         ┌──────────────┐
│ # width     │◄────────│ Collidable   │
│ # height    │         │ (interface)  │
├─────────────┤         └──────────────┘
│ + update()  │         ┌──────────────┐
│ + render()  │◄────────│ Renderable   │
└─────────────┘         │ (interface)  │
      △                 └──────────────┘
      │
      ├───────────┬───────────┬──────────┐
      │           │           │          │
┌─────────┐ ┌─────────┐ ┌─────────┐ ┌──────┐
│ Player  │ │  Ghost  │ │ PacGum  │ │ Wall │
└─────────┘ └─────────┘ └─────────┘ └──────┘
                │
                │ uses
                ▼
          ┌──────────┐
          │ GhostAI  │ (Strategy)
          │(interface)│
          └──────────┘
                △
                │
      ┌─────────┼─────────┬──────────┐
      │         │         │          │
┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐
│Aggress.│ │Ambush  │ │Random  │ │ Flee   │
└────────┘ └────────┘ └────────┘ └────────┘
```

## 🔒 Principes SOLID

### S - Single Responsibility
- Chaque classe a une responsabilité unique
- `CollisionManager` : uniquement les collisions
- `InputHandler` : uniquement les entrées

### O - Open/Closed
- Ouvert à l'extension (nouvelles IA, entités)
- Fermé à la modification (interfaces stables)

### L - Liskov Substitution
- Toute `Entity` peut être utilisée de manière polymorphe
- Toute `GhostAI` est interchangeable

### I - Interface Segregation
- Interfaces petites et ciblées
- `Movable`, `Collidable`, `Renderable` séparées

### D - Dependency Inversion
- Dépendance sur abstractions (`GhostAI`, `Entity`)
- Pas de dépendance directe sur implémentations

## 🚀 Performance

### Optimisations actuelles
- Delta time pour framerate indépendant
- Limitation du delta time (max 0.1s)
- Rendu uniquement des entités visibles

### Optimisations futures
- Spatial partitioning pour collisions
- Object pooling pour entités
- Dirty flag pour rendu sélectif

---

Cette architecture est conçue pour être **maintenable**, **extensible** et **testable**.
