
package game;

import core.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import util.Timer;

/**
 * Responsible for:
 * - Model and background loading
 * - Game flow (rounds, winner)
 * - Different screens (e.g. instructions screen)
 * - Camera switching
 */
public class GameRunner implements DrawCallback, KeyListener {
    // Specifies the various game screens
    private enum GameScreen {
        Instructions, InGame, End
    }

    private static final double secondsBetweenGames = 3; // time to wait after game ends before restart
    private static final int secondsBeforeGame = 3;
    private static final int PLAYERS = 4;
    private static final int ROUNDS = 10;

    private final Renderer renderer;
    private final Component canvas;
    private final Camera camera;
    private final ThirdPersonCamera thirdPersonCamera;
    private World world;
    private GameScreen gameScreen;
    private int lastWinner = -1;
    private int thisRoundWinner = -1;
    private Timer endRoundTimer; // for waiting after one player wins.
    private Timer startGameTimer; // for waiting before each round
    private int[] scores;
    private int totalRounds = 0;

    public GameRunner(Renderer renderer, Component canvas, Camera camera) {
        this.renderer = renderer;
        this.canvas = canvas;
        this.camera = camera;
        this.renderer.setDrawCallback(this);
        this.thirdPersonCamera = new ThirdPersonCamera(camera);
    }

    /**
     * Restart the game, zeros the score
     */
    public void restart() {
        scores = new int[PLAYERS];
        totalRounds = 0;

        SoundEngine.stopFinalWinner();
        startGameTimer = new Timer();
        startRound();
        world.pause();
    }

