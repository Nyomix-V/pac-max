package com.epitech.pacmax.levels;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * Classe représentant une carte de jeu.
 * Charge les niveaux depuis des fichiers JSON.
 * 
 * @author Epitech Team
 * @version 1.0
 */
public class GameMap {
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("width")
    private int width;
    
    @SerializedName("height")
    private int height;
    
    @SerializedName("tileSize")
    private int tileSize;
    
    @SerializedName("tiles")
    private int[][] tiles;
    
    @SerializedName("playerSpawn")
    private SpawnPoint playerSpawn;
    
    @SerializedName("ghostSpawns")
    private List<SpawnPoint> ghostSpawns;
    
    /**
     * Charge une carte depuis un fichier JSON.
     * 
     * @param filePath Chemin du fichier JSON
     * @return La carte chargée
     * @throws IOException Si le fichier ne peut pas être lu
     */
    public static GameMap loadFromFile(String filePath) throws IOException {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, GameMap.class);
        }
    }
    
    /**
     * Récupère le type de tuile à une position donnée.
     * 
     * @param x Coordonnée X (en tuiles)
     * @param y Coordonnée Y (en tuiles)
     * @return Le type de tuile (0 = vide, 1 = mur, 2 = pac-gum, 3 = power-up)
     */
    public int getTile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return 1; // Mur par défaut hors limites
        }
        return tiles[y][x];
    }
    
    /**
     * Définit le type de tuile à une position donnée.
     * 
     * @param x Coordonnée X (en tuiles)
     * @param y Coordonnée Y (en tuiles)
     * @param value Le type de tuile
     */
    public void setTile(int x, int y, int value) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            tiles[y][x] = value;
        }
    }
    
    /**
     * Vérifie si une tuile est un mur.
     * 
     * @param x Coordonnée X (en tuiles)
     * @param y Coordonnée Y (en tuiles)
     * @return true si c'est un mur
     */
    public boolean isWall(int x, int y) {
        return getTile(x, y) == 1;
    }
    
    // Getters et Setters
    
    public String getName() {
        return name;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public int getTileSize() {
        return tileSize;
    }
    
    public int[][] getTiles() {
        return tiles;
    }
    
    public SpawnPoint getPlayerSpawn() {
        return playerSpawn;
    }
    
    public List<SpawnPoint> getGhostSpawns() {
        return ghostSpawns;
    }
    
    /**
     * Classe représentant un point d'apparition.
     */
    public static class SpawnPoint {
        @SerializedName("x")
        private int x;
        
        @SerializedName("y")
        private int y;
        
        public SpawnPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public int getX() {
            return x;
        }
        
        public int getY() {
            return y;
        }
    }
}
