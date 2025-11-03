package com.epitech.pacmax.entities;

import com.epitech.pacmax.utils.SoundManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

import java.io.File;

/**
 * Classe représentant le joueur (Pac-Man).
 * Hérite de Entity et implémente la logique spécifique au joueur.
 * 
 * @author Epitech Team
 * @version 1.0
 */
public class Player extends Entity {
    
    /** Nombre de vies du joueur */
    private int lives;
    
    /** Score du joueur */
    private int score;
    
    /** Direction souhaitée (pour le buffer d'input) */
    private Direction nextDirection;
    
    /** Indique si le joueur est en mode power-up */
    private boolean powered;
    
    /** Temps restant du power-up */
    private double powerUpTimer;
    
    /** Durée du power-up en secondes */
    private static final double POWER_UP_DURATION = 10.0;
    
    /** Vitesse normale du joueur */
    private static final double NORMAL_SPEED = 100.0;
    
    /** Image normale de Pac-Man */
    private Image normalImage;
    
    /** Image de Pac-Man avec power-up (bouche ouverte) */
    private Image poweredImage;
    
    /**
     * Constructeur du joueur.
     * 
     * @param x Position X initiale
     * @param y Position Y initiale
     */
    public Player(double x, double y) {
        super(x, y, 30, 30); // Taille augmentée pour l'image
        this.lives = 3;
        this.score = 0;
        this.speed = NORMAL_SPEED;
        this.nextDirection = Direction.NONE;
        this.powered = false;
        this.powerUpTimer = 0;
        
        // Charger les images
        loadImages();
    }
    
    /**
     * Charge les images de Pac-Man.
     */
    private void loadImages() {
        try {
            // Charger l'image normale (à la racine)
            File normalFile = new File("pacmax.png");
            if (normalFile.exists()) {
                normalImage = new Image(normalFile.toURI().toString());
                System.out.println("✓ Image Pac-Man chargée: pacmax.png");
            } else {
                System.err.println("✗ Image non trouvée: pacmax.png (doit être à la racine)");
            }
            
            // Charger l'image powered (dans resources/images/)
            File poweredFile = new File("resources/images/pacmax_open_mouth.png");
            if (poweredFile.exists()) {
                poweredImage = new Image(poweredFile.toURI().toString());
                System.out.println("✓ Image Pac-Man powered chargée: pacmax_open_mouth.png");
            } else {
                System.err.println("✗ Image non trouvée: resources/images/pacmax_open_mouth.png");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des images: " + e.getMessage());
            // Les images resteront null, le rendu utilisera les formes par défaut
        }
    }
    
    @Override
    public void update(double deltaTime) {
        // Gestion du power-up
        if (powered) {
            powerUpTimer -= deltaTime;
            if (powerUpTimer <= 0) {
                powered = false;
                powerUpTimer = 0;
            }
        }
        
        // Tentative de changement de direction
        if (nextDirection != Direction.NONE && canChangeDirection(nextDirection)) {
            direction = nextDirection;
            nextDirection = Direction.NONE;
        }
        
        // Déplacement
        move(deltaTime);
    }
    
    @Override
    public void render(GraphicsContext gc) {
        if (!visible) return;
        
        // Choisir l'image selon le mode power-up
        Image currentImage = powered ? poweredImage : normalImage;
        
        // Si les images sont chargées, les afficher
        if (currentImage != null) {
            // Effet clignotant en fin de power-up
            if (powered && powerUpTimer < 3.0 && ((int)(powerUpTimer * 10) % 2 == 0)) {
                gc.setGlobalAlpha(0.5); // Transparence pour l'effet clignotant
            }
            
            // Dessiner l'image
            gc.drawImage(currentImage, x, y, width, height);
            
            // Réinitialiser l'opacité
            gc.setGlobalAlpha(1.0);
        } else {
            // Fallback : rendu par défaut si les images ne sont pas chargées
            renderDefault(gc);
        }
    }
    
    /**
     * Rendu par défaut (formes géométriques) si les images ne sont pas disponibles.
     * 
     * @param gc Le contexte graphique
     */
    private void renderDefault(GraphicsContext gc) {
        // Couleur change selon le mode power-up
        if (powered) {
            // Effet clignotant en fin de power-up
            if (powerUpTimer < 3.0 && ((int)(powerUpTimer * 10) % 2 == 0)) {
                gc.setFill(Color.ORANGE);
            } else {
                gc.setFill(Color.YELLOW);
            }
        } else {
            gc.setFill(Color.YELLOW);
        }
        
        // Dessine Pac-Man (cercle simple)
        gc.fillOval(x, y, width, height);
        
        // Dessine la "bouche" selon la direction
        gc.setFill(Color.BLACK);
        switch (direction) {
            case RIGHT -> gc.fillPolygon(
                new double[]{x + width/2, x + width, x + width},
                new double[]{y + height/2, y, y + height},
                3
            );
            case LEFT -> gc.fillPolygon(
                new double[]{x + width/2, x, x},
                new double[]{y + height/2, y, y + height},
                3
            );
            case UP -> gc.fillPolygon(
                new double[]{x + width/2, x, x + width},
                new double[]{y + height/2, y, y},
                3
            );
            case DOWN -> gc.fillPolygon(
                new double[]{x + width/2, x, x + width},
                new double[]{y + height/2, y + height, y + height},
                3
            );
        }
    }
    
    /**
     * Vérifie si le joueur peut changer de direction.
     * TODO: Implémenter la vérification avec les murs de la carte.
     * 
     * @param newDirection La nouvelle direction souhaitée
     * @return true si le changement est possible
     */
    private boolean canChangeDirection(Direction newDirection) {
        // Pour l'instant, toujours autoriser
        // À améliorer avec la détection de collision avec les murs
        return true;
    }
    
    /**
     * Active le mode power-up.
     * Joue le son "jadore_les_hot_dogs_ikea.ogg".
     */
    public void activatePowerUp() {
        powered = true;
        powerUpTimer = POWER_UP_DURATION;
        
        // Jouer le son du hot-dog
        SoundManager.getInstance().playSound("hotdog");
    }
    
    /**
     * Ajoute des points au score.
     * 
     * @param points Nombre de points à ajouter
     */
    public void addScore(int points) {
        score += points;
    }
    
    /**
     * Fait perdre une vie au joueur.
     */
    public void loseLife() {
        lives--;
        if (lives < 0) lives = 0;
    }
    
    /**
     * Réinitialise la position du joueur.
     * 
     * @param x Nouvelle position X
     * @param y Nouvelle position Y
     */
    public void reset(double x, double y) {
        this.x = x;
        this.y = y;
        this.direction = Direction.NONE;
        this.nextDirection = Direction.NONE;
        this.powered = false;
        this.powerUpTimer = 0;
    }
    
    // Getters et Setters
    
    public int getLives() {
        return lives;
    }
    
    public void setLives(int lives) {
        this.lives = lives;
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public Direction getNextDirection() {
        return nextDirection;
    }
    
    public void setNextDirection(Direction nextDirection) {
        this.nextDirection = nextDirection;
    }
    
    public boolean isPowered() {
        return powered;
    }
    
    public double getPowerUpTimer() {
        return powerUpTimer;
    }
}
