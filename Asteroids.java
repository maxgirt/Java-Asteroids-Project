package Asteroids2;

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
import java.util.concurrent.atomic.AtomicLong;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;


public class Asteroids  {
	
	private void resetShip(Ship ship) {
		ship.lives--; // 飞船生命值减1
		ship.isInvincible = true; // 将飞船设置为无敌状态
		ship.getCharacter().setTranslateX(paneWidth / 2);
		ship.getCharacter().setTranslateY(paneHeight / 2);
		ship.lastCollisionTime = System.nanoTime(); // 记录碰撞发生的时间
	}
	
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
		
		List<Bullet> alienBullets = new ArrayList<>();
		List<Bullet> deadAlienBullets = new ArrayList<>();

		AtomicLong alienTimer = new AtomicLong(System.nanoTime() - (long)1e9);
		AtomicLong alienShootTimer = new AtomicLong(System.nanoTime() - (long)1e9);
		List<Alien> aliens = new ArrayList<>();
		AtomicLong alienSpawnTimer = new AtomicLong(System.nanoTime());
		List<Alien> deadAliens = new ArrayList<>();
		
		Text livesText = new Text("Lives: " + ship.lives);
		livesText.setFont(Font.font("Verdana", 14));
		livesText.setX(paneWidth - 100);
		livesText.setY(20);
		pane.getChildren().add(livesText);

		Text invincibleText = new Text();
		invincibleText.setFont(Font.font("Verdana", 14));
		invincibleText.setX(paneWidth/2  - invincibleText.getBoundsInLocal().getWidth() / 4);
		invincibleText.setY(20);
		pane.getChildren().add(invincibleText);



		Text gameOverText = new Text("Game Over");
		gameOverText.setFont(Font.font("Verdana", 30));
		gameOverText.setFill(Color.RED);
		gameOverText.setX(paneWidth / 2 - gameOverText.getBoundsInLocal().getWidth() / 2);
		gameOverText.setY(paneHeight / 2 - gameOverText.getBoundsInLocal().getHeight() / 2);
		gameOverText.setVisible(false);
		pane.getChildren().add(gameOverText);

		
		new AnimationTimer() {
			double shootTimer = System.nanoTime()-1e8;
			long lastUpdateTime = System.nanoTime();

			@Override
			public void handle(long nanotime) {
				long elapsedTime = nanotime - lastUpdateTime;
		        lastUpdateTime = nanotime;
				ship.elapsedTime += elapsedTime;
                if (ship.elapsedTime >= 5e9){
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
				
				
				
				
				
				
				if (currentLevel % 1 == 0 && (aliens.size() <1 ) && System.nanoTime() - alienSpawnTimer.get() >= 15e9) {
					Alien newAlien = new Alien(0, 0);
					newAlien.setRandomStartPosition(paneWidth, paneHeight);
					aliens.add(newAlien);
					pane.getChildren().add(newAlien.getCharacter());
					alienSpawnTimer.set(System.nanoTime());
				}

				// Alien moves continuously
				aliens.forEach(a -> a.move());

				// handle collisions with alien
				aliens.forEach(a -> {
					// handle collisions with ship
					if (ship.collide(a) && !ship.isInvincible) {
						resetShip(ship);

						livesText.setText("Lives: " + ship.lives);

						if (ship.lives <= 0) {
							stop();
							gameOverText.setVisible(true); // Set the game over text visible
						}
					}
				});

				if (System.nanoTime() - alienTimer.get() >= 5e9) { // every 3 seconds, the alien moves randomly
					aliens.forEach(a -> a.moveRandomly());
					alienTimer.set(System.nanoTime());
				}

				if (System.nanoTime() - alienShootTimer.get() >= 2e9) {
					for (Alien a : aliens) {
						Bullet alienBullet = new Bullet(a.getCharacter().getTranslateX(), a.getCharacter().getTranslateY(), Color.RED);
						double angleToShip = a.calculateAngleToShip(ship);
						alienBullet.getCharacter().setRotate(angleToShip);
						alienBullets.add(alienBullet);

						Point2D alienBulletVelocity = new Point2D(Math.cos(Math.toRadians(alienBullet.getCharacter().getRotate())),
								Math.sin(Math.toRadians(alienBullet.getCharacter().getRotate())))
								.multiply(3);
						alienBullet.setMovement(alienBulletVelocity);

						pane.getChildren().add(alienBullet.getCharacter());
					}
					alienShootTimer.set(System.nanoTime());
				}


				alienBullets.forEach(bullet -> bullet.move());

				for (Bullet bullet : alienBullets) {
					if (bullet.getAge() > 2e9) { // 如果子弹已经存在超过 2 秒
						deadAlienBullets.add(bullet);
						pane.getChildren().remove(bullet.getCharacter());
					}
				}
				alienBullets.removeAll(deadAlienBullets);

				// Deal with collisions between alien bullets and ships
				alienBullets.forEach(bullet -> {
					// handle collisions with ship
					// handle collisions with ship
					if (ship.collide(bullet) && !ship.isInvincible) {
						resetShip(ship);

						livesText.setText("Lives: " + ship.lives);

						if (ship.lives <= 0) {
							stop();
							gameOverText.setVisible(true); // Set the game over text visible
						}
					}
				});


				// Check if bullets hit any alien
				aliens.forEach(alien -> {
					bullets.forEach(bullet -> {
						if (bullet.collide(alien)) {
							deadBullets.add(bullet);
							deadAliens.add(alien);
							alien.setAlive(false); // Set the alien as not alive
							pane.getChildren().removeAll(bullet.getCharacter(), alien.getCharacter());
						}
					});
				});
				bullets.removeAll(deadBullets);
				aliens.removeAll(deadAliens);

				
				
				if (ship.isInvincible) {
					long timeSinceCollision = System.nanoTime() - ship.lastCollisionTime;
					if (timeSinceCollision < 1e9) { // in 1 s
						double remainingTime = 1 - timeSinceCollision / 1e9;
						String formattedTime = String.format("%.2f", remainingTime);
						invincibleText.setText("Invincible Time: " + formattedTime + "s");
					} else {
						ship.isInvincible = false;
						invincibleText.setText("");
					}
				}
				
				
				
				
				// create separate lists for storing bullets and asteroids to be removed
				//List<Bullet> deadBullets = new ArrayList<>();
				//List<Asteroid> deadAsteroids = new ArrayList<>();

				// iterate over asteroids and bullets
				//increments the score after a bullet hits an asteroid
				asteroids.forEach(asteroid -> {
					// handle collisions with ship
					if (ship.collide(asteroid) && !ship.isInvincible) {
						resetShip(ship);

						livesText.setText("Lives: " + ship.lives);

						if (ship.lives <= 0) {
							stop();
							gameOverText.setVisible(true); // Set the game over text visible
						}
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
					Bullet bullet = new Bullet(ship.getCharacter().getTranslateX(), ship.getCharacter().getTranslateY(),Color.BLUE);
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