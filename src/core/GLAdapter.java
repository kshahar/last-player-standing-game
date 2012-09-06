
package core;

import com.sun.opengl.util.j2d.TextRenderer;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;

import net.java.joglutils.model.ModelFactory;
import net.java.joglutils.model.ModelLoadException;
import net.java.joglutils.model.examples.DisplayListRenderer;
import net.java.joglutils.model.geometry.Model;
import net.java.joglutils.model.iModel3DRenderer;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Implements Renderer using JOGL
 * 
 * @see Renderer
 */
public class GLAdapter implements GLEventListener, Renderer {
    private final static double FOV = 45;
    private final static double NEAR_CLIP = 1;
    private final static double FAR_CLIP = 1000;

    private final GLU glu = new GLU();
    private final TextRenderer textRenderer = new TextRenderer(new Font("Times New Roman", Font.BOLD, 40));
    private final java.util.HashMap<String, Model> modelCache = new java.util.HashMap<String, Model>();
    private Camera camera;
    private DrawCallback callback;
    private GL gl;
    private iModel3DRenderer modelRenderer;

    private int width;
    private int height;

    private String texturePath;
    private Texture backgroundTexture;

    public GLAdapter() {
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void setDrawCallback(DrawCallback callback) {
        this.callback = callback;
    }

    /**
     *
     * @param path
     * @return
     */
    public Model loadModel(String path) {
        if (this.modelCache.containsKey(path)) {
            return this.modelCache.get(path);
        }

        try {
            Model model = ModelFactory.createModel(path);

            model.setUseTexture(true);

            model.setUseLighting(true);

            // Render the bounding box of the entire model
            model.setRenderModelBounds(false);

            // Render the bounding boxes for all of the objects of the model
            model.setRenderObjectBounds(false);

            // Make the model unit size
            model.setUnitizeSize(true);

            this.modelCache.put(path, model);

            return model;
        } catch (ModelLoadException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Draw a game object by setting the position, orientation and scaling,
     * and then using jogl-utils render()
     * 
     * @param gameObject object to draw
     */
    public void renderGameObject(GameObject gameObject) {
        final GL gl = this.gl;

        gl.glPushMatrix();

        final double[] position = gameObject.getPosition().getData();
        final double[] orientation = gameObject.getOrientation().getData();
        final double[] scaling = gameObject.getScaling().getData();

        gl.glRotated(orientation[2], 0, 0, 1);
        gl.glRotated(orientation[1], 1, 0, 0);

        gl.glScaled(scaling[0], scaling[1], scaling[2]);

        gl.glTranslated(position[0], position[1], position[2]);

        gl.glRotated(orientation[0], 0, 1, 0);

        modelRenderer.render(gl, gameObject.getModel());

        gl.glPopMatrix();
    }

    /**
     * Draw the background texture on a quad
     */
    public void renderBackground() {
        final GL gl = this.gl;

        // Load the background texture if not loaded
        if (backgroundTexture == null && texturePath != null) {
            try {
                backgroundTexture = TextureIO.newTexture(new File(texturePath), true);

                gl.glEnable(GL.GL_TEXTURE_2D);

                gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
                gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // End Texture

        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glPushMatrix();

        // Textures are upside down
        gl.glLoadIdentity();
        gl.glScalef(1, -1, 1);

        gl.glOrtho(0, 1, 0, 1, 0, 1);

        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();

        // No depth buffer writes for background.
        gl.glDepthMask(false);

        backgroundTexture.bind();
        gl.glBegin(GL.GL_QUADS);
        {
            gl.glTexCoord2f(0f, 1f);
            gl.glVertex2f(0, 1f);
            gl.glTexCoord2f(1f, 1f);
            gl.glVertex2f(1f, 1f);
            gl.glTexCoord2f(1f, 0f);
            gl.glVertex2f(1f, 0);
            gl.glTexCoord2f(0f, 0f);
            gl.glVertex2f(0, 0);
        }
        gl.glEnd();

        gl.glDepthMask(true);

        gl.glPopMatrix();
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glPopMatrix();
        gl.glMatrixMode(GL.GL_MODELVIEW);
    }

    /**
     * Draw text using TextRenderer
     */
    public void renderText(String text, Point pos) {
        textRenderer.draw(text, pos.x, this.height - 44 - pos.y); // OpenGL (0,0) is at down left
    }

    /**
     * GL setup, lighting
     */
    public void init(GLAutoDrawable gLDrawable) {
        final GL gl = gLDrawable.getGL();

        this.modelRenderer = DisplayListRenderer.getInstance();
        // this.modelRenderer.debug(true);

        gl.glShadeModel(GL.GL_SMOOTH);

        // Setup the depth buffer and enable the depth testing
        gl.glClearDepth(1.0f); // clear z-buffer to the farthest
        gl.glEnable(GL.GL_DEPTH_TEST); // enables depth testing
        gl.glDepthFunc(GL.GL_LEQUAL); // the type of depth test to do

        // Light
        final float ambient0[] = { 1, 1, 1, 1.0f };
        final float diffuse0[] = { 1f, 1f, 1f, 1.0f };
        final float ambient1[] = { 0.8f, 0.8f, 0.8f, 1.0f };
        final float diffuse1[] = { 1.0f, 1.0f, 1.0f, 1.0f };
        final float position1[] = { 0.0f, 20, 0.0f, 1.0f };

        gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambient0, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, diffuse0, 0);
        gl.glEnable(GL.GL_LIGHT0);

        gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, ambient1, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, diffuse1, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, position1, 0);
        gl.glEnable(GL.GL_LIGHT1);

        gl.glEnable(GL.GL_LIGHTING);

        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glPushMatrix();
    }

    /**
     * Set path to background texture (image). the image will be loaded in the next
     * update.
     * 
     * @param path string path
     */
    public void setBackgroundTexturePath(String path) {
        texturePath = path;
        backgroundTexture = null;
    }

    /**
     * Render method, called each frame
     * Uses the internal DrawCallback to determine what to draw
     * 
     * @see DrawCallback
     */
    public void display(GLAutoDrawable gLDrawable) {
        final GL gl = gLDrawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        // Look at
        final Vector position = camera.getPosition();
        final Vector center = camera.getCenter();
        final Vector up = camera.getUp();
        glu.gluLookAt(
                position.x(), position.y(), position.z(),
                center.x(), center.y(), center.z(),
                up.x(), up.y(), up.z());

        this.gl = gl;
        this.callback.drawObjects();
        this.gl = null;

        textRenderer.beginRendering(width, height);
        this.callback.drawText();
        textRenderer.endRendering();

        gl.glFlush();
    }

    public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width, int height) {
        final GL gl = gLDrawable.getGL();

        if (height <= 0)
            height = 1;
        final float ratio = (float) width / (float) height;
        this.width = width;
        this.height = height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(FOV, ratio, NEAR_CLIP, FAR_CLIP);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }
}