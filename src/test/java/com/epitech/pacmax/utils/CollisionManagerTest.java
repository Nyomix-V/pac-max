package com.epitech.pacmax.utils;

import com.epitech.pacmax.entities.Player;
import com.epitech.pacmax.entities.Wall;
import com.epitech.pacmax.entities.PacGum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour le CollisionManager.
 * Teste la détection et la résolution des collisions.
 * 
 * @author Epitech Team
 * @version 1.0
 */
class CollisionManagerTest {
    
    private CollisionManager collisionManager;
    
    @BeforeEach
    void setUp() {
        collisionManager = new CollisionManager();
    }
    
    @Test
    @DisplayName("Deux entités qui se chevauchent doivent être en collision")
    void testCollisionDetection() {
        Player player = new Player(100, 100);
        PacGum pacGum = new PacGum(105, 105, PacGum.PacGumType.NORMAL);
        
        assertTrue(collisionManager.checkCollision(player, pacGum),
            "Le joueur et la pac-gum devraient être en collision");
    }
    
    @Test
    @DisplayName("Deux entités éloignées ne doivent pas être en collision")
    void testNoCollision() {
        Player player = new Player(100, 100);
        PacGum pacGum = new PacGum(200, 200, PacGum.PacGumType.NORMAL);
        
        assertFalse(collisionManager.checkCollision(player, pacGum),
            "Le joueur et la pac-gum ne devraient pas être en collision");
    }
    
    @Test
    @DisplayName("Deux entités adjacentes sans chevauchement ne doivent pas être en collision")
    void testAdjacentNoCollision() {
        Player player = new Player(100, 100); // 20x20
        Wall wall = new Wall(120, 100, 30, 30); // Juste à côté
        
        assertFalse(collisionManager.checkCollision(player, wall),
            "Le joueur et le mur adjacents ne devraient pas être en collision");
    }
    
    @Test
    @DisplayName("Résolution de collision avec un mur à droite")
    void testResolveWallCollisionRight() {
        Player player = new Player(100, 100);
        Wall wall = new Wall(115, 100, 30, 30);
        
        // Le joueur devrait être repoussé à gauche
        collisionManager.resolveWallCollision(player, wall);
        
        assertEquals(115 - player.getWidth(), player.getX(), 0.01,
            "Le joueur devrait être repoussé à gauche du mur");
    }
    
    @Test
    @DisplayName("Résolution de collision avec un mur à gauche")
    void testResolveWallCollisionLeft() {
        Player player = new Player(100, 100);
        Wall wall = new Wall(85, 100, 30, 30);
        
        // Le joueur devrait être repoussé à droite
        collisionManager.resolveWallCollision(player, wall);
        
        assertEquals(85 + wall.getWidth(), player.getX(), 0.01,
            "Le joueur devrait être repoussé à droite du mur");
    }
    
    @Test
    @DisplayName("Calcul de distance entre deux entités")
    void testGetDistance() {
        Player player = new Player(0, 0); // Centre à (10, 10)
        PacGum pacGum = new PacGum(30, 40, PacGum.PacGumType.NORMAL); // Centre à (32, 42)
        
        // Distance entre (10, 10) et (32, 42)
        // sqrt((32-10)² + (42-10)²) = sqrt(484 + 1024) = sqrt(1508) ≈ 38.83
        double distance = collisionManager.getDistance(player, pacGum);
        
        assertTrue(distance > 38 && distance < 39,
            "La distance calculée devrait être environ 38.83");
    }
    
    @Test
    @DisplayName("Vérification de collision potentielle avec un mur")
    void testWouldCollideWithWall() {
        Wall wall = new Wall(100, 100, 30, 30);
        
        // Position qui entrerait en collision
        assertTrue(collisionManager.wouldCollideWithWall(105, 105, 20, 20, wall),
            "Devrait détecter une collision potentielle");
        
        // Position qui n'entrerait pas en collision
        assertFalse(collisionManager.wouldCollideWithWall(200, 200, 20, 20, wall),
            "Ne devrait pas détecter de collision potentielle");
    }
    
    @Test
    @DisplayName("Collision AABB - cas limite : bord contre bord")
    void testAABBEdgeCase() {
        Player player = new Player(100, 100); // 20x20, donc de 100 à 120
        Wall wall = new Wall(120, 100, 30, 30); // De 120 à 150
        
        // Les bords se touchent exactement, pas de chevauchement
        assertFalse(collisionManager.checkCollision(player, wall),
            "Les entités qui se touchent sans chevaucher ne devraient pas être en collision");
    }
}
