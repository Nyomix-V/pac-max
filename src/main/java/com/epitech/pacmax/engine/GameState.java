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
    
    /** Menu de pause avec options */
    PAUSE_MENU,
    
    /** Menu des options (volume, etc.) */
    OPTIONS_MENU,
    
    /** Menu d'assignation des touches */
    KEYBINDING_MENU,
    
    /** Game Over */
    GAME_OVER,
    
    /** Victoire (niveau terminé) */
    LEVEL_COMPLETE,
    
    /** Le joueur a atteint le seuil pour finir le niveau mais peut encore jouer */
    LEVEL_CLEARED,
    
    /** Transition entre niveaux */
    TRANSITION,

    /** Le joueur a gagné la partie */
    VICTORY
}
