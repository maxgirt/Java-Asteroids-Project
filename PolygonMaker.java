package demo6;

import java.util.Random;
import javafx.scene.shape.Polygon;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;


public class PolygonMaker {

    public Polygon createPolygon(double size) {
        Random rand = new Random();
        int sides = 5 + rand.nextInt(8); // randomize number of sides between 5 and 10
        if (size == 22) {
            size = size + rand.nextInt(10);
        }
        else if (size == 14){
            size = size + rand.nextInt(5);
        }
        else if (size == 7) {
            size = size + rand.nextInt(2);
        }

        List<Double> myList = new ArrayList<>();
        double angle = Math.toRadians(360.0 / sides);

        Polygon polygon = new Polygon();



        for (int i = 1; i < sides; i++) {
            double x = Math.cos(i * angle);
            double y = Math.sin(i * angle);
            myList.add(x);
            myList.add(y);
        }

        polygon.getPoints().addAll(size, 0.0);

        for (int i = 0; i < myList.size(); i += 2) {
            polygon.getPoints().addAll(size * myList.get(i), size * myList.get(i+1));
        }

        for (int i = 0; i < polygon.getPoints().size(); i++) {  // This makes the shapes more random looking
            int change = rand.nextInt(11) - 5;
            polygon.getPoints().set(i, polygon.getPoints().get(i) + change);
        }

        return polygon;
    }

}
