package asteroids;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import asteroids.Controllers.AsteroidsController;

public class Asteroid extends Sprite {
    public enum AsteroidSize {
        LARGE(55, 55, Level.asteroid_speed, "asteroids/images/asteroid.png"),
        MEDIUM(39, 39, Level.mediumAsteroid_speed, "asteroids/images/medium_asteroid.png"),
        SMALL(20, 20, Level.smallAsteroid_speed, "asteroids/images/small_asteroid.png");

        private final int width;
        private final int height;
        private final double speed;
        private final String imagePath;

        AsteroidSize(int width, int height, double speed, String imagePath) {
            this.width = width;
            this.height = height;
            this.speed = speed;
            this.imagePath = imagePath;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public double getSpeed() {
            return speed;
        }

        public String getImagePath() {
            return imagePath;
        }
    }
    // Default constructor for Large Asteroids
    public Asteroid() {
        this(Math.random(), AsteroidSize.LARGE);
    }

    // Constructor for creating Asteroids with specified size
    private Asteroid(double randomNumber, AsteroidSize size) {
        super(
                randomNumber > 0.5 ? Math.random() * AsteroidsController.CANVASWIDTH : -size.getWidth(),
                randomNumber < 0.5 ? Math.random() * AsteroidsController.CANVASHEIGHT : -size.getHeight(),
                size.getSpeed(),
                Math.random() * 6.28,
                size.getWidth(), size.getHeight(),
                size.getImagePath()
        );
    }

    // Constructor for Medium and Small Asteroids
    private Asteroid(int x1, int y1, Vector velocity, AsteroidSize size) {
        super(
                x1, y1,
                size.getSpeed(),
                Math.random() * 6.28,
                size.getWidth(), size.getHeight(),
                size.getImagePath()
        );
        getVelocity().addXY(velocity.getX(), velocity.getY());
    }

    // Method to split a large asteroid into medium asteroids
    public List<Sprite> splitLargeAsteroid(long currentTime) {
        List<Sprite> list = new ArrayList<>(
                Arrays.asList(
                        new Asteroid((int) getPosX(), (int) getPosY(), getVelocity(), AsteroidSize.MEDIUM),
                        new Asteroid((int) getPosX(), (int) getPosY(), getVelocity(), AsteroidSize.MEDIUM)
                )
        );
        list.addAll(explode(currentTime));
        return list;
    }

    // Method to split a medium asteroid into small asteroids
    public List<Sprite> splitMediumAsteroid(long currentTime) {
        List<Sprite> list = new ArrayList<>(
                Arrays.asList(
                        new Asteroid((int) getPosX(), (int) getPosY(), getVelocity(), AsteroidSize.SMALL),
                        new Asteroid((int) getPosX(), (int) getPosY(), getVelocity(), AsteroidSize.SMALL)
                )
        );
        list.addAll(explode(currentTime));
        return list;
    }

    // Update the position of the asteroid and handle wrapping
    @Override
    public void updatePosition() {
        super.updatePosition();
        wrap();
    }

    // Check if the asteroid has collided with another sprite
    public boolean checkCollision(Collection<Sprite> list) {
        return list.stream()
                .filter(sprite -> sprite instanceof Bullet || sprite instanceof Spaceship)
                .anyMatch(sprite -> overlapsSprite(sprite));
    }

    // Check if the asteroid is large or medium
    public AsteroidSize getAsteroidSize() {
        if (getImageWidth() == AsteroidSize.LARGE.getWidth()) {
            return AsteroidSize.LARGE;
        } else if (getImageWidth() == AsteroidSize.MEDIUM.getWidth()) {
            return AsteroidSize.MEDIUM;
        }
        return null;
    }
}
