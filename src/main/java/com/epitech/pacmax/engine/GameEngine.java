package com.epitech.pacmax.engine;

import com.epitech.pacmax.entities.*;
import com.epitech.pacmax.levels.GameMap;
import com.epitech.pacmax.levels.LevelLoader;
import com.epitech.pacmax.utils.CollisionManager;
import com.epitech.pacmax.utils.SoundManager;
import com.epitech.pacmax.utils.ProximitySound;
import javafx.animation.AnimationTimer;

import java.util.ArrayList;
import java.util.List;

/**
 * Moteur principal du jeu.
 * Implémente le Pattern Singleton pour garantir une seule instance.
 * Gère la boucle de jeu, les mises à jour et les collisions.
 * 
 * @author Epitech Team
 * @version 1.0
 */
public class GameEngine {
    
    /** Instance unique du moteur (Pattern Singleton) */
    private static GameEngine instance;
    
    /** État actuel du jeu */
    private GameState currentState;
    
    /** Joueur */
    private Player player;
    
    /** Liste des fantômes */
    private List<Ghost> ghosts;
    
    /** Liste des pac-gums */
    private List<PacGum> pacGums;
    
    /** Liste des murs */
    private List<Wall> walls;
    
    /** Liste des portails */
    private List<Portal> portals;
    
    /** Point de spawn unique pour tous les fantômes */
    private double ghostSpawnX;
    private double ghostSpawnY;
    
    /** Délai entre la sortie de chaque fantôme (en secondes) */
    private static final double GHOST_RELEASE_DELAY = 8.0;
    
    /** Carte actuelle */
    private GameMap currentMap;
    
    /** Gestionnaire de collisions */
    private CollisionManager collisionManager;
    
    /** Gestionnaire de sons */
    private SoundManager soundManager;
    
    /** Gestionnaire de sons contextuels (proximité) */
    private ProximitySound proximitySound;
    
    /** Timer d'animation JavaFX */
    private AnimationTimer gameLoop;
    
    /** Temps de la dernière frame */
    private long lastFrameTime;
    
    /** Niveau actuel */
    private int currentLevel;
    
    /** Nombre total de pac-gums au début du niveau */
    private int totalPacGums;
    
    /** Option sélectionnée dans le menu de pause */
    private int selectedPauseMenuOption = 0;
    
    /** Volume principal du jeu (0.0 à 1.0) */
    private double masterVolume = 0.7;
    
    /** Option sélectionnée dans le menu des keybindings */
    private int selectedKeybindingOption = 0;
    
    /** Indique si le jeu attend une nouvelle touche pour un keybinding */
    private boolean isWaitingForKey = false;
    
    /** Indique si le jeu est en mode plein écran */
    private boolean isFullscreen = false;
    
    /** Option sélectionnée dans le menu de victoire */
    private int selectedVictoryMenuOption = 0;
    
    /**
     * Constructeur privé (Pattern Singleton).
     */
    private GameEngine() {
        this.currentState = GameState.MENU;
        this.ghosts = new ArrayList<>();
        this.pacGums = new ArrayList<>();
        this.walls = new ArrayList<>();
        this.portals = new ArrayList<>();
        this.ghostSpawnX = 1920 / 2.0; // Centre de la nouvelle map
        this.ghostSpawnY = 1080 / 2.0;
        this.collisionManager = new CollisionManager();
        this.currentLevel = 1;
        this.lastFrameTime = System.nanoTime();
    }
    
    /**
     * Récupère l'instance unique du moteur (Pattern Singleton).
     * 
     * @return L'instance du GameEngine
     */
    public static GameEngine getInstance() {
        if (instance == null) {
            instance = new GameEngine();
        }
        return instance;
    }
    
    /**
     * Initialise le jeu avec une nouvelle partie.
     */
    public void initialize() {
        // Réinitialiser le niveau au tout début
        this.currentLevel = 1;

        // Initialiser le gestionnaire de sons (charge tous les sons)
        soundManager = SoundManager.getInstance();
        System.out.println("\n🔊 Initialisation du système audio...");
        
        // Créer le joueur (position différente du spawn des fantômes)
        player = new Player(100, 1080 - 200); // Position de départ ajustée pour être dans la zone de jeu
        
        // Charger la carte (pour l'instant, une carte simple)
        loadLevel(currentLevel);
        
        // Initialiser le système de sons contextuels (proximité)
        proximitySound = new ProximitySound(1920, 1080); // Nouvelle taille de la map
        
        // Jouer le son de démarrage
        System.out.println("🎵 Lecture du son de démarrage...");
        soundManager.playSound("start");
        
        // État d'attente : le joueur est invisible jusqu'au premier mouvement
        currentState = GameState.WAITING_TO_START;
    }
    
