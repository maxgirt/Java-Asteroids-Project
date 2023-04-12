package asteroids.Controllers;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.paint.*;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.canvas.*;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.Collectors;

import asteroids.*;

public class AsteroidsController implements GameListener {
    public static final int CANVASWIDTH = 800, CANVASHEIGHT = 600;
    private Timer timer = new Timer();
    private Game game;
    private GraphicsContext gc;
    private boolean UP = false, LEFT = false,
            RIGHT = false, SPACEpressed = false, SPACEreleased = true, difficulty;
    private Media collisionSound, laserSoundTrack, hardSoundTrack, normalSoundTrack;
    private MediaPlayer collisionSoundPlayer, laserSoundTrackPlayer, hardSoundTrackPlayer, normalSoundTrackPlayer,
            currentSoundTrackPlayer;
    private ScoreBoard scoreBoard;
    private MenuController menuController = new MenuController();

    @FXML
    private Canvas canvas = new Canvas(CANVASWIDTH, CANVASHEIGHT);

    @FXML
    private Text currentScore, livesLeft;

    @FXML
    private BorderPane menuContainer;

    @FXML
    private ListView<String> scoreBoardList;

    private Pane newGamePane, settingsPane, controlsPane, aboutPane, audioPane, difficultyPane, welcomePane;

    // @FXML
    // private SettingsController settingsWindowController;

