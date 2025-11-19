package com.epitech.pacmax.levels;

import com.epitech.pacmax.entities.PacGum;
import com.epitech.pacmax.entities.Wall;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Charge les niveaux créés avec l'éditeur Tiled (.tmj).
 * Interprète les données du fichier JSON pour construire le niveau.
 *
 * @author Epitech Team
 * @version 1.1
 */
public class LevelLoader {

    // Constantes pour les GID (Global Tile ID) des tuiles dans Tiled
    // Ces valeurs doivent correspondre à celles définies dans votre tileset.
    private static final int GID_WALL = 2;
    private static final int GID_PACGUM = 1;
    private static final int GID_POWERUP = 5;
    private static final int GID_EMPTY = 0; // Espace vide

    /**
     * Charge un niveau depuis un fichier de ressource .tmj.
     *
     * @param levelPath Le chemin vers le fichier du niveau (ex: "/levels/level3.tmj").
     * @param walls     La liste des murs à remplir.
     * @param pacGums   La liste des pac-gums à remplir.
     */
    public static void load(String levelPath, List<Wall> walls, List<PacGum> pacGums) {
        Gson gson = new Gson();
        try (InputStream is = LevelLoader.class.getResourceAsStream(levelPath)) {
            if (is == null) {
                System.err.println("✗ Fichier de niveau non trouvé: " + levelPath);
                return;
            }

            InputStreamReader reader = new InputStreamReader(is);
            JsonObject mapData = gson.fromJson(reader, JsonObject.class);

            int tileWidth = mapData.get("tilewidth").getAsInt();
            int tileHeight = mapData.get("tileheight").getAsInt();
            int mapWidth = mapData.get("width").getAsInt();

            JsonArray layers = mapData.getAsJsonArray("layers");
            if (layers.isEmpty()) {
                System.err.println("✗ Aucune couche (layer) trouvée dans le fichier de niveau.");
                return;
            }
            // On suppose que la première couche contient les tuiles du jeu
            JsonObject tileLayer = layers.get(0).getAsJsonObject();
            JsonArray tileData = tileLayer.getAsJsonArray("data");
            
            int layerWidth = tileLayer.get("width").getAsInt();

            parseTileData(tileData, layerWidth, tileWidth, tileHeight, walls, pacGums);

            System.out.println("✓ Niveau '" + levelPath + "' chargé avec succès.");

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du niveau '" + levelPath + "': " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Analyse les données des tuiles et crée les entités correspondantes.
     *
     * @param tileData   Le tableau des GID des tuiles.
     * @param layerWidth La largeur de la couche en tuiles.
     * @param tileWidth  La largeur d'une tuile en pixels.
     * @param tileHeight La hauteur d'une tuile en pixels.
     * @param walls      La liste des murs à remplir.
     * @param pacGums    La liste des pac-gums à remplir.
     */
    private static void parseTileData(JsonArray tileData, int layerWidth, int tileWidth, int tileHeight, List<Wall> walls, List<PacGum> pacGums) {
        for (int i = 0; i < tileData.size(); i++) {
            // Les GID peuvent avoir des flags de rotation/flip, on les ignore pour l'instant
            // en ne gardant que les bits de l'ID de la tuile.
            long rawGid = tileData.get(i).getAsLong();
            int gid = (int) (rawGid & 0x1FFFFFFF); // Masque pour enlever les flags

            int x = (i % layerWidth) * tileWidth;
            int y = (i / layerWidth) * tileHeight;

            switch (gid) {
                case GID_WALL:
                    // Les GID > 1 peuvent aussi être des murs dans votre tileset
                case 3:
                case 4:
                case 6:
                case 7:
                    walls.add(new Wall(x, y, tileWidth, tileHeight));
                    break;

                case GID_PACGUM:
                    pacGums.add(new PacGum(x + tileWidth / 2.0 - 2, y + tileHeight / 2.0 - 2, PacGum.PacGumType.NORMAL));
                    break;

                case GID_POWERUP:
                    pacGums.add(new PacGum(x + tileWidth / 2.0 - 10, y + tileHeight / 2.0 - 10, PacGum.PacGumType.POWER_UP));
                    break;

                case GID_EMPTY:
                    // Espace vide, on ne fait rien
                    break;

                default:
                    // Si une tuile inconnue est trouvée, on peut l'ignorer ou afficher un avertissement.
                    // System.out.println("Tuile inconnue avec GID: " + gid + " à la position (" + x + "," + y + ")");
                    break;
            }
        }
    }
}