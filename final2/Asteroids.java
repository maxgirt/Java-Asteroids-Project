package Asteroids2;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import javafx.animation.AnimationTimer;
import javafx.animation.FillTransition;
import javafx.application.Platform;
import javafx.util.Duration;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;

import static Asteroids2.Asteroid.Size.*;


public class Asteroids  {


	public boolean gameOver = false;
	boolean levelStarted = false;
	public boolean gameInProgress = false;
	public boolean alienFlag = false;
	public boolean canShoot = true;
	public static int PANE_HEIGHT = 800;
	public static final int PANE_WIDTH = 1200;
    int currentLevel = 1;
    int maximumLevel = 5;
    public static Ship ship = new Ship(PANE_WIDTH / 2,PANE_HEIGHT / 2);

    private List<Asteroid> asteroids = new ArrayList<>();
	public List<Bullet> bullets = new ArrayList<>();
	public Map<KeyCode, Boolean> pressedKeys = new HashMap<>(); // keys are mapped to values, 
	public int score = 0;
	public Stage menuStage;

	private GameText gameText;
	
	
	private ThemeMusic sfx1 = new ThemeMusic();
    private Sfx sfx2 = new Sfx();
    private Sfx sfx3 = new Sfx();
    private Sfx sfx4 = new Sfx();
    private Font font = Font.font("Lucida Console", FontWeight.BOLD, 20); // create a Font object with desired font settings
    private Font fontLarge = Font.font("Lucida Console", FontWeight.BOLD, 40); // create a Font object with desired font settings
    private Font fontSmall = Font.font("Lucida Console", FontWeight.BOLD, 14); // create a Font object with desired font settings

    Image image = new Image("file:src/images/grayBackground.png");
    BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
    Background background = new Background(backgroundImage);



