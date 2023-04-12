package com.example.asteroids;

public class Vector
{
    public double x;
    public double y;

    public Vector()
    {
        this.set(0,0);
    }

    public Vector(double x, double y)
    {
        this.set(x,y);

    }
    public void set(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public void add(double dx, double dy)
    {
        this.x += dx;
        this.y += dy;
    }

    public Vector addreturn(Vector v) {
        return new Vector(this.x + v.x, this.y + v.y);
    }


    public void multiply(double m)
    {
        this.x *= m;
        this.y *= m;
    }

    public double getLenght()
    {
        return Math.sqrt((this.x * this.x + this.y * this.y));

    }

    public void setLenght(double L)
    {
        double currentLenght = this.getLenght();
        //if current lenght = 0, then current angle = undefined;
        // assume current angle is 0 (pointing to the right)
        if (currentLenght == 0){
            this.set(L,0);
            return;
        }
        else { //able to preserve current angle
            //scale vector to have length 1
            this.multiply( 1/currentLenght);
            // scale vector to have lenght L
            this.multiply(L);
        }
    }

    public double getAngle()
    {
        return Math.toDegrees(Math.atan2(this.y , this.x));
    }

    public void setAngle(double angleDegrees)
    {
        double L = this.getLenght();
        double andgleRadains = Math.toRadians(angleDegrees);
        this.x = L * Math.cos(andgleRadains);
        this.y = L * Math.sin(andgleRadains);
    }
    public Vector scale(double scalar) {
        return new Vector(this.x * scalar, this.y * scalar);
    }
}