    // initializes the game
    public void initialize() {
        // load sound and music
        try {
            collisionSound = new Media(
                    getClass().getClassLoader().getResource("asteroids/audio/boom.mp3").toURI()
                            .toString());
            hardSoundTrack = new Media(
                    getClass().getClassLoader().getResource("asteroids/audio/ripAndTear.mp3").toURI()
                            .toString());
            normalSoundTrack = new Media(
                    getClass().getClassLoader().getResource("asteroids/audio/soundTrack.mp3").toURI()
                            .toString());
            laserSoundTrack = new Media(
                    getClass().getClassLoader().getResource("asteroids/audio/laser.mp3").toURI()
                            .toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        collisionSoundPlayer = new MediaPlayer(collisionSound);
        hardSoundTrackPlayer = new MediaPlayer(hardSoundTrack);
        normalSoundTrackPlayer = new MediaPlayer(normalSoundTrack);
        laserSoundTrackPlayer = new MediaPlayer(laserSoundTrack);
        laserSoundTrackPlayer.setVolume(0.5);

        scoreBoard = new ScoreBoard("saves", "score_saves");

        // loads scoreboard from file and updates view
        updateScoreBoard();

        // start rendering
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, CANVASWIDTH, CANVASHEIGHT);

        // Initalize all menu views
        newGamePane = getMenuPane("/asteroids/FXMLs/NewGameFx.fxml");
        settingsPane = getMenuPane("/asteroids/FXMLs/SettingsFx.fxml");
        controlsPane = getMenuPane("/asteroids/FXMLs/ControlsFx.fxml");
        aboutPane = getMenuPane("/asteroids/FXMLs/AboutFx.fxml");
        audioPane = getMenuPane("/asteroids/FXMLs/AudioFx.fxml");
        difficultyPane = getMenuPane("/asteroids/FXMLs/DifficultyFx.fxml");
        welcomePane = getMenuPane("/asteroids/FXMLs/WelcomeFx.fxml");
        menuController.init(this);

        // Propt user with welcome screen
        changeMenu("NewGameFx.fxml");

        // Alternativly prompt user with welcome screen if it is first time opening the
        // game
        menuController.firstTimePlaying();

    }

    // AnimationTimer runs once every frame
    private class Timer extends AnimationTimer {

        @Override
        public void handle(long nanotime) {
            gc.fillRect(0, 0, CANVASWIDTH, CANVASHEIGHT);

            game.gameLoop(nanotime);

            // renders all the objects on screen
            game.getSprites().stream().forEach((sprite) -> {
                renderSprite(sprite);
            });

            // controls spaceship actions
            spaceshipAction(game.getSpaceship());
        }
    };

    private void renderSprite(Sprite sprite) {
        // Save the current graphics context state
        gc.save();

        // Translate the graphics context to the position of the sprite
        gc.translate(sprite.getPosX(), sprite.getPosY());

        // If the sprite is a Spaceship, rotate the graphics context based on the spaceship's rotation
        if (sprite instanceof Spaceship) {
            // Translate to the center of the image (for rotation around the center point)
            gc.translate(sprite.getImageWidth() / 2, sprite.getImageHeight() / 2);

            // Rotate the graphics context by the spaceship's rotation (in degrees)
            gc.rotate(Math.toDegrees(((Spaceship) sprite).getRotation()));

            // Translate back to the top-left corner of the image
            gc.translate(-sprite.getImageWidth() / 2, -sprite.getImageHeight() / 2);
        }

        // Draw the sprite's image at the (0, 0) position of the translated and rotated graphics context
        gc.drawImage(new Image(sprite.getImageURL()), 0, 0);

        // Restore the graphics context state to the state before the translations and rotations
        gc.restore();
    }


    private void updateScoreBoard() {
        scoreBoardList.setItems(scoreBoard.getScores().stream()
                .limit(18)
                .map(element -> scoreBoard.getScores().indexOf(element) + 1 + ". " + element.getKey() + ": "
                        + element.getValue())
                .collect(Collectors.toCollection(FXCollections::observableArrayList)));
    }

    public void addScore(String playerName) {
        scoreBoard.addScore(playerName, game.getScore());
        updateScoreBoard();
    }

    private void initiateSoundTrack(MediaPlayer soundTrack) {
        if (currentSoundTrackPlayer != null)
            currentSoundTrackPlayer.stop();
        currentSoundTrackPlayer = soundTrack;
        currentSoundTrackPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                soundTrack.seek(Duration.ZERO);
            }
        });
        currentSoundTrackPlayer.play();
    }

    public void startNewGame() {
        game = new Game(this, difficulty);
        scoreBoardList.requestFocus();

        // starts AnimationTimer
        timer.start();

    }

    private void spaceshipAction(Spaceship spaceship) {
        if (UP)
            spaceship.thrust();
        if (LEFT)
            spaceship.rotateLeft();
        if (RIGHT)
            spaceship.rotateRight();
        if (SPACEpressed && SPACEreleased && game.doesGameContainSpaceship()) {
            laserSoundTrackPlayer.play();
            laserSoundTrackPlayer.seek(Duration.ZERO);
            game.getSprites().add(spaceship.shoot());
            SPACEreleased = false;
        }
    }

    public void setFXVolume(double FXVolume) {
        collisionSoundPlayer.setVolume(FXVolume);
        laserSoundTrackPlayer.setVolume(FXVolume / 2);
    }

    public void setMusicVolume(double musicVolume) {
        hardSoundTrackPlayer.setVolume(musicVolume);
        normalSoundTrackPlayer.setVolume(musicVolume);
    }

    public void updateDifficulty(Boolean difficulty) {
        this.difficulty = difficulty;
        if (difficulty)
            initiateSoundTrack(hardSoundTrackPlayer);
        else
            initiateSoundTrack(normalSoundTrackPlayer);
    }

    @FXML
    private void keyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case UP -> UP = true;
            case W -> UP = true;
            case LEFT -> LEFT = true;
            case A -> LEFT = true;
            case RIGHT -> RIGHT = true;
            case D -> RIGHT = true;
            case SPACE -> SPACEpressed = true;
            default -> {
            }
        }
    }

    @FXML
    private void keyReleased(KeyEvent event) {
        switch (event.getCode()) {
            case UP -> UP = false;
            case W -> UP = false;
            case LEFT -> LEFT = false;
            case A -> LEFT = false;
            case RIGHT -> RIGHT = false;
            case D -> RIGHT = false;
            case SPACE -> {
                SPACEpressed = false;
                SPACEreleased = true;
            }
            default -> {
            }
        }
    }

    @Override
    public void livesLeftChanged(int livesLeft) {
        this.livesLeft.setText(livesLeft + " lives left");

    }

    @Override
    public void gameOver() {
        UP = false;
        LEFT = false;
        RIGHT = false;

        menuController.gameOver(game.getScore(), scoreBoard.getHighScore());
    }

    @Override
    public void spirteCollided() {
        collisionSoundPlayer.play();
        collisionSoundPlayer.seek(Duration.ZERO);
    }

    @Override
    public void scoreChanged(int newScore) {
        currentScore.setText("Score: " + newScore);
    }

    public void changeMenu(String s) {
        switch (s) {
            case "NewGameFx.fxml" -> menuContainer.setCenter(newGamePane);
            case "SettingsFx.fxml" -> menuContainer.setCenter(settingsPane);
            case "ControlsFx.fxml" -> menuContainer.setCenter(controlsPane);
            case "AboutFx.fxml" -> menuContainer.setCenter(aboutPane);
            case "AudioFx.fxml" -> menuContainer.setCenter(audioPane);
            case "DifficultyFx.fxml" -> menuContainer.setCenter(difficultyPane);
            case "WelcomeFx.fxml" -> menuContainer.setCenter(welcomePane);
            default -> menuContainer.setCenter(null);
        }
    }

    public Pane getMenuPane(String s) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(s));
        fxmlLoader.setController(menuController);
        try {
            return fxmlLoader.load();
        } catch (IOException e) {
            System.out.println(e.toString());
            return null;
        }
    }
}
