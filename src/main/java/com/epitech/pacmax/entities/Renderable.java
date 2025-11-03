package com.epitech.pacmax.entities;

import javafx.scene.canvas.GraphicsContext;

/**
 * Interface pour les entités qui peuvent être affichées à l'écran.
 * Sépare la logique de rendu de la logique métier.
 * 
 * @author Epitech Team
 * @version 1.0
 */
public interface Renderable {
    
    /**
     * Dessine l'entité sur le contexte graphique fourni.
     * 
     * @param gc Le contexte graphique JavaFX
     */
    void render(GraphicsContext gc);
    
    /**
     * Indique si l'entité doit être affichée.
     * 
     * @return true si visible, false sinon
     */
    boolean isVisible();
    
    /**
     * Définit la visibilité de l'entité.
     * 
     * @param visible true pour rendre visible, false pour cacher
     */
    void setVisible(boolean visible);
}
