
package core;

import java.awt.Point;

import net.java.joglutils.model.geometry.Model;

/**
 * Interface for a 3D renderer that can load models, render game objects,
 * text and background.
 */
public interface Renderer {
    /**
     * Sets the active camera
     */
    void setCamera(Camera camera);

    /**
     * Set the active callback that will be used for rendering
     */
    void setDrawCallback(DrawCallback callback);

    /**
     * Load a 3D model from the specified path
     * 
     * @return the loaded model, or null on error
     */
    Model loadModel(String path);

    /**
     * Draw a game object with its current position, rotation and scaling
     * 
     * @param gameObject object to draw
     */
    void renderGameObject(GameObject gameObject);

    /**
     * Render text in a position
     */
    void renderText(String text, Point pos);

    /**
     * Render the background image that was set earlier
     */
    void renderBackground();

    /**
     * Set the background texture to be used in renderBackground()
     * 
     * @param path
     */
    void setBackgroundTexturePath(String path);
}
