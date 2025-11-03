# 📐 Diagrammes UML - Pac-Max

## 🏗️ Diagramme de Classes Principal

```
┌─────────────────────────────────────────────────────────────┐
│                    <<interface>>                            │
│                      Movable                                │
├─────────────────────────────────────────────────────────────┤
│ + move(deltaTime: double): void                            │
│ + setDirection(direction: Direction): void                 │
│ + getDirection(): Direction                                │
│ + setSpeed(speed: double): void                            │
│ + getSpeed(): double                                       │
└─────────────────────────────────────────────────────────────┘
                            △
                            │
┌─────────────────────────────────────────────────────────────┐
│                    <<interface>>                            │
│                     Collidable                              │
├─────────────────────────────────────────────────────────────┤
│ + collidesWith(other: Collidable): boolean                 │
│ + getX(): double                                           │
│ + getY(): double                                           │
│ + getWidth(): double                                       │
│ + getHeight(): double                                      │
│ + onCollision(other: Collidable): void                     │
└─────────────────────────────────────────────────────────────┘
                            △
                            │
┌─────────────────────────────────────────────────────────────┐
│                    <<interface>>                            │
│                     Renderable                              │
├─────────────────────────────────────────────────────────────┤
│ + render(gc: GraphicsContext): void                        │
│ + isVisible(): boolean                                     │
│ + setVisible(visible: boolean): void                       │
└─────────────────────────────────────────────────────────────┘
                            △
                            │
        ┌───────────────────┼───────────────────┐
        │                   │                   │
┌───────────────────────────────────────────────────────────┐
│              <<abstract>>                                 │
│                 Entity                                    │
├───────────────────────────────────────────────────────────┤
│ # x: double                                              │
│ # y: double                                              │
│ # width: double                                          │
│ # height: double                                         │
│ # speed: double                                          │
│ # direction: Direction                                   │
│ # visible: boolean                                       │
│ # active: boolean                                        │
├───────────────────────────────────────────────────────────┤
│ + Entity(x, y, width, height)                           │
│ + {abstract} update(deltaTime: double): void            │
│ + move(deltaTime: double): void                         │
│ + collidesWith(other: Collidable): boolean              │
│ + render(gc: GraphicsContext): void                     │
│ + getCenterX(): double                                  │
│ + getCenterY(): double                                  │
└───────────────────────────────────────────────────────────┘
                    △
                    │
        ┌───────────┼───────────┬──────────┐
        │           │           │          │
┌───────────┐ ┌─────────┐ ┌─────────┐ ┌──────┐
│  Player   │ │  Ghost  │ │ PacGum  │ │ Wall │
├───────────┤ ├─────────┤ ├─────────┤ ├──────┤
│- lives    │ │- type   │ │- type   │ │      │
│- score    │ │- ai     │ │- points │ │      │
│- powered  │ │- state  │ │-collect.│ │      │
├───────────┤ ├─────────┤ ├─────────┤ ├──────┤
│+addScore()│ │+setAI() │ │+collect()│ │      │
│+loseLife()│ │+respawn()│ │         │ │      │
└───────────┘ └─────────┘ └─────────┘ └──────┘
                  │
                  │ uses
                  ▼
        ┌──────────────────┐
        │  <<interface>>   │
        │    GhostAI       │
        ├──────────────────┤
        │+chooseDirection()│
        │+setPlayerPos()   │
        └──────────────────┘
                  △
                  │
    ┌─────────────┼─────────────┬──────────┐
    │             │             │          │
┌──────────┐ ┌─────────┐ ┌──────────┐ ┌────────┐
│Aggressive│ │ Ambush  │ │ Random   │ │ Flee   │
│   AI     │ │   AI    │ │   AI     │ │  AI    │
└──────────┘ └─────────┘ └──────────┘ └────────┘
```

## 🎮 Diagramme de Classes - Moteur de Jeu

```
┌────────────────────────────────────────┐
│      <<singleton>>                     │
│       GameEngine                       │
├────────────────────────────────────────┤
│ - instance: GameEngine {static}        │
│ - currentState: GameState              │
│ - player: Player                       │
│ - ghosts: List<Ghost>                  │
│ - pacGums: List<PacGum>                │
│ - walls: List<Wall>                    │
│ - collisionManager: CollisionManager   │
│ - currentLevel: int                    │
├────────────────────────────────────────┤
│ + getInstance(): GameEngine {static}   │
│ + initialize(): void                   │
│ + start(): void                        │
│ + stop(): void                         │
│ + update(deltaTime: double): void      │
│ + setState(state: GameState): void     │
│ - checkCollisions(): void              │
│ - checkGameConditions(): void          │
└────────────────────────────────────────┘
            │
            │ uses
            ▼
┌────────────────────────────────────────┐
│      <<enumeration>>                   │
│        GameState                       │
├────────────────────────────────────────┤
│ MENU                                   │
│ RUNNING                                │
│ PAUSED                                 │
│ GAME_OVER                              │
│ LEVEL_COMPLETE                         │
│ TRANSITION                             │
└────────────────────────────────────────┘
```

