package edu.um.chromaster;

public class Vector2 {

    private double x, y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double distance(Vector2 vector) {
        return Math.sqrt(Math.pow(this.x - vector.getX(), 2) + Math.pow(this.y - vector.getY(), 2));
    }

}
