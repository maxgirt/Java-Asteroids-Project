package demo6;

import javafx.scene.shape.Polygon;
import javafx.geometry.Point2D;


public class Ship extends Character {
    public Long elapsedTime;
    public Double jumps;
    private Point2D movement;
    public boolean isInvincible;
    public int lives;
    public long collisionCooldown;
    public long lastCollisionTime;

    public Ship(int x, int y) {
        super(new Polygon(-10, -10, 20, 0, -10, 10), x, y);
        this.elapsedTime = (long) 0;
        this.jumps = 0.0;
        this.movement = new Point2D(0, 0);
        this.isInvincible = false;
        this.lives = 3;
        this.collisionCooldown = 1000000000L; // 1 second cooldown
        this.lastCollisionTime = 0L;
    }
    /*
     * public void setMovement(Point2D movement) { this.movement = movement; }
     */

    //restart the ship
    public void resetShip(int x,int y) {

//        this.character = polygon;
//        this.character.setTranslateX(x);
//        this.character.setTranslateY(y);
//        this.position = new Point2D(x, y);
//        this.movement = new Point2D(0, 0);
//
//
//
//
//
//
//
//
//
//        this.elapsedTime = (long) 0;
//        this.jumps = 0.0;
//        this.movement = new Point2D(0, 0);
//        this.isInvincible = false;
//        this.lives = 3;
//        this.collisionCooldown = 1000000000L; // 1 second cooldown
//        this.lastCollisionTime = 0L;
    }



    private int hyperJumpTimer = 0;
    public void hyperspaceJump(Ship dummyShip) {
        // Get the new position from the dummyShip's position
        double newX = dummyShip.getCharacter().getTranslateX();
        double newY = dummyShip.getCharacter().getTranslateY();

        // Set the new position and reset the movement vector
        this.getCharacter().setTranslateX(newX);
        this.getCharacter().setTranslateY(newY);
        this.setMovement(new Point2D(0,0));
    }



}