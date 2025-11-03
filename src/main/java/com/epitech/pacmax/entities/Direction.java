package com.epitech.pacmax.entities;

/**
 * Énumération représentant les directions possibles dans le jeu.
 * Chaque direction contient un vecteur de déplacement (dx, dy).
 * 
 * @author Epitech Team
 * @version 1.0
 */
public enum Direction {
    /** Direction vers le haut */
    UP(0, -1),
    
    /** Direction vers le bas */
    DOWN(0, 1),
    
    /** Direction vers la gauche */
    LEFT(-1, 0),
    
    /** Direction vers la droite */
    RIGHT(1, 0),
    
    /** Aucune direction (immobile) */
    NONE(0, 0);
    
    private final int dx;
    private final int dy;
    
    /**
     * Constructeur de Direction.
     * 
     * @param dx Déplacement horizontal (-1, 0, ou 1)
     * @param dy Déplacement vertical (-1, 0, ou 1)
     */
    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
    
    /**
     * Récupère le déplacement horizontal.
     * 
     * @return -1 pour gauche, 1 pour droite, 0 sinon
     */
    public int getDx() {
        return dx;
    }
    
    /**
     * Récupère le déplacement vertical.
     * 
     * @return -1 pour haut, 1 pour bas, 0 sinon
     */
    public int getDy() {
        return dy;
    }
    
    /**
     * Retourne la direction opposée.
     * 
     * @return La direction opposée
     */
    public Direction opposite() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
            case NONE -> NONE;
        };
    }
}
