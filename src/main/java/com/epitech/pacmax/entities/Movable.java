package com.epitech.pacmax.entities;

/**
 * Interface pour les entités qui peuvent se déplacer dans le jeu.
 * Implémente le principe d'interface segregation (SOLID).
 * 
 * @author Epitech Team
 * @version 1.0
 */
public interface Movable {
    
    /**
     * Déplace l'entité selon sa direction et sa vitesse actuelles.
     * 
     * @param deltaTime Le temps écoulé depuis la dernière frame (en secondes)
     */
    void move(double deltaTime);
    
    /**
     * Définit la direction de déplacement de l'entité.
     * 
     * @param direction La nouvelle direction (UP, DOWN, LEFT, RIGHT)
     */
    void setDirection(Direction direction);
    
    /**
     * Récupère la direction actuelle de l'entité.
     * 
     * @return La direction actuelle
     */
    Direction getDirection();
    
    /**
     * Définit la vitesse de déplacement de l'entité.
     * 
     * @param speed La vitesse en pixels par seconde
     */
    void setSpeed(double speed);
    
    /**
     * Récupère la vitesse actuelle de l'entité.
     * 
     * @return La vitesse en pixels par seconde
     */
    double getSpeed();
}
