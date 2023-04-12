package Asteroids1;

import java.util.Random;

public class Asteroid extends Character {

    private double rotationalMovement;
    private double size;

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
    
    

    @Override
    public void move() {
        super.move();
        super.getCharacter().setRotate(super.getCharacter().getRotate() + rotationalMovement);
    }
}
