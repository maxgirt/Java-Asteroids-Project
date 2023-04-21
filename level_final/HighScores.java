package Asteroids2;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void display(Stage menuStage, HashMap<String, Integer> hashMap) {
        System.out.println("Displaying high scores");
        Stage highScoresStage = new Stage();
        VBox highScoresBox = new VBox(10);
        highScoresBox.setPrefSize(1200, 800);
        highScoresBox.setAlignment(Pos.CENTER);

        Text title = new Text("High Scores");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        title.setFill(Color.WHITE);

        VBox scoresBox = new VBox(5);
        scoresBox.setAlignment(Pos.CENTER);
        
        List<Map.Entry<String, Integer>> list = new ArrayList<>(hashMap.entrySet());
        list.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));
        int rank = 1;
        for (int i = 0; i < Math.min(list.size(), 10); i++) {//get the 10 scores
            Text scoreText = new Text(rank + ". " + list.get(i).getKey() + ": " + list.get(i).getValue());
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

    public void saveScore(HashMap<String, Integer> scoresMap) {
        try {
            FileWriter writer = new FileWriter(filePath);
            for (Map.Entry<String, Integer> entry : scoresMap.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
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


    public HashMap<String, Integer> loadScores() {
        HashMap<String, Integer> scoresMap = new HashMap<>();
        try {
            FileReader reader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println("Line read: " + line);
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    try {
                        String name = parts[0].trim();
                        int score = Integer.parseInt(parts[1].trim());
                        scoresMap.put(name, score);
                    } catch (NumberFormatException e) {
                        // Ignore lines that do not contain a valid score
                    }
                }
            }
            bufferedReader.close();
            reader.close();
        } catch (FileNotFoundException e) {
            // if the file does not exist, return an empty list
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scoresMap;
    }    
    
    public void saveNameAndScore(String name, int score) {
        try {
            FileWriter writer = new FileWriter(filePath, true);
            writer.write(name + " : " + score + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public HashMap<String, Integer> getScores() {
        return loadScores();
    }

}
