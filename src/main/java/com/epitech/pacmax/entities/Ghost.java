package com.epitech.pacmax.entities;

import com.epitech.pacmax.ai.GhostAI;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    
    /** Images des fantômes (mode normal) */
    private static List<Image> chaimaImages;
    
    /** Image du fantôme en mode scared (power-up actif) */
    private static Image chaimaNegativeImage;
    
    /** Image assignée à ce fantôme */
    private Image normalImage;
    
    /** Random pour sélection aléatoire */
    private static final Random random = new Random();
    
    /**
     * Constructeur d'un fantôme.
     * 
     * @param x Position X initiale
     * @param y Position Y initiale
     * @param type Type de fantôme
     * @param ai Stratégie d'IA à utiliser
     */
    public Ghost(double x, double y, GhostType type, GhostAI ai) {
        super(x, y, 30, 30); // Taille augmentée pour les images
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
        
        // Charger les images (une seule fois pour toutes les instances)
        if (chaimaImages == null) {
            loadChaimaImages();
        }
        
        // Assigner une image aléatoire à ce fantôme
        if (chaimaImages != null && !chaimaImages.isEmpty()) {
            this.normalImage = chaimaImages.get(random.nextInt(chaimaImages.size()));
        }
    }
    
    /**
     * Charge toutes les images "chaima" depuis resources/images/.
     * Appelé une seule fois pour toutes les instances de Ghost.
     */
    private static void loadChaimaImages() {
        chaimaImages = new ArrayList<>();
        
        // Liste des fichiers chaima (sans chaima_negatif.png)
        String[] chaimaFiles = {
            "angrychaima.png",
            "chaima1.png",
            "chaimalaughing.png",
            "chaimaopenmouth.png",
            "chaimasmiling.png",
            "karenchaima.png",
            "madchaima.png",
            "madchaima2.png",
            "sadchaima.png",
            "sadchaima2.png"
            // Note: realchaimasama.jpg est en JPG, on le skip pour la cohérence
        };
        
        for (String filename : chaimaFiles) {
            try {
                File file = new File("resources/images/" + filename);
                if (file.exists()) {
                    Image img = new Image(file.toURI().toString());
                    chaimaImages.add(img);
                    System.out.println("✓ Image chaima chargée: " + filename);
                }
            } catch (Exception e) {
                System.err.println("Erreur chargement " + filename + ": " + e.getMessage());
            }
        }
        
        // Charger l'image négative (mode scared)
        try {
            File negFile = new File("resources/images/chaima_negatif.png");
            if (negFile.exists()) {
                chaimaNegativeImage = new Image(negFile.toURI().toString());
                System.out.println("✓ Image chaima_negatif chargée");
            }
        } catch (Exception e) {
            System.err.println("Erreur chargement chaima_negatif: " + e.getMessage());
        }
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
        
        // Choisir l'image selon l'état
        Image imageToRender = null;
        
        if (state == GhostState.SCARED && chaimaNegativeImage != null) {
            // Mode scared : utiliser chaima_negatif.png
            imageToRender = chaimaNegativeImage;
            
            // Effet clignotant en fin de scared
            if (scaredTimer < 2.0 && ((int)(scaredTimer * 10) % 2 == 0)) {
                gc.setGlobalAlpha(0.5);
            }
        } else if (normalImage != null) {
            // Mode normal : utiliser l'image assignée
            imageToRender = normalImage;
        }
        
        // Afficher l'image si disponible
        if (imageToRender != null) {
            gc.drawImage(imageToRender, x, y, width, height);
            gc.setGlobalAlpha(1.0); // Réinitialiser l'opacité
        } else {
            // Fallback : rendu par défaut si pas d'images
            renderDefault(gc);
        }
    }
    
    /**
     * Rendu par défaut (formes géométriques) si les images ne sont pas disponibles.
     * 
     * @param gc Le contexte graphique
     */
    private void renderDefault(GraphicsContext gc) {
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
