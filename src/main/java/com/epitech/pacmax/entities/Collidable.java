package com.epitech.pacmax.entities;

/**
 * Interface pour les entités qui peuvent entrer en collision.
 * Permet la détection et la gestion des collisions.
 * 
 * @author Epitech Team
 * @version 1.0
 */
public interface Collidable {
    
    /**
     * Vérifie si cette entité entre en collision avec une autre.
     * 
     * @param other L'autre entité à tester
     * @return true si collision, false sinon
     */
    boolean collidesWith(Collidable other);
    
    /**
     * Récupère la position X de l'entité.
     * 
     * @return La position X en pixels
     */
    double getX();
    
    /**
     * Récupère la position Y de l'entité.
     * 
     * @return La position Y en pixels
     */
    double getY();
    
    /**
     * Récupère la largeur de la boîte de collision.
     * 
     * @return La largeur en pixels
     */
    double getWidth();
    
    /**
     * Récupère la hauteur de la boîte de collision.
     * 
     * @return La hauteur en pixels
     */
    double getHeight();
    
    /**
     * Méthode appelée lorsqu'une collision se produit.
     * 
     * @param other L'entité avec laquelle la collision s'est produite
     */
    void onCollision(Collidable other);
}
