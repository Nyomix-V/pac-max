package com.epitech.pacmax.engine;

import com.epitech.pacmax.entities.*;
import com.epitech.pacmax.levels.GameMap;
import com.epitech.pacmax.utils.CollisionManager;
import javafx.animation.AnimationTimer;

import java.util.ArrayList;
import java.util.List;

/**
 * Moteur principal du jeu.
 * Implémente le Pattern Singleton pour garantir une seule instance.
 * Gère la boucle de jeu, les mises à jour et les collisions.
 * 
 * @author Epitech Team
 * @version 1.0
 */
public class GameEngine {
    
    /** Instance unique du moteur (Pattern Singleton) */
    private static GameEngine instance;
    
    /** État actuel du jeu */
    private GameState currentState;
    
    /** Joueur */
    private Player player;
    
    /** Liste des fantômes */
    private List<Ghost> ghosts;
    
    /** Liste des pac-gums */
    private List<PacGum> pacGums;
    
    /** Liste des murs */
    private List<Wall> walls;
    
    /** Carte actuelle */
    private GameMap currentMap;
    
    /** Gestionnaire de collisions */
    private CollisionManager collisionManager;
    
    /** Timer d'animation JavaFX */
    private AnimationTimer gameLoop;
    
    /** Temps de la dernière frame */
    private long lastFrameTime;
    
    /** Niveau actuel */
    private int currentLevel;
    
    /** Nombre total de pac-gums au début du niveau */
    private int totalPacGums;
    
    /**
     * Constructeur privé (Pattern Singleton).
     */
    private GameEngine() {
        this.currentState = GameState.MENU;
        this.ghosts = new ArrayList<>();
        this.pacGums = new ArrayList<>();
        this.walls = new ArrayList<>();
        this.collisionManager = new CollisionManager();
        this.currentLevel = 1;
        this.lastFrameTime = System.nanoTime();
    }
    
    /**
     * Récupère l'instance unique du moteur (Pattern Singleton).
     * 
     * @return L'instance du GameEngine
     */
    public static GameEngine getInstance() {
        if (instance == null) {
            instance = new GameEngine();
        }
        return instance;
    }
    
    /**
     * Initialise le jeu avec une nouvelle partie.
     */
    public void initialize() {
        // Créer le joueur
        player = new Player(400, 300);
        
        // Charger la carte (pour l'instant, une carte simple)
        loadLevel(currentLevel);
        
        currentState = GameState.RUNNING;
    }
    
    /**
     * Charge un niveau.
     * 
     * @param level Numéro du niveau à charger
     */
    private void loadLevel(int level) {
        // TODO: Charger depuis un fichier JSON
        // Pour l'instant, création d'un niveau simple
        
        ghosts.clear();
        pacGums.clear();
        walls.clear();
        
        // Créer des murs de bordure
        createBorderWalls();
        
        // Créer quelques pac-gums
        createPacGums();
        
        // Créer les fantômes avec différentes IA
        createGhosts();
        
        totalPacGums = pacGums.size();
    }
    
    /**
     * Crée les murs de bordure du niveau.
     */
    private void createBorderWalls() {
        int tileSize = 30;
        int mapWidth = 800;
        int mapHeight = 600;
        
        // Murs horizontaux
        for (int x = 0; x < mapWidth; x += tileSize) {
            walls.add(new Wall(x, 0, tileSize, tileSize)); // Haut
            walls.add(new Wall(x, mapHeight - tileSize, tileSize, tileSize)); // Bas
        }
        
        // Murs verticaux
        for (int y = tileSize; y < mapHeight - tileSize; y += tileSize) {
            walls.add(new Wall(0, y, tileSize, tileSize)); // Gauche
            walls.add(new Wall(mapWidth - tileSize, y, tileSize, tileSize)); // Droite
        }
        
        // Quelques murs intérieurs pour créer un labyrinthe simple
        for (int x = 100; x < 300; x += tileSize) {
            walls.add(new Wall(x, 100, tileSize, tileSize));
        }
        for (int x = 500; x < 700; x += tileSize) {
            walls.add(new Wall(x, 100, tileSize, tileSize));
        }
    }
    
    /**
     * Crée les pac-gums dans le niveau.
     */
    private void createPacGums() {
        int tileSize = 30;
        
        // Créer une grille de pac-gums
        for (int x = 60; x < 750; x += 40) {
            for (int y = 60; y < 550; y += 40) {
                // Vérifier qu'il n'y a pas de mur à cet endroit
                boolean hasWall = false;
                for (Wall wall : walls) {
                    if (Math.abs(wall.getX() - x) < 30 && Math.abs(wall.getY() - y) < 30) {
                        hasWall = true;
                        break;
                    }
                }
                
                if (!hasWall) {
                    // 5% de chance d'être un power-up
                    PacGum.PacGumType type = Math.random() < 0.05 ? 
                        PacGum.PacGumType.POWER_UP : PacGum.PacGumType.NORMAL;
                    pacGums.add(new PacGum(x, y, type));
                }
            }
        }
    }
    
