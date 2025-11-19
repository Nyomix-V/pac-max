package com.epitech.pacmax.ui;

import com.epitech.pacmax.engine.GameEngine;
import com.epitech.pacmax.engine.GameState;
import com.epitech.pacmax.entities.*;
import com.epitech.pacmax.utils.GameAction;
import com.epitech.pacmax.utils.KeybindingManager;
import com.epitech.pacmax.utils.InputHandler;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.Map;

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
    
    // Résolution native du jeu, sur laquelle tout est basé
    private static final double NATIVE_WIDTH = 1920;
    private static final double NATIVE_HEIGHT = 1080;

    // Variables pour la mise à l'échelle
    private double scale = 1.0;
    private double offsetX = 0, offsetY = 0;
    
    /**
     * Constructeur de l'interface utilisateur.
     */
    public GameUI() {
        this.canvas = new Canvas(); // La taille sera liée à la fenêtre
        this.gc = canvas.getGraphicsContext2D();
        this.gameEngine = GameEngine.getInstance();
        this.inputHandler = new InputHandler(gameEngine);
        
        // Ajouter le canvas au StackPane
        getChildren().add(canvas);

        // Lier la taille du canvas à la taille du pane
        canvas.widthProperty().bind(this.widthProperty());
        canvas.heightProperty().bind(this.heightProperty());
        
        // Configurer les événements clavier
        setFocusTraversable(true);
        setOnKeyPressed(inputHandler::handleKeyPressed);

        // Configurer les événements souris
        setOnMouseMoved(this::handleMouseMoved);
        setOnMouseClicked(this::handleMouseClicked);
        
        // Initialiser le jeu
        gameEngine.initialize();
        gameEngine.start();
    }
    
    /**
     * Rend l'interface graphique.
     * Appelé à chaque frame.
     */
    public void render() {
        double windowWidth = getWidth();
        double windowHeight = getHeight();
        gc.fillRect(0, 0, windowWidth, windowHeight);

        // --- Logique de mise à l'échelle ---
        // Calculer le ratio de mise à l'échelle en conservant l'aspect ratio
        scale = Math.min(windowWidth / NATIVE_WIDTH, windowHeight / NATIVE_HEIGHT);

        // Calculer la nouvelle taille du contenu du jeu
        double scaledWidth = NATIVE_WIDTH * scale;
        double scaledHeight = NATIVE_HEIGHT * scale;

        // Calculer le décalage pour centrer le contenu
        offsetX = (windowWidth - scaledWidth) / 2;
        offsetY = (windowHeight - scaledHeight) / 2;

        // Sauvegarder l'état du canvas, appliquer la transformation, puis dessiner
        gc.save();
        gc.translate(offsetX, offsetY);
        gc.scale(scale, scale);
        
        // Gérer la visibilité du curseur
        GameState currentState = gameEngine.getCurrentState();
        if (getScene() != null) {
            getScene().setCursor(currentState == GameState.PAUSE_MENU || currentState == GameState.OPTIONS_MENU || currentState == GameState.KEYBINDING_MENU ? javafx.scene.Cursor.DEFAULT : javafx.scene.Cursor.NONE);

            // Gérer le mode plein écran
            Stage stage = (Stage) getScene().getWindow();
            if (stage != null && stage.isFullScreen() != gameEngine.isFullscreen()) {
                stage.setFullScreen(gameEngine.isFullscreen());
            }
        }

        // Afficher selon l'état du jeu
        switch (currentState) {
            case MENU -> renderMenu();
            case WAITING_TO_START -> {
                renderGame();
                renderWaitingMessage();
            }
            case RUNNING -> renderGame();
            case PAUSE_MENU -> {
                renderGame();
                renderPauseMenu();
            }
            case OPTIONS_MENU -> {
                renderGame();
                renderOptionsMenu();
            }
            case KEYBINDING_MENU -> {
                renderGame();
                renderKeybindingMenu();
            }
            case GAME_OVER -> {
                renderGame();
                renderGameOver();
            }
            case LEVEL_CLEARED -> {
                renderGame();
                renderLevelComplete(); // Affiche le message clignotant
            }
            case LEVEL_COMPLETE -> {
                renderGame();
                renderLevelComplete();
            }
            case VICTORY -> {
                renderGame(); // Affiche le jeu en fond
                renderVictoryScreen();
            }
        }

        // Restaurer l'état du canvas pour la prochaine frame
        gc.restore();
    }
    
    /**
     * Affiche le menu principal.
     */
    private void renderMenu() {
        gc.setFill(Color.YELLOW);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("PAC-MAX", NATIVE_WIDTH / 2.0, 150);
        
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        gc.fillText("Appuyez sur ENTRÉE pour commencer", NATIVE_WIDTH / 2.0, 300);
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        gc.fillText("Mangez 80% des pac-gums pour gagner !", NATIVE_WIDTH / 2.0, 430);
        gc.fillText("Évitez les fantômes ou mangez-les avec un power-up !", NATIVE_WIDTH / 2.0, 460);
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
                NATIVE_WIDTH - 200, 25);
        }
    }
    
    /**
     * Affiche le menu de pause.
     */
    private void renderPauseMenu() {
        // Fond semi-transparent
        gc.setFill(Color.color(0, 0, 0, 0.7));
        gc.fillRect(0, 0, NATIVE_WIDTH, NATIVE_HEIGHT);
        
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("PAUSE", NATIVE_WIDTH / 2.0, NATIVE_HEIGHT / 2.0 - 150);

        // Options du menu
        String[] menuOptions = {"Option", "Keybinding"};
        int selectedOption = gameEngine.getSelectedPauseMenuOption();

        for (int i = 0; i < menuOptions.length; i++) {
            if (i == selectedOption) {
                gc.setFill(Color.YELLOW);
                gc.setFont(Font.font("Arial", FontWeight.BOLD, 36));
            } else {
                gc.setFill(Color.WHITE);
                gc.setFont(Font.font("Arial", FontWeight.NORMAL, 30));
            }
            gc.fillText(menuOptions[i], NATIVE_WIDTH / 2.0, NATIVE_HEIGHT / 2.0 - 50 + (i * 60));
        }

        // Instruction pour quitter
        gc.setFill(Color.GRAY);
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        gc.fillText("Appuyez sur ÉCHAP pour reprendre", NATIVE_WIDTH / 2.0, NATIVE_HEIGHT / 2.0 + 150);
    }
    
    /**
     * Affiche le menu des options.
     */
    private void renderOptionsMenu() {
        // Fond semi-transparent
        gc.setFill(Color.color(0, 0, 0, 0.7));
        gc.fillRect(0, 0, NATIVE_WIDTH, NATIVE_HEIGHT);

        gc.setTextAlign(TextAlignment.CENTER);

        // Titre
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        gc.fillText("OPTIONS", NATIVE_WIDTH / 2.0, NATIVE_HEIGHT / 2.0 - 150);

        // Libellé du volume
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 30));
        gc.fillText("Volume", NATIVE_WIDTH / 2.0, NATIVE_HEIGHT / 2.0 - 50);

        // Barre du slider
        double sliderWidth = 400;
        double sliderX = (NATIVE_WIDTH - sliderWidth) / 2.0;
        double sliderY = NATIVE_HEIGHT / 2.0;
        gc.setFill(Color.GRAY);
        gc.fillRect(sliderX, sliderY - 5, sliderWidth, 10);

        // Point sur le slider
        double volume = gameEngine.getMasterVolume();
        double pointX = sliderX + (sliderWidth * volume);
        gc.setFill(Color.YELLOW);
        gc.fillOval(pointX - 10, sliderY - 10, 20, 20);

        // Option Plein écran
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 30));
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("Plein écran", sliderX, NATIVE_HEIGHT / 2.0 + 180);

        gc.setTextAlign(TextAlignment.RIGHT);
        if (gameEngine.isFullscreen()) {
            gc.setFill(Color.YELLOW);
            gc.fillText("[Activé]", sliderX + sliderWidth, NATIVE_HEIGHT / 2.0 + 180);
        } else {
            gc.setFill(Color.WHITE);
            gc.fillText("[Désactivé]", sliderX + sliderWidth, NATIVE_HEIGHT / 2.0 + 180);
        }

        // Instruction pour quitter
        gc.setFill(Color.GRAY);
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        gc.fillText("Utilisez les flèches GAUCHE/DROITE pour ajuster", NATIVE_WIDTH / 2.0, NATIVE_HEIGHT / 2.0 + 80);
        gc.fillText("Appuyez sur ÉCHAP pour retourner", NATIVE_WIDTH / 2.0, NATIVE_HEIGHT / 2.0 + 110);
    }
    
    /**
     * Affiche le menu d'assignation des touches.
     */
    private void renderKeybindingMenu() {
        // Fond semi-transparent
        gc.setFill(Color.color(0, 0, 0, 0.7));
        gc.fillRect(0, 0, NATIVE_WIDTH, NATIVE_HEIGHT);

        gc.setTextAlign(TextAlignment.CENTER);

        // Titre
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        gc.fillText("COMMANDES", NATIVE_WIDTH / 2.0, NATIVE_HEIGHT / 2.0 - 200);

        // Liste des actions
        Map<GameAction, javafx.scene.input.KeyCode> keybindings = KeybindingManager.getInstance().getAllKeybindings();
        GameAction[] actions = GameAction.values();
        int selectedOption = gameEngine.getSelectedKeybindingOption();

        for (int i = 0; i < actions.length; i++) {
            GameAction action = actions[i];
            String actionName = action.getDisplayName();
            String keyName = keybindings.get(action).getName();

            if (i == selectedOption) {
                gc.setFill(Color.YELLOW);
                gc.setFont(Font.font("Arial", FontWeight.BOLD, 32));
                if (gameEngine.isWaitingForKey()) {
                    keyName = "...";
                }
            } else {
                gc.setFill(Color.WHITE);
                gc.setFont(Font.font("Arial", FontWeight.NORMAL, 28));
            }
            gc.setTextAlign(TextAlignment.LEFT);
            gc.fillText(actionName, NATIVE_WIDTH / 2.0 - 200, NATIVE_HEIGHT / 2.0 - 100 + (i * 50));
            gc.setTextAlign(TextAlignment.RIGHT);
            gc.fillText(keyName, NATIVE_WIDTH / 2.0 + 200, NATIVE_HEIGHT / 2.0 - 100 + (i * 50));
        }

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFill(Color.GRAY);
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        gc.fillText("Appuyez sur ENTRÉE pour modifier une touche, puis sur la nouvelle touche. ÉCHAP pour retourner.", NATIVE_WIDTH / 2.0, NATIVE_HEIGHT / 2.0 + 150);
    }

    /**
     * Affiche l'écran de game over.
     */
    private void renderGameOver() {
        // Fond semi-transparent
        gc.setFill(Color.color(0, 0, 0, 0.8));
        gc.fillRect(0, 0, NATIVE_WIDTH, NATIVE_HEIGHT);
        
        gc.setFill(Color.RED);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("GAME OVER", NATIVE_WIDTH / 2.0, NATIVE_HEIGHT / 2.0 - 50);
        
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        gc.fillText("Score final: " + gameEngine.getPlayer().getScore(), NATIVE_WIDTH / 2.0, NATIVE_HEIGHT / 2.0 + 20);
        
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        gc.fillText("Appuyez sur ENTRÉE pour rejouer", NATIVE_WIDTH / 2.0, NATIVE_HEIGHT / 2.0 + 70);
        gc.fillText("Appuyez sur ÉCHAP pour retourner au menu", NATIVE_WIDTH / 2.0, NATIVE_HEIGHT / 2.0 + 100);
    }
    
    /**
     * Affiche l'écran de fin de niveau.
     */
    private void renderLevelComplete() {
        // Le fond semi-transparent est retiré pour que le jeu reste entièrement visible.
        // gc.setFill(Color.color(0, 0, 0, 0.8));
        // gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        
        gc.setFill(Color.GREEN);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("NIVEAU TERMINÉ !", NATIVE_WIDTH / 2.0, NATIVE_HEIGHT / 2.0 - 50);
        
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        gc.fillText("Score: " + gameEngine.getPlayer().getScore(), NATIVE_WIDTH / 2.0, NATIVE_HEIGHT / 2.0 + 20);
        
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 20));

        // Faire clignoter le texte d'invitation
        if ((System.currentTimeMillis() / 500) % 2 == 0) {
        gc.fillText("Appuyez sur ENTRÉE pour le niveau suivant", NATIVE_WIDTH / 2.0, NATIVE_HEIGHT / 2.0 + 70);
        }
    }
    
    /**
     * Affiche l'écran de victoire.
     */
    private void renderVictoryScreen() {
        // Fond semi-transparent
        gc.setFill(Color.color(0, 0.1, 0, 0.8));
        gc.fillRect(0, 0, NATIVE_WIDTH, NATIVE_HEIGHT);

        gc.setFill(Color.GOLD);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 70));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("VICTOIRE !", NATIVE_WIDTH / 2.0, NATIVE_HEIGHT / 2.0 - 100);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 30));
        gc.fillText("Votre score final : " + gameEngine.getPlayer().getScore(), NATIVE_WIDTH / 2.0, NATIVE_HEIGHT / 2.0 + 50);
        gc.fillText("Félicitations, vous avez terminé tous les niveaux !", NATIVE_WIDTH / 2.0, NATIVE_HEIGHT / 2.0);
        // Options du menu
        String[] menuOptions = {"Rejouer", "Quitter"};
        int selectedOption = gameEngine.getSelectedVictoryMenuOption();

        for (int i = 0; i < menuOptions.length; i++) {
            if (i == selectedOption) {
                gc.setFill(Color.YELLOW);
                gc.setFont(Font.font("Arial", FontWeight.BOLD, 36));
            } else {
                gc.setFill(Color.WHITE);
                gc.setFont(Font.font("Arial", FontWeight.NORMAL, 30));
            }
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText(menuOptions[i], NATIVE_WIDTH / 2.0, NATIVE_HEIGHT / 2.0 + 150 + (i * 60));
        }
    }
    
    /**
     * Affiche le message d'attente du premier mouvement.
     */
    private void renderWaitingMessage() {
        // Message semi-transparent en haut
        gc.setFill(Color.color(0, 0, 0, 0.7));
        gc.fillRect(0, 0, NATIVE_WIDTH, 80);
        
        gc.setFill(Color.YELLOW);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("Appuyez sur une touche de mouvement pour commencer", NATIVE_WIDTH / 2.0, 35);
        
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        gc.fillText("Les fantômes ne bougeront pas tant que vous ne bougez pas", NATIVE_WIDTH / 2.0, 60);
    }
    
    /**
     * Récupère le canvas.
     * 
     * @return Le canvas JavaFX
     */
    public Canvas getCanvas() {
        return canvas;
    }

    /**
     * Gère les mouvements de la souris, notamment pour le survol des menus.
     * @param event L'événement de la souris.
     */
    private void handleMouseMoved(javafx.scene.input.MouseEvent event) {
        double mouseX = (event.getX() - offsetX) / scale;
        double mouseY = (event.getY() - offsetY) / scale;

        if (gameEngine.getCurrentState() == GameState.PAUSE_MENU) {
            String[] menuOptions = {"Option", "Keybinding"};
            for (int i = 0; i < menuOptions.length; i++) {
                Rectangle buttonBounds = getPauseMenuButtonBounds(i);
                if (buttonBounds.contains(mouseX, mouseY)) {
                    gameEngine.setSelectedPauseMenuOption(i);
                    break;
                }
            }
        } else if (gameEngine.getCurrentState() == GameState.KEYBINDING_MENU) {
            GameAction[] actions = GameAction.values();
            for (int i = 0; i < actions.length; i++) {
                // Créer une zone cliquable pour chaque ligne
                Rectangle buttonBounds = new Rectangle(NATIVE_WIDTH / 2.0 - 250, NATIVE_HEIGHT / 2.0 - 115 + (i * 50), 500, 40);
                if (buttonBounds.contains(mouseX, mouseY)) {
                    gameEngine.setSelectedKeybindingOption(i);
                    break;
                }
            }
        } else if (gameEngine.getCurrentState() == GameState.VICTORY) {
            String[] menuOptions = {"Rejouer", "Quitter"};
            for (int i = 0; i < menuOptions.length; i++) {
                Rectangle buttonBounds = getVictoryMenuButtonBounds(i);
                if (buttonBounds.contains(mouseX, mouseY)) {
                    gameEngine.setSelectedVictoryMenuOption(i);
                    break;
                }
            }
        } else if (gameEngine.getCurrentState() == GameState.VICTORY) {
            String[] menuOptions = {"Rejouer", "Quitter"};
            for (int i = 0; i < menuOptions.length; i++) {
                Rectangle buttonBounds = getVictoryMenuButtonBounds(i);
                if (buttonBounds.contains(mouseX, mouseY)) {
                    gameEngine.setSelectedVictoryMenuOption(i);
                    break;
                }
            }
        }
    }

    /**
     * Gère les clics de la souris.
     * @param event L'événement de la souris.
     */
    private void handleMouseClicked(javafx.scene.input.MouseEvent event) {
        double mouseX = (event.getX() - offsetX) / scale;
        double mouseY = (event.getY() - offsetY) / scale;

        if (gameEngine.getCurrentState() == GameState.PAUSE_MENU) {
            int selectedOption = gameEngine.getSelectedPauseMenuOption();
            Rectangle buttonBounds = getPauseMenuButtonBounds(selectedOption);

            if (buttonBounds.contains(mouseX, mouseY)) {
                // Simuler l'appui sur "Entrée"
                if (selectedOption == 0) { // "Option"
                    gameEngine.setState(GameState.OPTIONS_MENU);
                } else { // "Keybinding"
                    gameEngine.setState(GameState.KEYBINDING_MENU);
                }
            }
        } else if (gameEngine.getCurrentState() == GameState.OPTIONS_MENU) {
            // Logique pour le slider de volume (si on veut le rendre cliquable)
            double sliderWidth = 400;
            double sliderX = (NATIVE_WIDTH - sliderWidth) / 2.0;
            Rectangle sliderBounds = new Rectangle(sliderX, NATIVE_HEIGHT / 2.0 - 15, sliderWidth, 30);

            if (sliderBounds.contains(mouseX, mouseY)) {
                double clickX = mouseX - sliderX;
                double newVolume = clickX / sliderWidth;
                gameEngine.setMasterVolume(newVolume);
            }

            // Zone cliquable pour l'option plein écran
            Rectangle fullscreenBounds = new Rectangle(sliderX, NATIVE_HEIGHT / 2.0 + 150, sliderWidth, 40);
            if (fullscreenBounds.contains(mouseX, mouseY)) {
                gameEngine.setFullscreen(!gameEngine.isFullscreen());
            }
        } else if (gameEngine.getCurrentState() == GameState.KEYBINDING_MENU) {
            int selectedOption = gameEngine.getSelectedKeybindingOption();
            // Créer une zone cliquable pour la ligne sélectionnée
            Rectangle buttonBounds = new Rectangle(NATIVE_WIDTH / 2.0 - 250, NATIVE_HEIGHT / 2.0 - 115 + (selectedOption * 50), 500, 40);
            if (buttonBounds.contains(mouseX, mouseY)) {
                gameEngine.setWaitingForKey(true);
            } else if (gameEngine.isWaitingForKey()) {
                // Si on clique ailleurs pendant l'attente, on annule
                gameEngine.setWaitingForKey(false);
            }
        } else if (gameEngine.getCurrentState() == GameState.VICTORY) {
            int selectedOption = gameEngine.getSelectedVictoryMenuOption();
            Rectangle buttonBounds = getVictoryMenuButtonBounds(selectedOption);

            if (buttonBounds.contains(mouseX, mouseY)) {
                if (selectedOption == 0) { // Rejouer
                    gameEngine.initialize();
                } else { // Quitter
                    Platform.exit();
                }
            }
        }
    }

    /**
     * Calcule la zone rectangulaire d'un bouton du menu de pause.
     * @param index L'index du bouton (0 pour "Option", 1 pour "Keybinding").
     * @return Un objet Rectangle représentant la zone cliquable.
     */
    private Rectangle getPauseMenuButtonBounds(int index) {
        double buttonWidth = 200;
        double buttonHeight = 40;
        double x = (NATIVE_WIDTH - buttonWidth) / 2.0;
        double y = NATIVE_HEIGHT / 2.0 - 65 + (index * 60);
        return new Rectangle(x, y, buttonWidth, buttonHeight);
    }

    /**
     * Calcule la zone rectangulaire d'un bouton du menu de victoire.
     * @param index L'index du bouton (0 pour "Rejouer", 1 pour "Quitter").
     * @return Un objet Rectangle représentant la zone cliquable.
     */
    private Rectangle getVictoryMenuButtonBounds(int index) {
        double buttonWidth = 200;
        double buttonHeight = 40;
        double x = (NATIVE_WIDTH - buttonWidth) / 2.0;
        double y = NATIVE_HEIGHT / 2.0 + 135 + (index * 60);
        return new Rectangle(x, y, buttonWidth, buttonHeight);
    }
}