## 🔄 Diagramme de Séquence - Collision

```
Player    CollisionManager    PacGum    GameEngine
  │              │              │           │
  │──move()──────│              │           │
  │              │              │           │
  │              │◄─checkCollision(player, pacGum)─┤
  │              │              │           │
  │              │──collidesWith()──────────►│
  │              │◄─────true────────────────│
  │              │              │           │
  │              ├──────────────────────────►│
  │              │         return true       │
  │              │              │           │
  │◄─────────────┼──────────────┼───collect()┤
  │              │              │           │
  │◄─────────────┼──────────────┼─addScore()┤
  │              │              │           │
```

## 🎯 Diagramme d'États - Ghost

```
         ┌─────────┐
    ┌───►│  CHASE  │◄────┐
    │    └─────────┘     │
    │         │          │
    │    powerUp    timeout
    │         │          │
    │         ▼          │
    │    ┌─────────┐     │
    │    │ SCARED  │─────┘
    │    └─────────┘
    │         │
    │    collision
    │    with player
    │         │
    │         ▼
    │    ┌─────────┐
    └────│  DEAD   │
         └─────────┘
              │
         respawn at base
              │
              ▼
         ┌─────────┐
         │ SCATTER │
         └─────────┘
```

## 📊 Diagramme de Packages

```
┌─────────────────────────────────────────────────┐
│           com.epitech.pacmax                    │
├─────────────────────────────────────────────────┤
│                                                 │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐     │
│  │ entities │  │  engine  │  │    ai    │     │
│  └──────────┘  └──────────┘  └──────────┘     │
│       │             │              │           │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐     │
│  │  levels  │  │    ui    │  │  utils   │     │
│  └──────────┘  └──────────┘  └──────────┘     │
│                                                 │
└─────────────────────────────────────────────────┘

Dépendances:
- ui ──► engine, entities
- engine ──► entities, ai, levels, utils
- entities ──► ai
- levels ──► entities
```

## 🏛️ Diagramme de Composants

```
┌─────────────────────────────────────────────┐
│            Application Layer                │
│  ┌───────────────────────────────────┐     │
│  │         Main.java                 │     │
│  │  (Point d'entrée JavaFX)          │     │
│  └───────────────────────────────────┘     │
└─────────────────────────────────────────────┘
                    │
                    ▼
┌─────────────────────────────────────────────┐
│          Presentation Layer                 │
│  ┌───────────────────────────────────┐     │
│  │         GameUI                    │     │
│  │  (Affichage JavaFX)               │     │
│  └───────────────────────────────────┘     │
└─────────────────────────────────────────────┘
                    │
                    ▼
┌─────────────────────────────────────────────┐
│            Business Layer                   │
│  ┌───────────────────────────────────┐     │
│  │       GameEngine                  │     │
│  │  (Logique du jeu)                 │     │
│  └───────────────────────────────────┘     │
│                                             │
│  ┌───────────────────────────────────┐     │
│  │       Entities                    │     │
│  │  (Player, Ghost, etc.)            │     │
│  └───────────────────────────────────┘     │
│                                             │
│  ┌───────────────────────────────────┐     │
│  │          AI                       │     │
│  │  (Stratégies des fantômes)        │     │
│  └───────────────────────────────────┘     │
└─────────────────────────────────────────────┘
                    │
                    ▼
┌─────────────────────────────────────────────┐
│            Service Layer                    │
│  ┌───────────────────────────────────┐     │
│  │    CollisionManager               │     │
│  │    InputHandler                   │     │
│  │    ScoreManager                   │     │
│  │    SoundManager                   │     │
│  └───────────────────────────────────┘     │
└─────────────────────────────────────────────┘
                    │
                    ▼
┌─────────────────────────────────────────────┐
│            Data Layer                       │
│  ┌───────────────────────────────────┐     │
│  │      JSON Files                   │     │
│  │  (Niveaux, Scores)                │     │
│  └───────────────────────────────────┘     │
└─────────────────────────────────────────────┘
```

## 📋 Légende

- `<<interface>>` : Interface Java
- `<<abstract>>` : Classe abstraite
- `<<singleton>>` : Pattern Singleton
- `<<enumeration>>` : Énumération
- `△` : Héritage / Implémentation
- `◄─` : Association / Utilisation
- `#` : Attribut/méthode protégé(e)
- `+` : Attribut/méthode public(que)
- `-` : Attribut/méthode privé(e)

---

**Note** : Ces diagrammes peuvent être générés automatiquement avec des outils comme PlantUML ou dessinés avec des outils UML (StarUML, Lucidchart, draw.io).
