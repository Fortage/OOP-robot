package gui;

public class GameObj {

    public double x, y;

    protected GameObj(double x, double y) {
        this.x = x;
        this.y = y;
    }

    protected double getX() {
        return x;
    }

    protected double getY() {
        return y;
    }
}
