package com.epitech.pacmax.utils;

import com.epitech.pacmax.engine.GameEngine;
import com.epitech.pacmax.engine.GameState;
import com.epitech.pacmax.entities.Direction;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Gestionnaire des entrées clavier.
 * Traduit les événements clavier en actions de jeu.
 * 
 * @author Epitech Team
 * @version 1.0
 */
public class InputHandler {
    
    private final GameEngine gameEngine;
    private final KeybindingManager keybindingManager;
    
    /**
     * Constructeur du gestionnaire d'entrées.
     * 
     * @param gameEngine Le moteur de jeu
     */
    public InputHandler(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        this.keybindingManager = KeybindingManager.getInstance();
    }
    
    /**
     * Gère les événements de touche pressée.
     * 
     * @param event L'événement clavier
     */
    public void handleKeyPressed(KeyEvent event) {
        KeyCode code = event.getCode();
        
        // Gestion selon l'état du jeu
        switch (gameEngine.getCurrentState()) {
            case WAITING_TO_START -> handleGameInput(code); // Permettre au joueur de bouger pour démarrer
            case RUNNING -> handleGameInput(code);
            case PAUSE_MENU -> handlePauseMenuInput(code);
            case OPTIONS_MENU -> handleOptionsMenuInput(code);
            case KEYBINDING_MENU -> handleKeybindingMenuInput(code);
            case MENU -> handleMenuInput(code);
            case GAME_OVER -> handleGameOverInput(code);
            case LEVEL_COMPLETE -> handleLevelCompleteInput(code);
            case LEVEL_CLEARED -> {
                handleGameInput(code); // Continuer de gérer les mouvements du joueur
                handleLevelCompleteInput(code); // Gérer aussi la touche ENTRÉE pour passer au niveau suivant
            }
            case VICTORY -> handleVictoryInput(code);
        }
    }
    
    /**
     * Gère les entrées pendant le jeu.
     * 
     * @param code Le code de la touche
     */
    private void handleGameInput(KeyCode code) {
        GameAction action = keybindingManager.getActionForKey(code);
        if (action == null) return;

        switch (action) {
            case MOVE_UP -> gameEngine.getPlayer().setNextDirection(Direction.UP);
            case MOVE_DOWN -> gameEngine.getPlayer().setNextDirection(Direction.DOWN);
            case MOVE_LEFT -> gameEngine.getPlayer().setNextDirection(Direction.LEFT);
            case MOVE_RIGHT -> gameEngine.getPlayer().setNextDirection(Direction.RIGHT);
            case PAUSE -> gameEngine.setState(GameState.PAUSE_MENU);
        }
        if (gameEngine.getCurrentState() == GameState.WAITING_TO_START && gameEngine.getPlayer().getDirection() != Direction.NONE) {
            gameEngine.setState(GameState.RUNNING);
        }
    }
    
    /**
     * Gère les entrées dans le menu de pause.
     * 
     * @param code Le code de la touche
     */
    private void handlePauseMenuInput(KeyCode code) {
        int currentOption = gameEngine.getSelectedPauseMenuOption();
        int totalOptions = 2; // "Option" et "Keybinding"

        switch (code) {
            case UP, Z, W -> {
                int newOption = (currentOption - 1 + totalOptions) % totalOptions;
                gameEngine.setSelectedPauseMenuOption(newOption);
            }
            case DOWN, S -> {
                int newOption = (currentOption + 1) % totalOptions;
                gameEngine.setSelectedPauseMenuOption(newOption);
            }
            case ENTER -> {
                if (currentOption == 0) { // "Option"
                    gameEngine.setState(GameState.OPTIONS_MENU);
                } else { // "Keybinding"
                    gameEngine.setState(GameState.KEYBINDING_MENU);
                }
            }
            case ESCAPE, P -> {
                gameEngine.setState(GameState.RUNNING);
            }
        }
    }
    
    /**
     * Gère les entrées dans le menu des options.
     * 
     * @param code Le code de la touche
     */
    private void handleOptionsMenuInput(KeyCode code) {
        double currentVolume = gameEngine.getMasterVolume();

        switch (code) {
            case LEFT, Q, A -> {
                gameEngine.setMasterVolume(currentVolume - 0.1);
            }
            case RIGHT, D -> {
                gameEngine.setMasterVolume(currentVolume + 0.1);
            }
            case ESCAPE -> {
                gameEngine.setState(GameState.PAUSE_MENU); // Retourner au menu de pause
            }
        }
    }
    
    /**
     * Gère les entrées dans le menu d'assignation des touches.
     *
     * @param code Le code de la touche
     */
    private void handleKeybindingMenuInput(KeyCode code) {
        if (gameEngine.isWaitingForKey()) {
            GameAction actionToBind = GameAction.values()[gameEngine.getSelectedKeybindingOption()];
            keybindingManager.setKeyForAction(actionToBind, code);
            gameEngine.setWaitingForKey(false);
            return;
        }

        int currentOption = gameEngine.getSelectedKeybindingOption();
        int totalOptions = GameAction.values().length;

        switch (code) {
            case UP, Z, W -> {
                int newOption = (currentOption - 1 + totalOptions) % totalOptions;
                gameEngine.setSelectedKeybindingOption(newOption);
            }
            case DOWN, S -> {
                int newOption = (currentOption + 1) % totalOptions;
                gameEngine.setSelectedKeybindingOption(newOption);
            }
            case ENTER -> {
                gameEngine.setWaitingForKey(true);
            }
            case ESCAPE -> {
                gameEngine.setState(GameState.PAUSE_MENU);
            }
        }
    }

    /**
     * Gère les entrées dans le menu.
     * 
     * @param code Le code de la touche
     */
    private void handleMenuInput(KeyCode code) {
        if (code == KeyCode.ENTER || code == KeyCode.SPACE) {
            gameEngine.initialize();
            gameEngine.setState(GameState.RUNNING);
        }
    }
    
    /**
     * Gère les entrées en game over.
     * 
     * @param code Le code de la touche
     */
    private void handleGameOverInput(KeyCode code) {
        if (code == KeyCode.ENTER || code == KeyCode.SPACE) {
            gameEngine.initialize();
            gameEngine.setState(GameState.RUNNING);
        } else if (code == KeyCode.ESCAPE) {
            gameEngine.setState(GameState.MENU);
        }
    }
    
    /**
     * Gère les entrées en fin de niveau.
     * 
     * @param code Le code de la touche
     */
    private void handleLevelCompleteInput(KeyCode code) {
        if (code == KeyCode.ENTER) {
            gameEngine.proceedToNextLevel();
        }
    }

    /**
     * Gère les entrées sur l'écran de victoire.
     *
     * @param code Le code de la touche
     */
    private void handleVictoryInput(KeyCode code) {
        int currentOption = gameEngine.getSelectedVictoryMenuOption();
        int totalOptions = 2; // "Rejouer" et "Quitter"

        switch (code) {
            case UP, W, Z -> {
                int newOption = (currentOption - 1 + totalOptions) % totalOptions;
                gameEngine.setSelectedVictoryMenuOption(newOption);
            }
            case DOWN, S -> {
                int newOption = (currentOption + 1) % totalOptions;
                gameEngine.setSelectedVictoryMenuOption(newOption);
            }
            case ENTER -> {
                if (currentOption == 0) { // Rejouer
                    gameEngine.initialize(); // Réinitialise le jeu au niveau 1
                } else { // Quitter
                    Platform.exit();
                }
            }
        }
    }
}
