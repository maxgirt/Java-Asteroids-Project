import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.Map;
import java.util.HashMap;
import javafx.animation.AnimationTimer;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import java.lang.*;
import java.util.concurrent.atomic.AtomicLong;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;




public class Asteroids extends Application {

	public static int paneHeight = 400;
	public static int paneWidth = 600;
	private void resetShip(Ship ship) {
		ship.lives--; // 飞船生命值减1
		ship.isInvincible = true; // 将飞船设置为无敌状态
		ship.getCharacter().setTranslateX(paneWidth / 2);
		ship.getCharacter().setTranslateY(paneHeight / 2);
		ship.lastCollisionTime = System.nanoTime(); // 记录碰撞发生的时间
	}

	@Override
	public void start(Stage stage) throws Exception {
		Random rnd = new Random();
		Pane pane = new Pane();
		pane.setPrefSize(paneWidth, paneHeight);

		Scene scene = new Scene(pane);
		stage.setTitle("Asteroids");
		stage.setScene(scene);
		stage.show();

		Ship ship = new Ship(paneWidth / 2, paneHeight / 2);
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

//		Alien alien = new Alien(0, 0);
//		alien.setRandomStartPosition(paneWidth, paneHeight);
//		pane.getChildren().add(alien.getCharacter());

		List<Bullet> alienBullets = new ArrayList<>();
		List<Bullet> deadAlienBullets = new ArrayList<>();

		AtomicLong alienTimer = new AtomicLong(System.nanoTime() - (long)1e9);
		AtomicLong alienShootTimer = new AtomicLong(System.nanoTime() - (long)1e9);
//		AtomicLong alienSpawnTimer = new AtomicLong(System.nanoTime());


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



		List<Bullet> bullets = new ArrayList<>();
		List<Bullet> deadBullets = new ArrayList<>();
		List<Asteroid> deadAsteroids = new ArrayList<>();
		List<Asteroid> mediumAsteroids = new ArrayList<>();
		List<Asteroid> smallAsteroids = new ArrayList<>();
		List<Alien> aliens = new ArrayList<>();
//		aliens.add(alien);
		AtomicLong alienSpawnTimer = new AtomicLong(System.nanoTime());

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
//				alien.move();

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

				if (System.nanoTime() - alienSpawnTimer.get() >= 5e9) { //Every 5 seconds, a new Alien is generated
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
							pane.getChildren().remove(alien.getCharacter());
							alien.setAlive(false); // Set the alien as not alive
						}
					});
				});

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
				
				ship.move();
				asteroids.forEach(asteroid -> asteroid.move());
				bullets.forEach(bullet -> bullet.move());

				// create separate lists for storing bullets and asteroids to be removed
				//List<Bullet> deadBullets = new ArrayList<>();
				//List<Asteroid> deadAsteroids = new ArrayList<>();

				// iterate over asteroids and bullets
				asteroids.forEach(asteroid -> {
					// handle collisions with ship
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
					Bullet bullet = new Bullet(ship.getCharacter().getTranslateX(), ship.getCharacter().getTranslateY(), Color.BLACK);
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
