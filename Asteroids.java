package Asteroids1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import java.util.Map;
import java.util.HashMap;
import javafx.animation.AnimationTimer;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;

public class Asteroids extends Application {

	public static int paneHeight = 400;
	public static int paneWidth = 600;

	@Override
	public void start(Stage stage) throws Exception {
		Random rnd = new Random();
		Pane pane = new Pane();
		pane.setPrefSize(paneWidth, paneHeight);

		Scene scene = new Scene(pane);
		stage.setTitle("Asteroids");
		stage.setScene(scene);
		stage.show();

		Ship ship = new Ship(150,100);
		pane.getChildren().add(ship.getCharacter());

		List<Asteroid> asteroids = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Asteroid asteroid = new Asteroid(rnd.nextInt(paneWidth), rnd.nextInt(paneHeight), 22, 10);
			asteroids.add(asteroid);
		}

		asteroids.forEach(asteroid -> pane.getChildren().add(asteroid.getCharacter()));

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
				asteroids.forEach(asteroid -> {
					// handle collisions with ship
					if (ship.collide(asteroid)) {
						stop();
					}

					bullets.forEach(bullet -> {
						if (bullet.collide(asteroid) && asteroid.getSize() == 22) {
							Asteroid newAsteroid1 = new Asteroid(asteroid.getCharacter().getTranslateX(),
									asteroid.getCharacter().getTranslateY(),14, 15);
							mediumAsteroids.add(newAsteroid1);
							
							pane.getChildren().add(newAsteroid1.getCharacter());
							Asteroid newAsteroid2 = new Asteroid(asteroid.getCharacter().getTranslateX(),
									asteroid.getCharacter().getTranslateY(),14, 15);
							mediumAsteroids.add(newAsteroid2);
							
							pane.getChildren().add(newAsteroid2.getCharacter());
							deadBullets.add(bullet);
							deadAsteroids.add(asteroid);
							pane.getChildren().removeAll(bullet.getCharacter(), asteroid.getCharacter());

						}
						else if (bullet.collide(asteroid) && asteroid.getSize() == 14) {
							Asteroid newAsteroid1 = new Asteroid(asteroid.getCharacter().getTranslateX(),
									asteroid.getCharacter().getTranslateY(),7, 25);
							smallAsteroids.add(newAsteroid1);
							
							pane.getChildren().add(newAsteroid1.getCharacter());
							Asteroid newAsteroid2 = new Asteroid(asteroid.getCharacter().getTranslateX(),
									asteroid.getCharacter().getTranslateY(),7, 25);
							smallAsteroids.add(newAsteroid2);
							
							pane.getChildren().add(newAsteroid2.getCharacter());
							deadBullets.add(bullet);
							deadAsteroids.add(asteroid);
							pane.getChildren().removeAll(bullet.getCharacter(), asteroid.getCharacter());

						}
						else if (bullet.collide(asteroid)) {
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

				if (pressedKeys.getOrDefault(KeyCode.SPACE, false) && System.nanoTime()-shootTimer >= 2e8 && bullets.size() < 5) {
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
				System.out.println(ship.jumps);
			}
			
		}.start();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