    /**
     * Charge un niveau.
     * 
     * @param level Numéro du niveau à charger
     */
    private void loadLevel(int level) {
        ghosts.clear();
        pacGums.clear();
        walls.clear();
        portals.clear();

        // Génération procédurale pour tous les niveaux
        createBorderWalls();
        addLevelObstacles(level);
        createPortals();
        createPacGums();

        // Créer les fantômes avec spawn unique
        createGhosts();
        
        totalPacGums = pacGums.size();
    }
    
    /**
     * Crée les murs de bordure du niveau.
     */
    private void createBorderWalls() {
        int tileSize = 40; // Tuiles un peu plus grandes pour la grande résolution
        int mapWidth = 1920;
        int mapHeight = 1080;
        
        // Murs horizontaux
        for (int x = 0; x < mapWidth; x += tileSize) {
            walls.add(new Wall(x, 0, tileSize, tileSize)); // Haut
            walls.add(new Wall(x, mapHeight - tileSize * 3, tileSize, tileSize)); // Bas (laisse de la place pour le HUD)
        }
        
        // Murs verticaux
        for (int y = tileSize; y < mapHeight - tileSize * 3; y += tileSize) {
            walls.add(new Wall(0, y, tileSize, tileSize)); // Gauche
            walls.add(new Wall(mapWidth - tileSize, y, tileSize, tileSize)); // Droite
        }
        
        // Quelques murs intérieurs pour créer un labyrinthe simple
        for (int x = 100; x < 300; x += tileSize) {
            walls.add(new Wall(x, 100, tileSize, tileSize));
        }
        for (int x = 500; x < 700; x += tileSize) {
            walls.add(new Wall(x, 100, tileSize, tileSize));
        }
    }
    
    /**
     * Crée les portails de téléportation aux extrémités gauche et droite de la map.
     */
    private void createPortals() {
        int mapHeight = 1080;
        int portalHeight = 120;
        int portalWidth = 20;
        
        // Portail gauche (au milieu du bord gauche, légèrement à l'intérieur)
        Portal leftPortal = new Portal(45, (mapHeight / 2.0) - (portalHeight / 2.0), portalWidth, portalHeight);
        
        // Portail droit (au milieu du bord droit, légèrement à l'intérieur)
        Portal rightPortal = new Portal(1920 - portalWidth - 45, (mapHeight / 2.0) - (portalHeight / 2.0), portalWidth, portalHeight);
        
        // Lier les portails entre eux
        leftPortal.linkTo(rightPortal);
        rightPortal.linkTo(leftPortal);
        
        portals.add(leftPortal);
        portals.add(rightPortal);
    }
    
    /**
     * Ajoute des obstacles supplémentaires selon le niveau.
     * Plus le niveau est élevé, plus il y a d'obstacles (labyrinthe).
     * 
     * @param level Le niveau actuel
     */
    private void addLevelObstacles(int level) {
        int tileSize = 40;
        
        // Niveau 2+ : ajouter des murs verticaux
        if (level >= 2) {
            for (int y = 300; y < 780; y += tileSize) {
                walls.add(new Wall(400, y, tileSize, tileSize));
                walls.add(new Wall(1520, y, tileSize, tileSize));
            }
        }
        
        // Niveau 3+ : ajouter plus de complexité au labyrinthe
        if (level >= 3) {
            for (int x = 600; x < 1320; x += tileSize) {
                walls.add(new Wall(x, 520, tileSize, tileSize));
            }
            for (int y = 200; y < 400; y += tileSize) {
                walls.add(new Wall(960, y, tileSize, tileSize));
            }
        }
        
        // Niveau 4+ : encore plus d'obstacles
        if (level >= 4) { // Cette logique est conservée si vous ajoutez des niveaux plus tard
            for (int x = 300; x < 500; x += tileSize) {
                walls.add(new Wall(x, 800, tileSize, tileSize));
            }
            for (int x = 1420; x < 1620; x += tileSize) {
                walls.add(new Wall(x, 300, tileSize, tileSize));
            }
        }
    }
    
