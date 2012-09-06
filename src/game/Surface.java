
package game;

import java.awt.geom.Point2D;
import java.util.Random;

import core.GameObject;
import core.Vector;
import net.java.joglutils.model.geometry.Model;

/**
 * Implements the auto-rotating surface game object.
 * The rotation change is randomized.
 */
public class Surface implements GameObject {
    private static final double MAX_ROTATION = 15;
    private static final double CHANGE_FACTOR = 30;
    private static final int CHANGE_AFTER_SECS = 5;

    private final Model model;
    private final double radius;
    private final Random random = new Random();

    final Vector position = new Vector();
    Vector orientation = new Vector();
    final Vector scaling = new Vector(10, 5, 10);

    private Vector changeDirection = new Vector(0, 1, 1);
    private Vector changeSize = new Vector(0, 0, 0);

    public Surface(Model model, double radius) {
        this.model = model;
        this.radius = radius;
    }

    public void setOrientation(Vector orientation) {
        this.orientation = orientation;
    }

    public Model getModel() {
        return this.model;
    }

    public Vector getPosition() {
        return this.position;
    }

    public Vector getOrientation() {
        return this.orientation;
    }

    public Vector getScaling() {
        return scaling;
    }

    public Point2D.Double getCenter() {
        return new Point2D.Double(position.x(), position.z());
    }

    public double getRadius() {
        return radius;
    }

    public double overlappingDistance(GameObject obj) {
        Point2D.Double center1 = getCenter();
        Point2D.Double center2 = obj.getCenter();

        double dist = center1.distance(center2);

        double radius1 = getRadius();
        double radius2 = obj.getRadius();

        if (dist > radius1 + radius2)
            return 0;

        return radius1 + radius2 - dist;
    }

    /**
     * Sets the current surface rotation by considering elapsed time
     */
    public void update(double timePassedSecs) {
        final double changeMax = CHANGE_FACTOR * timePassedSecs;
        this.changeSize.setY(random.nextDouble() * changeMax);
        this.changeSize.setZ(random.nextDouble() * changeMax);

        updateChangeAxis(Vector.Axis.Y, timePassedSecs);
        updateChangeAxis(Vector.Axis.Z, timePassedSecs);

        final Vector change = this.changeDirection.mul(this.changeSize);
        this.orientation = this.orientation.add(change);
    }

    /**
     * Update rotation on one axis
     */
    private void updateChangeAxis(Vector.Axis axis, double timePassedSecs) {
        boolean shouldInvert = false;

        if (Math.abs(this.orientation.getValue(axis)) > MAX_ROTATION) {
            shouldInvert = true;
        } else if (this.random.nextDouble() < timePassedSecs / CHANGE_AFTER_SECS) {
            shouldInvert = true;
        }

        if (shouldInvert) {
            changeDirection.setValue(axis, -changeDirection.getValue(axis));
        }
    }
}
