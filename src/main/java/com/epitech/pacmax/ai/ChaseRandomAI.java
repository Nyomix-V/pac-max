package com.epitech.pacmax.ai;

import com.epitech.pacmax.entities.Direction;
import com.epitech.pacmax.entities.Ghost;

import java.util.Random;

/**
 * IA qui combine mouvement aléatoire et poursuite conditionnelle.
 * Poursuit le joueur uniquement quand il n'est pas sous powerup (< 30 pixels).
 * Sinon, se déplace de manière complètement aléatoire et imprévisible.
 * 
 * @author Epitech Team
 * @version 1.0
 */
public class ChaseRandomAI implements GhostAI {
    
    private final Random random;
    private double playerX;
    private double playerY;
    private boolean playerPowered;
    private int moveCounter;
    
    /** Distance en dessous de laquelle on considère le joueur "proche" pour la poursuite */
    private static final double CHASE_DISTANCE = 30.0;
    
    /** Fréquence de changement de direction en mode aléatoire (frames) */
    private static final int RANDOM_CHANGE_FREQUENCY = 15;
    
    public ChaseRandomAI() {
        this.random = new Random();
        this.moveCounter = 0;
        this.playerPowered = false;
    }
    
    @Override
    public Direction chooseDirection(Ghost ghost) {
        moveCounter++;
        
        // Calculer la distance au joueur
        double dx = playerX - ghost.getCenterX();
        double dy = playerY - ghost.getCenterY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        // Si le joueur n'est PAS sous powerup ET qu'il est proche, le poursuivre
        if (!playerPowered && distance < CHASE_DISTANCE) {
            // Mode poursuite : aller vers le joueur
            if (Math.abs(dx) > Math.abs(dy)) {
                return dx > 0 ? Direction.RIGHT : Direction.LEFT;
            } else {
                return dy > 0 ? Direction.DOWN : Direction.UP;
            }
        }
        
        // Sinon, mouvement complètement aléatoire et imprévisible
        // Change de direction très fréquemment pour être imprévisible
        if (moveCounter >= RANDOM_CHANGE_FREQUENCY || ghost.getDirection() == Direction.NONE) {
            moveCounter = 0;
            
            Direction[] directions = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
            Direction newDirection = directions[random.nextInt(directions.length)];
            
            // Parfois, on accepte même les demi-tours pour être vraiment imprévisible
            if (random.nextDouble() < 0.3) {
                return newDirection; // 30% de chance de faire n'importe quoi, même demi-tour
            }
            
            // Sinon, évite juste de faire demi-tour
            if (newDirection != ghost.getDirection().opposite()) {
                return newDirection;
            }
        }
        
        // Garde la direction actuelle
        return ghost.getDirection();
    }
    
    @Override
    public void setPlayerPosition(double playerX, double playerY) {
        this.playerX = playerX;
        this.playerY = playerY;
    }
    
    /**
     * Définit si le joueur est actuellement sous powerup.
     * 
     * @param powered true si le joueur est sous powerup
     */
    public void setPlayerPowered(boolean powered) {
        this.playerPowered = powered;
    }
}
