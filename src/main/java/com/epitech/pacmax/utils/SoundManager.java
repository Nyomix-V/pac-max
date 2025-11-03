package com.epitech.pacmax.utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestionnaire des sons et musiques du jeu (Singleton).
 * Centralise le chargement et la lecture des fichiers audio.
 * Gère la file d'attente pour éviter les conflits sonores.
 * 
 * @author Epitech Team
 * @version 1.0
 */
public class SoundManager {
    
    /** Instance unique (Singleton) */
    private static SoundManager instance;
    
    /** Map des effets sonores */
    private final Map<String, MediaPlayer> soundEffects;
    
    /** Lecteur de musique de fond */
    private MediaPlayer musicPlayer;
    
    /** Volume des effets sonores (0.0 à 1.0) */
    private double sfxVolume;
    
    /** Volume de la musique (0.0 à 1.0) */
    private double musicVolume;
    
    /** Indique si les sons sont activés */
    private boolean soundEnabled;
    
    /** Son actuellement en cours de lecture */
    private MediaPlayer currentSound;
    
    /** Dernier son joué (pour éviter les répétitions) */
    private String lastPlayedSound;
    
    /**
     * Constructeur privé (Singleton).
     */
    private SoundManager() {
        this.soundEffects = new HashMap<>();
        this.sfxVolume = 0.7;
        this.musicVolume = 0.5;
        this.soundEnabled = true;
        this.currentSound = null;
        this.lastPlayedSound = null;
        
        // Charger les sons
        loadSounds();
    }
    
    /**
     * Récupère l'instance unique du SoundManager.
     * 
     * @return L'instance du SoundManager
     */
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }
    
    /**
     * Charge les effets sonores depuis resources/sounds/effects/.
     */
    private void loadSounds() {
        // Sons du jeu (format MP3 pour meilleure compatibilité Windows)
        loadSound("start", "resources/sounds/effects/ça_va_bien_se_passer_louis.mp3");
        loadSound("hotdog", "resources/sounds/effects/jadore_les_hot_dogs_ikea.mp3");
        loadSound("far", "resources/sounds/effects/noubliez_pas_de_signer_loud.mp3");
        loadSound("danger", "resources/sounds/effects/oulala_quinze_minute_de_retard.mp3");
        loadSound("chase", "resources/sounds/effects/ça_va_bien_se_passer_chaima.mp3");
        loadSound("death", "resources/sounds/effects/vous_avez_signes.mp3");
        loadSound("gameover", "resources/sounds/effects/a_bientot_sur_le_reseau_ligne_dazur_voice.mp3");
    }
    
    /**
     * Charge un effet sonore.
     * 
     * @param name Nom de l'effet
     * @param path Chemin du fichier audio
     */
    private void loadSound(String name, String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                System.out.println("📂 Tentative de chargement: " + path);
                Media media = new Media(file.toURI().toString());
                MediaPlayer player = new MediaPlayer(media);
                player.setVolume(sfxVolume);
                
                // Vérifier les erreurs de chargement
                player.setOnError(() -> {
                    System.err.println("❌ Erreur Media pour " + name + ": " + player.getError().getMessage());
                });
                
                player.setOnReady(() -> {
                    System.out.println("✓ Son prêt: " + name + " (durée: " + media.getDuration().toSeconds() + "s)");
                });
                
                soundEffects.put(name, player);
            } else {
                System.err.println("✗ Fichier non trouvé: " + path);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du son " + name + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Joue un effet sonore (un seul à la fois).
     * Si un son est déjà en cours, il est ignoré.
     * 
     * @param soundName Nom de l'effet à jouer
     */
    public void playSound(String soundName) {
        if (!soundEnabled) return;
        
        // Ne pas rejouer le même son
        if (soundName.equals(lastPlayedSound)) {
            return;
        }
        
        // Si un son est en cours, ne pas jouer
        if (currentSound != null && currentSound.getStatus() == MediaPlayer.Status.PLAYING) {
            return;
        }
        
        MediaPlayer player = soundEffects.get(soundName);
        if (player != null) {
            // Arrêter le son précédent
            if (currentSound != null) {
                currentSound.stop();
            }
            
            // Réinitialiser et jouer le nouveau son
            player.seek(Duration.ZERO);
            player.setVolume(sfxVolume);
            player.play();
            
            currentSound = player;
            lastPlayedSound = soundName;
            
            // Réinitialiser lastPlayedSound quand le son est terminé
            player.setOnEndOfMedia(() -> {
                lastPlayedSound = null;
                currentSound = null;
            });
        }
    }
    
    /**
     * Vérifie si un son est en cours de lecture.
     * 
     * @return true si un son joue actuellement
     */
    public boolean isSoundPlaying() {
        return currentSound != null && currentSound.getStatus() == MediaPlayer.Status.PLAYING;
    }
    
    /**
     * Joue la musique de fond.
     * 
     * @param musicPath Chemin du fichier musical
     */
    public void playMusic(String musicPath) {
        if (!soundEnabled) return;
        
        try {
            stopMusic();
            
            File file = new File(musicPath);
            if (file.exists()) {
                Media media = new Media(file.toURI().toString());
                musicPlayer = new MediaPlayer(media);
                musicPlayer.setVolume(musicVolume);
                musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                musicPlayer.play();
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la lecture de la musique: " + e.getMessage());
        }
    }
    
    /**
     * Arrête la musique de fond.
     */
    public void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer = null;
        }
    }
    
    /**
     * Met en pause la musique.
     */
    public void pauseMusic() {
        if (musicPlayer != null) {
            musicPlayer.pause();
        }
    }
    
    /**
     * Reprend la musique.
     */
    public void resumeMusic() {
        if (musicPlayer != null) {
            musicPlayer.play();
        }
    }
    
    /**
     * Active ou désactive tous les sons.
     * 
     * @param enabled true pour activer, false pour désactiver
     */
    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
        if (!enabled) {
            stopMusic();
        }
    }
    
    /**
     * Définit le volume des effets sonores.
     * 
     * @param volume Volume entre 0.0 et 1.0
     */
    public void setSfxVolume(double volume) {
        this.sfxVolume = Math.max(0.0, Math.min(1.0, volume));
    }
    
    /**
     * Définit le volume de la musique.
     * 
     * @param volume Volume entre 0.0 et 1.0
     */
    public void setMusicVolume(double volume) {
        this.musicVolume = Math.max(0.0, Math.min(1.0, volume));
        if (musicPlayer != null) {
            musicPlayer.setVolume(this.musicVolume);
        }
    }
    
    // Getters
    
    public boolean isSoundEnabled() {
        return soundEnabled;
    }
    
    public double getSfxVolume() {
        return sfxVolume;
    }
    
    public double getMusicVolume() {
        return musicVolume;
    }
}
