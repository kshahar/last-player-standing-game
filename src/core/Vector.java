
package core;

/**
 * Implements a vector in 3D space
 */
public class Vector {
    // Dimensions
    public enum Axis {
        X, Y, Z
    };

    public static final int N = 3;
    public static final Vector Zero = new Vector();

    // Internal representation
    private double[] data;

    public Vector() {
        this.data = new double[N];
    }

    public Vector(double x, double y, double z) {
        this();
        setX(x);
        setY(y);
        setZ(z);
    }

    public double getValue(Axis axis) {
        return this.data[axis.ordinal()];
    }

    public void setValue(Axis axis, double value) {
        this.data[axis.ordinal()] = value;
    }

    public double[] getData() {
        return this.data;
    }

    public double x() {
        return getValue(Axis.X);
    }

    public double y() {
        return getValue(Axis.Y);
    }

    public double z() {
        return getValue(Axis.Z);
    }

    public void setX(double value) {
        setValue(Axis.X, value);
    }

    public void setY(double value) {
        setValue(Axis.Y, value);
    }

    public void setZ(double value) {
        setValue(Axis.Z, value);
    }

    public String toString() {
        return String.format("(%f, %f, %f)", x(), y(), z());
    }

    // Returns the norm of this vector
    public double size() {
        return Math.sqrt(sizeSqr());
    }

    public double sizeSqr() {
        return x() * x() + y() * y() + z() * z();
    }

    // Returns this vector + other vector
    public Vector add(Vector other) {
        return new Vector(x() + other.x(), y() + other.y(), z() + other.z());
    }

    // Returns this vector - other vector
    public Vector subtract(Vector other) {
        return new Vector(x() - other.x(), y() - other.y(), z() - other.z());
    }

    // Returns this vector multiplied by a scalar
    public Vector mul(double scalar) {
        return new Vector(x() * scalar, y() * scalar, z() * scalar);
    }

    public Vector mul(Vector scalars) {
        return new Vector(x() * scalars.x(), y() * scalars.y(), z() * scalars.z());
    }

    public Vector reverse() {
        return mul(-1);
    }

    public Vector div(double scalar) {
        if (scalar == 0) {
            throw new IllegalArgumentException("Argument 'scalar' is 0");
        }
        return new Vector(x() / scalar, y() / scalar, z() / scalar);
    }

    // Returns the dot product of this and another vector
    public double dot(Vector other) {
        return x() * other.x() + y() * other.y() + z() * other.z();
    }

    // Returns the cross product of this and another vector
    public Vector cross(Vector other) {
        return new Vector(
                (y() * other.z()) - (z() * other.y()),
                (z() * other.x()) - (x() * other.z()),
                (x() * other.y()) - (y() * other.x()));
    }

    public double distance(Vector other) {
        return Math.sqrt(sqr(x() - other.x()) + sqr(y() - other.y()) + sqr(z() - other.z()));
    }

    // Normalize this vector
    public Vector normalize() {
        final double size = this.size();
        if (size == 0) {
            return new Vector();
        }

        return this.div(size);
    }

    public double angleBetween(Vector vec) {
        // find angle(degrees) between this and vec
        double angle = Math.atan2(vec.z(), vec.x()) - Math.atan2(this.z(), this.x());

        return angle;
    }

    private static double sqr(double value) {
        return value * value;
    }
}
