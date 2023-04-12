package asteroids;

import java.util.Collection;
import asteroids.Controllers.AsteroidsController;

public class Alien extends Sprite {

    public Alien() {
        this(Math.random());
    }


    // Constructor for Alien class with random number as input
    private Alien(double randomNumber) {
        super(
                getRandomPosition(randomNumber > 0.5, AsteroidsController.CANVASWIDTH, -55),
                getRandomPosition(randomNumber < 0.5, AsteroidsController.CANVASHEIGHT, -35),
                Level.alien_speed,
                getRandomAngle(randomNumber),
                55, 35,
                "asteroids/images/ufo.png"
        );
    }

    private static double getRandomPosition(boolean condition, double max, double defaultValue) {
        return condition ? Math.random() * max : defaultValue;
    }

    private static double getRandomAngle(double randomNumber) {
        // If randomNumber is less than 0.15, generate an angle in the range (-PI/4, PI/4)
        if (randomNumber < 0.15) {
            return Math.random() * Math.PI / 2 - Math.PI / 4;
        }

        else if (randomNumber < 0.3) {
            return Math.random() * Math.PI / 2 + 3 * Math.PI / 4;
        }

        else if (randomNumber < 0.6) {
            return Math.random() * Math.PI / 2 + Math.PI / 4;
        }

        else {
            return Math.random() * Math.PI / 2 + 5 * Math.PI / 4;
        }
    }

    // Update the position of the alien and handle wrapping
    @Override
    public void updatePosition() {
        super.updatePosition();
        wrap();
    }

    // Check if the alien has collided with another sprite
    public boolean checkCollision(Collection<Sprite> list) {
        return list.stream()
                .filter(sprite -> ((sprite instanceof Bullet && ((Bullet) sprite).isFriendly()) || sprite instanceof Spaceship))
                .anyMatch(sprite -> overlapsSprite(sprite));
    }

    // Shoot laser towards the spaceship
    public Bullet shootTowardSpaceship(Double x1, Double y1) {
        double result = Math.atan2((y1 - this.y1), (-this.x1 + x1));

        // Add a random spread of 30 degrees in either direction
        result += Math.random() * 2 * Level.alien_aim_accuracy - Level.alien_aim_accuracy;
        return new Bullet(
                getPosX() + getImageWidth() / 2 - 4 + 20 * Math.cos(result),
                getPosY() + getImageHeight() / 2 - 4 + 20 * Math.sin(result),
                1.5,
                result,
                false
        );
    }

    // Change the direction of the alien
    public void changeDirection() {
        getVelocity().setAngle(Math.random() * 2 * Math.PI);
    }
}
