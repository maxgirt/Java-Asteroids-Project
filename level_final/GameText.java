package Asteroids2;


import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;

import static Asteroids2.Asteroids.PANE_HEIGHT;
import static Asteroids2.Asteroids.PANE_WIDTH;

public class GameText {
    private Pane pane;

    // Add all Text objects as class members
    private Text text;
    private Text livesText;
    private Text jumpText;
    private Text invincibleText;
    private Text levelText;
    private Text gameOverText;

    private Font font = Font.font("Lucida Console", FontWeight.BOLD, 20);
    private Font fontLarge = Font.font("Lucida Console", FontWeight.BOLD, 40);
    private Font fontSmall = Font.font("Lucida Console", FontWeight.BOLD, 14);
    public void updateLevelText(int currentLevel) {
        levelText.setText("Level: " + currentLevel);
    }

    public void updateScore(int score) {
        text.setText("Points: " + score);
    }
    public void updateLive(int live){
        livesText.setText("Lives: " + live);
    }

    public GameText(Pane pane) {
        this.pane = pane;
    }

    public void updateInvincibleText(boolean isInvincible, long timeSinceCollision) {
        if (isInvincible) {
            if (timeSinceCollision < 2.5e9) {
                double remainingTime = 2.59 - timeSinceCollision / 1e9;
                String formattedTime = String.format("%.2f", remainingTime);
                invincibleText.setText("Invincible Time: " + formattedTime + "s");
            } else {
                invincibleText.setText("");
            }
        } else {
            invincibleText.setText("");
        }
    }

    public void setGameOverTextVisible(boolean isVisible) {
        gameOverText.setVisible(isVisible);
    }



    public void initializeText(int score, Ship ship, int currentLevel) {
        text = new Text(10, 20, "Points: " + score);
        text.setFont(font);
        text.setFill(Color.WHITESMOKE);
        text.setX(PANE_WIDTH * 0.16);
        text.setY(PANE_HEIGHT * 0.04);
        pane.getChildren().add(text);

        // Add the other text elements here
        livesText = new Text("Lives: " + ship.lives);
        livesText.setFont(font);
        livesText.setFill(Color.WHITESMOKE);
        livesText.setX(PANE_WIDTH * 0.05);
        livesText.setY(PANE_HEIGHT * 0.04);
        pane.getChildren().add(livesText);

        jumpText = new Text("Jump: ");
        jumpText.setFont(font);
        jumpText.setFill(Color.WHITESMOKE);
        jumpText.setX(PANE_WIDTH * 0.30);
        jumpText.setY(PANE_HEIGHT * 0.04);
        pane.getChildren().add(jumpText);

        invincibleText = new Text();
        invincibleText.setFont(font);
        invincibleText.setFill(Color.WHITESMOKE);
        invincibleText.setX(PANE_WIDTH / 2 - invincibleText.getBoundsInLocal().getWidth() / 4);
        invincibleText.setY(PANE_HEIGHT * 0.04);
        pane.getChildren().add(invincibleText);

        levelText = new Text("Level: " + Integer.toString(currentLevel - 1));
        levelText.setFont(font);
        levelText.setFill(Color.WHITESMOKE);
        levelText.setLayoutX(PANE_WIDTH / 2 + 300);
        levelText.setLayoutY(PANE_HEIGHT * 0.04);
        pane.getChildren().add(levelText);

        gameOverText = new Text("Game Over");
        gameOverText.setFont(fontLarge);
        gameOverText.setFill(Color.WHITESMOKE);
        gameOverText.setFill(Color.RED);
        gameOverText.setX(PANE_WIDTH / 2 - gameOverText.getBoundsInLocal().getWidth() / 2);
        gameOverText.setY(PANE_HEIGHT / 2 - gameOverText.getBoundsInLocal().getHeight() / 2);
        gameOverText.setVisible(false);
        pane.getChildren().add(gameOverText);
    }


}
