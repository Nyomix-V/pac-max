package com.epitech.pacmax.utils;

import com.epitech.pacmax.engine.GameEngine;
import com.epitech.pacmax.engine.GameState;
import com.epitech.pacmax.entities.Direction;
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
    
    /**
     * Constructeur du gestionnaire d'entrées.
     * 
     * @param gameEngine Le moteur de jeu
     */
    public InputHandler(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
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
            case PAUSED -> handlePauseInput(code);
            case MENU -> handleMenuInput(code);
            case GAME_OVER -> handleGameOverInput(code);
            case LEVEL_COMPLETE -> handleLevelCompleteInput(code);
        }
    }
    
    /**
     * Gère les entrées pendant le jeu.
     * 
     * @param code Le code de la touche
     */
    private void handleGameInput(KeyCode code) {
        Direction newDirection = switch (code) {
            case UP, Z, W -> Direction.UP;
            case DOWN, S -> Direction.DOWN;
            case LEFT, Q, A -> Direction.LEFT;
            case RIGHT, D -> Direction.RIGHT;
            case ESCAPE, P -> {
                gameEngine.setState(GameState.PAUSED);
                yield null;
            }
            default -> null;
        };
        
        if (newDirection != null && gameEngine.getPlayer() != null) {
            gameEngine.getPlayer().setNextDirection(newDirection);
        }
    }
    
    /**
     * Gère les entrées en pause.
     * 
     * @param code Le code de la touche
     */
    private void handlePauseInput(KeyCode code) {
        if (code == KeyCode.ESCAPE || code == KeyCode.P) {
            gameEngine.setState(GameState.RUNNING);
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
        if (code == KeyCode.ENTER || code == KeyCode.SPACE) {
            gameEngine.initialize();
            gameEngine.setState(GameState.RUNNING);
        }
    }
}
