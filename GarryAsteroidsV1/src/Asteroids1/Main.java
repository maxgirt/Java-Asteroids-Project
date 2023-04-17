package Asteroids1;

import javafx.application.Application;
import javafx.stage.Stage;


//this is the start of our java application

public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {
        // create an instance of the menu screen
        Menu menu = new Menu();
        menu.start(primaryStage);
    }

    public static void main(String[] args) {
        // launch the JavaFX application
        launch(args);
    }
}