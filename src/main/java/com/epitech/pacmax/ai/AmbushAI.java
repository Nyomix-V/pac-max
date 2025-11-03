package com.epitech.pacmax.ai;

import com.epitech.pacmax.entities.Direction;
import com.epitech.pacmax.entities.Ghost;

/**
 * IA d'embuscade qui tente de se positionner devant le joueur.
 * Utilisée par Pinky (fantôme rose).
 * 
 * @author Epitech Team
 * @version 1.0
 */
public class AmbushAI implements GhostAI {
    
    private double playerX;
    private double playerY;
    private Direction playerDirection = Direction.NONE;
    
    /** Distance d'anticipation en pixels */
    private static final double ANTICIPATION_DISTANCE = 80.0;
    
    @Override
    public Direction chooseDirection(Ghost ghost) {
        // Calcule une position cible devant le joueur
        double targetX = playerX + playerDirection.getDx() * ANTICIPATION_DISTANCE;
        double targetY = playerY + playerDirection.getDy() * ANTICIPATION_DISTANCE;
        
        double dx = targetX - ghost.getCenterX();
        double dy = targetY - ghost.getCenterY();
        
        // Se dirige vers la position anticipée
        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? Direction.RIGHT : Direction.LEFT;
        } else {
            return dy > 0 ? Direction.DOWN : Direction.UP;
        }
    }
    
    @Override
    public void setPlayerPosition(double playerX, double playerY) {
        this.playerX = playerX;
        this.playerY = playerY;
    }
    
    /**
     * Définit la direction actuelle du joueur pour anticiper.
     * 
     * @param direction Direction du joueur
     */
    public void setPlayerDirection(Direction direction) {
        this.playerDirection = direction;
    }
}
