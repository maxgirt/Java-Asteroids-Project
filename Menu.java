package demo6;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Collections;


public class Menu  {
    private Stage stage;
    private VBox menu;
    private ArrayList<Integer> scoresList;
    Asteroids asteroidsGame = new Asteroids();

    private HighScores highScores;
    private String highScoresFilePath;

    public Menu() {
        // constructor code

        highScoresFilePath = "D:\\IDEA\\Java_project\\FX\\src\\demo6\\score.txt";

        highScores = new HighScores(highScoresFilePath);
        scoresList = highScores.loadScores();
    }
    public HighScores getHighScores()
    {
        return  highScores;
    }
    public ArrayList<Integer> getScoresList() {
        return scoresList;
    }

    //start method of our application which creates the main window
    public void start(Stage menuStage) throws Exception {
        System.out.println();
        VBox menu = new VBox(10); // create a VBox container with a spacing of 10 pixels
        menu.setPrefSize(800, 600);


        //create a background image for our menu
        Image image = new Image("file:src/images/asteroids.jpg");
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);



        menu.setBackground(new Background(backgroundImage));


        Button startButton = new Button("Start Game");
        startButton.setPrefSize(120, 40); // Set preferred width and height


        Button exitButton = new Button("Exit Game");
        exitButton.setPrefSize(120, 40); // Set preferred width and height


        Button highScoreButton = new Button("High Scores");
        highScoreButton.setPrefSize(120, 40); // Set preferred width and height


        //style the buttons

        startButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2px;");
        exitButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2px;");
        highScoreButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2px;");


        startButton.setOnMouseEntered(e -> startButton.setStyle("-fx-background-color: black; -fx-text-fill: white;"));
        startButton.setOnMouseExited(e -> startButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2px;"));

        exitButton.setOnMouseEntered(e -> exitButton.setStyle("-fx-background-color: black; -fx-text-fill: white;"));
        exitButton.setOnMouseExited(e -> exitButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2px;"));

        highScoreButton.setOnMouseEntered(e -> highScoreButton.setStyle("-fx-background-color: black; -fx-text-fill: white;"));
        highScoreButton.setOnMouseExited(e -> highScoreButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2px;"));


        menu.getChildren().addAll(startButton, exitButton, highScoreButton);

        // center the buttons horizontally
        menu.setAlignment(Pos.CENTER);

        Scene scene = new Scene(menu);
        menuStage.setTitle("Asteroids!");
        menuStage.setScene(scene);
        menuStage.show();



        //This method will create a new pane for the game menu where the game will be played.
        //when the button is clicked we will create an instance of our gameWindow class that is defined in the package
        startButton.setOnAction(e -> {
            try {
                Stage gameStage = new Stage();
                asteroidsGame.startGame(gameStage, this);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            menuStage.hide();
        });


        //Exit the game
        exitButton.setOnAction(e -> {
            highScores.saveScore(scoresList);
            System.exit(0);
        });

        highScoreButton.setOnAction(e -> {
            highScores.display(menuStage, scoresList);
        });


    }

    public Stage getStage() {
        return stage;
    }

    public VBox getMenu() {
        return menu;
    }
}
