package Asteroids2;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import java.util.Random;

public class Alien extends Character {
    private static final Random random = new Random();


    private Point2D alienMovement;
    private boolean isAlive;



    public Alien(double x, double y) {
        super(createShape(), x, y);
        getCharacter().setFill(Color.BLACK);
        getCharacter().setStroke(Color.RED);
        getCharacter().setStrokeWidth(2.0);
        Random rnd = new Random();
        double randomDirection = rnd.nextDouble() * 360;
        double speed = 1.0;
        alienMovement = new Point2D(Math.cos(Math.toRadians(randomDirection)), Math.sin(Math.toRadians(randomDirection))).multiply(speed);
        isAlive = true;
    }

    private static Polygon createShape() {
        Polygon ufoShape = new Polygon();
        double bodyWidth = 60;
        double bodyHeight = 30;
        double cabinWidth = 30;
        double cabinHeight = 15;
        int numOfPoints = 30;
        double angleIncrement = 180.0 / numOfPoints;

        // UFO bottom
        for (int i = 0; i <= numOfPoints; i++) {
            double currentAngle = Math.toRadians(i * angleIncrement);
            double x = bodyWidth / 2 * Math.cos(currentAngle);
            double y = bodyHeight / 2 * Math.sin(currentAngle);

            ufoShape.getPoints().addAll(x, y);
        }

        // UFO top
        for (int i = numOfPoints; i >= 0; i--) {
            double currentAngle = Math.toRadians(i * angleIncrement);
            double x = cabinWidth / 2 * Math.cos(currentAngle);
            double y = -cabinHeight / 2 * Math.sin(currentAngle);

            ufoShape.getPoints().addAll(x, y);
        }

        // UFO bottom edge
        for (int i = 0; i <= numOfPoints; i++) {
            double currentAngle = Math.toRadians(i * angleIncrement);
            double x = (bodyWidth / 2 - 5) * Math.cos(currentAngle);
            double y = (bodyHeight / 2 - 5) * Math.sin(currentAngle);

            ufoShape.getPoints().addAll(x, y);
        }

        ufoShape.setFill(Color.BLACK);
        ufoShape.setStroke(Color.GREEN);
        ufoShape.setStrokeWidth(2.0);

        return ufoShape;
    }
    
    @Override
    public void move() {
        getCharacter().setTranslateX(getCharacter().getTranslateX() + alienMovement.getX());
        getCharacter().setTranslateY(getCharacter().getTranslateY() + alienMovement.getY());

        // Wrap the Alien around the screen edges
        if (getCharacter().getTranslateX() > Asteroids.paneWidth) {
            getCharacter().setTranslateX(0);
        } else if (getCharacter().getTranslateX() < 0) {
            getCharacter().setTranslateX(Asteroids.paneWidth);
        }

        if (getCharacter().getTranslateY() > Asteroids.paneHeight) {
            getCharacter().setTranslateY(0);
        } else if (getCharacter().getTranslateY() < 0) {
            getCharacter().setTranslateY(Asteroids.paneHeight);
        }
    }

    public void moveRandomly() {
        double angle = Math.toRadians(random.nextDouble() * 360);
        double changeX = Math.cos(angle);
        double changeY = Math.sin(angle);

        changeX *= 0.5;
        changeY *= 0.5;

        setMovement(getMovement().add(changeX, changeY));
        move();
    }
    public void setRandomStartPosition(int paneWidth, int paneHeight) {
        Random rnd = new Random();
        int side = rnd.nextInt(4);
        switch (side) {
            case 0: // 上边缘
                getCharacter().setTranslateX(rnd.nextInt(paneWidth));
                getCharacter().setTranslateY(0);
                break;
            case 1: // 右边缘
                getCharacter().setTranslateX(paneWidth);
                getCharacter().setTranslateY(rnd.nextInt(paneHeight));
                break;
            case 2: // 下边缘
                getCharacter().setTranslateX(rnd.nextInt(paneWidth));
                getCharacter().setTranslateY(paneHeight);
                break;
            case 3: // 左边缘
                getCharacter().setTranslateX(0);
                getCharacter().setTranslateY(rnd.nextInt(paneHeight));
                break;
        }
    }
    public double calculateAngleToShip(Ship ship) {
        double deltaX = ship.getCharacter().getTranslateX() - getCharacter().getTranslateX();
        double deltaY = ship.getCharacter().getTranslateY() - getCharacter().getTranslateY();
        return Math.toDegrees(Math.atan2(deltaY, deltaX));
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public boolean isAlive() {
        return isAlive;
    }
}