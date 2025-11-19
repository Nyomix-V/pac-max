package com.epitech.pacmax.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Classe représentant un portail de téléportation.
 * Les portails permettent au joueur et aux fantômes de se téléporter
 * d'un côté de la carte à l'autre.
 * 
 * @author Epitech Team
 * @version 1.0
 */
public class Portal extends Entity {
    
    /** Portail de destination lié à ce portail */
    private Portal linkedPortal;
    
    /** Cooldown pour éviter les téléportations en boucle */
    private double teleportCooldown;
    
    /** Durée du cooldown en secondes */
    private static final double COOLDOWN_DURATION = 0.5;
    
    /**
     * Constructeur d'un portail.
     * 
     * @param x Position X
     * @param y Position Y
     * @param width Largeur du portail
     * @param height Hauteur du portail
     */
    public Portal(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.speed = 0; // Les portails ne bougent pas
        this.teleportCooldown = 0;
    }
    
    /**
     * Lie ce portail à un autre portail (destination).
     * 
     * @param portal Le portail de destination
     */
    public void linkTo(Portal portal) {
        this.linkedPortal = portal;
    }
    
    /**
     * Téléporte une entité vers le portail lié.
     * 
     * @param entity L'entité à téléporter
     * @return true si la téléportation a eu lieu
     */
    public boolean teleport(Entity entity) {
        if (linkedPortal == null || teleportCooldown > 0) {
            return false;
        }
        
        // Déterminer la direction de sortie (gauche ou droite)
        double offsetX = 0;
        if (linkedPortal.getX() < 100) {
            // Portail de gauche -> sortir vers la droite
            offsetX = linkedPortal.getWidth() + 10;
        } else {
            // Portail de droite -> sortir vers la gauche
            offsetX = -entity.getWidth() - 10;
        }
        
        // Téléporter l'entité au portail lié avec un décalage
        entity.setX(linkedPortal.getX() + offsetX);
        entity.setY(linkedPortal.getY() + (linkedPortal.getHeight() - entity.getHeight()) / 2);
        
        // Activer le cooldown sur les deux portails
        this.teleportCooldown = COOLDOWN_DURATION;
        linkedPortal.teleportCooldown = COOLDOWN_DURATION;
        
        return true;
    }
    
    @Override
    public void update(double deltaTime) {
        // Décrémenter le cooldown
        if (teleportCooldown > 0) {
            teleportCooldown -= deltaTime;
            if (teleportCooldown < 0) {
                teleportCooldown = 0;
            }
        }
    }
    
    @Override
    public void render(GraphicsContext gc) {
        if (!visible) return;
        
        // Effet visuel de portail avec animation
        double time = System.currentTimeMillis() / 1000.0;
        double alpha = 0.5 + 0.3 * Math.sin(time * 3);
        
        // Cercle extérieur (violet)
        gc.setGlobalAlpha(alpha);
        gc.setFill(Color.PURPLE);
        gc.fillOval(x - 5, y - 5, width + 10, height + 10);
        
        // Cercle intérieur (cyan)
        gc.setFill(Color.CYAN);
        gc.fillOval(x, y, width, height);
        
        // Cercle central (blanc)
        gc.setFill(Color.WHITE);
        gc.fillOval(x + width/4, y + height/4, width/2, height/2);
        
        gc.setGlobalAlpha(1.0);
    }
    
    /**
     * Récupère le portail lié.
     * 
     * @return Le portail de destination
     */
    public Portal getLinkedPortal() {
        return linkedPortal;
    }
    
    /**
     * Vérifie si le portail est prêt à téléporter.
     * 
     * @return true si le cooldown est terminé
     */
    public boolean isReady() {
        return teleportCooldown <= 0;
    }
}
