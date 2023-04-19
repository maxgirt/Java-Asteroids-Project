package demo6;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.paint.Color;


public class HighScores {


    private String filePath;

    public HighScores(String filePath) {
        this.filePath = filePath;
    }

    public void display(Stage menuStage, ArrayList<Integer> scoresList) {
        Stage highScoresStage = new Stage();
        VBox highScoresBox = new VBox(10);
        highScoresBox.setPrefSize(800, 600);
        highScoresBox.setAlignment(Pos.CENTER);

        Text title = new Text("High Scores");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        title.setFill(Color.WHITE);

        VBox scoresBox = new VBox(5);
        scoresBox.setAlignment(Pos.CENTER);

        Collections.sort(scoresList, Collections.reverseOrder());
        int rank = 1;
        for (int i = 0; i < Math.min(scoresList.size(), 10); i++){//get the 10 scores
            Text scoreText = new Text(rank + ". " + scoresList.get(i));
            scoreText.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
            scoreText.setFill(Color.WHITE);
            scoresBox.getChildren().add(scoreText);
            rank++;
        }

        Button backButton = new Button("Back");
        backButton.setPrefSize(120, 40);
        backButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2px;");
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: black; -fx-text-fill: white;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2px;"));

        backButton.setOnAction(e -> {
            highScoresStage.hide();
            menuStage.show();
        });

        highScoresBox.getChildren().addAll(title, scoresBox, backButton);

        Scene scene = new Scene(highScoresBox);
        highScoresBox.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        highScoresStage.setTitle("High Scores");
        highScoresStage.setScene(scene);
        highScoresStage.show();
        menuStage.hide();
    }

    public void saveScore(ArrayList<Integer> scoresList) {
        try {
            FileWriter writer = new FileWriter(filePath);
            for (Integer score : scoresList) {
                writer.write(score + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void saveScore(Integer score) throws IOException {
        FileWriter writer = new FileWriter(filePath,true);
        writer.write(score + "\n");
        writer.close();
    }


    public ArrayList<Integer> loadScores() {
        ArrayList<Integer> scoresList = new ArrayList<>();
        try {
            FileReader reader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                int score = Integer.parseInt(line.trim());
                scoresList.add(score);
            }
            bufferedReader.close();
            reader.close();
        } catch (FileNotFoundException e) {
            // if the file does not exist, return an empty list
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scoresList;
    }


}


