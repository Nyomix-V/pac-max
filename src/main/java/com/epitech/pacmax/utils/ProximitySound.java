package com.epitech.pacmax.utils;

import com.epitech.pacmax.entities.Ghost;
import com.epitech.pacmax.entities.Player;

import java.util.List;
import java.util.Random;

/**
 * Gère les sons contextuels basés sur la proximité des fantômes.
 * 
 * ⚙️ PARAMÈTRES MODIFIABLES :
 * - DISTANCE_FAR_PERCENT : Distance considérée comme "loin" (défaut: 40%)
 * - DISTANCE_CLOSE_PERCENT : Distance considérée comme "proche" (défaut: 20%)
 * 
 * @author Epitech Team
 * @version 1.0
 */
public class ProximitySound {
    
    // ═══════════════════════════════════════════════════════════════════
    // ⚙️ PARAMÈTRES MODIFIABLES - Ajustez ces valeurs selon vos besoins
    // ═══════════════════════════════════════════════════════════════════
    
    /**
     * Distance "loin" en pourcentage de la diagonale de la map (défaut: 40%).
     * Si le fantôme le plus proche est > 40%, joue "noubliez_pas_de_signer_loud.ogg"
     */
    private static final double DISTANCE_FAR_PERCENT = 0.40;
    
    /**
     * Distance "proche" en pourcentage de la diagonale de la map (défaut: 20%).
     * Si le fantôme le plus proche est < 20%, joue un son d'alerte.
     */
    private static final double DISTANCE_CLOSE_PERCENT = 0.20;
    
    // ═══════════════════════════════════════════════════════════════════
    
    private final SoundManager soundManager;
    private final double mapDiagonal;
    private String currentProximityState;
    private final Random random;
    
    /**
     * Constructeur.
     * 
     * @param mapWidth Largeur de la map
     * @param mapHeight Hauteur de la map
     */
    public ProximitySound(double mapWidth, double mapHeight) {
        this.soundManager = SoundManager.getInstance();
        // Calculer la diagonale de la map (distance maximale possible)
        this.mapDiagonal = Math.sqrt(mapWidth * mapWidth + mapHeight * mapHeight);
        this.currentProximityState = null;
        this.random = new Random();
    }
    
    /**
     * Met à jour les sons contextuels basés sur la proximité des fantômes.
     * 
     * @param player Le joueur
     * @param ghosts Liste des fantômes
     */
    public void update(Player player, List<Ghost> ghosts) {
        if (ghosts.isEmpty()) return;
        
        // Trouver le fantôme le plus proche
        double minDistance = Double.MAX_VALUE;
        for (Ghost ghost : ghosts) {
            if (!ghost.isActive()) continue;
            
            double distance = calculateDistance(
                player.getCenterX(), player.getCenterY(),
                ghost.getCenterX(), ghost.getCenterY()
            );
            
            if (distance < minDistance) {
                minDistance = distance;
            }
        }
        
        // Calculer le pourcentage de distance par rapport à la diagonale
        double distancePercent = minDistance / mapDiagonal;
        
        // Déterminer l'état de proximité
        String newState = determineProximityState(distancePercent, player.isPowered());
        
        // Jouer le son seulement si l'état a changé
        if (newState != null && !newState.equals(currentProximityState)) {
            soundManager.playSound(newState);
            currentProximityState = newState;
        }
    }
    
    /**
     * Détermine l'état de proximité et le son à jouer.
     * 
     * @param distancePercent Pourcentage de distance (0.0 à 1.0)
     * @param isPowered Le joueur est-il sous power-up ?
     * @return Le nom du son à jouer, ou null si aucun changement
     */
    private String determineProximityState(double distancePercent, boolean isPowered) {
        if (distancePercent > DISTANCE_FAR_PERCENT) {
            // Loin : > 40% - Alterner aléatoirement entre "far" et "danger"
            return random.nextBoolean() ? "far" : "danger";
        } else if (distancePercent < DISTANCE_CLOSE_PERCENT) {
            // Proche : < 20%
            if (isPowered) {
                // Sous power-up : on ne joue PAS de son (supprimé)
                return null;
            } else {
                // Sans power-up : danger ! Alterner aléatoirement
                return random.nextBoolean() ? "far" : "danger";
            }
        }
        
        // Distance moyenne (20-40%) : pas de son spécifique
        return null;
    }
    
    /**
     * Calcule la distance euclidienne entre deux points.
     * 
     * @param x1 X du premier point
     * @param y1 Y du premier point
     * @param x2 X du deuxième point
     * @param y2 Y du deuxième point
     * @return La distance
     */
    private double calculateDistance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * Réinitialise l'état de proximité.
     * Utile lors du changement de niveau ou de respawn.
     */
    public void reset() {
        currentProximityState = null;
    }
}
