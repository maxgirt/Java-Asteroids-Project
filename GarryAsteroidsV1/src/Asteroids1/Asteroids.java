package Asteroids1;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import java.util.Map;
import java.util.HashMap;
import javafx.animation.AnimationTimer;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;


public class Asteroids  {
	
	boolean levelStarted = false;

	public static int paneHeight = 600;
	public static int paneWidth = 800;
    int currentLevel = 1;
    int maximumLevel = 5;
    private Ship ship = new Ship(150,100);

    private List<Asteroid> asteroids = new ArrayList<>();

	public void startGame(Stage stage, Menu menu) throws Exception {
		Random rnd = new Random();
		Pane pane = new Pane();
		pane.setPrefSize(paneWidth, paneHeight);

		Scene scene = new Scene(pane);
		stage.setTitle("Asteroids");
        Text text = new Text(10, 20, "Points: 0");
        pane.getChildren().add(text);
		stage.setScene(scene);
		stage.show();
	    System.out.println("startGame Pane: " + pane.hashCode());
		pane.getChildren().add(ship.getCharacter());

		
		
		Asteroid asteroid = new Asteroid(rnd.nextInt(paneWidth), rnd.nextInt(paneHeight), 50, 10);
		asteroids.add(asteroid);
		pane.getChildren().add(asteroid.getCharacter());

		//asteroids.forEach(asteroid -> pane.getChildren().add(asteroid.getCharacter()));

		Map<KeyCode, Boolean> pressedKeys = new HashMap<>(); // keys are mapped to values, 
		//KeyCode is the enum of the key pressed
		// using Map instead of HashMap does not restrict us to only HashMap methods if we want to later
		//    change the Map interface implementation
		scene.setOnKeyPressed(event -> {
			pressedKeys.put(event.getCode(), Boolean.TRUE);
		});

		scene.setOnKeyReleased(event -> {
			pressedKeys.put(event.getCode(), Boolean.FALSE);
		});
		
        AtomicInteger points = new AtomicInteger();

		List<Bullet> bullets = new ArrayList<>();
		List<Bullet> deadBullets = new ArrayList<>();
		List<Asteroid> deadAsteroids = new ArrayList<>();
		List<Asteroid> mediumAsteroids = new ArrayList<>();
		List<Asteroid> smallAsteroids = new ArrayList<>();

		
		new AnimationTimer() {
			double shootTimer = System.nanoTime()-1e8;
			long lastUpdateTime = System.nanoTime();

			@Override
			public void handle(long nanotime) {
				long elapsedTime = nanotime - lastUpdateTime;
		        lastUpdateTime = nanotime;
				ship.elapsedTime += elapsedTime;
                if (ship.elapsedTime >= 2e9){
                    ship.jumps ++;
                    ship.elapsedTime = (long) 0;
                }
				if (pressedKeys.getOrDefault(KeyCode.LEFT, false)) {
					ship.turnLeft();
				}

				if (pressedKeys.getOrDefault(KeyCode.RIGHT, false)) {
					ship.turnRight();
				}

				if (pressedKeys.getOrDefault(KeyCode.UP, false) && ship.getSpeed() < 10) {
					ship.accelerate();
				}
				if(pressedKeys.getOrDefault(KeyCode.UP, false) && ship.getSpeed() >= 10 && ship.isOppositeDirection()) {
					ship.accelerate();
				}
			
				if(pressedKeys.getOrDefault(KeyCode.W, false)) {
                    if(ship.jumps >= 1) {
                        ship.jumps --;
                        ship.elapsedTime = (long) 0;
               
                        int jumpX = rnd.nextInt(600);
                        int jumpY = rnd.nextInt(400);
                        Ship dummyShip = new Ship(jumpX,jumpY);
                        boolean isOverlap = false;
                        
                        for (Asteroid obj : asteroids) {
                            //dummy.getBoundary().overlaps(obj.getBoundary()) || dummy.isInside(obj.getBoundary())
                            if (dummyShip.collide(obj)) {
                                isOverlap = true;
                                break;
                            }
                        
                        }
                        if (!isOverlap) {
                            // If there is an overlap, try again with a new jump destination
                            ship.hyperspaceJump(dummyShip);
                            System.out.println("Jumped to new position: X=" + dummyShip.position.getX() + ", Y=" + dummyShip.position.getY());
                            //System.out.println(spaceship.elapsedTime);
                            return;
                            }
                       }
				}
				
				ship.move();
				asteroids.forEach(asteroid -> asteroid.move());
				bullets.forEach(bullet -> bullet.move());

				// create separate lists for storing bullets and asteroids to be removed
				//List<Bullet> deadBullets = new ArrayList<>();
				//List<Asteroid> deadAsteroids = new ArrayList<>();

				// iterate over asteroids and bullets
				//increments the score after a bullet hits an asteroid
				asteroids.forEach(asteroid -> {
					// handle collisions with ship
					if (ship.collide(asteroid)) {
						stop();
					}

					bullets.forEach(bullet -> {
					    if (bullet.collide(asteroid) && asteroid.getSize() == 50) {
					    	points.addAndGet(25); // increment the score by 25 for each destroyed
	                        text.setText("Points: " + points.get()); // update the score display
					        Asteroid newAsteroid1 = new Asteroid(asteroid.getCharacter().getTranslateX(),
					                asteroid.getCharacter().getTranslateY(),30, 35);
					        mediumAsteroids.add(newAsteroid1);

					        pane.getChildren().add(newAsteroid1.getCharacter());
					        Asteroid newAsteroid2 = new Asteroid(asteroid.getCharacter().getTranslateX(),
					                asteroid.getCharacter().getTranslateY(),30, 35);
					        mediumAsteroids.add(newAsteroid2);

					        pane.getChildren().add(newAsteroid2.getCharacter());
					        deadBullets.add(bullet);
					        deadAsteroids.add(asteroid);
					        pane.getChildren().removeAll(bullet.getCharacter(), asteroid.getCharacter());

					    } else if (bullet.collide(asteroid) && asteroid.getSize() == 30) {
					    	points.addAndGet(50); // increment the score by 25 for each destroyed
	                        text.setText("Points: " + points.get()); // update the score display
					        Asteroid newAsteroid1 = new Asteroid(asteroid.getCharacter().getTranslateX(),
					                asteroid.getCharacter().getTranslateY(),20, 45);
					        smallAsteroids.add(newAsteroid1);

					        pane.getChildren().add(newAsteroid1.getCharacter());
					        Asteroid newAsteroid2 = new Asteroid(asteroid.getCharacter().getTranslateX(),
					                asteroid.getCharacter().getTranslateY(),20, 45);
					        smallAsteroids.add(newAsteroid2);

					        pane.getChildren().add(newAsteroid2.getCharacter());
					        deadBullets.add(bullet);
					        deadAsteroids.add(asteroid);
					        pane.getChildren().removeAll(bullet.getCharacter(), asteroid.getCharacter());

					    } else if (bullet.collide(asteroid)) {
					    	points.addAndGet(100); // increment the score by 25 for each destroyed
	                        text.setText("Points: " + points.get()); // update the score display
					        deadBullets.add(bullet);
					        deadAsteroids.add(asteroid);
					        pane.getChildren().removeAll(bullet.getCharacter(), asteroid.getCharacter());

					    }
					});
				});
				// remove dead bullets and asteroids from the original lists
				bullets.removeAll(deadBullets);
				asteroids.removeAll(deadAsteroids);
				asteroids.addAll(mediumAsteroids);
				asteroids.addAll(smallAsteroids);
				mediumAsteroids.clear();
				smallAsteroids.clear();
				//mediumAsteroids.forEach(asteroid -> asteroid.move());

				if (pressedKeys.getOrDefault(KeyCode.SPACE, false) && System.nanoTime()-shootTimer >= 2e8 && bullets.size() < 50) {
					Bullet bullet = new Bullet(ship.getCharacter().getTranslateX(), ship.getCharacter().getTranslateY());
					bullet.getCharacter().setRotate(ship.getCharacter().getRotate());
					bullets.add(bullet);

					Point2D bulletVelocity = new Point2D(Math.cos(Math.toRadians(bullet.getCharacter().getRotate())),
							Math.sin(Math.toRadians(bullet.getCharacter().getRotate())))
							.multiply(5);
					bullet.setMovement(ship.getMovement().add(bulletVelocity));

					pane.getChildren().add(bullet.getCharacter());
					shootTimer = System.nanoTime();
				}

				for (Bullet bullet : bullets) {

					if (bullet.getAge() > 2e9) { // if bullet has been alive for more than 100 frames
						deadBullets.add(bullet); // add bullet to removal list
						pane.getChildren().remove(bullet.getCharacter()); // remove bullet's character from pane
					}
				}

				bullets.removeAll(deadBullets);
				//System.out.println(ship.jumps);
			
			
			// logic to increase level 
				
				if (asteroids.isEmpty() && currentLevel < maximumLevel && !levelStarted) {
				      levelStarted = true;
					  levelScreen(rnd, pane);
					}

		       }
			
			
		}.start();
	}

	
	
	
	
