package Asteroids2;

import java.util.ArrayList;
import java.util.Random;

import java.util.List;
import javafx.scene.paint.Color;

public class Asteroid extends Character {

    private double rotationalMovement;
    private Size size;

    public enum Size {
        LARGE(50, 10),
        MEDIUM(30, 35),
        SMALL(20, 45);

        private final int size;
        private final int speed;

        Size(int size, int speed) {
            this.size = size;
            this.speed = speed;
        }

        public int getSize() {
            return size;
        }

        public int getSpeed() {
            return speed;
        }
    }

    public Asteroid(double x, double y, Size size) {
        super(new PolygonMaker().createPolygon(size.getSize()), x, y);

        Random rnd = new Random();
        super.getCharacter().setRotate(rnd.nextInt(360));

        double accelerationAmount = size.getSpeed() + rnd.nextInt(5);
        for (int i = 0; i < accelerationAmount; i++) {
            accelerate();
        }
        this.size = size;
        this.rotationalMovement = 0.5 - rnd.nextDouble();
    }

    public Size getSize() {
        return this.size;
    }

    public List<Asteroid> splitAsteroid(Asteroid asteroid, Asteroid.Size newSize) {
        List<Asteroid> newAsteroids = new ArrayList<>();

        Asteroid newAsteroid1 = new Asteroid(asteroid.getCharacter().getTranslateX(),
                asteroid.getCharacter().getTranslateY(), newSize);
        newAsteroid1.getCharacter().setStroke(Color.WHITESMOKE);
        newAsteroids.add(newAsteroid1);

        Asteroid newAsteroid2 = new Asteroid(asteroid.getCharacter().getTranslateX(),
                asteroid.getCharacter().getTranslateY(), newSize);
        newAsteroid2.getCharacter().setStroke(Color.WHITESMOKE);
        newAsteroids.add(newAsteroid2);

        return newAsteroids;
    }
    @Override
    public void move() {
        super.move();
        super.getCharacter().setRotate(super.getCharacter().getRotate() + rotationalMovement);
    }
}
