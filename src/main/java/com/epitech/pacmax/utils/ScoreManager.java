package com.epitech.pacmax.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Gestionnaire des scores.
 * Sauvegarde et charge les meilleurs scores depuis un fichier JSON.
 * 
 * @author Epitech Team
 * @version 1.0
 */
public class ScoreManager {
    
    private static final String SCORES_FILE = "scores.json";
    private static final int MAX_SCORES = 10;
    
    private final Gson gson;
    private List<ScoreEntry> highScores;
    
    /**
     * Constructeur du gestionnaire de scores.
     */
    public ScoreManager() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.highScores = new ArrayList<>();
        loadScores();
    }
    
    /**
     * Charge les scores depuis le fichier JSON.
     */
    public void loadScores() {
        File file = new File(SCORES_FILE);
        
        if (!file.exists()) {
            highScores = new ArrayList<>();
            return;
        }
        
        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<ScoreEntry>>(){}.getType();
            highScores = gson.fromJson(reader, listType);
            
            if (highScores == null) {
                highScores = new ArrayList<>();
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement des scores: " + e.getMessage());
            highScores = new ArrayList<>();
        }
    }
    
    /**
     * Sauvegarde les scores dans le fichier JSON.
     */
    public void saveScores() {
        try (Writer writer = new FileWriter(SCORES_FILE)) {
            gson.toJson(highScores, writer);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des scores: " + e.getMessage());
        }
    }
    
    /**
     * Ajoute un nouveau score.
     * 
     * @param playerName Nom du joueur
     * @param score Score obtenu
     * @param level Niveau atteint
     * @return true si le score entre dans le top 10
     */
    public boolean addScore(String playerName, int score, int level) {
        ScoreEntry entry = new ScoreEntry(playerName, score, level);
        highScores.add(entry);
        
        // Trier par score décroissant
        highScores.sort(Comparator.comparingInt(ScoreEntry::getScore).reversed());
        
        // Garder seulement les 10 meilleurs
        boolean isHighScore = highScores.indexOf(entry) < MAX_SCORES;
        
        if (highScores.size() > MAX_SCORES) {
            highScores = highScores.subList(0, MAX_SCORES);
        }
        
        saveScores();
        return isHighScore;
    }
    
    /**
     * Récupère la liste des meilleurs scores.
     * 
     * @return Liste des scores
     */
    public List<ScoreEntry> getHighScores() {
        return new ArrayList<>(highScores);
    }
    
    /**
     * Vérifie si un score peut entrer dans le top 10.
     * 
     * @param score Le score à vérifier
     * @return true si le score est suffisant
     */
    public boolean isHighScore(int score) {
        if (highScores.size() < MAX_SCORES) {
            return true;
        }
        return score > highScores.get(highScores.size() - 1).getScore();
    }
    
    /**
     * Classe représentant une entrée de score.
     */
    public static class ScoreEntry {
        private String playerName;
        private int score;
        private int level;
        private String date;
        
        /**
         * Constructeur d'une entrée de score.
         * 
         * @param playerName Nom du joueur
         * @param score Score obtenu
         * @param level Niveau atteint
         */
        public ScoreEntry(String playerName, int score, int level) {
            this.playerName = playerName;
            this.score = score;
            this.level = level;
            this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        }
        
        public String getPlayerName() {
            return playerName;
        }
        
        public int getScore() {
            return score;
        }
        
        public int getLevel() {
            return level;
        }
        
        public String getDate() {
            return date;
        }
    }
}
