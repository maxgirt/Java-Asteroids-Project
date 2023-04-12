package com.example.asteroids;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite {
    public Vector position;
    public Vector velocity;
    public Double rotation; //degrees
    public Rectangle boundary;
    public Image image;
    public Double elapsedTime;
    public Double jumps;

    public Sprite() {
        this.position = new Vector();
        this.velocity = new Vector();
        this.rotation = 0.0;
        this.boundary = new Rectangle();
        this.elapsedTime = 0.0;
        this.jumps = 0.0;



    }

    public Sprite(String imageFileName) {
        this();
        setImage(imageFileName);
    }

    public void setImage(String imageFileName) {
        this.image = new Image(imageFileName);
        this.boundary.setSize(this.image.getWidth(), this.image.getHeight());

    }

    public Rectangle getBoundary() {
        this.boundary.setPosition(this.position.x, this.position.y);
        return this.boundary;
    }

    public boolean overlaps(Sprite other) {
        return this.getBoundary().overlaps(other.getBoundary());

    }

    public void wrap(double screenWidth, double screenHeight){
        // TODO: center object
        double halfWidth = this.image.getWidth()/2;
        double halfHeight = this.image.getHeight()/2;
        if (this.position.x + halfWidth < 0)
            this.position.x = screenWidth + halfWidth;
        if (this.position.x > screenWidth + halfWidth)
            this.position.x = -halfWidth;
        if (this.position.y + halfHeight < 0)
            this.position.y = screenHeight + halfHeight;
        if (this.position.y > screenHeight + halfHeight)
            this.position.y = -halfHeight;
    }
    public void update(double deltaTime) { //changing from bool to void to avoid errro

        // increase elapsed time
        this.elapsedTime += deltaTime;
        //update the position according to velocity
        this.position.add(this.velocity.x * deltaTime, this.velocity.y * deltaTime);
        // wrap around screen
        this.wrap(900,600);
    }

    public void render(GraphicsContext context) {
        context.save();

        context.translate(this.position.x, this.position.y);
        context.rotate(this.rotation);
        context.translate(-this.image.getWidth() / 2, -this.image.getHeight() / 2); //centers image by moving its center to set parameters (-width/2 and -height/2)
        context.drawImage(this.image, 0, 0);

        context.restore();
    }
    public boolean isInside(Rectangle other) {
        return this.getBoundary().x > other.x &&
                this.getBoundary().y > other.y &&
                this.getBoundary().x + this.getBoundary().width < other.x + other.width &&
                this.getBoundary().y + this.getBoundary().height < other.y + other.height;
    }

    public void justJumped(){
        this.jumps = this.jumps - 1;

    }

    public double getJumps(){
        return this.jumps;
    }
}

