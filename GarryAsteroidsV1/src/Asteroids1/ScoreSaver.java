package Asteroids1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


//class that saves our score to a file

public class ScoreSaver {
    private File file;
    private FileWriter writer;

    public ScoreSaver(String filename) throws IOException {
        file = new File(filename);
        writer = new FileWriter(file, true); //true so file is not overwritten 
    }

    public void saveScore(int score) throws IOException {
        writer.write(score + "\n");
    }

    public void close() throws IOException {
        writer.close();
    }
    
    
    //method that allows us to pull out the scores and display them on a menu screen
    public List<Integer> readScores() throws IOException {
        List<Integer> scores = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            scores.add(Integer.parseInt(line));
        }
        reader.close();
        Collections.sort(scores, Collections.reverseOrder()); // sort in descending order
        if (scores.size() > 5) {
            scores = scores.subList(0, 5); // limit to top 5 scores
        }
        return scores;
    }
    
    
}




// By making file and writer private, we're making them internal implementation details of the ScoreSaver class 
//that are not visible or accessible to external code. Instead, external code interacts with ScoreSaver 
//through its public methods, such as saveScore and close, which use file and writer internally to perform their tasks.