	public void startGame(Stage menuStage, Stage stage, Menu menu) throws Exception {

	    ship.lives = 3;

		Random rnd = new Random();
		Pane pane = new Pane();
		pane.setPrefSize(PANE_WIDTH, PANE_HEIGHT);

		ship.getCharacter().setFill(Color.TRANSPARENT);
        ship.getCharacter().setStroke(Color.GHOSTWHITE);
		Scene scene = new Scene(pane);
		stage.setTitle("Asteroids");


		// Create a GameText instance and initialize the text objects
		gameText = new GameText(pane);
		gameText.initializeText(score,ship,currentLevel);

		stage.setScene(scene);
		stage.show();
	    System.out.println("startGame Pane: " + pane.hashCode());
		pane.getChildren().add(ship.getCharacter());



		Asteroid asteroid = new Asteroid(rnd.nextInt(PANE_WIDTH), rnd.nextInt(PANE_HEIGHT), LARGE);

		// Set the stroke color of the asteroid to transparent
        asteroid.getCharacter().setStroke(Color.WHITESMOKE);



        // Set the stroke type of the asteroid to CENTERED
        asteroid.getCharacter().setStrokeType(StrokeType.CENTERED);

        // Set the stroke width of the asteroid to the desired thickness
        asteroid.getCharacter().setStrokeWidth(1);
        asteroids.add(asteroid);
        pane.getChildren().add(asteroid.getCharacter());
        pane.setBackground(background);

		//asteroids.forEach(asteroid -> pane.getChildren().add(asteroid.getCharacter()));

//		Map<KeyCode, Boolean> pressedKeys = new HashMap<>(); // keys are mapped to values, 
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

		
		new AnimationTimer() {
			double shootTimer = System.nanoTime()-1e8;
			long lastUpdateTime = System.nanoTime();

			@Override
			public void handle(long nanotime) {
			    new Thread(() -> {
			        sfx1.playMainTheme();
			    }).start();
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
                    	sfx2.hypersound();
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
				

				if (currentLevel % 1 == 0 && !alienFlag) {
					Alien newAlien = new Alien(0, 0);
					newAlien.setRandomStartPosition(PANE_WIDTH, PANE_HEIGHT);
					aliens.add(newAlien);
					pane.getChildren().add(newAlien.getCharacter());
					alienSpawnTimer.set(System.nanoTime());
					alienFlag = true;
				}
				

				// Alien moves continuously
				aliens.forEach(a -> a.move());

				// handle collisions with alien
				aliens.forEach(a -> {
					// handle collisions with ship
					if (ship.collide(a) && !ship.isInvincible) {
						ship.resetShip();
						FillTransition ft = new FillTransition(Duration.millis(250), ship.getCharacter(), Color.BLACK, Color.WHITE);
						ft.setCycleCount(8); // repeat the transition 4 times
						ft.setAutoReverse(true); // reverse the transition back to black
						ft.play();
						gameText.updateLive(ship.lives);

						if (ship.lives <= 0) {
							stop();
							gameText.setGameOverTextVisible(true);  // Set the game over text visible
							Platform.runLater(() -> {
								TextInputDialog dialog = new TextInputDialog();
								dialog.setTitle("Enter Name");
								dialog.setHeaderText(null);
								dialog.setContentText("Please enter your name:");
								Optional<String> result = dialog.showAndWait();
								if (result.isPresent()) {
								    String name = result.get();
								    HighScores highScores = new HighScores("high_scores.txt");
								    highScores.saveNameAndScore(name, score);
								    stage.hide();
								    highScores.display(menuStage, highScores.loadScores());
								    

							}
							});
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
					if (bullet.getDistance() > 15) {
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
						ship.resetShip();
						FillTransition ft = new FillTransition(Duration.millis(250), ship.getCharacter(), Color.BLACK, Color.WHITE);
						ft.setCycleCount(8); // repeat the transition 4 times
						ft.setAutoReverse(true); // reverse the transition back to black
						ft.play();

						gameText.updateLive(ship.lives);

						if (ship.lives <= 0) {
							stop();
							gameText.setGameOverTextVisible(true);  // Set the game over text visible
							Platform.runLater(() -> {
								TextInputDialog dialog = new TextInputDialog();
								dialog.setTitle("Enter Name");
								dialog.setHeaderText(null);
								dialog.setContentText("Please enter your name:");
								Optional<String> result = dialog.showAndWait();
								if (result.isPresent()) {
								    String name = result.get();
								    HighScores highScores = new HighScores("high_scores.txt");
								    highScores.saveNameAndScore(name, score);
								    stage.hide();
								    highScores.display(menuStage, highScores.loadScores());
							}
							});
						}
					}
				});


				// Check if bullets hit any alien
				aliens.forEach(alien -> {
					bullets.forEach(bullet -> {
						if (bullet.collide(alien)) {
							points.addAndGet(125);
							score += 125;// increment the score by 125 for each destroyed
							gameText.updateScore(score); // update the score display
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
					if (timeSinceCollision < 2.5e9) { // in 1 s
						gameText.updateInvincibleText(ship.isInvincible, timeSinceCollision);
					} else {
						ship.isInvincible = false;
						gameText.updateInvincibleText(ship.isInvincible, 0);
					}
				}




				// create separate lists for storing bullets and asteroids to be removed
				//List<Bullet> deadBullets = new ArrayList<>();
				//List<Asteroid> deadAsteroids = new ArrayList<>();

				// iterate over asteroids and bullets
				//increments the score after a bullet hits an asteroid
				asteroids.forEach(asteroid -> {
					//FOR MAX! Adding splitting asteroids for when the player hits it
					if (ship.collide(asteroid) && asteroid.getSize() == Asteroid.Size.LARGE) {
						List<Asteroid> newMediumAsteroids = asteroid.splitAsteroid(asteroid, Asteroid.Size.MEDIUM);
						mediumAsteroids.addAll(newMediumAsteroids);
						pane.getChildren().addAll(newMediumAsteroids.stream().map(Asteroid::getCharacter).collect(Collectors.toList()));
						deadAsteroids.add(asteroid);
						pane.getChildren().remove(asteroid.getCharacter());
					} else if (ship.collide(asteroid) && asteroid.getSize() == Asteroid.Size.MEDIUM) {
						List<Asteroid> newSmallAsteroids = asteroid.splitAsteroid(asteroid, Asteroid.Size.SMALL);
						smallAsteroids.addAll(newSmallAsteroids);
						pane.getChildren().addAll(newSmallAsteroids.stream().map(Asteroid::getCharacter).collect(Collectors.toList()));
						deadAsteroids.add(asteroid);
						pane.getChildren().remove(asteroid.getCharacter());
					} else if (ship.collide(asteroid)) {
						deadAsteroids.add(asteroid);
						pane.getChildren().remove(asteroid.getCharacter());
					}



					if (ship.collide(asteroid) && !ship.isInvincible) {
						ship.resetShip();
						FillTransition ft = new FillTransition(Duration.millis(250), ship.getCharacter(), Color.BLACK, Color.WHITE);
						ft.setCycleCount(8); // repeat the transition 4 times
						ft.setAutoReverse(true); // reverse the transition back to black
						ft.play();


						gameText.updateLive(ship.lives);

						if (ship.lives <= 0) {
							sfx4.gameover();
							sfx1.stopMainTheme();
							stop();
							gameText.setGameOverTextVisible(true);  // Set the game over text visible
							Platform.runLater(() -> {
								TextInputDialog dialog = new TextInputDialog();
								dialog.setTitle("Enter Name");
								dialog.setHeaderText(null);
								dialog.setContentText("Please enter your name:");
								Optional<String> result = dialog.showAndWait();
								if (result.isPresent()) {
									String name = result.get();
									HighScores highScores = new HighScores("high_scores.txt");
									highScores.saveNameAndScore(name, score);
									stage.hide();
									highScores.display(menuStage, highScores.loadScores());
								}
							});
						}
					}



					bullets.forEach(bullet -> {
						if (bullet.collide(asteroid) && asteroid.getSize() == LARGE) {
							points.addAndGet(25);
							score += 25;// increment the score by 25 for each destroyed
							gameText.updateScore(score);// update the score display
							List<Asteroid> newMediumAsteroids = asteroid.splitAsteroid(asteroid, Asteroid.Size.MEDIUM);
							mediumAsteroids.addAll(newMediumAsteroids);
							pane.getChildren().addAll(newMediumAsteroids.stream().map(Asteroid::getCharacter).collect(Collectors.toList()));
							deadAsteroids.add(asteroid);
							deadBullets.add(bullet);
							pane.getChildren().remove(asteroid.getCharacter());

						} else if (bullet.collide(asteroid) && asteroid.getSize() == MEDIUM) {
							points.addAndGet(50);
							score += 50;// increment the score by 25 for each destroyed
							gameText.updateScore(score); // update the score display
							List<Asteroid> newSmallAsteroids = asteroid.splitAsteroid(asteroid, Asteroid.Size.SMALL);
							smallAsteroids.addAll(newSmallAsteroids);
							pane.getChildren().addAll(newSmallAsteroids.stream().map(Asteroid::getCharacter).collect(Collectors.toList()));
							deadAsteroids.add(asteroid);
							deadBullets.add(bullet);
							pane.getChildren().remove(asteroid.getCharacter());

						} else if (bullet.collide(asteroid)) {
							points.addAndGet(100);
							score += 100;// increment the score by 25 for each destroyed
							gameText.updateScore(score);// update the score display
							deadBullets.add(bullet);
							deadAsteroids.add(asteroid);
							pane.getChildren().remove(asteroid.getCharacter());

						}

						// remove the bullet from the pane when it collides with an asteroid
						if (bullet.collide(asteroid) && asteroid.getSize() == LARGE ||
								bullet.collide(asteroid) && asteroid.getSize() == MEDIUM ||
								bullet.collide(asteroid)) {
							pane.getChildren().remove(bullet.getCharacter());
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

				if (pressedKeys.getOrDefault(KeyCode.SPACE, false) && canShoot && System.nanoTime()-shootTimer >= 2e8 && bullets.size() < 5) {
				    sfx3.lazersound(); // play the sound effect first
				    shootTimer = System.nanoTime(); // reset the shoot timer

				    Bullet bullet = new Bullet(ship.getCharacter().getTranslateX(), ship.getCharacter().getTranslateY(),Color.BLUE);
				    bullet.getCharacter().setRotate(ship.getCharacter().getRotate());
				    bullets.add(bullet);

				    Point2D bulletVelocity = new Point2D(Math.cos(Math.toRadians(bullet.getCharacter().getRotate())),
				            Math.sin(Math.toRadians(bullet.getCharacter().getRotate())))
				            .multiply(5);
				    bullet.setMovement(ship.getMovement().add(bulletVelocity));

				    pane.getChildren().add(bullet.getCharacter());
				}
				for (Bullet bullet : bullets) {

					if (bullet.getDistance() > 15) { // if bullet has been alive for more than 100 frames
						deadBullets.add(bullet); // add bullet to removal list
						pane.getChildren().remove(bullet.getCharacter()); // remove bullet's character from pane
					}
				}

				bullets.removeAll(deadBullets);
				//System.out.println(ship.jumps);
			
			
			// logic to increase level 
				
				if (asteroids.isEmpty() && !levelStarted && aliens.isEmpty()) {
					  canShoot = false;
				      levelStarted = true;
					  levelScreen(rnd, pane);
					}
				
				
				if (ship.jumps >=1) {
                    Text jumpIsOn = new Text("ON");
                    jumpIsOn.setFont(font);
                    jumpIsOn.setFill(Color.LAWNGREEN);
                    jumpIsOn.setLayoutX(PANE_WIDTH*0.36);
                    jumpIsOn.setLayoutY(PANE_HEIGHT*0.04);

                    pane.getChildren().removeIf(node -> node instanceof Text && ((Text) node).getText().equals("OFF")); // remove "OFF" Text node

                    pane.getChildren().add(jumpIsOn);
                }
                else {
                    Text jumpIsOff = new Text("OFF");
                    jumpIsOff.setFont(font);
                    jumpIsOff.setFill(Color.RED);
                    jumpIsOff.setLayoutX(PANE_WIDTH*0.36);
                    jumpIsOff.setLayoutY(PANE_HEIGHT*0.04);

                    pane.getChildren().removeIf(node -> node instanceof Text && ((Text) node).getText().equals("ON")); // remove "ON" Text node
                    pane.getChildren().add(jumpIsOff);
                }

		       }
			
			
			
		}.start();
	}

	public void levelScreen(Random rnd, Pane pane) {
		clearBullets(pane);
		Button continueButton = new Button("Start Game");
	    continueButton.setPrefSize(120, 40); // Set preferred width and height
	    continueButton.setFont(font);

		Text levelText = new Text("Level: " + currentLevel);
		levelText.setFont(fontSmall);
        levelText.setFill(Color.WHITESMOKE);

        levelText.setLayoutX(PANE_WIDTH/2 - 42);
        levelText.setLayoutY(PANE_HEIGHT/2 - 75);
        pane.getChildren().add(levelText);		
		
	  // Set the button's position after it is added to the scene graph
		continueButton.setLayoutX(PANE_WIDTH/2 - 60);
		continueButton.setLayoutY(PANE_HEIGHT/2 - 60);
		pane.getChildren().add(continueButton);
		
        System.out.println("levelScreen Pane: " + pane.hashCode());
		continueButton.setOnMouseClicked(e -> {
		    try {
		    	incrementLevel();
		        System.out.println("Button clicked!");
		        while (!gameOver) {
		        for (int i = 0; i < currentLevel; i++) {
		        	Asteroid asteroid = new Asteroid(rnd.nextInt(PANE_WIDTH), rnd.nextInt(PANE_HEIGHT), LARGE);
                    asteroid.getCharacter().setStroke(Color.WHITESMOKE);



                    // Set the stroke type of the asteroid to CENTERED
                    asteroid.getCharacter().setStrokeType(StrokeType.CENTERED);

                    // Set the stroke width of the asteroid to the desired thickness
                    asteroid.getCharacter().setStrokeWidth(1);

                    asteroids.add(asteroid);
                    pane.getChildren().add(asteroid.getCharacter());
		        } break;}
		        ship.resetPosition(150,100);
		        pressedKeys.clear();
		        pane.getChildren().remove(continueButton);
		        pane.getChildren().remove(levelText);
		        alienFlag = false;
		        levelStarted = false;
		        canShoot = true;
	            gameInProgress = true;
                int jumpX = rnd.nextInt(600);
                int jumpY = rnd.nextInt(400);
	            Ship dummyShip = new Ship(jumpX, jumpY);
	            ship.hyperspaceJump(dummyShip);
		    } catch (Exception ex) {
		        ex.printStackTrace();
		    }
		});	
		}
	
	//Method which clears the screen to add a new one:
	public void clearBullets(Pane pane) {
		for (Bullet bullet : bullets) {
		    pane.getChildren().remove(bullet.getCharacter());
		}

		}
	
	
	public void clearScreen(Pane pane) {
	    pane.getChildren().clear();
	}

	
	// Method for incrementing the current level
	private void incrementLevel() {
		this.currentLevel += 1;
		gameText.updateLevelText(currentLevel);

	}
	}