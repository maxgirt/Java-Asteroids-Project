package com.example.asteroids;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Random;
import javafx.scene.shape.Shape;
import java.util.Timer;
import java.util.TimerTask;

public class HelloApplication extends Application {
    Random random = new Random();
    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception error) {
            error.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    int score;
    boolean gameIsOver = false;

    public void start(Stage mainStage) {
        mainStage.setTitle("Asteroids");

        BorderPane root = new BorderPane();
        Scene mainScene = new Scene(root);
        mainStage.setScene(mainScene);

        Canvas canvas = new Canvas(900,600);
        GraphicsContext context = canvas.getGraphicsContext2D();
        root.setCenter(canvas);

        // continuous key press
        ArrayList<String> keyPressed = new ArrayList<>();
        //once per key pres
        ArrayList<String> keyJustPressed = new ArrayList<>();

        // Storing the available jumps
        ArrayList<Integer> jumps = new ArrayList<>();

        mainScene.setOnKeyPressed(
                (KeyEvent event) ->
                {
                    String keyName = event.getCode().toString();
                    //avoid duplicates!
                    if (!keyPressed.contains(keyName)) {
                        keyPressed.add(keyName);
                        keyJustPressed.add(keyName);
                    }
                }
        );

        mainScene.setOnKeyReleased(
                (KeyEvent event) ->
                {
                    String keyName = event.getCode().toString();
                    //avoid duplicates!
                    if ( keyPressed.contains(keyName))
                        keyPressed.remove(keyName);
                }
        );

        Sprite background = new Sprite("C:\\Users\\arnas\\Desktop\\COLLEGE\\Semester2\\COMP30820-JAVA\\Asteroids\\Asteroids\\src\\Asteroids images\\background.jpg");
        background.position.set(400,300);
        Sprite spaceship = new Sprite("C:\\Users\\arnas\\Desktop\\COLLEGE\\Semester2\\COMP30820-JAVA\\Asteroids\\Asteroids\\src\\Asteroids images\\maxship2.png");
        spaceship.position.set(100,300);
        spaceship.velocity.set(50,0);
        Sprite gameOver = new Sprite("C:\\Users\\arnas\\Desktop\\COLLEGE\\Semester2\\COMP30820-JAVA\\Asteroids\\Asteroids\\src\\Asteroids images\\gameover.gif");
        gameOver.position.set(450,300);

        ArrayList<Sprite> laserList = new ArrayList<>();
        ArrayList<Sprite> asteroidList = new ArrayList<>();
        int asteroidCount = 6; //Add more asteroids here
        for (int n=0; n < asteroidCount; n++){
            Sprite asteroid = new Sprite("C:\\Users\\arnas\\Desktop\\COLLEGE\\Semester2\\COMP30820-JAVA\\Asteroids\\Asteroids\\src\\Asteroids images\\vape3.png");
            double x = 500* Math.random() + 300; //300-800
            double y = 400 * Math.random() + 100; // 100-500
            asteroid.position.set(x,y);
            double angle = 360 * Math.random();
            asteroid.velocity.setLenght(50);
            asteroid.velocity.setAngle(angle);
            asteroidList.add(asteroid);

        }

        score = 0;

        AnimationTimer gameloop = new AnimationTimer() {
            @Override
            public void handle(long nanotime) {
                int jumps = 0;
                if (spaceship.elapsedTime >= 5){
                    spaceship.jumps ++;
                    spaceship.elapsedTime = 0.0;
                }
                if (keyPressed.contains("LEFT")) {
                    spaceship.rotation -= 3;
                }
                if (keyPressed.contains("RIGHT")) {
                    spaceship.rotation += 3;
                }
                if (keyPressed.contains("UP")) {
                    spaceship.velocity.setLenght(200);
                    spaceship.velocity.setAngle(spaceship.rotation);
                    /*spaceship.rotation += 90; // GOOFY SPIN */
                }
                else //not pressing up
                { //TODO: SHOULD SLOW DOWN GRADUALLY RATHER THAN STOP IMMEDIATELY
                    //TODO: CANNOT SHOOT WHILE HOLDING UP AND RIGHT AT THE SAME TIME
                    //TODO: ASTEROIDS DO NOT ROTATE
                    //TODO: SPAWN MORE ASTEROIDS BIG + SMALL
                    //TODO: SPAWN ENEMY SHIP
                    //TODO: MAIN MENU
                    //TODO: DEATH
                    spaceship.velocity.setLenght(0);
                }
                if (keyJustPressed.contains("W")) {
                    if (spaceship.jumps >= 1) {
                        spaceship.jumps --;
                        spaceship.elapsedTime = 0.0;

                    //Vector jumpDistance = (200.00); // Distance to jump
                    //Vector spaceshipPosition = spaceship.position;
                    //double spaceshipRotation = spaceship.rotation;
                    Sprite Dummy = new Sprite();


                    // double jumpDistance = 50.0; // Distance to jump

                    // Calculate the position of the hyperjump destination
                    //double jumpX = spaceshipPosition.x + jumpDistance * Math.cos(Math.toRadians(spaceshipRotation));
                    //double jumpY = spaceshipPosition.y + jumpDistance * Math.sin(Math.toRadians(spaceshipRotation));
                    int jumpX = random.nextInt(901);
                    int jumpY = random.nextInt(601);
                    Sprite dummy = new Sprite();
                    dummy.position.set(jumpX, jumpY);
                    // System.out.print(dummy.position.x + dummy.position.y);
                    boolean isOverlap = false; // Check if there is any overlap with other objects
                    for (Sprite obj : asteroidList) {
                        //dummy.getBoundary().overlaps(obj.getBoundary()) || dummy.isInside(obj.getBoundary())
                        if (dummy.isInside(obj.getBoundary())) {
                            isOverlap = true;
                            break;
                        }
                    }
                    if (!isOverlap) {
                        // If there is an overlap, try again with a new jump destination
                        spaceship.position = dummy.position;
                        //System.out.println(spaceship.elapsedTime);
                        return;
                        }
                    }

                }

                if (keyJustPressed.contains("SPACE")) {
                    Sprite laser = new Sprite("C:\\Users\\arnas\\Desktop\\COLLEGE\\Semester2\\COMP30820-JAVA\\Asteroids\\Asteroids\\src\\Asteroids images\\laser3.png");
                    laser.position.set(spaceship.position.x, spaceship.position.y);
                    laser.velocity.setLenght( 400 );
                    laser.velocity.setAngle( spaceship.rotation );
                    laserList.add(laser);
                }
                //after processing user input, clear just pressed list
                keyJustPressed.clear();
                spaceship.update(1/60.0);
                for (Sprite asteroid : asteroidList){
                    asteroid.update(1/60.0);
                }
                // update lasers; destroy if more than two seconds have passed
                for (int n = 0; n <  laserList.size(); n++){
                    Sprite laser = laserList.get(n);
                    laser.update(1/60.0);
                    if (laser.elapsedTime > 2){
                        laserList.remove(n);
                    }
                }
                // remove asteroid when laser overlaps it
                for (int laserNum = 0; laserNum < laserList.size(); laserNum++){

                    Sprite laser = laserList.get(laserNum);

                    for ( int asteroidNum = 0; asteroidNum < asteroidList.size(); asteroidNum++){
                        Sprite asteroid = asteroidList.get(asteroidNum);
                        if (laser.overlaps(asteroid)){
                            laserList.remove(laserNum);
                            asteroidList.remove(asteroidNum);
                            score += 100;
                        }
                    }
                }
                // show game over screen if player dies

                for (Sprite obstacle : asteroidList) {
                    Rectangle obstacleBounds = obstacle.getBoundary();
                    if (spaceship.overlaps(obstacle) &&
                            spaceship.isInside(obstacle.getBoundary())) {
                        gameIsOver = true;
                        break;
                    }

                }
                if (gameIsOver) {
                    // update and render game over screen
                    gameOver.update(1/180.0);
                    gameOver.render(context);
                } else {
                    // update and render game
                    background.render(context);

                    spaceship.render(context);

                background.render(context);

                spaceship.render(context);

                for (Sprite laser : laserList)
                    laser.render(context);

                for (Sprite asteroid : asteroidList)
                    asteroid.render(context);

                // drawing scoreboard
                context.setFill(Color.ANTIQUEWHITE);
                context.setStroke(Color.BLUEVIOLET);
                context.setFont( new Font("sans serif", 48));
                context.setLineWidth(3);
                String text = "SCORE:" + score ;
                String timeText = "HJUMPS:" + spaceship.jumps;
                String text2 = "Timer" + String.format("%.1f", spaceship.elapsedTime);
                int textX = 600;
                int textY = 60;
                context.fillText(text, textX, textY);
                context.strokeText(text, textX, textY);
                //context.fillText(timeText, textX, textY + 60);
                //context.strokeText(timeText, textX, textY + 60);
                context.fillText(text2, textX, textY + 120);
                context.strokeText(text2, textX, textY + 120);
                if (spaceship.jumps >=1) {
                    context.setFont( new Font("sans serif", 48));
                    context.setLineWidth(3);
                    context.setFill(Color.LAWNGREEN);
                    context.setStroke(Color.LAWNGREEN);
                    String jumpOn = "JUMP : ON";
                    context.fillText(jumpOn, textX, textY + 60);
                    context.strokeText(jumpOn, textX, textY + 60);
                }
                else {
                    context.setFont( new Font("sans serif", 48));
                    context.setLineWidth(3);
                    context.setFill(Color.RED);
                    context.setStroke(Color.RED);
                    String jumpOff = "JUMP : OFF";
                    context.fillText(jumpOff, textX, textY + 60);
                    context.strokeText(jumpOff, textX, textY + 60);
                }

                    for (Sprite laser : laserList)
                        laser.render(context);

                    for (Sprite asteroid : asteroidList)
                        asteroid.render(context);
                }
            }
        };

        gameloop.start();
        mainStage.show();

    }
}