    /**
     * Crée les pac-gums dans le niveau.
     */
    private void createPacGums() {
        int spacing = 100; // Augmentation de l'espacement pour réduire le nombre de pac-gums
        
        // Créer une grille de pac-gums
        for (int x = 80; x < 1920 - 80; x += spacing) {
            for (int y = 80; y < 1080 - 120; y += spacing) {
                // Vérifier qu'il n'y a pas de mur ou de portail à cet endroit
                boolean hasObstacle = false;
                
                // Vérifier les murs
                for (Wall wall : walls) {
                    if (Math.abs(wall.getX() - x) < spacing && Math.abs(wall.getY() - y) < spacing) {
                        hasObstacle = true;
                        break;
                    }
                }
                
                // Vérifier les portails
                if (!hasObstacle) {
                    for (Portal portal : portals) {
                        if (x >= portal.getX() - spacing && x <= portal.getX() + portal.getWidth() + spacing &&
                            y >= portal.getY() - spacing && y <= portal.getY() + portal.getHeight() + spacing) {
                            hasObstacle = true;
                            break;
                        }
                    }
                }
                
                if (!hasObstacle) {
                    // 5% de chance d'être un power-up
                    PacGum.PacGumType type = Math.random() < 0.05 ? 
                        PacGum.PacGumType.POWER_UP : PacGum.PacGumType.NORMAL;
                    pacGums.add(new PacGum(x, y, type));
                }
            }
        }
    }
    
    /**
     * Crée les fantômes avec spawn unique et sortie séquentielle.
     * Tous partent du même point et sortent avec un délai de 8 secondes.
     */
    private void createGhosts() {
        // Tous les fantômes utilisent la nouvelle IA aléatoire avec poursuite conditionnelle
        Ghost ghost1 = new Ghost(ghostSpawnX, ghostSpawnY, Ghost.GhostType.BLINKY, 
            new com.epitech.pacmax.ai.ChaseRandomAI());
        ghost1.setReleaseTimer(0); // Sort immédiatement
        ghosts.add(ghost1);
        
        Ghost ghost2 = new Ghost(ghostSpawnX, ghostSpawnY, Ghost.GhostType.PINKY, 
            new com.epitech.pacmax.ai.ChaseRandomAI());
        ghost2.setReleaseTimer(GHOST_RELEASE_DELAY); // Sort après 8 secondes
        ghosts.add(ghost2);
        
        Ghost ghost3 = new Ghost(ghostSpawnX, ghostSpawnY, Ghost.GhostType.INKY, 
            new com.epitech.pacmax.ai.ChaseRandomAI());
        ghost3.setReleaseTimer(GHOST_RELEASE_DELAY * 2); // Sort après 16 secondes
        ghosts.add(ghost3);
        
        Ghost ghost4 = new Ghost(ghostSpawnX, ghostSpawnY, Ghost.GhostType.CLYDE, 
            new com.epitech.pacmax.ai.ChaseRandomAI());
        ghost4.setReleaseTimer(GHOST_RELEASE_DELAY * 3); // Sort après 24 secondes
        ghosts.add(ghost4);
        
        // Augmenter la vitesse des fantômes selon le niveau
        double speedMultiplier = 1.0 + (currentLevel - 1) * 0.15; // +15% par niveau
        for (Ghost ghost : ghosts) {
            ghost.setNormalSpeed(80.0 * speedMultiplier);
        }
    }
    
    /**
     * Démarre la boucle de jeu.
     */
    public void start() {
        // La boucle de jeu est maintenant gérée par la classe Main pour synchroniser update() et render().
    }
    
    /**
     * Arrête la boucle de jeu.
     */
    public void stop() {
        // La boucle de jeu est gérée par Main, donc cette méthode peut rester vide ou être supprimée.
    }
    
