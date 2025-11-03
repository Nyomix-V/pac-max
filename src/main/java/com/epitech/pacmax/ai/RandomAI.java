package com.epitech.pacmax.ai;

import com.epitech.pacmax.entities.Direction;
import com.epitech.pacmax.entities.Ghost;

import java.util.Random;

/**
 * IA aléatoire qui change de direction de manière imprévisible.
 * Utilisée par Clyde (fantôme orange).
 * 
 * @author Epitech Team
 * @version 1.0
 */
public class RandomAI implements GhostAI {
    
    private final Random random;
    private double playerX;
    private double playerY;
    private int moveCounter;
    
    /** Nombre de frames avant de potentiellement changer de direction */
    private static final int CHANGE_FREQUENCY = 60;
    
    public RandomAI() {
        this.random = new Random();
        this.moveCounter = 0;
    }
    
    @Override
    public Direction chooseDirection(Ghost ghost) {
        moveCounter++;
        
        // Change de direction aléatoirement
        if (moveCounter >= CHANGE_FREQUENCY) {
            moveCounter = 0;
            
            Direction[] directions = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
            Direction newDirection = directions[random.nextInt(directions.length)];
            
            // Évite de faire demi-tour
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
}
