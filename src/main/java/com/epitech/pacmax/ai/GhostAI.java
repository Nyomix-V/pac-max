package com.epitech.pacmax.ai;

import com.epitech.pacmax.entities.Direction;
import com.epitech.pacmax.entities.Ghost;

/**
 * Interface définissant le comportement d'IA d'un fantôme.
 * Implémente le Pattern Strategy pour permettre différents comportements.
 * 
 * @author Epitech Team
 * @version 1.0
 */
public interface GhostAI {
    
    /**
     * Choisit la prochaine direction pour le fantôme.
     * 
     * @param ghost Le fantôme qui doit choisir sa direction
     * @return La direction choisie
     */
    Direction chooseDirection(Ghost ghost);
    
    /**
     * Définit la référence au joueur pour les IA qui en ont besoin.
     * 
     * @param playerX Position X du joueur
     * @param playerY Position Y du joueur
     */
    void setPlayerPosition(double playerX, double playerY);
}
