
package game;

import java.awt.geom.Point2D;

import core.GameObject;
import core.Vector;
import net.java.joglutils.model.geometry.Model;

/**
 * Implements a game object that is placed on the surface.
 * Maintains data that is used for physics computations (position, velocity,
 * force).
 * 
 * @see GameObject
 */
public class Player implements GameObject {
    private final Model model;
    private final double mass;
    private final double radius;
    private Vector force = new Vector();
    private Vector position = new Vector();
    private Vector velocity = new Vector();
    private Vector orientation = new Vector();
    private final Vector scaling = new Vector(1, 1, 1);
    private boolean isOnSurface = true;

    public Player(Model model, double mass, double radius) {
        this.model = model;
        this.mass = mass;
        this.radius = radius;
    }

    public void setForce(Vector force) {
        this.force = force;
    }

    public void addForce(Vector force) {
        this.force = force.add(force);
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public void addVelocity(Vector velocity) {
        this.velocity = velocity.add(velocity);
    }

    public void setOrientation(double xrot, double yrot, double zrot) {
        this.orientation.setX(xrot);
        this.orientation.setY(yrot);
        this.orientation.setZ(zrot);
    }

    public Model getModel() {
        return this.model;
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getOrientation() {
        return orientation;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public Vector getScaling() {
        return scaling;
    }

    public Vector getForce() {
        return force;
    }

    public double getMass() {
        return mass;
    }

    public double getSpeed() {
        return velocity.size();
    }

    public boolean isOnSurface() {
        return isOnSurface;
    }

    public void setIsOnSurface(boolean isOnSurface) {
        this.isOnSurface = isOnSurface;
    }

    /** Move the player in case of a collision */
    public void retract(Vector direction, double dist) {

        double directionX = direction.x(), directionZ = direction.z();

        if ((directionX == 0) && (directionZ == 0)) // atan2(0,0) is undefined
            return;

        double alpha = Math.atan2(directionZ, directionX);

        Vector delta = new Vector(-dist * Math.cos(alpha), 0, -dist * Math.sin(alpha));

        position = position.add(delta);
    }

    public Point2D.Double getCenter() {
        return new Point2D.Double(position.x(), position.z());
    }

    public double getRadius() {
        return this.radius;
    }

    public double overlappingDistance(GameObject obj) {
        Point2D.Double center1 = getCenter();
        Point2D.Double center2 = obj.getCenter();

        double dist = center1.distance(center2);

        double radius1 = getRadius();
        double radius2 = obj.getRadius();

        if (dist >= radius1 + radius2)
            return 0;

        return radius1 + radius2 - dist;
    }
}
