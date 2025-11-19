package com.epitech.pacmax.ui;

import com.epitech.pacmax.engine.GameEngine;
import com.epitech.pacmax.engine.GameState;
import com.epitech.pacmax.entities.*;
import com.epitech.pacmax.utils.InputHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * Interface utilisateur principale du jeu.
 * Gère l'affichage graphique avec JavaFX.
 * 
 * @author Epitech Team
 * @version 1.0
 */
public class GameUI extends StackPane {
    
    private final Canvas canvas;
    private final GraphicsContext gc;
    private final GameEngine gameEngine;
    private final InputHandler inputHandler;
    
    private static final int CANVAS_WIDTH = 800;
    private static final int CANVAS_HEIGHT = 600;
    
    /**
     * Constructeur de l'interface utilisateur.
     */
    public GameUI() {
        this.canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        this.gc = canvas.getGraphicsContext2D();
        this.gameEngine = GameEngine.getInstance();
        this.inputHandler = new InputHandler(gameEngine);
        
        // Ajouter le canvas au StackPane
        getChildren().add(canvas);
        
        // Configurer les événements clavier
        setFocusTraversable(true);
        setOnKeyPressed(inputHandler::handleKeyPressed);
        
        // Initialiser le jeu
        gameEngine.initialize();
        gameEngine.start();
    }
    
    /**
     * Rend l'interface graphique.
     * Appelé à chaque frame.
     */
    public void render() {
        // Effacer l'écran
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        
        // Afficher selon l'état du jeu
        switch (gameEngine.getCurrentState()) {
            case MENU -> renderMenu();
            case WAITING_TO_START -> {
                renderGame();
                renderWaitingMessage();
            }
            case RUNNING -> renderGame();
            case PAUSED -> {
                renderGame();
                renderPauseOverlay();
            }
            case GAME_OVER -> {
                renderGame();
                renderGameOver();
            }
            case LEVEL_COMPLETE -> {
                renderGame();
                renderLevelComplete();
            }
        }
    }
    
    /**
     * Affiche le menu principal.
     */
    private void renderMenu() {
        gc.setFill(Color.YELLOW);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("PAC-MAX", CANVAS_WIDTH / 2.0, 150);
        
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        gc.fillText("Appuyez sur ENTRÉE pour commencer", CANVAS_WIDTH / 2.0, 300);
        
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        gc.fillText("Utilisez les flèches ou ZQSD pour vous déplacer", CANVAS_WIDTH / 2.0, 400);
        gc.fillText("Mangez toutes les pac-gums pour gagner !", CANVAS_WIDTH / 2.0, 430);
        gc.fillText("Évitez les fantômes ou mangez-les avec un power-up !", CANVAS_WIDTH / 2.0, 460);
    }
    
    /**
     * Affiche le jeu en cours.
     */
    private void renderGame() {
        // Dessiner les murs
        for (Wall wall : gameEngine.getWalls()) {
            wall.render(gc);
        }
        
        // Dessiner les portails
        for (Portal portal : gameEngine.getPortals()) {
            portal.render(gc);
        }
        
        // Dessiner les pac-gums
        for (PacGum pacGum : gameEngine.getPacGums()) {
            pacGum.render(gc);
        }
        
        // Dessiner les fantômes
        for (Ghost ghost : gameEngine.getGhosts()) {
            ghost.render(gc);
        }
        
        // Dessiner le joueur
        if (gameEngine.getPlayer() != null) {
            gameEngine.getPlayer().render(gc);
        }
        
        // Afficher le HUD
        renderHUD();
    }
    
    /**
     * Affiche le HUD (score, vies, niveau).
     */
    private void renderHUD() {
        Player player = gameEngine.getPlayer();
        if (player == null) return;
        
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gc.setTextAlign(TextAlignment.LEFT);
        
        // Score
        gc.fillText("Score: " + player.getScore(), 10, 25);
        
        // Vies
        gc.fillText("Vies: " + player.getLives(), 10, 50);
        
        // Niveau
        gc.fillText("Niveau: " + gameEngine.getCurrentLevel(), 10, 75);
        
        // Power-up timer
        if (player.isPowered()) {
            gc.setFill(Color.YELLOW);
            gc.fillText("POWER-UP: " + String.format("%.1f", player.getPowerUpTimer()), 
                CANVAS_WIDTH - 200, 25);
        }
    }
    
    /**
     * Affiche l'overlay de pause.
     */
    private void renderPauseOverlay() {
        // Fond semi-transparent
        gc.setFill(Color.color(0, 0, 0, 0.7));
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("PAUSE", CANVAS_WIDTH / 2.0, CANVAS_HEIGHT / 2.0);
        
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        gc.fillText("Appuyez sur P ou ÉCHAP pour continuer", 
            CANVAS_WIDTH / 2.0, CANVAS_HEIGHT / 2.0 + 50);
    }
    
    /**
     * Affiche l'écran de game over.
     */
    private void renderGameOver() {
        // Fond semi-transparent
        gc.setFill(Color.color(0, 0, 0, 0.8));
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        
        gc.setFill(Color.RED);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("GAME OVER", CANVAS_WIDTH / 2.0, CANVAS_HEIGHT / 2.0 - 50);
        
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        gc.fillText("Score final: " + gameEngine.getPlayer().getScore(), 
            CANVAS_WIDTH / 2.0, CANVAS_HEIGHT / 2.0 + 20);
        
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        gc.fillText("Appuyez sur ENTRÉE pour rejouer", 
            CANVAS_WIDTH / 2.0, CANVAS_HEIGHT / 2.0 + 70);
        gc.fillText("Appuyez sur ÉCHAP pour retourner au menu", 
            CANVAS_WIDTH / 2.0, CANVAS_HEIGHT / 2.0 + 100);
    }
    
    /**
     * Affiche l'écran de fin de niveau.
     */
    private void renderLevelComplete() {
        // Fond semi-transparent
        gc.setFill(Color.color(0, 0, 0, 0.8));
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        
        gc.setFill(Color.GREEN);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("NIVEAU TERMINÉ !", CANVAS_WIDTH / 2.0, CANVAS_HEIGHT / 2.0 - 50);
        
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        gc.fillText("Score: " + gameEngine.getPlayer().getScore(), 
            CANVAS_WIDTH / 2.0, CANVAS_HEIGHT / 2.0 + 20);
        
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        gc.fillText("Appuyez sur ENTRÉE pour le niveau suivant", 
            CANVAS_WIDTH / 2.0, CANVAS_HEIGHT / 2.0 + 70);
    }
    
    /**
     * Affiche le message d'attente du premier mouvement.
     */
    private void renderWaitingMessage() {
        // Message semi-transparent en haut
        gc.setFill(Color.color(0, 0, 0, 0.7));
        gc.fillRect(0, 0, CANVAS_WIDTH, 80);
        
        gc.setFill(Color.YELLOW);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("Appuyez sur une flèche pour commencer", CANVAS_WIDTH / 2.0, 35);
        
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        gc.fillText("Les fantômes ne bougeront pas tant que vous ne bougez pas", CANVAS_WIDTH / 2.0, 60);
    }
    
    /**
     * Récupère le canvas.
     * 
     * @return Le canvas JavaFX
     */
    public Canvas getCanvas() {
        return canvas;
    }
}
