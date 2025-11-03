package com.epitech.pacmax.utils;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestionnaire des sons et musiques du jeu.
 * Centralise le chargement et la lecture des fichiers audio.
 * 
 * @author Epitech Team
 * @version 1.0
 */
public class SoundManager {
    
    /** Map des effets sonores */
    private final Map<String, AudioClip> soundEffects;
    
    /** Lecteur de musique de fond */
    private MediaPlayer musicPlayer;
    
    /** Volume des effets sonores (0.0 à 1.0) */
    private double sfxVolume;
    
    /** Volume de la musique (0.0 à 1.0) */
    private double musicVolume;
    
    /** Indique si les sons sont activés */
    private boolean soundEnabled;
    
    /**
     * Constructeur du gestionnaire de sons.
     */
    public SoundManager() {
        this.soundEffects = new HashMap<>();
        this.sfxVolume = 0.7;
        this.musicVolume = 0.5;
        this.soundEnabled = true;
        
        // TODO: Charger les sons depuis les ressources
        // loadSounds();
    }
    
    /**
     * Charge les effets sonores.
     * TODO: Implémenter le chargement depuis les ressources.
     */
    private void loadSounds() {
        // Exemple de chargement (nécessite les fichiers audio)
        // loadSound("eat", "/sounds/eat.wav");
        // loadSound("death", "/sounds/death.wav");
        // loadSound("ghost_eat", "/sounds/ghost_eat.wav");
        // loadSound("power_up", "/sounds/power_up.wav");
    }
    
    /**
     * Charge un effet sonore.
     * 
     * @param name Nom de l'effet
     * @param path Chemin du fichier audio
     */
    private void loadSound(String name, String path) {
        try {
            URL resource = getClass().getResource(path);
            if (resource != null) {
                AudioClip clip = new AudioClip(resource.toString());
                soundEffects.put(name, clip);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du son " + name + ": " + e.getMessage());
        }
    }
    
    /**
     * Joue un effet sonore.
     * 
     * @param soundName Nom de l'effet à jouer
     */
    public void playSound(String soundName) {
        if (!soundEnabled) return;
        
        AudioClip clip = soundEffects.get(soundName);
        if (clip != null) {
            clip.setVolume(sfxVolume);
            clip.play();
        }
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
            
            URL resource = getClass().getResource(musicPath);
            if (resource != null) {
                Media media = new Media(resource.toString());
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
