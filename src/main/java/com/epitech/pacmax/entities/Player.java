package com.epitech.pacmax.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Classe représentant le joueur (Pac-Man).
 * Hérite de Entity et implémente la logique spécifique au joueur.
 * 
 * @author Epitech Team
 * @version 1.0
 */
public class Player extends Entity {
    
    /** Nombre de vies du joueur */
    private int lives;
    
    /** Score du joueur */
    private int score;
    
    /** Direction souhaitée (pour le buffer d'input) */
    private Direction nextDirection;
    
    /** Indique si le joueur est en mode power-up */
    private boolean powered;
    
    /** Temps restant du power-up */
    private double powerUpTimer;
    
    /** Durée du power-up en secondes */
    private static final double POWER_UP_DURATION = 10.0;
    
    /** Vitesse normale du joueur */
    private static final double NORMAL_SPEED = 100.0;
    
    /**
     * Constructeur du joueur.
     * 
     * @param x Position X initiale
     * @param y Position Y initiale
     */
    public Player(double x, double y) {
        super(x, y, 20, 20);
        this.lives = 3;
        this.score = 0;
        this.speed = NORMAL_SPEED;
        this.nextDirection = Direction.NONE;
        this.powered = false;
        this.powerUpTimer = 0;
    }
    
    @Override
    public void update(double deltaTime) {
        // Gestion du power-up
        if (powered) {
            powerUpTimer -= deltaTime;
            if (powerUpTimer <= 0) {
                powered = false;
                powerUpTimer = 0;
            }
        }
        
        // Tentative de changement de direction
        if (nextDirection != Direction.NONE && canChangeDirection(nextDirection)) {
            direction = nextDirection;
            nextDirection = Direction.NONE;
        }
        
        // Déplacement
        move(deltaTime);
    }
    
    @Override
    public void render(GraphicsContext gc) {
        if (!visible) return;
        
        // Couleur change selon le mode power-up
        if (powered) {
            // Effet clignotant en fin de power-up
            if (powerUpTimer < 3.0 && ((int)(powerUpTimer * 10) % 2 == 0)) {
                gc.setFill(Color.ORANGE);
            } else {
                gc.setFill(Color.YELLOW);
            }
        } else {
            gc.setFill(Color.YELLOW);
        }
        
        // Dessine Pac-Man (cercle simple pour l'instant)
        gc.fillOval(x, y, width, height);
        
        // Dessine la "bouche" selon la direction
        gc.setFill(Color.BLACK);
        double mouthSize = 5;
        switch (direction) {
            case RIGHT -> gc.fillPolygon(
                new double[]{x + width/2, x + width, x + width},
                new double[]{y + height/2, y, y + height},
                3
            );
            case LEFT -> gc.fillPolygon(
                new double[]{x + width/2, x, x},
                new double[]{y + height/2, y, y + height},
                3
            );
            case UP -> gc.fillPolygon(
                new double[]{x + width/2, x, x + width},
                new double[]{y + height/2, y, y},
                3
            );
            case DOWN -> gc.fillPolygon(
                new double[]{x + width/2, x, x + width},
                new double[]{y + height/2, y + height, y + height},
                3
            );
        }
    }
    
    /**
     * Vérifie si le joueur peut changer de direction.
     * TODO: Implémenter la vérification avec les murs de la carte.
     * 
     * @param newDirection La nouvelle direction souhaitée
     * @return true si le changement est possible
     */
    private boolean canChangeDirection(Direction newDirection) {
        // Pour l'instant, toujours autoriser
        // À améliorer avec la détection de collision avec les murs
        return true;
    }
    
    /**
     * Active le mode power-up.
     */
    public void activatePowerUp() {
        powered = true;
        powerUpTimer = POWER_UP_DURATION;
    }
    
    /**
     * Ajoute des points au score.
     * 
     * @param points Nombre de points à ajouter
     */
    public void addScore(int points) {
        score += points;
    }
    
    /**
     * Fait perdre une vie au joueur.
     */
    public void loseLife() {
        lives--;
        if (lives < 0) lives = 0;
    }
    
    /**
     * Réinitialise la position du joueur.
     * 
     * @param x Nouvelle position X
     * @param y Nouvelle position Y
     */
    public void reset(double x, double y) {
        this.x = x;
        this.y = y;
        this.direction = Direction.NONE;
        this.nextDirection = Direction.NONE;
        this.powered = false;
        this.powerUpTimer = 0;
    }
    
    // Getters et Setters
    
    public int getLives() {
        return lives;
    }
    
    public void setLives(int lives) {
        this.lives = lives;
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public Direction getNextDirection() {
        return nextDirection;
    }
    
    public void setNextDirection(Direction nextDirection) {
        this.nextDirection = nextDirection;
    }
    
    public boolean isPowered() {
        return powered;
    }
    
    public double getPowerUpTimer() {
        return powerUpTimer;
    }
}
