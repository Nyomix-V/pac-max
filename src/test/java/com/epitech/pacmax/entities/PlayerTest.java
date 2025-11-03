package com.epitech.pacmax.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe Player.
 * Démontre l'utilisation de JUnit 5 pour tester les entités.
 * 
 * @author Epitech Team
 * @version 1.0
 */
class PlayerTest {
    
    private Player player;
    
    @BeforeEach
    void setUp() {
        // Initialiser un joueur avant chaque test
        player = new Player(100, 100);
    }
    
    @Test
    @DisplayName("Le joueur doit avoir 3 vies au départ")
    void testInitialLives() {
        assertEquals(3, player.getLives(), "Le joueur devrait commencer avec 3 vies");
    }
    
    @Test
    @DisplayName("Le joueur doit avoir un score de 0 au départ")
    void testInitialScore() {
        assertEquals(0, player.getScore(), "Le score initial devrait être 0");
    }
    
    @Test
    @DisplayName("Le joueur doit être visible par défaut")
    void testInitialVisibility() {
        assertTrue(player.isVisible(), "Le joueur devrait être visible par défaut");
    }
    
    @Test
    @DisplayName("Le joueur doit être actif par défaut")
    void testInitialActive() {
        assertTrue(player.isActive(), "Le joueur devrait être actif par défaut");
    }
    
    @Test
    @DisplayName("Ajouter des points doit augmenter le score")
    void testAddScore() {
        player.addScore(100);
        assertEquals(100, player.getScore(), "Le score devrait être 100 après avoir ajouté 100 points");
        
        player.addScore(50);
        assertEquals(150, player.getScore(), "Le score devrait être 150 après avoir ajouté 50 points supplémentaires");
    }
    
    @Test
    @DisplayName("Perdre une vie doit décrémenter le compteur de vies")
    void testLoseLife() {
        player.loseLife();
        assertEquals(2, player.getLives(), "Le joueur devrait avoir 2 vies après en avoir perdu une");
        
        player.loseLife();
        player.loseLife();
        assertEquals(0, player.getLives(), "Le joueur devrait avoir 0 vie après en avoir perdu 3");
    }
    
    @Test
    @DisplayName("Les vies ne doivent pas devenir négatives")
    void testLivesCannotBeNegative() {
        player.loseLife();
        player.loseLife();
        player.loseLife();
        player.loseLife(); // Tentative de passer en négatif
        
        assertEquals(0, player.getLives(), "Les vies ne devraient pas être négatives");
    }
    
    @Test
    @DisplayName("Activer le power-up doit mettre le joueur en mode powered")
    void testActivatePowerUp() {
        assertFalse(player.isPowered(), "Le joueur ne devrait pas être powered initialement");
        
        player.activatePowerUp();
        assertTrue(player.isPowered(), "Le joueur devrait être powered après activation");
        assertTrue(player.getPowerUpTimer() > 0, "Le timer du power-up devrait être positif");
    }
    
    @Test
    @DisplayName("Le power-up doit se désactiver après expiration")
    void testPowerUpExpiration() {
        player.activatePowerUp();
        
        // Simuler l'écoulement du temps (11 secondes)
        player.update(11.0);
        
        assertFalse(player.isPowered(), "Le power-up devrait être expiré après 11 secondes");
        assertEquals(0, player.getPowerUpTimer(), "Le timer devrait être à 0");
    }
    
    @Test
    @DisplayName("Changer la direction doit mettre à jour nextDirection")
    void testSetDirection() {
        player.setNextDirection(Direction.RIGHT);
        assertEquals(Direction.RIGHT, player.getNextDirection(), "La prochaine direction devrait être RIGHT");
    }
    
    @Test
    @DisplayName("Le déplacement doit modifier la position")
    void testMovement() {
        player.setDirection(Direction.RIGHT);
        double initialX = player.getX();
        
        player.move(1.0); // 1 seconde de mouvement
        
        assertTrue(player.getX() > initialX, "La position X devrait avoir augmenté");
    }
    
    @Test
    @DisplayName("Reset doit réinitialiser la position et l'état")
    void testReset() {
        player.setDirection(Direction.RIGHT);
        player.activatePowerUp();
        player.addScore(100);
        
        player.reset(200, 200);
        
        assertEquals(200, player.getX(), "La position X devrait être 200");
        assertEquals(200, player.getY(), "La position Y devrait être 200");
        assertEquals(Direction.NONE, player.getDirection(), "La direction devrait être NONE");
        assertFalse(player.isPowered(), "Le power-up devrait être désactivé");
        // Note : le score n'est pas réinitialisé par reset()
    }
    
    @Test
    @DisplayName("getCenterX doit retourner le centre horizontal")
    void testGetCenterX() {
        // Player a une largeur de 20
        double expectedCenterX = 100 + 20 / 2.0;
        assertEquals(expectedCenterX, player.getCenterX(), "Le centre X devrait être correct");
    }
    
    @Test
    @DisplayName("getCenterY doit retourner le centre vertical")
    void testGetCenterY() {
        // Player a une hauteur de 20
        double expectedCenterY = 100 + 20 / 2.0;
        assertEquals(expectedCenterY, player.getCenterY(), "Le centre Y devrait être correct");
    }
}
