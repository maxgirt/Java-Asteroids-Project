package Asteroids2;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.input.KeyCode;


import java.util.*;





import javafx.scene.text.Font;

import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;

import static Asteroids2.Asteroid.Size.*;
import static Asteroids2.Asteroids.PANE_HEIGHT;
import static Asteroids2.Asteroids.PANE_WIDTH;


public class Level {
    private int currentLevel;
    private GameText gameText;
    private Font font;
    private Font fontSmall;

    private List<Asteroid> asteroids;
    private Ship ship;
    private Map<KeyCode, Boolean> pressedKeys;
    	public boolean levelStarted = false;
	public boolean gameInProgress = false;
	public boolean alienFlag = false;
	public boolean canShoot = true;
    public boolean gameOver = false;
    private List<Bullet> bullets;

    public Level(GameText gameText, Font font, Font fontSmall,List<Asteroid> asteroids, Ship ship,
                 Map<KeyCode, Boolean> pressedKeys,List<Bullet> bullets) {
        this.currentLevel = 0;
        this.gameText = gameText;
        this.font = font;
        this.fontSmall = fontSmall;
        this.gameOver = gameOver;
        this.asteroids = asteroids;
        this.ship = ship;
        this.pressedKeys = pressedKeys;
        this.alienFlag = alienFlag;
        this.canShoot = canShoot;
        this.gameInProgress = gameInProgress;
        this.bullets = bullets;
    }

    public void setGameOver(boolean canShoot) {
        this.gameOver = gameOver;
    }
    public void setAlienFlag(boolean alienFlag) {
        this.alienFlag = alienFlag;
    }

    public void setCanShoot(boolean canShoot) {
        this.canShoot = canShoot;
    }

    public void setGameInProgress(boolean gameInProgress) {
        this.gameInProgress = gameInProgress;
    }

    public void setLevelStarted(boolean levelStarted) {
        this.levelStarted = levelStarted;
    }

    public void incrementLevel() {
        this.currentLevel += 1;
        gameText.updateLevelText(currentLevel);
    }
    public boolean setGameOver() {
        return this.gameOver = gameOver;
    }

    public boolean getAlienFlag() {
        return this.alienFlag;
    }

    public boolean getCanShoot() {
        return this.canShoot;
    }

    public boolean getGameInProgress() {
        return this.gameInProgress;
    }

    public boolean getLevelStarted() {
        return this.levelStarted;
    }

//    public int getCurrentLevel() {
//        return this.currentLevel;
//    }

    public void levelScreen(Random rnd, Pane pane) {
        clearBullets(pane);
        Button continueButton = new Button("Start Game");
        continueButton.setPrefSize(120, 40); // Set preferred width and height
        continueButton.setFont(font);

        Text levelText = new Text("Level: " + currentLevel);
        levelText.setFont(fontSmall);
        levelText.setFill(Color.WHITESMOKE);

        levelText.setLayoutX(PANE_WIDTH/2 - 42);
        levelText.setLayoutY(PANE_HEIGHT/2 - 75);
        pane.getChildren().add(levelText);

        // Set the button's position after it is added to the scene graph
        continueButton.setLayoutX(PANE_WIDTH/2 - 60);
        continueButton.setLayoutY(PANE_HEIGHT/2 - 60);
        pane.getChildren().add(continueButton);

        System.out.println("levelScreen Pane: " + pane.hashCode());
        continueButton.setOnMouseClicked(e -> {
            try {
                incrementLevel();
                System.out.println("Button clicked!");
                while (!gameOver) {
                    for (int i = 0; i < currentLevel; i++) {
                        Asteroid asteroid = new Asteroid(rnd.nextInt(PANE_WIDTH), rnd.nextInt(PANE_HEIGHT), LARGE);
                        asteroid.getCharacter().setStroke(Color.WHITESMOKE);



                        // Set the stroke type of the asteroid to CENTERED
                        asteroid.getCharacter().setStrokeType(StrokeType.CENTERED);

                        // Set the stroke width of the asteroid to the desired thickness
                        asteroid.getCharacter().setStrokeWidth(1);

                        asteroids.add(asteroid);
                        pane.getChildren().add(asteroid.getCharacter());
                    } break;}
                ship.resetPosition(150,100);
                pressedKeys.clear();
                pane.getChildren().remove(continueButton);
                pane.getChildren().remove(levelText);
                alienFlag = false;
                levelStarted = false;
                canShoot = true;
                gameInProgress = true;
                int jumpX = rnd.nextInt(600);
                int jumpY = rnd.nextInt(400);
                Ship dummyShip = new Ship(jumpX, jumpY);
                ship.hyperspaceJump(dummyShip);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }


    public int getCurrentLevel() {
        return currentLevel;
    }

    public void clearBullets(Pane pane) {
        for (Bullet bullet : bullets) {
            pane.getChildren().remove(bullet.getCharacter());
        }
    }
}
