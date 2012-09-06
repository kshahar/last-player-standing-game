
import core.*;
import game.*;
import test.*;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.GLCanvas;

import com.sun.opengl.util.Animator;

public class Main {

    public static void main(String[] args) {

        try {
            run();
        } catch (Exception e) {
        }
    }

    private static void run() throws Exception {
        Frame frame = new Frame();
        GLCanvas canvas = new GLCanvas();

        SoundEffect.init();
        SoundEffect.volume = SoundEffect.Volume.LOW; // un-mute

        final GLAdapter renderer = new GLAdapter();
        final Camera camera = new Camera();
        final GameRunner gameRunner = new GameRunner(renderer, canvas, camera);

        final test.CameraKeyboardController controller = new CameraKeyboardController(camera);
        // final test.TiltMouseController mouseController =
        // new test.TiltMouseController(gameRunner);
        renderer.setCamera(camera);

        canvas.addGLEventListener(renderer);
        canvas.addKeyListener(controller);
        canvas.addKeyListener(gameRunner);
        // canvas.addMouseListener(mouseController);
        // canvas.addMouseMotionListener(mouseController);

        frame.add(canvas);
        frame.setSize(800, 800);
        final Animator animator = new Animator(canvas);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                new Thread(new Runnable() {
                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });
        frame.setVisible(true);
        animator.start();
        canvas.requestFocus();

        gameRunner.restart();
    }

}
