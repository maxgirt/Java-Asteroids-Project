package asteroids;

import java.lang.Math;
import java.util.Collection;

public class Spaceship extends Sprite {

    private final String THRUSTIMAGE_URL = "asteroids/images/spaceship_thrust.png";
    private Boolean showThrust = false;
    private double rotation = 3 * Math.PI / 2;

    // Add invincible and invincibilityEndTime fields
    private boolean invincible = false;
    private long invincibilityEndTime;

    public Spaceship() {
        super(400, 300, 0, 0, 39, 23, "asteroids/images/spaceship.png");
    }

    public void rotateLeft() {
        rotation -= Level.spaceship_rotationSpeed;
    }

    public void rotateRight() {
        rotation += Level.spaceship_rotationSpeed;
    }

    public double getRotation() {
        return rotation;
    }

    public Sprite shoot() {
        return new Bullet(getPosX() + getImageWidth() / 2.0 - 4 + 15 * Math.cos(rotation),
                getPosY() + getImageHeight() / 2.0 - 4 + 15 * Math.sin(rotation),
                getVelocity().getLength(), getRotation(), true);
    }

    public void thrust() {
        showThrust = true;
        velocity.addXY(Math.cos(getRotation()) * Level.spaceship_acceleration_increase,
                Math.sin(getRotation()) * Level.spaceship_acceleration_increase);
    }

    private void aeroBrake() {
        velocity.addXY(Level.spaceship_acceleration_reduction * velocity.getX(),
                Level.spaceship_acceleration_reduction * velocity.getY());
    }
    // Modify constructor to accept invincibility duration
    public Spaceship(long invincibilityDuration) {
        super(400, 300, 0, 0, 39, 23, "asteroids/images/spaceship.png");
        invincible = true;
        invincibilityEndTime = System.nanoTime() + invincibilityDuration;
    }

    // Add isInvincible() method
    public boolean isInvincible() {
        if (invincible && System.nanoTime() > invincibilityEndTime) {
            invincible = false;
        }
        return invincible;
    }



    @Override
    public String getImageURL() {
        if (showThrust) {
            showThrust = false;
            return THRUSTIMAGE_URL;
        }
        return super.getImageURL();
    }

    @Override
    public void updatePosition() {
        super.updatePosition();
        wrap();
        aeroBrake();
    }

    // Update checkCollision method to return false if spaceship is invincible
    public boolean checkCollision(Collection<Sprite> list) {
        if (isInvincible()) {
            return false;
        }

        return list.stream().filter(
                        sprite -> (sprite instanceof Bullet && !((Bullet) sprite).isFriendly()) || sprite instanceof Alien
                                || sprite instanceof Asteroid)
                .anyMatch(sprite -> overlapsSprite(sprite));
    }

}