	public void levelScreen(Random rnd, Pane pane) {
		
		Button continueButton = new Button("Start Game");
	    continueButton.setPrefSize(120, 40); // Set preferred width and height

		Text levelText = new Text("Level: " + currentLevel);
		levelText.setLayoutX(paneWidth/2 - levelText.getBoundsInLocal().getWidth()/2);
		levelText.setLayoutY(paneHeight/2 - levelText.getBoundsInLocal().getHeight()/2);
		pane.getChildren().add(levelText);
		
		
	  // Set the button's position after it is added to the scene graph
		continueButton.setLayoutX(paneWidth/2);
		continueButton.setLayoutY(paneHeight/2);
		pane.getChildren().add(continueButton);
		
        System.out.println("levelScreen Pane: " + pane.hashCode());
		continueButton.setOnMouseClicked(e -> {
		    try {
		    	incrementLevel();
		        System.out.println("Button clicked!");
		        for (int i = 0; i < currentLevel; i++) {
		            Asteroid asteroid = new Asteroid(rnd.nextInt(paneWidth), rnd.nextInt(paneHeight), 50, 10);
		            asteroids.add(asteroid);
		            pane.getChildren().add(asteroid.getCharacter());
		        }
		        ship.resetPosition(150,100);
		        pane.getChildren().remove(continueButton);
		        pane.getChildren().remove(levelText);
		        levelStarted = false;
		    } catch (Exception ex) {
		        ex.printStackTrace();
		    }
		});	
		}
	
	//Method which clears the screen to add a new one:
	
	
	public void clearScreen(Pane pane) {
	    pane.getChildren().clear();
	}

	
	// Method for incrementing the current level
	private void incrementLevel() {
		this.currentLevel += 1;
	}
	
	}