    /**
     * Starts a new round
     * Recreates the world and sets the camera
     */
    public void startRound() {
        world = new World(renderer);
        setNormalCamera();

        try {
            world.loadSurface("models/surface/surface_r.3ds");
            world.loadPillars("models/pillar/pillar.3ds");
            world.loadCrown("models/crown/crown.3ds");

            PlayerController player1 = world.loadPlayer("models/player/player_1.3ds", 1);
            PlayerController player2 = world.loadPlayer("models/player/player_2.3ds", 2);
            PlayerController player3 = world.loadPlayer("models/player/player_3.3ds", 3);
            PlayerController player4 = world.loadPlayer("models/player/player_4.3ds", 4);

            player1.getPlayer().setPosition(new Vector(-5, 0, 0));
            player1.faceDirection(new Vector(1, 0, 0));

            player2.getPlayer().setPosition(new Vector(0, 0, 5));
            player2.faceDirection(new Vector(0, 0, -1));

            player3.getPlayer().setPosition(new Vector(0, 0, -5));
            player3.faceDirection(new Vector(0, 0, 1));

            player4.getPlayer().setPosition(new Vector(5, 0, 0));
            player4.faceDirection(new Vector(-1, 0, 0));

            endRoundTimer = null;

            canvas.addKeyListener((KeyListener) player1);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        this.gameScreen = GameScreen.InGame;
        setBackgroundFromScreen();
        if (startGameTimer == null) // not first round
            SoundEngine.playStartRound();
    }

    /**
     * Determine what to draw, depending on the current screen
     * Update the world if necessary
     */
    public void drawObjects() {
        renderer.renderBackground();

        if (this.gameScreen == GameScreen.Instructions) {
            return;
        } else if (this.gameScreen == GameScreen.End) {
            this.world.showFinalWinner();
            return;
        }

        this.world.update();
        thirdPersonCamera.update();

        determineRoundWinner();
    }

    public World getWorld() {
        return this.world;
    }

    /**
     * Draw text on screen
     */
    public void drawText() {
        if (this.gameScreen == GameScreen.End) {
            String text = "The winner: player " + lastWinner;
            renderer.renderText(text, new Point(0, 0));
        } else if (this.gameScreen == GameScreen.InGame) {
            if (startGameTimer != null) {
                double secLeft = secondsBeforeGame - startGameTimer.elapsedSeconds() + 1;
                if (secLeft > 0.5) { // half sec of the go

                    Dimension size = canvas.getSize();
                    Point middleScreen = new Point(size.width / 2, size.height / 2 - 35);

                    String openingString;
                    if (secLeft > 1)
                        openingString = String.valueOf((int) Math.ceil(secLeft - 1));
                    else {
                        openingString = "Go!"; // half second for the Go
                        middleScreen.x -= 20;
                    }

                    renderer.renderText(openingString, middleScreen);
                } else {
                    startGameTimer = null;
                    SoundEngine.playStartRound();
                    world.run();
                }
            }
        }
    }

    /**
     * Determine if there is a winner in this round
     */
    private void determineRoundWinner() {
        int active = 0;
        Player winnerPlayer = null;
        for (PlayerController player : this.world.getPlayers()) {
            if (player.getPlayer().isOnSurface()) {
                active++;
                thisRoundWinner = player.getId();
                winnerPlayer = player.getPlayer();
            }
        }

        if (active <= 1) {
            if (active == 1) {
                world.setWinner(winnerPlayer);
            }

            if (endRoundTimer == null) {
                endRoundTimer = new Timer();
                endRoundTimer.restart();
                SoundEngine.playWinRound();
                scores[thisRoundWinner - 1] += 1;
                totalRounds++;

                if (totalRounds == ROUNDS) {
                    determineFinalWinner();
                }
            }

            if (endRoundTimer.elapsedSeconds() >= secondsBetweenGames) {
                lastWinner = thisRoundWinner;
                startRound();
            }
        }
    }

    /**
     * Determine a winner for all rounds
     */
    private void determineFinalWinner() {
        int max = Integer.MIN_VALUE;
        int id = -1;
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] > max) {
                max = scores[i];
                id = i + 1;
            }
        }

        if (id != -1) {
            lastWinner = id;
            world.setWinner(world.getPlayer(id).getPlayer());
            setNormalCamera();
            SoundEngine.playFinalWinner();
            gameScreen = GameScreen.End;
        }
    }

    /**
     * Activated by pressing '0'
     */
    private void setNormalCamera() {
        setFixedCamera(new Vector(0, 15, 16), 45);
    }

    /**
     * Activated by pressing '9'
     */
    private void setTopCamera() {
        setFixedCamera(new Vector(0, 20, 0), 90);
    }

    /**
     * Helper method to set a fixed camera
     */
    private void setFixedCamera(Vector position, double pitch) {
        thirdPersonCamera.setGameObject(null);
        camera.reset();
        camera.setPosition(position);
        camera.changePitch(pitch * Math.PI / 180.0);
    }

    /**
     * Activated by pressing '1'-'4'
     */
    private void setThirdPersonCamera(int id) {
        camera.reset();
        final PlayerController player = this.world.getPlayer(id);
        thirdPersonCamera.setGameObject(player.getPlayer());
    }

    /**
     * Set the background, depending on the current screen
     */
    private void setBackgroundFromScreen() {
        if (gameScreen == GameScreen.Instructions) {
            renderer.setBackgroundTexturePath("models/landscape/instructions.png");
        } else {
            renderer.setBackgroundTexturePath("models/landscape/ocean_marine.jpg");
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    /**
     * Switch cameras, move to a new screen or restart
     */
    public void keyPressed(KeyEvent e) {
        final int code = e.getKeyCode();

        switch (code) {
            case KeyEvent.VK_1:
            case KeyEvent.VK_2:
            case KeyEvent.VK_3:
            case KeyEvent.VK_4:
                final int playerId = code - KeyEvent.VK_1 + 1;
                setThirdPersonCamera(playerId);
                break;
            case KeyEvent.VK_0:
                setNormalCamera();
                break;
            case KeyEvent.VK_9:
                setTopCamera();
                break;
            case KeyEvent.VK_F1:
                if (gameScreen == GameScreen.InGame) {
                    gameScreen = GameScreen.Instructions;
                    this.world.pause();
                } else if (gameScreen == GameScreen.Instructions) {
                    gameScreen = GameScreen.InGame;
                    this.world.run();
                } else {
                    return;
                }

                setBackgroundFromScreen();
                break;
            case KeyEvent.VK_R:
                restart();
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
    }
}
