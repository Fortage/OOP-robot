package logic;

import log.RobotLogger;

import java.util.function.Function;

public class Robot extends GameObj {

    private static final double RADIUS = 0.5;
    private static final double DURATION = 10;
    private static final double MAX_VELOCITY = 0.1;
    private static final double MAX_ANGULAR_VELOCITY = 0.001;

    private double direction;

    public Robot(double x, double y, double direction) {
        super(x, y);
        this.direction = direction;
    }

    public double getDirection() {
        return direction;
    }

    public void update(Map map) {
        GameObj target = map.getTarget();
        if (GameMath.distance(getX(), getY(), target.getX(), target.getY()) >= RADIUS) {
            moveToTarget(target);
        }
        setX(pushOffFromBorder(getX(), map.getWidth(), GameMath.TWO_PI));
        setY(pushOffFromBorder(getY(), map.getHeight(), Math.PI));
    }

    private void moveToTarget(GameObj target) {
        RobotLogger.debug(super.getX() + "  " + super.getY());
        double angularVelocity = calculateAngularVelocity(target);
        double velocityRatio = MAX_VELOCITY / angularVelocity;
        double newDirection = direction + angularVelocity * DURATION;
        setX(shiftCoordinate(getX(), velocityRatio, newDirection, Math::sin, Math::cos));
        setY(shiftCoordinate(getY(), -velocityRatio, newDirection, Math::cos, Math::sin));
        direction = GameMath.asNormalizedRadians(newDirection);
    }

    private double calculateAngularVelocity(GameObj target) {
        double angleToTarget = GameMath.angleTo(getX(), getY(), target.getX(), target.getY()) - direction;
        angleToTarget = GameMath.asNormalizedRadians(angleToTarget);
        double angleDifference = GameMath.minByModulus(angleToTarget - GameMath.TWO_PI, angleToTarget);
        return Math.signum(angleDifference) * MAX_ANGULAR_VELOCITY;
    }

    private double shiftCoordinate(double coordinate, double velocity, double angle,
                                   Function<Double, Double> f1, Function<Double, Double> f2) {
        double newCoordinate = coordinate + velocity * (f1.apply(angle) - f1.apply(direction));
        if (Double.isFinite(newCoordinate)) {
            return newCoordinate;
        }
        return coordinate + MAX_VELOCITY * DURATION + f2.apply(direction);
    }

    private double pushOffFromBorder(double coordinate, double border, double criticalAngle) {
        if (coordinate < 0 || coordinate > border) {
            if (direction % (criticalAngle / 2) > MAX_ANGULAR_VELOCITY) {
                direction = GameMath.TWO_PI - criticalAngle - direction;
            }
            direction = GameMath.asNormalizedRadians(direction + Math.PI);
            coordinate = GameMath.applyLimits(coordinate, 0, border);
        }
        return coordinate;
    }
}