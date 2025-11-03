package com.epitech.pacmax.utils;

import com.epitech.pacmax.entities.Collidable;
import com.epitech.pacmax.entities.Entity;
import com.epitech.pacmax.entities.Wall;

/**
 * Gestionnaire de collisions du jeu.
 * Centralise la détection et la résolution des collisions.
 * 
 * @author Epitech Team
 * @version 1.0
 */
public class CollisionManager {
    
    /**
     * Vérifie si deux entités sont en collision (AABB - Axis-Aligned Bounding Box).
     * 
     * @param a Première entité
     * @param b Deuxième entité
     * @return true si collision, false sinon
     */
    public boolean checkCollision(Collidable a, Collidable b) {
        return a.getX() < b.getX() + b.getWidth() &&
               a.getX() + a.getWidth() > b.getX() &&
               a.getY() < b.getY() + b.getHeight() &&
               a.getY() + a.getHeight() > b.getY();
    }
    
    /**
     * Résout une collision entre une entité mobile et un mur.
     * Empêche l'entité de traverser le mur.
     * 
     * @param entity L'entité mobile
     * @param wall Le mur
     */
    public void resolveWallCollision(Entity entity, Wall wall) {
        // Calculer le chevauchement sur chaque axe
        double overlapX = Math.min(
            entity.getX() + entity.getWidth() - wall.getX(),
            wall.getX() + wall.getWidth() - entity.getX()
        );
        
        double overlapY = Math.min(
            entity.getY() + entity.getHeight() - wall.getY(),
            wall.getY() + wall.getHeight() - entity.getY()
        );
        
        // Résoudre selon l'axe de moindre chevauchement
        if (overlapX < overlapY) {
            // Collision horizontale
            if (entity.getX() < wall.getX()) {
                entity.setX(wall.getX() - entity.getWidth());
            } else {
                entity.setX(wall.getX() + wall.getWidth());
            }
        } else {
            // Collision verticale
            if (entity.getY() < wall.getY()) {
                entity.setY(wall.getY() - entity.getHeight());
            } else {
                entity.setY(wall.getY() + wall.getHeight());
            }
        }
    }
    
    /**
     * Vérifie si une position donnée est en collision avec un mur.
     * 
     * @param x Position X
     * @param y Position Y
     * @param width Largeur
     * @param height Hauteur
     * @param wall Le mur à tester
     * @return true si collision
     */
    public boolean wouldCollideWithWall(double x, double y, double width, double height, Wall wall) {
        return x < wall.getX() + wall.getWidth() &&
               x + width > wall.getX() &&
               y < wall.getY() + wall.getHeight() &&
               y + height > wall.getY();
    }
    
    /**
     * Calcule la distance entre deux entités.
     * 
     * @param a Première entité
     * @param b Deuxième entité
     * @return La distance en pixels
     */
    public double getDistance(Collidable a, Collidable b) {
        double dx = (a.getX() + a.getWidth() / 2) - (b.getX() + b.getWidth() / 2);
        double dy = (a.getY() + a.getHeight() / 2) - (b.getY() + b.getHeight() / 2);
        return Math.sqrt(dx * dx + dy * dy);
    }
}