    /**
     * Crée les fantômes avec leurs IA respectives.
     */
    private void createGhosts() {
        ghosts.add(new Ghost(100, 100, Ghost.GhostType.BLINKY, 
            new com.epitech.pacmax.ai.AggressiveAI()));
        ghosts.add(new Ghost(700, 100, Ghost.GhostType.PINKY, 
            new com.epitech.pacmax.ai.AmbushAI()));
        ghosts.add(new Ghost(100, 500, Ghost.GhostType.INKY, 
            new com.epitech.pacmax.ai.RandomAI()));
        ghosts.add(new Ghost(700, 500, Ghost.GhostType.CLYDE, 
            new com.epitech.pacmax.ai.RandomAI()));
    }
    
    /**
     * Démarre la boucle de jeu.
     */
    public void start() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double deltaTime = (now - lastFrameTime) / 1_000_000_000.0;
                lastFrameTime = now;
                
                update(deltaTime);
            }
        };
        gameLoop.start();
    }
    
    /**
     * Arrête la boucle de jeu.
     */
    public void stop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }
    
    /**
     * Met à jour le jeu à chaque frame.
     * 
     * @param deltaTime Temps écoulé depuis la dernière frame
     */
    public void update(double deltaTime) {
        if (currentState != GameState.RUNNING) {
            return;
        }
        
        // Limiter le deltaTime pour éviter les gros sauts
        if (deltaTime > 0.1) deltaTime = 0.1;
        
        // Mettre à jour le joueur
        player.update(deltaTime);
        
        // Mettre à jour les fantômes
        for (Ghost ghost : ghosts) {
            // Mettre à jour la position du joueur pour l'IA
            if (ghost.getAI() != null) {
                ghost.getAI().setPlayerPosition(player.getCenterX(), player.getCenterY());
            }
            ghost.update(deltaTime);
        }
        
        // Vérifier les collisions
        checkCollisions();
        
        // Vérifier les conditions de victoire/défaite
        checkGameConditions();
    }
    
    /**
     * Vérifie toutes les collisions.
     */
    private void checkCollisions() {
        // Collision joueur - murs
        for (Wall wall : walls) {
            if (collisionManager.checkCollision(player, wall)) {
                collisionManager.resolveWallCollision(player, wall);
            }
        }
        
        // Collision fantômes - murs
        for (Ghost ghost : ghosts) {
            for (Wall wall : walls) {
                if (collisionManager.checkCollision(ghost, wall)) {
                    collisionManager.resolveWallCollision(ghost, wall);
                }
            }
        }
        
        // Collision joueur - pac-gums
        for (PacGum pacGum : pacGums) {
            if (!pacGum.isCollected() && collisionManager.checkCollision(player, pacGum)) {
                pacGum.collect();
                player.addScore(pacGum.getPoints());
                
                // Activer le power-up si c'est un power-up
                if (pacGum.getType() == PacGum.PacGumType.POWER_UP) {
                    player.activatePowerUp();
                    // Mettre tous les fantômes en mode scared
                    for (Ghost ghost : ghosts) {
                        ghost.setState(Ghost.GhostState.SCARED);
                    }
                }
            }
        }
        
        // Collision joueur - fantômes
        for (Ghost ghost : ghosts) {
            if (collisionManager.checkCollision(player, ghost)) {
                if (ghost.getState() == Ghost.GhostState.SCARED) {
                    // Le joueur mange le fantôme
                    ghost.setState(Ghost.GhostState.DEAD);
                    ghost.respawn();
                    player.addScore(200);
                } else if (ghost.getState() != Ghost.GhostState.DEAD) {
                    // Le fantôme touche le joueur
                    player.loseLife();
                    if (player.getLives() > 0) {
                        resetPositions();
                    } else {
                        currentState = GameState.GAME_OVER;
                    }
                }
            }
        }
    }
    
    /**
     * Vérifie les conditions de victoire/défaite.
     */
    private void checkGameConditions() {
        // Compter les pac-gums restantes
        long remainingPacGums = pacGums.stream()
            .filter(p -> !p.isCollected())
            .count();
        
        if (remainingPacGums == 0) {
            currentState = GameState.LEVEL_COMPLETE;
            currentLevel++;
        }
    }
    
    /**
     * Réinitialise les positions après une mort.
     */
    private void resetPositions() {
        player.reset(400, 300);
        for (Ghost ghost : ghosts) {
            ghost.respawn();
        }
    }
    
    /**
     * Change l'état du jeu.
     * 
     * @param newState Le nouvel état
     */
    public void setState(GameState newState) {
        this.currentState = newState;
    }
    
    // Getters
    
    public GameState getCurrentState() {
        return currentState;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public List<Ghost> getGhosts() {
        return ghosts;
    }
    
    public List<PacGum> getPacGums() {
        return pacGums;
    }
    
    public List<Wall> getWalls() {
        return walls;
    }
    
    public int getCurrentLevel() {
        return currentLevel;
    }
}
