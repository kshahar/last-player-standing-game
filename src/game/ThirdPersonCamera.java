
package game;

import core.*;

/**
 * Uses Camera to follow a chosen GameObject from behind
 * 
 * @see Camera
 * @see GameObject
 */
public class ThirdPersonCamera {
    private static final double FOLLOW_DISTANCE = 10;

    private final Camera camera;
    private GameObject gameObject;

    public ThirdPersonCamera(Camera camera) {
        this.camera = camera;
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public void update() {
        if (this.gameObject == null) {
            return;
        }

        final Vector currentOrientation = this.gameObject.getOrientation();
        final double yaw = (270 - currentOrientation.x()) * Math.PI / 180.0;
        final Vector currentPosition = this.gameObject.getPosition();
        final Vector positionChange = new Vector(Math.cos(yaw), 0, Math.sin(yaw)).mul(FOLLOW_DISTANCE);
        final Vector newPosition = currentPosition.subtract(positionChange);
        newPosition.setY(FOLLOW_DISTANCE);

        this.camera.setPosition(newPosition);
        this.camera.setCenter(currentPosition);
    }
}
