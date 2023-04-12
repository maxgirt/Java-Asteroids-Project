package testing;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AsteroidsApplication extends Application {
    public static int WIDTH = 800;
    public static int HEIGHT = 600;

    @Override
    public void start(Stage stage) throws Exception {
        Pane pane = new Pane();
        pane.setPrefSize(WIDTH, HEIGHT);
        Text text = new Text(10, 20, "Points: 0");
        pane.getChildren().add(text);
        
        List<Projectile> projectiles = new ArrayList<>();
        Ship ship = new Ship(WIDTH / 2, HEIGHT / 2);
        List<Asteroid> asteroids = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Random rnd = new Random();
            Asteroid asteroid = new Asteroid(rnd.nextInt(WIDTH / 3), rnd.nextInt(HEIGHT));
            asteroids.add(asteroid);
        }

        pane.getChildren().add(ship.getCharacter());
        asteroids.forEach(asteroid -> pane.getChildren().add(asteroid.getCharacter()));
        
        
        
        Scene scene = new Scene(pane);
        stage.setTitle("Asteroids!");
        stage.setScene(scene);
        stage.show();
        
        
        Map<KeyCode, Boolean> pressedKeys = new HashMap<>();
        
        scene.setOnKeyPressed(event -> {
            pressedKeys.put(event.getCode(), Boolean.TRUE);
        });

        scene.setOnKeyReleased(event -> {
            pressedKeys.put(event.getCode(), Boolean.FALSE);
        });
        
        
        AtomicInteger points = new AtomicInteger();
        
        //Point2D movement = new Point2D(1, 0);
        
        new AnimationTimer() {
        	
            public void handle(long now) {
            	
            	
                scene.setOnKeyPressed(event -> {
                    pressedKeys.put(event.getCode(), Boolean.TRUE);
                    
                    if (event.getCode() == KeyCode.S) {
                        ship.hyperspace();
                    }
                });

            	
            	//text.setText("Points: " + points.incrementAndGet());
            	
                if(pressedKeys.getOrDefault(KeyCode.LEFT, false)) {
                    ship.turnLeft();
                }

                if(pressedKeys.getOrDefault(KeyCode.RIGHT, false)) {
                    ship.turnRight();
                }
                
                if(pressedKeys.getOrDefault(KeyCode.UP, false)) {
                    ship.accelerate();
                }
                
                //spawn projectiles on pressing spacebar 
                if (pressedKeys.getOrDefault(KeyCode.SPACE, false) && projectiles.size() < 100) {
                    // we shoot
                    Projectile projectile = new Projectile((int) ship.getCharacter().getTranslateX(), (int) ship.getCharacter().getTranslateY());
                   
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        public void run() {
                            if (pressedKeys.getOrDefault(KeyCode.SPACE, false) && projectiles.size() < 3) {
                                Projectile projectile = new Projectile((int) ship.getCharacter().getTranslateX(), (int) ship.getCharacter().getTranslateY());
                                projectile.getCharacter().setRotate(ship.getCharacter().getRotate());
                                projectiles.add(projectile);

                                projectile.accelerate();
                                projectile.setMovement(projectile.getMovement().normalize().multiply(3));

                                pane.getChildren().add(projectile.getCharacter());
                            }
                        }
                    }, 0, 500); // delay of 500 milliseconds between each projectile
                    
                    
                    
                    
                    
                    projectile.getCharacter().setRotate(ship.getCharacter().getRotate());
                    projectiles.add(projectile);

                    projectile.accelerate();
                    projectile.setMovement(projectile.getMovement().normalize().multiply(3));

                    pane.getChildren().add(projectile.getCharacter());
                
                
                
                }                
                
                
                
                List<Projectile> projectilesToRemove = projectiles.stream().filter(projectile -> {
                    List<Asteroid> collisions = asteroids.stream().filter(asteroid -> asteroid.collide(projectile))
                    .collect(Collectors.toList());

                    if(collisions.isEmpty()) {
                        return false;
                    }

                    collisions.stream().forEach(collided -> {
                        asteroids.remove(collided);
                        pane.getChildren().remove(collided.getCharacter());
                        points.incrementAndGet(); // increment the score for each destroyed asteroid
                        text.setText("Points: " + points.get()); // update the score display
                    });

                    return true;
                }).collect(Collectors.toList());

                projectilesToRemove.forEach(projectile -> {
                    pane.getChildren().remove(projectile.getCharacter());
                    projectiles.remove(projectile);
                });                
                
                
                
                ship.move();
                asteroids.forEach(asteroid -> asteroid.move() );
                projectiles.forEach(projectile -> projectile.move());
                
                
                asteroids.forEach(asteroid -> {
                    if (ship.collide(asteroid)) {
                        stop();
                    }
                });
                     
                    
               };
        
            }.start();
            
    }
    
    
    

    public static void main(String[] args) {
        launch(args);
    }
}