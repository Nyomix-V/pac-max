# Guide de Développement - Pac-Max

## 🎯 Pour les Développeurs Juniors

Ce guide explique comment travailler sur le projet Pac-Max, étendre ses fonctionnalités et comprendre son architecture.

## 📚 Prérequis

### Connaissances requises
- Java 17+ (syntaxe de base, POO)
- Maven (gestion de dépendances)
- JavaFX (bases de l'affichage graphique)
- Git (versioning)

### Outils nécessaires
- JDK 17 ou supérieur
- Maven 3.6+
- IDE (IntelliJ IDEA, Eclipse, ou VS Code)
- Git

## 🚀 Démarrage Rapide

### 1. Cloner et compiler
```bash
git clone <votre-repo>
cd Pacmax
mvn clean install
```

### 2. Lancer le jeu
```bash
mvn javafx:run
```

### 3. Lancer les tests
```bash
mvn test
```

## 🏗️ Structure du Projet Expliquée

### Package `entities` - Les Objets du Jeu

#### Entity.java (Classe Abstraite)
**Rôle** : Classe de base pour tous les objets du jeu.

**Concepts POO démontrés** :
- **Abstraction** : Méthode `update()` abstraite
- **Encapsulation** : Attributs `protected`
- **Polymorphisme** : Implémente 3 interfaces

**Comment l'utiliser** :
```java
// Créer une nouvelle entité
public class Fruit extends Entity {
    public Fruit(double x, double y) {
        super(x, y, 15, 15);
        this.speed = 0; // Les fruits ne bougent pas
    }
    
    @Override
    public void update(double deltaTime) {
        // Logique de mise à jour
    }
    
    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillOval(x, y, width, height);
    }
}
```

#### Player.java
**Rôle** : Gère Pac-Man (déplacement, score, vies).

**Points clés** :
- `nextDirection` : Buffer d'input pour des contrôles fluides
- `powered` : Mode power-up temporaire
- `update()` : Gère le timer du power-up et le mouvement

**Exemple d'extension** :
```java
// Ajouter une capacité de dash
public class Player extends Entity {
    private boolean isDashing;
    private double dashCooldown;
    
    public void dash() {
        if (dashCooldown <= 0) {
            speed *= 2;
            isDashing = true;
            dashCooldown = 5.0; // 5 secondes
        }
    }
}
```

#### Ghost.java
**Rôle** : Fantômes ennemis avec IA interchangeable.

**Pattern Strategy en action** :
```java
// Changer l'IA d'un fantôme
Ghost ghost = new Ghost(100, 100, GhostType.BLINKY, new AggressiveAI());

// Plus tard, changer de comportement
ghost.setAI(new FleeAI()); // Le fantôme fuit maintenant
```

### Package `ai` - Intelligence Artificielle

#### Comment créer une nouvelle IA

**Étape 1** : Créer une classe implémentant `GhostAI`
```java
package com.epitech.pacmax.ai;

public class PatrolAI implements GhostAI {
    private double playerX, playerY;
    private int patrolIndex = 0;
    private double[][] patrolPoints = {
        {100, 100}, {200, 100}, {200, 200}, {100, 200}
    };
    
    @Override
    public Direction chooseDirection(Ghost ghost) {
        // Aller vers le prochain point de patrouille
        double targetX = patrolPoints[patrolIndex][0];
        double targetY = patrolPoints[patrolIndex][1];
        
        // Si proche du point, passer au suivant
        double distance = Math.sqrt(
            Math.pow(ghost.getCenterX() - targetX, 2) +
            Math.pow(ghost.getCenterY() - targetY, 2)
        );
        
        if (distance < 10) {
            patrolIndex = (patrolIndex + 1) % patrolPoints.length;
        }
        
        // Se diriger vers le point
        double dx = targetX - ghost.getCenterX();
        double dy = targetY - ghost.getCenterY();
        
        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? Direction.RIGHT : Direction.LEFT;
        } else {
            return dy > 0 ? Direction.DOWN : Direction.UP;
        }
    }
    
    @Override
    public void setPlayerPosition(double x, double y) {
        this.playerX = x;
        this.playerY = y;
    }
}
```

**Étape 2** : Utiliser la nouvelle IA
```java
Ghost patrolGhost = new Ghost(100, 100, GhostType.INKY, new PatrolAI());
```

### Package `engine` - Moteur de Jeu

#### GameEngine.java (Singleton)

**Pourquoi Singleton ?**
- Un seul moteur de jeu doit exister
- Accès global depuis n'importe où

**Comment l'utiliser** :
```java
// Récupérer l'instance
GameEngine engine = GameEngine.getInstance();

// Accéder aux entités
Player player = engine.getPlayer();
List<Ghost> ghosts = engine.getGhosts();

// Changer l'état
engine.setState(GameState.PAUSED);
```

**Boucle de jeu expliquée** :
```java
public void update(double deltaTime) {
    // 1. Mettre à jour les entités
    player.update(deltaTime);
    
    // 2. Mettre à jour les IA
    for (Ghost ghost : ghosts) {
        ghost.getAI().setPlayerPosition(player.getCenterX(), player.getCenterY());
        ghost.update(deltaTime);
    }
    
    // 3. Vérifier les collisions
    checkCollisions();
    
    // 4. Vérifier les conditions de victoire/défaite
    checkGameConditions();
}
```

### Package `utils` - Utilitaires

#### CollisionManager.java

**Algorithme AABB (Axis-Aligned Bounding Box)** :
```java
// Deux rectangles se chevauchent si :
// - Le bord gauche de A est à gauche du bord droit de B
// - Le bord droit de A est à droite du bord gauche de B
// - Le bord haut de A est au-dessus du bord bas de B
// - Le bord bas de A est en-dessous du bord haut de B

boolean collision = 
    a.x < b.x + b.width &&
    a.x + a.width > b.x &&
    a.y < b.y + b.height &&
    a.y + a.height > b.y;
```

**Résolution de collision** :
```java
// Calculer le chevauchement sur chaque axe
double overlapX = min(a.right - b.left, b.right - a.left);
double overlapY = min(a.bottom - b.top, b.bottom - a.top);

// Résoudre selon l'axe de moindre chevauchement
if (overlapX < overlapY) {
    // Pousser horizontalement
} else {
    // Pousser verticalement
}
```

## 🎨 Ajouter de Nouvelles Fonctionnalités

### Exemple 1 : Ajouter un Fruit Bonus

**1. Créer la classe Fruit**
```java
package com.epitech.pacmax.entities;

public class Fruit extends Entity {
    private FruitType type;
    private int points;
    private double lifespan;
    
    public Fruit(double x, double y, FruitType type) {
        super(x, y, 20, 20);
        this.type = type;
        this.points = type.getPoints();
        this.lifespan = 10.0; // 10 secondes
    }
    
    @Override
    public void update(double deltaTime) {
        lifespan -= deltaTime;
        if (lifespan <= 0) {
            active = false;
            visible = false;
        }
    }
    
    @Override
    public void render(GraphicsContext gc) {
        if (!visible) return;
        
        gc.setFill(type.getColor());
        gc.fillOval(x, y, width, height);
        
        // Effet de clignotement en fin de vie
        if (lifespan < 3.0) {
            double alpha = 0.5 + 0.5 * Math.sin(lifespan * 10);
            gc.setGlobalAlpha(alpha);
        }
        gc.setGlobalAlpha(1.0);
    }
    
    public enum FruitType {
        CHERRY(100, Color.RED),
        STRAWBERRY(300, Color.PINK),
        ORANGE(500, Color.ORANGE);
        
        private final int points;
        private final Color color;
        
        FruitType(int points, Color color) {
            this.points = points;
            this.color = color;
        }
        
        public int getPoints() { return points; }
        public Color getColor() { return color; }
    }
}
```

**2. Intégrer dans GameEngine**
```java
// Dans GameEngine.java
private List<Fruit> fruits;

// Dans update()
for (Fruit fruit : fruits) {
    fruit.update(deltaTime);
    if (collisionManager.checkCollision(player, fruit) && fruit.isActive()) {
        player.addScore(fruit.getPoints());
        fruit.setActive(false);
    }
}
```

### Exemple 2 : Ajouter un Système de Combo

**1. Créer ComboManager**
```java
package com.epitech.pacmax.utils;

public class ComboManager {
    private int comboCount;
    private double comboTimer;
    private static final double COMBO_TIMEOUT = 2.0;
    
    public void update(double deltaTime) {
        if (comboCount > 0) {
            comboTimer -= deltaTime;
            if (comboTimer <= 0) {
                resetCombo();
            }
        }
    }
    
    public int addToCombo() {
        comboCount++;
        comboTimer = COMBO_TIMEOUT;
        return getComboMultiplier();
    }
    
    public int getComboMultiplier() {
        if (comboCount < 5) return 1;
        if (comboCount < 10) return 2;
        if (comboCount < 20) return 3;
        return 5;
    }
    
    public void resetCombo() {
        comboCount = 0;
        comboTimer = 0;
    }
}
```

**2. Utiliser dans Player**
```java
// Quand le joueur mange une pac-gum
int multiplier = comboManager.addToCombo();
player.addScore(pacGum.getPoints() * multiplier);
```

## 🧪 Écrire des Tests Unitaires

### Structure d'un test JUnit 5

```java
@Test
@DisplayName("Description claire de ce que teste le test")
void testMethodName() {
    // 1. ARRANGE - Préparer les données
    Player player = new Player(100, 100);
    
    // 2. ACT - Exécuter l'action
    player.addScore(100);
    
    // 3. ASSERT - Vérifier le résultat
    assertEquals(100, player.getScore(), "Le score devrait être 100");
}
```

### Tests paramétrés

```java
@ParameterizedTest
@ValueSource(ints = {10, 50, 100, 500})
@DisplayName("Ajouter différents scores")
void testAddVariousScores(int points) {
    Player player = new Player(100, 100);
    player.addScore(points);
    assertEquals(points, player.getScore());
}
```

### Tests avec @BeforeEach et @AfterEach

```java
class GameEngineTest {
    private GameEngine engine;
    
    @BeforeEach
    void setUp() {
        engine = GameEngine.getInstance();
        engine.initialize();
    }
    
    @AfterEach
    void tearDown() {
        engine.stop();
    }
    
    @Test
    void testSomething() {
        // Le moteur est déjà initialisé
    }
}
```

## 🎯 Bonnes Pratiques

### 1. Nommage
```java
// ✅ BON
public class PlayerMovementController { }
private int playerScore;
public void calculateTotalScore() { }

// ❌ MAUVAIS
public class PMC { }
private int ps;
public void calc() { }
```

### 2. Commentaires Javadoc
```java
/**
 * Calcule le score total avec les bonus.
 * 
 * @param baseScore Le score de base
 * @param bonusMultiplier Le multiplicateur de bonus
 * @return Le score total calculé
 * @throws IllegalArgumentException si baseScore est négatif
 */
public int calculateTotalScore(int baseScore, double bonusMultiplier) {
    if (baseScore < 0) {
        throw new IllegalArgumentException("Le score ne peut pas être négatif");
    }
    return (int) (baseScore * bonusMultiplier);
}
```

### 3. Gestion des erreurs
```java
// ✅ BON - Gestion explicite
public GameMap loadLevel(String path) {
    try {
        return GameMap.loadFromFile(path);
    } catch (IOException e) {
        System.err.println("Erreur de chargement du niveau: " + e.getMessage());
        return createDefaultLevel();
    }
}

// ❌ MAUVAIS - Ignorer les erreurs
public GameMap loadLevel(String path) {
    try {
        return GameMap.loadFromFile(path);
    } catch (IOException e) {
        // Rien
    }
    return null;
}
```

### 4. Principe DRY (Don't Repeat Yourself)
```java
// ✅ BON
private void updateEntity(Entity entity, double deltaTime) {
    entity.update(deltaTime);
    if (!entity.isActive()) {
        removeEntity(entity);
    }
}

public void updateAll(double deltaTime) {
    ghosts.forEach(g -> updateEntity(g, deltaTime));
    pacGums.forEach(p -> updateEntity(p, deltaTime));
}

// ❌ MAUVAIS
public void updateAll(double deltaTime) {
    for (Ghost g : ghosts) {
        g.update(deltaTime);
        if (!g.isActive()) removeGhost(g);
    }
    for (PacGum p : pacGums) {
        p.update(deltaTime);
        if (!p.isActive()) removePacGum(p);
    }
}
```

## 🐛 Debugging

### Activer les logs de debug
```java
public class GameEngine {
    private static final boolean DEBUG = true;
    
    private void checkCollisions() {
        if (DEBUG) {
            System.out.println("Checking collisions for " + ghosts.size() + " ghosts");
        }
        // ...
    }
}
```

### Afficher les hitboxes
```java
@Override
public void render(GraphicsContext gc) {
    // Rendu normal
    gc.setFill(Color.YELLOW);
    gc.fillOval(x, y, width, height);
    
    // Debug : afficher la hitbox
    if (GameEngine.DEBUG) {
        gc.setStroke(Color.RED);
        gc.setLineWidth(1);
        gc.strokeRect(x, y, width, height);
    }
}
```

## 📊 Génération de la Documentation

### Javadoc
```bash
# Générer la Javadoc
mvn javadoc:javadoc

# Ouvrir dans le navigateur
start target/site/apidocs/index.html
```

### UML avec PlantUML
```bash
# Installer PlantUML
# Créer un fichier .puml

@startuml
abstract class Entity {
    # double x, y
    # double width, height
    + {abstract} update(deltaTime)
    + {abstract} render(gc)
}

class Player extends Entity {
    - int lives
    - int score
    + addScore(points)
    + loseLife()
}

interface GhostAI {
    + chooseDirection(ghost)
}

class Ghost extends Entity {
    - GhostAI ai
    - GhostState state
}

Ghost --> GhostAI
@enduml
```

## 🚀 Workflow Git Recommandé

```bash
# Créer une branche pour une fonctionnalité
git checkout -b feature/add-fruit-bonus

# Faire des commits atomiques
git add src/main/java/com/epitech/pacmax/entities/Fruit.java
git commit -m "feat: Add Fruit entity class"

git add src/main/java/com/epitech/pacmax/engine/GameEngine.java
git commit -m "feat: Integrate Fruit spawning in GameEngine"

# Pousser et créer une PR
git push origin feature/add-fruit-bonus
```

## 📝 Checklist Avant de Commit

- [ ] Le code compile sans erreur
- [ ] Les tests passent (`mvn test`)
- [ ] Javadoc ajoutée pour les méthodes publiques
- [ ] Pas de code commenté inutile
- [ ] Nommage cohérent avec le reste du projet
- [ ] Pas de valeurs hardcodées (utiliser des constantes)
- [ ] Gestion des erreurs appropriée

## 🎓 Ressources d'Apprentissage

### Java & POO
- [Oracle Java Tutorials](https://docs.oracle.com/javase/tutorial/)
- [Effective Java (livre)](https://www.oreilly.com/library/view/effective-java/9780134686097/)

### Design Patterns
- [Refactoring Guru](https://refactoring.guru/design-patterns)
- [Design Patterns (Gang of Four)](https://en.wikipedia.org/wiki/Design_Patterns)

### JavaFX
- [OpenJFX Documentation](https://openjfx.io/)
- [JavaFX Tutorial](https://jenkov.com/tutorials/javafx/index.html)

### Tests
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)

---

**Bon développement ! 🚀**
