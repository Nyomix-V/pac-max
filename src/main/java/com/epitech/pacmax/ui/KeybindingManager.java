package com.epitech.pacmax.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.scene.input.KeyCode;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Map;

/**
 * Gère les assignations de touches (keybindings).
 * Permet de sauvegarder et charger les préférences du joueur.
 *
 * @author Epitech Team
 * @version 1.0
 */
public class KeybindingManager {
    private static KeybindingManager instance;
    private static final String KEYBINDING_FILE = "keybindings.json";
    private final Map<GameAction, KeyCode> keyMap;
    private final Gson gson;

    private KeybindingManager() {
        this.keyMap = new EnumMap<>(GameAction.class);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        loadKeybindings();
    }

    public static KeybindingManager getInstance() {
        if (instance == null) {
            instance = new KeybindingManager();
        }
        return instance;
    }

    private void setDefaultKeybindings() {
        keyMap.put(GameAction.MOVE_UP, KeyCode.W);
        keyMap.put(GameAction.MOVE_DOWN, KeyCode.S);
        keyMap.put(GameAction.MOVE_LEFT, KeyCode.A);
        keyMap.put(GameAction.MOVE_RIGHT, KeyCode.D);
        keyMap.put(GameAction.PAUSE, KeyCode.ESCAPE);
    }

    public KeyCode getKeyForAction(GameAction action) {
        return keyMap.get(action);
    }

    public void setKeyForAction(GameAction action, KeyCode keyCode) {
        // Vérifier si la touche est déjà utilisée et la libérer
        keyMap.entrySet().removeIf(entry -> entry.getValue() == keyCode);
        keyMap.put(action, keyCode);
        saveKeybindings();
    }

    public GameAction getActionForKey(KeyCode keyCode) {
        for (Map.Entry<GameAction, KeyCode> entry : keyMap.entrySet()) {
            if (entry.getValue() == keyCode) {
                return entry.getKey();
            }
        }
        // Gérer les touches alternatives non modifiables
        return switch (keyCode) {
            case UP -> GameAction.MOVE_UP;
            case DOWN -> GameAction.MOVE_DOWN;
            case LEFT -> GameAction.MOVE_LEFT;
            case RIGHT -> GameAction.MOVE_RIGHT;
            case P -> GameAction.PAUSE;
            default -> null;
        };
    }

    public Map<GameAction, KeyCode> getAllKeybindings() {
        return new EnumMap<>(keyMap);
    }

    private void loadKeybindings() {
        try (Reader reader = new FileReader(KEYBINDING_FILE)) {
            Type type = new TypeToken<EnumMap<GameAction, KeyCode>>() {}.getType();
            Map<GameAction, KeyCode> loadedMap = gson.fromJson(reader, type);
            if (loadedMap != null && !loadedMap.isEmpty()) {
                keyMap.putAll(loadedMap);
                System.out.println("✓ Assignations de touches chargées.");
            } else {
                setDefaultKeybindings();
            }
        } catch (Exception e) {
            System.out.println("⚠️ Fichier de keybindings non trouvé ou corrompu, utilisation des valeurs par défaut.");
            setDefaultKeybindings();
        }
    }

    private void saveKeybindings() {
        try (Writer writer = new FileWriter(KEYBINDING_FILE)) {
            gson.toJson(keyMap, writer);
            System.out.println("✓ Assignations de touches sauvegardées.");
        } catch (Exception e) {
            System.err.println("Erreur lors de la sauvegarde des keybindings: " + e.getMessage());
        }
    }
}