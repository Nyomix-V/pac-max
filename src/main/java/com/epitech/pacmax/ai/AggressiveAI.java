package com.epitech.pacmax.ai;

import com.epitech.pacmax.entities.Direction;
import com.epitech.pacmax.entities.Ghost;

/**
 * IA agressive qui poursuit directement le joueur.
 * Utilisée par Blinky (fantôme rouge).
 * 
 * @author Epitech Team
 * @version 1.0
 */
public class AggressiveAI implements GhostAI {
    
    private double playerX;
    private double playerY;
    
    @Override
    public Direction chooseDirection(Ghost ghost) {
        double dx = playerX - ghost.getCenterX();
        double dy = playerY - ghost.getCenterY();
        
        // Choisit la direction qui rapproche le plus du joueur
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
}
