
package core;

/**
 * Helper class to help use yaw, pitch, roll with gluLookAt.
 * Allows to set a center instead of computing it.
 */
public class Camera {
    private Vector position = new Vector();
    private Vector right;
    private Vector up;
    private Vector forward;
    private Vector center;

    public Camera() {
        reset();
    }

    public void reset() {
        right = new Vector(1, 0, 0);
        up = new Vector(0, 1, 0);
        forward = new Vector(0, 0, 1);
        center = null;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getRight() {
        return right;
    }

    public Vector getUp() {
        return up;
    }

    public Vector getForward() {
        return forward;
    }

    public Vector getCenter() {
        if (this.center != null) {
            return this.center;
        }
        return getPosition().subtract(getForward());
    }

    public void setCenter(Vector center) {
        this.center = center;
    }

    public void changeYaw(double theta) {
        Vector temp = this.right.mul(Math.cos(theta));
        this.right = temp.add(this.forward.mul(Math.sin(theta))).normalize();
        this.forward = this.right.cross(this.up);
    }

    public void changePitch(double theta) {
        Vector temp = this.forward.mul(Math.cos(theta));
        this.forward = temp.add(this.up.mul(Math.sin(theta))).normalize();
        this.up = this.forward.cross(this.right);
    }

    public void changeRoll(double theta) {
        Vector temp = this.up.mul(Math.cos(theta));
        this.up = temp.subtract(this.right.mul(Math.sin(theta))).normalize();
        this.right = this.forward.cross(this.up).mul(-1);
    }
}
