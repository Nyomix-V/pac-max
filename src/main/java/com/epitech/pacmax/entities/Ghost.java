package com.epitech.pacmax.entities;

import com.epitech.pacmax.ai.GhostAI;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Classe représentant un fantôme ennemi.
 * Utilise le pattern Strategy pour l'IA via l'interface GhostAI.
 * 
 * @author Epitech Team
 * @version 1.0
 */
public class Ghost extends Entity {
    
    /** Type de fantôme */
    private GhostType type;
    
    /** Stratégie d'IA du fantôme (Pattern Strategy) */
    private GhostAI ai;
    
    /** État du fantôme */
    private GhostState state;
    
    /** Couleur du fantôme */
    private Color color;
    
    /** Position de spawn */
    private double spawnX;
    private double spawnY;
    
    /** Timer pour l'état effrayé */
    private double scaredTimer;
    
    /** Vitesse normale */
    private static final double NORMAL_SPEED = 80.0;
    
    /** Vitesse en mode effrayé */
    private static final double SCARED_SPEED = 50.0;
    
    /**
     * Constructeur d'un fantôme.
     * 
     * @param x Position X initiale
     * @param y Position Y initiale
     * @param type Type de fantôme
     * @param ai Stratégie d'IA à utiliser
     */
    public Ghost(double x, double y, GhostType type, GhostAI ai) {
        super(x, y, 20, 20);
        this.type = type;
        this.ai = ai;
        this.state = GhostState.CHASE;
        this.spawnX = x;
        this.spawnY = y;
        this.speed = NORMAL_SPEED;
        this.scaredTimer = 0;
        
        // Définir la couleur selon le type
        this.color = switch (type) {
            case BLINKY -> Color.RED;
            case PINKY -> Color.PINK;
            case INKY -> Color.CYAN;
            case CLYDE -> Color.ORANGE;
        };
    }
    
    @Override
    public void update(double deltaTime) {
        // Gestion du timer d'état effrayé
        if (state == GhostState.SCARED) {
            scaredTimer -= deltaTime;
            if (scaredTimer <= 0) {
                setState(GhostState.CHASE);
            }
        }
        
        // L'IA décide de la direction
        if (ai != null && state != GhostState.DEAD) {
            Direction newDirection = ai.chooseDirection(this);
            if (newDirection != null && newDirection != direction.opposite()) {
                direction = newDirection;
            }
        }
        
        // Déplacement
        move(deltaTime);
    }
    
    @Override
    public void render(GraphicsContext gc) {
        if (!visible) return;
        
        // Couleur selon l'état
        switch (state) {
            case SCARED -> {
                // Effet clignotant en fin de scared
                if (scaredTimer < 2.0 && ((int)(scaredTimer * 10) % 2 == 0)) {
                    gc.setFill(Color.WHITE);
                } else {
                    gc.setFill(Color.BLUE);
                }
            }
            case DEAD -> gc.setFill(Color.GRAY);
            default -> gc.setFill(color);
        }
        
        // Corps du fantôme (rectangle arrondi)
        gc.fillRoundRect(x, y, width, height, 10, 10);
        
        // Yeux
        gc.setFill(Color.WHITE);
        gc.fillOval(x + 4, y + 4, 5, 5);
        gc.fillOval(x + 11, y + 4, 5, 5);
        
        gc.setFill(Color.BLACK);
        double pupilOffsetX = direction.getDx() * 2;
        double pupilOffsetY = direction.getDy() * 2;
        gc.fillOval(x + 5 + pupilOffsetX, y + 5 + pupilOffsetY, 3, 3);
        gc.fillOval(x + 12 + pupilOffsetX, y + 5 + pupilOffsetY, 3, 3);
    }
    
    /**
     * Change l'état du fantôme.
     * 
     * @param newState Le nouvel état
     */
    public void setState(GhostState newState) {
        this.state = newState;
        
        switch (newState) {
            case SCARED -> {
                speed = SCARED_SPEED;
                scaredTimer = 10.0; // 10 secondes d'état effrayé
            }
            case CHASE, SCATTER -> speed = NORMAL_SPEED;
            case DEAD -> {
                speed = NORMAL_SPEED * 1.5; // Plus rapide pour retourner à la base
            }
        }
    }
    
    /**
     * Réinitialise le fantôme à sa position de spawn.
     */
    public void respawn() {
        x = spawnX;
        y = spawnY;
        direction = Direction.NONE;
        setState(GhostState.CHASE);
    }
    
    /**
     * Change la stratégie d'IA du fantôme (Pattern Strategy).
     * 
     * @param ai La nouvelle stratégie d'IA
     */
    public void setAI(GhostAI ai) {
        this.ai = ai;
    }
    
    // Getters
    
    public GhostType getType() {
        return type;
    }
    
    public GhostState getState() {
        return state;
    }
    
    public GhostAI getAI() {
        return ai;
    }
    
    public Color getColor() {
        return color;
    }
    
    /**
     * Énumération des types de fantômes.
     */
    public enum GhostType {
        BLINKY,  // Rouge - Agressif
        PINKY,   // Rose - Embuscade
        INKY,    // Cyan - Imprévisible
        CLYDE    // Orange - Aléatoire
    }
    
    /**
     * Énumération des états possibles d'un fantôme (Pattern State).
     */
    public enum GhostState {
        CHASE,    // Poursuit le joueur
        SCATTER,  // Retourne dans son coin
        SCARED,   // Effrayé (vulnérable)
        DEAD      // Mort, retourne à la base
    }
}
