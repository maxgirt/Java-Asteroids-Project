package asteroids;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javafx.util.Pair;

public class ScoreBoard implements SaveHandler {

    private List<Pair<String, Integer>> scoresList = new ArrayList<>();
    private final String FILE_NAME;
    private final String PARENT_DIRECTORY_NAME;

    // Constructor for the ScoreBoard class.
    public ScoreBoard(String parentDirectory, String fileName) {
        // 检查文件名和目录名是否合法
        checkValidFileString(parentDirectory, "Directory name can only include letters, numbers and underscores.");
        checkValidFileString(fileName, "File name can only include letters, numbers and underscores.");

        this.PARENT_DIRECTORY_NAME = parentDirectory;
        this.FILE_NAME = fileName;
        load();
    }

    // Saves the scores list to the score file.
    @Override
    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getFilePath()))) {
            for (Pair<String, Integer> entry : scoresList) {
                writer.write(entry.getKey() + ":" + entry.getValue() + "\n");
            }
            writer.close();
        } catch (FileNotFoundException e) {
            new File(PARENT_DIRECTORY_NAME).mkdir();
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Loads the scores list from the score file.
    @Override
    public void load() {
        try (BufferedReader reader = new BufferedReader(new FileReader(getFilePath()))) {
            scoresList = reader.lines().map(element -> element.split(":"))
                    .map(element -> new Pair<String, Integer>(element[0], Integer.parseInt(element[1])))
                    .collect(Collectors.toList());
            reader.close();
        } catch (FileNotFoundException e) {
            new File(PARENT_DIRECTORY_NAME).mkdir();
            scoresList = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Adds a new score to the scores list.
    public void addScore(String player, int score) {
        if (player.isBlank() || player.isEmpty() || score < 0) {
            throw new IllegalArgumentException("invalid score");
        }
        scoresList.add(new Pair<>(player, score));
        // Gets the highest score from the scores list.
        scoresList.sort(Comparator.comparing(Pair<String, Integer>::getValue).reversed());
        save();
    }

    //  Gets the scores list.
    public int getHighScore() {
        return scoresList.isEmpty() ? 0 : scoresList.get(0).getValue();
    }


    public List<Pair<String, Integer>> getScores() {
        return scoresList;
    }

    // Gets the file path of the score file.
    private String getFilePath() {
        return PARENT_DIRECTORY_NAME + "/" + FILE_NAME + ".txt";
    }


    private void checkValidFileString(String s, String message) {
        if (!Pattern.matches("[a-zA-Z0-9_]+", s)) {
            throw new IllegalArgumentException(message);
        }
    }
}
