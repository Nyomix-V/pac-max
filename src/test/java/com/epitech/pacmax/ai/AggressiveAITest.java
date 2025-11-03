package com.epitech.pacmax.ai;

import com.epitech.pacmax.entities.Direction;
import com.epitech.pacmax.entities.Ghost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour l'IA agressive.
 * Vérifie que l'IA poursuit correctement le joueur.
 * 
 * @author Epitech Team
 * @version 1.0
 */
class AggressiveAITest {
    
    private AggressiveAI ai;
    private Ghost ghost;
    
    @BeforeEach
    void setUp() {
        ai = new AggressiveAI();
        ghost = new Ghost(100, 100, Ghost.GhostType.BLINKY, ai);
    }
    
    @Test
    @DisplayName("L'IA doit se diriger vers la droite si le joueur est à droite")
    void testMoveRight() {
        ai.setPlayerPosition(200, 110); // Joueur à droite
        
        Direction direction = ai.chooseDirection(ghost);
        
        assertEquals(Direction.RIGHT, direction,
            "L'IA devrait choisir la direction RIGHT");
    }
    
    @Test
    @DisplayName("L'IA doit se diriger vers la gauche si le joueur est à gauche")
    void testMoveLeft() {
        ai.setPlayerPosition(50, 110); // Joueur à gauche
        
        Direction direction = ai.chooseDirection(ghost);
        
        assertEquals(Direction.LEFT, direction,
            "L'IA devrait choisir la direction LEFT");
    }
    
    @Test
    @DisplayName("L'IA doit se diriger vers le haut si le joueur est en haut")
    void testMoveUp() {
        ai.setPlayerPosition(110, 50); // Joueur en haut
        
        Direction direction = ai.chooseDirection(ghost);
        
        assertEquals(Direction.UP, direction,
            "L'IA devrait choisir la direction UP");
    }
    
    @Test
    @DisplayName("L'IA doit se diriger vers le bas si le joueur est en bas")
    void testMoveDown() {
        ai.setPlayerPosition(110, 200); // Joueur en bas
        
        Direction direction = ai.chooseDirection(ghost);
        
        assertEquals(Direction.DOWN, direction,
            "L'IA devrait choisir la direction DOWN");
    }
    
    @Test
    @DisplayName("L'IA doit privilégier l'axe horizontal si la différence X est plus grande")
    void testPrioritizeHorizontal() {
        ai.setPlayerPosition(200, 120); // Différence X = 90, différence Y = 10
        
        Direction direction = ai.chooseDirection(ghost);
        
        assertTrue(direction == Direction.RIGHT || direction == Direction.LEFT,
            "L'IA devrait privilégier l'axe horizontal");
    }
    
    @Test
    @DisplayName("L'IA doit privilégier l'axe vertical si la différence Y est plus grande")
    void testPrioritizeVertical() {
        ai.setPlayerPosition(120, 200); // Différence X = 10, différence Y = 90
        
        Direction direction = ai.chooseDirection(ghost);
        
        assertTrue(direction == Direction.UP || direction == Direction.DOWN,
            "L'IA devrait privilégier l'axe vertical");
    }
    
    @Test
    @DisplayName("L'IA doit retourner une direction non-null")
    void testNonNullDirection() {
        ai.setPlayerPosition(150, 150);
        
        Direction direction = ai.chooseDirection(ghost);
        
        assertNotNull(direction, "La direction ne devrait jamais être null");
    }
}
