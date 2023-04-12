package asteroids;

public class Vector {

    private double x; // The x coordinate of the vector
    private double y; // The y coordinate of the vector

    // Default constructor: initializes the vector to (0,0)
    public Vector() {
        this.x = 0;
        this.y = 0;
    }

    // Constructor: initializes the vector with given x and y coordinates
    public Vector(double x, double y) {
        setXY(x, y);
    }

    // Returns the x coordinate of the vector
    public double getX() {
        return x;
    }

    // Returns the y coordinate of the vector
    public double getY() {
        return y;
    }

    // Adds the given delta x and delta y values to the current x and y values of the vector
    public void addXY(double deltaX, double deltaY) {
        setXY(x + deltaX, y + deltaY);
    }

    // Sets the angle of the vector while keeping its length unchanged
    public void setAngle(double angle) {
        double length = getLength();
        setXY(length * Math.cos(angle), length * Math.sin(angle));
    }

    // Sets the length of the vector while keeping its angle unchanged
    public void setLength(double length) {
        double currentLength = getLength();
        if (currentLength == 0) {
            // If the vector has zero length, set it to the given length with angle 0
            setXY(length, 0);
        } else {
            // Otherwise, set the vector to the given length while maintaining its angle
            setXY(x * (length / currentLength), y * (length / currentLength));
        }
    }

    // Returns the length (magnitude) of the vector
    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }

    // Sets the x and y coordinates of the vector
    private void setXY(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
