package asteroids;

import asteroids.Controllers.AsteroidsController;
import java.util.Collection;



public class Bullet extends Sprite {

    private boolean friendly;

    public Bullet(double x1, double y1, double speed, double speedDirection, boolean friendly) {
        super(x1, y1, speed + Level.bullet_speed, speedDirection, 8, 8,
                friendly ? "asteroids/images/laser.png" : "asteroids/images/laser_red.png");
        this.friendly = friendly;
    }

    public boolean checkOutOfBound() {
        return x1 > AsteroidsController.CANVASWIDTH || x2 < 0 || y1 > AsteroidsController.CANVASHEIGHT || y2 < 0;
    }

    public boolean checkCollision(Collection<Sprite> list) {
        return list.stream()
                .filter(sprite -> (sprite instanceof Spaceship && !isFriendly()) || sprite instanceof Asteroid)
                .anyMatch(asteroid -> overlapsSprite(asteroid));
    }

    public boolean isFriendly() {
        return friendly;
    }

}
