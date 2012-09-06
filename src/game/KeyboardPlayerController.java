
package game;

import core.Vector;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;
import java.util.TreeMap;

/**
 * Inherits PlayerController to provide control using the keyboard (8
 * directions).
 * Intended for a human player.
 */
public class KeyboardPlayerController extends PlayerController implements KeyListener {

    private static final Map<Integer, Vector> keyToDirectionMap = new TreeMap<Integer, Vector>();
    private Vector upDown = new Vector();
    private Vector leftRight = new Vector();

    static {
        keyToDirectionMap.put(KeyEvent.VK_UP, new Vector(0, 0, -1));
        keyToDirectionMap.put(KeyEvent.VK_DOWN, new Vector(0, 0, 1));
        keyToDirectionMap.put(KeyEvent.VK_LEFT, new Vector(-1, 0, 0));
        keyToDirectionMap.put(KeyEvent.VK_RIGHT, new Vector(1, 0, 0));
    }

    public KeyboardPlayerController(Player player) {
        super(player);
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        final int code = e.getKeyCode();
        boolean updated = false;
        switch (code) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_DOWN:
                upDown = keyToDirectionMap.get(code);
                updated = true;
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_RIGHT:
                leftRight = keyToDirectionMap.get(code);
                updated = true;
                break;
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_Z:
                push();
                break;
        }

        if (updated) {
            startRunning(upDown.add(leftRight));
        }
    }

    public void keyReleased(KeyEvent e) {
        final int code = e.getKeyCode();
        boolean updated = false;
        switch (code) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_DOWN:
                upDown = Vector.Zero;
                updated = true;
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_RIGHT:
                leftRight = Vector.Zero;
                updated = true;
                break;
            case KeyEvent.VK_SPACE:
                stopRunning();
                break;
        }

        if (updated) {
            final Vector direction = upDown.add(leftRight);
            if (direction.sizeSqr() == 0) {
                stopRunning();
            } else {
                startRunning(direction);
            }
        }
    }
}
