# Guide des Tests - Pac-Max

## 🧪 Tests Unitaires Suggérés

### Tests à Implémenter

#### 1. PlayerTest ✅ (Déjà créé)
- Vies initiales
- Score initial
- Ajout de score
- Perte de vie
- Power-up
- Déplacement

#### 2. GhostTest
```java
- État initial
- Changement d'état (CHASE → SCARED)
- Respawn
- Changement d'IA
```

#### 3. CollisionManagerTest ✅ (Déjà créé)
- Détection AABB
- Résolution avec murs
- Calcul de distance

#### 4. ScoreManagerTest
```java
- Sauvegarde JSON
- Chargement JSON
- Ajout de score
- Tri des scores
```

#### 5. GameEngineTest
```java
- Initialisation
- Changement d'état
- Chargement de niveau
```

## 🚀 Lancer les Tests

```bash
# Tous les tests
mvn test

# Tests d'une classe spécifique
mvn test -Dtest=PlayerTest

# Avec rapport de couverture
mvn test jacoco:report
```

## 📊 Couverture de Code

Ajouter au `pom.xml` :
```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
</plugin>
```
