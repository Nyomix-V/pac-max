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
        // Supprimer le message d'aide lors du passage en plein écran
        primaryStage.setFullScreenExitHint("");
        primaryStage.setResizable(true);
        primaryStage.show();
        
        // Demander le focus pour les événements clavier
        gameUI.requestFocus();
        
        // Boucle de rendu
        com.epitech.pacmax.engine.GameEngine gameEngine = com.epitech.pacmax.engine.GameEngine.getInstance();
        final long[] lastFrameTime = {System.nanoTime()};

        AnimationTimer renderLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double deltaTime = (now - lastFrameTime[0]) / 1_000_000_000.0;
                lastFrameTime[0] = now;

                gameEngine.update(deltaTime); // Appel de la logique de jeu
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
