package com.epitech.pacmax.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Gestionnaire du classement des meilleurs scores (Top 10).
 * Utilise Gson pour sauvegarder/charger depuis un fichier JSON.
 * 
 * @author Epitech Team
 * @version 1.0
 */
public class LeaderboardManager {
    
    /** Instance unique (Singleton) */
    private static LeaderboardManager instance;
    
    /** Chemin du fichier de sauvegarde */
    private static final String LEADERBOARD_FILE = "leaderboard.json";
    
    /** Nombre maximum d'entrées dans le classement */
    private static final int MAX_ENTRIES = 10;
    
    /** Liste des scores */
    private List<LeaderboardEntry> entries;
    
    /** Gson pour la sérialisation */
    private final Gson gson;
    
    /**
     * Constructeur privé (Singleton).
     */
    private LeaderboardManager() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.entries = new ArrayList<>();
        loadLeaderboard();
    }
    
    /**
     * Récupère l'instance unique.
     * 
     * @return L'instance du LeaderboardManager
     */
    public static LeaderboardManager getInstance() {
        if (instance == null) {
            instance = new LeaderboardManager();
        }
        return instance;
    }
    
    /**
     * Ajoute un nouveau score au classement.
     * 
     * @param playerName Nom du joueur (max 10 caractères, peut être vide)
     * @param score Score obtenu
     * @return true si le score entre dans le Top 10
     */
    public boolean addScore(String playerName, int score) {
        // Valider le nom du joueur
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Anonyme";
        } else {
            playerName = playerName.trim();
            if (playerName.length() > 10) {
                playerName = playerName.substring(0, 10);
            }
        }
        
        // Créer une nouvelle entrée
        LeaderboardEntry newEntry = new LeaderboardEntry(playerName, score);
        
        // Ajouter à la liste
        entries.add(newEntry);
        
        // Trier par score décroissant
        Collections.sort(entries);
        
        // Garder seulement le Top 10
        if (entries.size() > MAX_ENTRIES) {
            entries = entries.subList(0, MAX_ENTRIES);
        }
        
        // Sauvegarder
        saveLeaderboard();
        
        // Vérifier si le score est dans le Top 10
        return entries.contains(newEntry);
    }
    
    /**
     * Récupère la liste des scores (Top 10).
     * 
     * @return Liste des entrées du classement
     */
    public List<LeaderboardEntry> getTopScores() {
        return new ArrayList<>(entries);
    }
    
    /**
     * Vérifie si un score entre dans le Top 10.
     * 
     * @param score Score à vérifier
     * @return true si le score est suffisant pour le Top 10
     */
    public boolean isTopScore(int score) {
        if (entries.size() < MAX_ENTRIES) {
            return true; // Moins de 10 entrées, toujours accepter
        }
        
        // Comparer avec le dernier score du Top 10
        return score > entries.get(entries.size() - 1).getScore();
    }
    
    /**
     * Récupère le rang d'un score.
     * 
     * @param score Score à évaluer
     * @return Le rang (1-10), ou -1 si hors Top 10
     */
    public int getRank(int score) {
        for (int i = 0; i < entries.size(); i++) {
            if (score >= entries.get(i).getScore()) {
                return i + 1;
            }
        }
        
        if (entries.size() < MAX_ENTRIES) {
            return entries.size() + 1;
        }
        
        return -1; // Hors Top 10
    }
    
    /**
     * Charge le classement depuis le fichier JSON.
     */
    private void loadLeaderboard() {
        File file = new File(LEADERBOARD_FILE);
        if (!file.exists()) {
            System.out.println("📊 Nouveau classement créé");
            return;
        }
        
        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<LeaderboardEntry>>(){}.getType();
            List<LeaderboardEntry> loaded = gson.fromJson(reader, listType);
            
            if (loaded != null) {
                entries = loaded;
                System.out.println("✓ Classement chargé: " + entries.size() + " entrées");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du classement: " + e.getMessage());
        }
    }
    
    /**
     * Sauvegarde le classement dans le fichier JSON.
     */
    private void saveLeaderboard() {
        try (Writer writer = new FileWriter(LEADERBOARD_FILE)) {
            gson.toJson(entries, writer);
            System.out.println("✓ Classement sauvegardé");
        } catch (Exception e) {
            System.err.println("Erreur lors de la sauvegarde du classement: " + e.getMessage());
        }
    }
    
    /**
     * Réinitialise le classement (vide toutes les entrées).
     */
    public void reset() {
        entries.clear();
        saveLeaderboard();
        System.out.println("🗑️ Classement réinitialisé");
    }
    
    /**
     * Classe représentant une entrée du classement.
     */
    public static class LeaderboardEntry implements Comparable<LeaderboardEntry> {
        private String playerName;
        private int score;
        private String date;
        
        /**
         * Constructeur.
         * 
         * @param playerName Nom du joueur
         * @param score Score obtenu
         */
        public LeaderboardEntry(String playerName, int score) {
            this.playerName = playerName;
            this.score = score;
            this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        }
        
        public String getPlayerName() {
            return playerName;
        }
        
        public int getScore() {
            return score;
        }
        
        public String getDate() {
            return date;
        }
        
        @Override
        public int compareTo(LeaderboardEntry other) {
            // Tri décroissant par score
            return Integer.compare(other.score, this.score);
        }
        
        @Override
        public String toString() {
            return String.format("%s - %d points (%s)", playerName, score, date);
        }
    }
}
