/**
 * Module principal du jeu Pac-Max.
 * Définit les dépendances et exports pour Java 17+.
 * 
 * @author Epitech Team
 * @version 1.0
 */
module com.epitech.pacmax {
    // Dépendances JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires transitive javafx.graphics;
    requires javafx.base;
    
    // Dépendance JSON
    requires com.google.gson;
    
    // Exports des packages principaux
    exports com.epitech.pacmax;
    exports com.epitech.pacmax.entities;
    exports com.epitech.pacmax.engine;
    exports com.epitech.pacmax.ai;
    exports com.epitech.pacmax.levels;
    exports com.epitech.pacmax.ui;
    exports com.epitech.pacmax.utils;
    
    // Ouvre les packages pour la réflexion (nécessaire pour JavaFX et GSON)
    opens com.epitech.pacmax to javafx.fxml;
    opens com.epitech.pacmax.ui to javafx.fxml;
    opens com.epitech.pacmax.utils to com.google.gson;
}
