package org.openjfx.rendering;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.openjfx.models.GameObject;

import static org.openjfx.controllers.GameLoop.worldObject;

public class Renderer {

    Canvas canvas;
    GraphicsContext graphicsContext;

    Image background;


    public Renderer(Canvas canvas) {
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();
    }

    public void setBackground(Image background) {
        this.background = background;
    }

    public void render() {
        graphicsContext.save();

        if (background != null) {
            // draws the background image at (0,0)
            graphicsContext.drawImage(background, 0, 0);
        }

        for (GameObject gameObject : worldObject.getGameObjects()) {
            gameObject.draw(graphicsContext);    // Call draw method
        }

        graphicsContext.restore();
    }

    // Clearing the last frame
    public void prepare() {
        graphicsContext.setFill(new Color(0.68, 0.68, 0.68, 1.0));
        graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}
