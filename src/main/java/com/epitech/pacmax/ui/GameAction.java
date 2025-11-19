package com.epitech.pacmax.utils;

/**
 * Énumération des actions de jeu possibles pouvant être associées à une touche.
 *
 * @author Epitech Team
 * @version 1.0
 */
public enum GameAction {
    MOVE_UP("Monter"),
    MOVE_DOWN("Descendre"),
    MOVE_LEFT("Gauche"),
    MOVE_RIGHT("Droite"),
    PAUSE("Pause / Menu");

    private final String displayName;

    GameAction(String displayName) { this.displayName = displayName; }

    public String getDisplayName() { return displayName; }
}