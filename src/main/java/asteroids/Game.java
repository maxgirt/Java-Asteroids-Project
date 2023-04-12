package asteroids;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Game {

    private GameListener gameListener;

    private Spaceship spaceship = new Spaceship();
    private List<Sprite> sprites = new ArrayList<>();
    private int score = 0, lives = 3;
    private long lastAsteroidSpawnTime = 0, lastAlienSpawnTime = 0, lastAlienShootTime = 0, lastSpaceshipSpawnTime = 0;
    private static final long INVINCIBILITY_DURATION = 5_000_000_000L; // 5 seconds in nanoseconds

    public Game(GameListener gameListener, boolean difficulty) {
        this.gameListener = gameListener;
        Level.ChangeGameConfig(difficulty);
        gameListener.scoreChanged(score);
        gameListener.livesLeftChanged(lives);

        // Spawns in inital asteroid and spaceship
        sprites.add(spaceship);
    }

    public void gameLoop(long nanotime) {

        if (sprites.stream().anyMatch(sprite -> sprite.checkCollision(sprites))) {
            gameListener.spirteCollided();
        }

        sprites = sprites.stream()
                .flatMap(sprite -> {

                    if (sprite instanceof Bullet
                            && !((Bullet) sprite).checkOutOfBound() && !sprite.checkCollision(sprites)) {
                        return Stream.of(sprite);
                    }

                    // Checks asteroids
                    else if (sprite instanceof Asteroid) {
                        if (sprite.checkCollision(sprites)) {
                            Asteroid.AsteroidSize asteroidSize = ((Asteroid) sprite).getAsteroidSize();
                            if (asteroidSize == Asteroid.AsteroidSize.LARGE) {
                                incrementScore(20);
                                return ((Asteroid) sprite).splitLargeAsteroid(nanotime).stream();
                            } else if (asteroidSize == Asteroid.AsteroidSize.MEDIUM) {
                                incrementScore(50);
                                return ((Asteroid) sprite).splitMediumAsteroid(nanotime).stream();
                            } else {
                                incrementScore(100);
                                return ((Asteroid) sprite).explode(nanotime).stream();
                            }
                        }
                        return Stream.of(sprite);
                    }

                    // Checks spaceship
                    else if (sprite instanceof Spaceship) {
                        if (sprite.checkCollision(sprites)) {
                            if (lives == 0)
                                gameListener.gameOver();
                            return ((Spaceship) sprite).explode(nanotime).stream();
                        } else
                            return Stream.of(sprite);
                    }

                    // Cheks UFO
                    else if (sprite instanceof Alien) {
                        if (sprite.checkCollision(sprites)) {
                            incrementScore(50);
                            return ((Alien) sprite).explode(nanotime).stream();
                        } else
                            return Stream.of(sprite);
                    }

                    return null;

                }).collect(Collectors.toList());


        if (!doesGameContainSpaceship() && lives > 0
                && !sprites.stream().filter(sprite -> sprite instanceof Asteroid)
                .anyMatch(sprite -> sprite.isInsideRectangle(350, 250, 450, 350))) {
            spaceship = new Spaceship(INVINCIBILITY_DURATION);
            sprites.add(spaceship);
            lastSpaceshipSpawnTime = nanotime;
            gameListener.livesLeftChanged(lives -= 1);
        }
        // Spawns asteroids
        if (nanotime >= lastAsteroidSpawnTime + Level.asteroid_spawntime && doesGameContainSpaceship()) {
            sprites.add(new Asteroid());
            lastAsteroidSpawnTime = nanotime;
        }


        if (nanotime >= lastAlienShootTime + Level.alien_fire_delay && !isGameOver()
                && nanotime >= lastSpaceshipSpawnTime + 3500000000l) {
            sprites.addAll(sprites.stream().filter(sprite -> sprite instanceof Alien)
                    .map(sprite -> ((Alien) sprite).shootTowardSpaceship(spaceship.getPosX(), spaceship.getPosY()))
                    .collect(Collectors.toList()));
            lastAlienShootTime = nanotime;

            // 40% chance that any given UFO changes direction.
            sprites.stream().filter(sprite -> sprite instanceof Alien)
                    .forEach(sprite -> {
                        if (Math.random() < 0.4)
                            ((Alien) sprite).changeDirection();
                    });
        }

        // Spawns UFOs
        if (nanotime >= lastAlienSpawnTime + Level.alien_spawntime && !isGameOver()) {
            sprites.add(new Alien());
            lastAlienSpawnTime = nanotime;
        }

        // Updates the position of all the sprites
        sprites.stream().forEach((sprite) -> {
            sprite.updatePosition();
        });
    }

    public int getScore() {
        return score;
    }

    public Spaceship getSpaceship() {
        return spaceship;
    }

    public List<Sprite> getSprites() {
        return sprites;
    }

    public int getLives() {
        return lives;
    }

    public boolean isGameOver() {
        return lives == 0 && !doesGameContainSpaceship();
    }

    public boolean doesGameContainSpaceship() {
        return sprites.contains(spaceship);
    }

    private void incrementScore(int score) {
        gameListener.scoreChanged(this.score += score);
    }
}

