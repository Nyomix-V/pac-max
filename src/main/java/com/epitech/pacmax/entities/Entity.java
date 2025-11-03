package com.epitech.pacmax.entities;

import javafx.scene.canvas.GraphicsContext;

/**
 * Classe abstraite de base pour toutes les entités du jeu.
 * Implémente les interfaces Movable, Collidable et Renderable.
 * Démontre l'utilisation de l'héritage et du polymorphisme.
 * 
 * @author Epitech Team
 * @version 1.0
 */
public abstract class Entity implements Movable, Collidable, Renderable {
    
    /** Position X de l'entité */
    protected double x;
    
    /** Position Y de l'entité */
    protected double y;
    
    /** Largeur de l'entité */
    protected double width;
    
    /** Hauteur de l'entité */
    protected double height;
    
    /** Vitesse de déplacement */
    protected double speed;
    
    /** Direction actuelle */
    protected Direction direction;
    
    /** Visibilité de l'entité */
    protected boolean visible;
    
    /** Indique si l'entité est active */
    protected boolean active;
    
    /**
     * Constructeur de base d'une entité.
     * 
     * @param x Position X initiale
     * @param y Position Y initiale
     * @param width Largeur de l'entité
     * @param height Hauteur de l'entité
     */
    public Entity(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.direction = Direction.NONE;
        this.speed = 0;
        this.visible = true;
        this.active = true;
    }
    
    /**
     * Mise à jour de l'entité à chaque frame.
     * Méthode abstraite à implémenter par les sous-classes.
     * 
     * @param deltaTime Le temps écoulé depuis la dernière frame
     */
    public abstract void update(double deltaTime);
    
    @Override
    public void move(double deltaTime) {
        if (direction != Direction.NONE) {
            x += direction.getDx() * speed * deltaTime;
            y += direction.getDy() * speed * deltaTime;
        }
    }
    
    @Override
    public boolean collidesWith(Collidable other) {
        return x < other.getX() + other.getWidth() &&
               x + width > other.getX() &&
               y < other.getY() + other.getHeight() &&
               y + height > other.getY();
    }
    
    @Override
    public void onCollision(Collidable other) {
        // Implémentation par défaut vide
        // Les sous-classes peuvent override cette méthode
    }
    
    // Getters et Setters
    
    @Override
    public double getX() {
        return x;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    @Override
    public double getY() {
        return y;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    @Override
    public double getWidth() {
        return width;
    }
    
    @Override
    public double getHeight() {
        return height;
    }
    
    @Override
    public Direction getDirection() {
        return direction;
    }
    
    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    
    @Override
    public double getSpeed() {
        return speed;
    }
    
    @Override
    public void setSpeed(double speed) {
        this.speed = speed;
    }
    
    @Override
    public boolean isVisible() {
        return visible;
    }
    
    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    /**
     * Récupère le centre X de l'entité.
     * 
     * @return La position X du centre
     */
    public double getCenterX() {
        return x + width / 2;
    }
    
    /**
     * Récupère le centre Y de l'entité.
     * 
     * @return La position Y du centre
     */
    public double getCenterY() {
        return y + height / 2;
    }
}
