package com.epitech.pacmax.ai;

import com.epitech.pacmax.entities.Direction;
import com.epitech.pacmax.entities.Ghost;

/**
 * IA de fuite qui s'éloigne du joueur.
 * Utilisée quand les fantômes sont en mode "scared".
 * 
 * @author Epitech Team
 * @version 1.0
 */
public class FleeAI implements GhostAI {
    
    private double playerX;
    private double playerY;
    
    @Override
    public Direction chooseDirection(Ghost ghost) {
        double dx = playerX - ghost.getCenterX();
        double dy = playerY - ghost.getCenterY();
        
        // Choisit la direction qui éloigne le plus du joueur
        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? Direction.LEFT : Direction.RIGHT;
        } else {
            return dy > 0 ? Direction.UP : Direction.DOWN;
        }
    }
    
    @Override
    public void setPlayerPosition(double playerX, double playerY) {
        this.playerX = playerX;
        this.playerY = playerY;
    }
}
