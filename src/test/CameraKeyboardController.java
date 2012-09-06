
package test;

import core.Camera;
import core.Vector;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CameraKeyboardController implements KeyListener {

    private final Camera camera;
    private static final float STEP = 1;
    private static final double ANGLE = 5 * Math.PI / 180.0;

    public CameraKeyboardController(Camera camera) {
        this.camera = camera;
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        final int code = e.getKeyCode();
        // System.out.println("Key pressed: " + code);
        handleTranslateKey(code);
        handleRotateKey(code);
    }

    public void keyReleased(KeyEvent e) {
    }

    // Handle translate keys
    private void handleTranslateKey(int code) {
        switch (code) {
            case KeyEvent.VK_W:
                // Move forward
                updateTranslation(camera.getForward().mul(-STEP));
                break;
            case KeyEvent.VK_S:
                // Move backward
                updateTranslation(camera.getForward().mul(STEP));
                break;
            case KeyEvent.VK_D:
                // Move right
                updateTranslation(camera.getRight().mul(STEP));
                break;
            case KeyEvent.VK_A:
                // Move left
                updateTranslation(camera.getRight().mul(-STEP));
                break;
            case KeyEvent.VK_Q:
                // Move up
                updateTranslation(camera.getUp().mul(STEP));
                break;
            case KeyEvent.VK_E:
                // Move down
                updateTranslation(camera.getUp().mul(-STEP));
                break;
        }
    }

    // Handle rotate keys
    private void handleRotateKey(int code) {
        switch (code) {
            case KeyEvent.VK_I:
                // Pitch up
                camera.changePitch(-ANGLE);
                break;
            case KeyEvent.VK_K:
                // Pitch down
                camera.changePitch(ANGLE);
                break;
            case KeyEvent.VK_L:
                // Yaw right
                camera.changeYaw(ANGLE);
                break;
            case KeyEvent.VK_J:
                // Yaw left
                camera.changeYaw(-ANGLE);
                break;
            case KeyEvent.VK_O:
                // Roll right
                camera.changeRoll(-ANGLE);
                break;
            case KeyEvent.VK_U:
                // Roll left
                camera.changeRoll(ANGLE);
                break;
        }
    }

    private void updateTranslation(Vector newTranslation) {
        camera.setPosition(camera.getPosition().add(newTranslation));
    }
}
