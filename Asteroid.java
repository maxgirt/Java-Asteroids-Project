package demo6;

import javafx.geometry.Point2D;

import java.util.Random;


public class Asteroid extends Character implements  Cloneable {

    private double rotationalMovement;
    private double size;

    @Override
    public Asteroid clone() throws CloneNotSupportedException {
        Asteroid asteroid=(Asteroid) super.clone();
        return  asteroid;
    }

    public Asteroid(double x, double y, double size, double speed) {
        super(new PolygonMaker().createPolygon(size), x, y);
        Random rnd = new Random();
        super.getCharacter().setRotate(rnd.nextInt(360));
        double accelerationAmount = speed + rnd.nextInt(5);
        for (int i = 0; i < accelerationAmount; i++) {
            accelerate();
        }
        this.size=size;
        this.rotationalMovement = 0.5 - rnd.nextDouble();
    }
    public double getSize() {
        return this.size;
    }

    public void resetAsteroid(double x, double y, double size, double speed){
        this.character = new PolygonMaker().createPolygon(size);
        this.character.setTranslateX(x);
        this.character.setTranslateY(y);
        this.position = new Point2D(x, y);
        this.setMovement(new Point2D(0, 0));
        Random rnd = new Random();
        super.getCharacter().setRotate(rnd.nextInt(360));
        double accelerationAmount = speed + rnd.nextInt(5);
        for (int i = 0; i < accelerationAmount; i++) {
            accelerate();
        }
        this.size=size;
        this.rotationalMovement = 0.5 - rnd.nextDouble();
    }

    @Override
    public void move() {
        super.move();
        super.getCharacter().setRotate(super.getCharacter().getRotate() + rotationalMovement);
    }
}
