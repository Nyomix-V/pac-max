package com.epitech.pacmax.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Classe représentant une pac-gum (point à collecter).
 * Peut être une pac-gum normale ou un power-up.
 * 
 * @author Epitech Team
 * @version 1.0
 */
public class PacGum extends Entity {
    
    /** Type de pac-gum */
    private PacGumType type;
    
    /** Points accordés lors de la collecte */
    private int points;
    
    /** Indique si la pac-gum a été collectée */
    private boolean collected;
    
    /**
     * Constructeur d'une pac-gum.
     * 
     * @param x Position X
     * @param y Position Y
     * @param type Type de pac-gum
     */
    public PacGum(double x, double y, PacGumType type) {
        super(x, y, type == PacGumType.POWER_UP ? 12 : 4, type == PacGumType.POWER_UP ? 12 : 4);
        this.type = type;
        this.points = type == PacGumType.POWER_UP ? 50 : 10;
        this.collected = false;
        this.speed = 0; // Les pac-gums ne bougent pas
    }
    
    @Override
    public void update(double deltaTime) {
        // Les pac-gums sont statiques, pas de mise à jour nécessaire
    }
    
    @Override
    public void render(GraphicsContext gc) {
        if (!visible || collected) return;
        
        if (type == PacGumType.POWER_UP) {
            // Power-up : gros point qui clignote
            double alpha = 0.5 + 0.5 * Math.sin(System.currentTimeMillis() / 200.0);
            gc.setFill(Color.color(1, 1, 1, alpha));
            gc.fillOval(x, y, width, height);
        } else {
            // Pac-gum normale : petit point blanc
            gc.setFill(Color.WHITE);
            gc.fillOval(x, y, width, height);
        }
    }
    
    /**
     * Marque la pac-gum comme collectée.
     */
    public void collect() {
        collected = true;
        visible = false;
        active = false;
    }
    
    /**
     * Réinitialise la pac-gum.
     */
    public void reset() {
        collected = false;
        visible = true;
        active = true;
    }
    
    // Getters
    
    public PacGumType getType() {
        return type;
    }
    
    public int getPoints() {
        return points;
    }
    
    public boolean isCollected() {
        return collected;
    }
    
    /**
     * Énumération des types de pac-gums.
     */
    public enum PacGumType {
        NORMAL,    // Pac-gum normale
        POWER_UP   // Power-up (permet de manger les fantômes)
    }
}
