package Asteroids1;
import javafx.scene.shape.Polygon;
import javafx.geometry.Point2D;

public class Ship extends Character{
	public Long elapsedTime;
    public Double jumps;
    private Point2D movement;
	public Ship(int x, int y) {
		super(new Polygon(-5, -5, 10, 0, -5, 5), x, y);
		this.elapsedTime = (long) 0;
        this.jumps = 0.0;
        this.movement = new Point2D(0, 0);
	}
	/*
	 * public void setMovement(Point2D movement) { this.movement = movement; }
	 */
	
	public void hyperspaceJump(Ship dummyShip) {
	    // Get the new position from the dummyShip's position
	    double newX = dummyShip.getCharacter().getTranslateX();
	    double newY = dummyShip.getCharacter().getTranslateY();

	    // Set the new position and reset the movement vector
	    this.getCharacter().setTranslateX(newX);
	    this.getCharacter().setTranslateY(newY);
	    this.setMovement(new Point2D(0,0));
	}


	
	
	
	/*
	 * public void update(double deltaTime) { this.elapsedTime += deltaTime;
	 * this.position.add(this.getMovement().getX()*deltaTime,
	 * this.getMovement().getY()*deltaTime); this.move(); }
	 */
}