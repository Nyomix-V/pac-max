package com.epitech.pacmax.engine;

/**
 * Énumération représentant les différents états du jeu.
 * Implémente le Pattern State de manière simplifiée.
 * 
 * @author Epitech Team
 * @version 1.0
 */
public enum GameState {
    /** Menu principal */
    MENU,
    
    /** En attente du premier mouvement du joueur */
    WAITING_TO_START,
    
    /** Jeu en cours */
    RUNNING,
    
    /** Jeu en pause */
    PAUSED,
    
    /** Game Over */
    GAME_OVER,
    
    /** Victoire (niveau terminé) */
    LEVEL_COMPLETE,
    
    /** Transition entre niveaux */
    TRANSITION
}
