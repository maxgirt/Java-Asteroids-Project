package com.example.asteroids;

public class Rectangle
{
    // (x,y) represents the topleft corner of the rectangle
    double x;
    double y;
    double width;
    double height;

    public Rectangle()
    {
        this.setPosition(0,0);
        this.setSize(1,1);
    }
    public Rectangle(double x, double y, double w, double h)
    {
        this.setPosition(x,y);
        this.setSize(w,h);
    }

    public void setPosition(double x, double y)
    {
        this.x = x;
        this.y = y;

    }
    public void setSize(double w, double h)
    {
        this.width = w;
        this.height = h;
    }
    public boolean overlaps(Rectangle other)
    {
        //4 cases where there is no overlap
        //this is to the left of other
        //this is to the right of other
        //this is above other
        //other is above this
        boolean noOverlap = this.x + this.width < other.x||
                other.x + other.width < this.x||
                this.y + this.height < other.y ||
                other.y + other.height < this.y;

        return !noOverlap;

    }
}
