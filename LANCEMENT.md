# 🚀 Guide de Lancement - Pac-Max

## ⚡ Démarrage Rapide (5 minutes)

### 1. Vérifier les prérequis
```bash
# Vérifier Java (doit être 17+)
java -version

# Vérifier Maven
mvn -version
```

### 2. Compiler le projet
```bash
cd Pacmax
mvn clean compile
```

### 3. Lancer le jeu
```bash
mvn javafx:run
```

## 🎮 Contrôles du Jeu

| Touche | Action |
|--------|--------|
| ↑ ↓ ← → | Déplacer Pac-Man |
| Z Q S D | Déplacer Pac-Man (alternative) |
| P ou ÉCHAP | Pause |
| ENTRÉE | Démarrer / Rejouer |
| ESPACE | Démarrer (depuis le menu) |

## 📦 Commandes Maven Utiles

```bash
# Compiler
mvn compile

# Lancer les tests
mvn test

# Générer le JAR
mvn package

# Générer la Javadoc
mvn javadoc:javadoc

# Nettoyer le projet
mvn clean

# Tout recompiler
mvn clean install
```

## 🐛 Résolution de Problèmes

### Erreur : "JavaFX runtime components are missing"
**Solution** : Vérifier que JavaFX est bien dans le `pom.xml`
```bash
mvn clean install
mvn javafx:run
```

### Erreur de compilation
**Solution** : Nettoyer et recompiler
```bash
mvn clean compile
```

### Les tests échouent
**Solution** : Vérifier les dépendances
```bash
mvn clean test
```

## 📊 Structure des Fichiers Générés

```
target/
├── classes/              # Classes compilées
├── test-classes/         # Tests compilés
├── site/
│   └── apidocs/         # Javadoc générée
└── pacmax-1.0-SNAPSHOT.jar  # JAR exécutable
```

## 🎯 Prochaines Étapes

1. **Tester le jeu** : `mvn javafx:run`
2. **Lire la documentation** : Voir `README.md`
3. **Comprendre l'architecture** : Voir `ARCHITECTURE.md`
4. **Commencer à développer** : Voir `GUIDE_DEVELOPPEMENT.md`
5. **Écrire des tests** : Voir `TESTS_GUIDE.md`

## 📝 Fichiers Importants

- `README.md` - Vue d'ensemble du projet
- `ARCHITECTURE.md` - Explication détaillée de l'architecture
- `GUIDE_DEVELOPPEMENT.md` - Guide pour développeurs
- `pom.xml` - Configuration Maven
- `src/main/java/com/epitech/pacmax/Main.java` - Point d'entrée

---

**Bon jeu ! 🎮**