    /**
     * Met à jour le jeu à chaque frame.
     * 
     * @param deltaTime Temps écoulé depuis la dernière frame
     */
    public void update(double deltaTime) {
        // Limiter le deltaTime pour éviter les gros sauts
        if (deltaTime > 0.1) deltaTime = 0.1;
        
        // En mode WAITING_TO_START : attendre que le joueur bouge
        if (currentState == GameState.WAITING_TO_START) {
            // Mettre à jour le joueur pour qu'il puisse bouger
            player.update(deltaTime);
            
            // Vérifier si le joueur a bougé
            if (player.getDirection() != Direction.NONE) {
                currentState = GameState.RUNNING;
                System.out.println("🎮 Partie démarrée !");
            }
            // Ne pas mettre à jour les fantômes en mode attente
            return;
        }
        
        // Le jeu s'arrête seulement pour ces états
        if (currentState == GameState.PAUSED || currentState == GameState.PAUSE_MENU || currentState == GameState.OPTIONS_MENU || currentState == GameState.KEYBINDING_MENU || currentState == GameState.MENU || currentState == GameState.GAME_OVER || currentState == GameState.VICTORY || currentState == GameState.LEVEL_COMPLETE) {
            return;
        }
        
        // Mettre à jour le joueur
        player.update(deltaTime);
        
        // Mettre à jour les portails
        for (Portal portal : portals) {
            portal.update(deltaTime);
        }
        
        // Mettre à jour les fantômes
        for (Ghost ghost : ghosts) {
            // Mettre à jour la position du joueur et le statut powerup pour l'IA
            if (ghost.getAI() != null) {
                ghost.getAI().setPlayerPosition(player.getCenterX(), player.getCenterY());
                // Informer l'IA si le joueur est sous powerup
                if (ghost.getAI() instanceof com.epitech.pacmax.ai.ChaseRandomAI) {
                    ((com.epitech.pacmax.ai.ChaseRandomAI) ghost.getAI()).setPlayerPowered(player.isPowered());
                }
            }
            ghost.update(deltaTime);
        }
        
        // Mettre à jour les sons contextuels (proximité)
        if (proximitySound != null) {
            proximitySound.update(player, ghosts);
        }
        
        // Vérifier les collisions
        checkCollisions();
        
        // Vérifier les conditions de victoire/défaite
        checkGameConditions();
    }
    
    /**
     * Vérifie toutes les collisions.
     */
    private void checkCollisions() {
        // Collision joueur - murs
        for (Wall wall : walls) {
            if (collisionManager.checkCollision(player, wall)) {
                collisionManager.resolveWallCollision(player, wall);
            }
        }
        
        // Collision fantômes - murs
        for (Ghost ghost : ghosts) {
            for (Wall wall : walls) {
                if (collisionManager.checkCollision(ghost, wall)) {
                    collisionManager.resolveWallCollision(ghost, wall);
                }
            }
        }
        
        // Collision joueur - portails
        for (Portal portal : portals) {
            if (collisionManager.checkCollision(player, portal)) {
                portal.teleport(player);
            }
        }
        
        // Collision fantômes - portails
        for (Ghost ghost : ghosts) {
            for (Portal portal : portals) {
                if (collisionManager.checkCollision(ghost, portal)) {
                    portal.teleport(ghost);
                }
            }
        }
        
        // Collision joueur - pac-gums
        for (PacGum pacGum : pacGums) {
            if (!pacGum.isCollected() && collisionManager.checkCollision(player, pacGum)) {
                pacGum.collect();
                player.addScore(pacGum.getPoints());
                
                // Activer le power-up si c'est un power-up
                if (pacGum.getType() == PacGum.PacGumType.POWER_UP) {
                    player.activatePowerUp();
                    // Mettre tous les fantômes en mode scared
                    for (Ghost ghost : ghosts) {
                        ghost.setState(Ghost.GhostState.SCARED);
                    }
                }
            }
        }
        
        // Collision joueur - fantômes
        for (Ghost ghost : ghosts) {
            // Ignorer les fantômes en attente (WAITING) ou morts (DEAD)
            if (ghost.getState() == Ghost.GhostState.WAITING || ghost.getState() == Ghost.GhostState.DEAD) {
                continue;
            }
            
            if (collisionManager.checkCollision(player, ghost)) {
                if (ghost.getState() == Ghost.GhostState.SCARED) {
                    // Le joueur mange le fantôme
                    ghost.setState(Ghost.GhostState.DEAD);
                    ghost.respawn();
                    player.addScore(200);
                    
                    // Jouer le son "ça va bien se passer Chaima"
                    soundManager.playSound("chase");
                } else if (ghost.getState() != Ghost.GhostState.DEAD) {
                    // Le fantôme touche le joueur
                    player.loseLife();
                    
                    // Jouer le son "vous avez signé"
                    soundManager.playSound("death");
                    
                    if (player.getLives() > 0) {
                        resetPositions();
                    } else {
                        currentState = GameState.GAME_OVER;
                        
                        // Jouer le son de fin de jeu en PRIORITÉ (arrête tous les autres sons)
                        soundManager.playSoundPriority("gameover");
                    }
                }
            }
        }
    }
    
