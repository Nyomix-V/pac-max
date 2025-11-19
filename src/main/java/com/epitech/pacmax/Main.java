package com.epitech.pacmax;

import com.epitech.pacmax.ui.GameUI;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe principale du jeu Pac-Max.
 * Point d'entrée de l'application JavaFX.
 * 
 * @author Epitech Team
 * @version 1.0
 */
public class Main extends Application {
    
    private GameUI gameUI;
    
    @Override
    public void start(Stage primaryStage) {
        // Créer l'interface utilisateur
        gameUI = new GameUI();
        
        // Créer la scène
        Scene scene = new Scene(gameUI, 1920, 1080);
        
        // Configurer la fenêtre
        primaryStage.setTitle("Pac-Max - T-JAV-501 Project");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
        
        // Demander le focus pour les événements clavier
        gameUI.requestFocus();
        
        // Boucle de rendu
        AnimationTimer renderLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gameUI.render();
            }
        };
        renderLoop.start();
    }
    
    @Override
    public void stop() {
        // Arrêter le moteur de jeu lors de la fermeture
        com.epitech.pacmax.engine.GameEngine.getInstance().stop();
    }
    
    /**
     * Point d'entrée principal de l'application.
     * 
     * @param args Arguments de ligne de commande
     */
    public static void main(String[] args) {
        launch(args);
    }
}
