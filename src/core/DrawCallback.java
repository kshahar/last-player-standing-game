
package core;

/**
 * Callback interface that is used in GLAdapter
 * 
 * @see GLAdapter
 */
public interface DrawCallback {
    /**
     * @see Renderer renderGameObject(), renderBackground()
     */
    void drawObjects();

    /**
     * @see Renderer renderText()
     */
    void drawText();
}