    /**
     * Vérifie les conditions de victoire/défaite.
     */
    private void checkGameConditions() {
        // Compter les pac-gums restantes
        long remainingPacGums = pacGums.stream()
            .filter(p -> !p.isCollected())
            .count();
        
        if (currentState == GameState.RUNNING) { // Pour ne pas vérifier si on est déjà dans un autre état
            double percentageRemaining = (double) remainingPacGums / totalPacGums;

            if (totalPacGums > 0 && percentageRemaining <= 0.80) { // 0.20 = 20% restants (80% mangées)
                // Si le niveau 3 (ou plus) est terminé, le jeu est gagné.
                if (currentLevel >= 3) {
                    setState(GameState.VICTORY);
                    System.out.println("🏆 Victoire ! Vous avez terminé tous les niveaux !");
                    soundManager.playSoundPriority("victory");
                } else {
                    // Sinon, le niveau est "terminable", on passe à l'état LEVEL_CLEARED
                    setState(GameState.LEVEL_CLEARED);
                    System.out.println("🎉 Niveau " + currentLevel + " terminé ! Prêt pour le suivant.");
                }
            }
        }
    }
    
    /**
     * Fait passer le jeu au niveau suivant.
     * Appelé par l'InputHandler lorsque le joueur appuie sur la touche.
     */
    public void proceedToNextLevel() {
        if (currentState == GameState.LEVEL_CLEARED) {
            currentLevel++;
            loadLevel(currentLevel);
            resetPositions();
            setState(GameState.RUNNING);
        }
    }
    
    /**
     * Réinitialise les positions après une mort.
     */
    private void resetPositions() {
        player.reset(100, 1080 - 200); // Position de départ ajustée pour être dans la zone de jeu
        
        // Réinitialiser les fantômes avec leurs délais de sortie
        for (int i = 0; i < ghosts.size(); i++) {
            Ghost ghost = ghosts.get(i);
            ghost.respawn();
            ghost.setReleaseTimer(i * GHOST_RELEASE_DELAY); // Délai séquentiel
        }
    }
    
    /**
     * Change l'état du jeu.
     * 
     * @param newState Le nouvel état
     */
    public void setState(GameState newState) {
        this.currentState = newState;
    }
    
    // Getters
    
    public GameState getCurrentState() {
        return currentState;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public List<Ghost> getGhosts() {
        return ghosts;
    }
    
    public List<PacGum> getPacGums() {
        return pacGums;
    }
    
    public List<Wall> getWalls() {
        return walls;
    }
    
    public List<Portal> getPortals() {
        return portals;
    }
    
    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getSelectedPauseMenuOption() {
        return selectedPauseMenuOption;
    }

    public void setSelectedPauseMenuOption(int selectedPauseMenuOption) {
        this.selectedPauseMenuOption = selectedPauseMenuOption;
    }

    public double getMasterVolume() {
        return masterVolume;
    }

    public void setMasterVolume(double volume) {
        // Assurer que le volume reste entre 0.0 et 1.0
        this.masterVolume = Math.max(0.0, Math.min(1.0, volume));
        // Appliquer le volume au SoundManager
        if (soundManager != null) {
            soundManager.setMasterVolume(this.masterVolume);
        }
    }

    public int getSelectedKeybindingOption() {
        return selectedKeybindingOption;
    }

    public void setSelectedKeybindingOption(int selectedKeybindingOption) {
        this.selectedKeybindingOption = selectedKeybindingOption;
    }

    public boolean isWaitingForKey() {
        return isWaitingForKey;
    }

    public void setWaitingForKey(boolean waitingForKey) {
        isWaitingForKey = waitingForKey;
    }

    public boolean isFullscreen() {
        return isFullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.isFullscreen = fullscreen;
    }

    public int getSelectedVictoryMenuOption() {
        return selectedVictoryMenuOption;
    }

    public void setSelectedVictoryMenuOption(int selectedVictoryMenuOption) {
        this.selectedVictoryMenuOption = selectedVictoryMenuOption;
    }
}
