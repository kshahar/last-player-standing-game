
package core;

import java.awt.geom.Point2D;
import net.java.joglutils.model.geometry.Model;

/**
 * Interface for an object that has a model, position, rotation and a center
 */
public interface GameObject {
    Model getModel();

    Vector getPosition();

    Vector getOrientation();

    Vector getScaling();

    /** Get center 2D (x,z) */
    Point2D.Double getCenter();

    double getRadius();

    double overlappingDistance(GameObject obj);
}
