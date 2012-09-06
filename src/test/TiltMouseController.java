
package test;

import game.GameRunner;
import game.World;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class TiltMouseController implements MouseListener, MouseMotionListener {

    GameRunner runner;
    Point lastPoint;

    public TiltMouseController(GameRunner runner) {
        this.runner = runner;
    }

    public void mouseDragged(MouseEvent e) {
        World world = runner.getWorld();
        if (world != null) {
            Point newPoint = e.getPoint();

            int dX = newPoint.x - lastPoint.x;
            int dY = newPoint.y - lastPoint.y;

            world.tiltSurface(dY, -dX);

            lastPoint = newPoint;
        }
    }

    public void mouseMoved(MouseEvent e) {

    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        lastPoint = e.getPoint();
    }

    public void mouseReleased(MouseEvent e) {
    }

}
