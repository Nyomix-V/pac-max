package com.epitech.pacmax.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Classe représentant un mur dans le labyrinthe.
 * Les murs sont des obstacles statiques.
 * 
 * @author Epitech Team
 * @version 1.0
 */
public class Wall extends Entity {
    
    /**
     * Constructeur d'un mur.
     * 
     * @param x Position X
     * @param y Position Y
     * @param width Largeur du mur
     * @param height Hauteur du mur
     */
    public Wall(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.speed = 0; // Les murs ne bougent pas
    }
    
    @Override
    public void update(double deltaTime) {
        // Les murs sont statiques, pas de mise à jour
    }
    
    @Override
    public void render(GraphicsContext gc) {
        if (!visible) return;
        
        // Dessine le mur en bleu foncé avec bordure
        gc.setFill(Color.DARKBLUE);
        gc.fillRect(x, y, width, height);
        
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);
        gc.strokeRect(x, y, width, height);
    }
}
