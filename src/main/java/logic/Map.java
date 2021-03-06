package logic;

public class Map {
    private int width, height;
    private Robot robot;
    private GameObj target;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void update() {
        robot.update(this);
    }

    protected int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    protected int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public GameObj getTarget() {
        return target;
    }

    public void setTarget(GameObj target) {
        this.target = target;
    }
}
