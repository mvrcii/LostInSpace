package org.openjfx.models;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import org.openjfx.misc.Settings;

public class HitboxObject {

    private final GameObject gameObject;
    private Rectangle rect;

    private double angle;
    private double x;
    private double y;
    private double width;
    private double height;


    public HitboxObject(GameObject gameObject) {
        this.gameObject = gameObject;
        this.width = gameObject.width;
        this.height = gameObject.height;
        this.angle = gameObject.angle;
        this.x = gameObject.getX();
        this.y = gameObject.getY();

        this.rect = new Rectangle(x, y, width, height);
        update();
    }

    public void update() {
        this.updateCoordinates();
        this.updateDimensions();
        this.updateRotation();
    }

    private void updateCoordinates() {
        this.x = gameObject.getX();
        this.y = gameObject.getY();
        this.rect = new Rectangle(this.x, this.y, this.width, this.height);
    }

    private void updateDimensions() {
        this.width = gameObject.getWidth();
        this.height = gameObject.getHeight();
    }

    private void updateRotation() {
        this.angle = gameObject.angle;

        Rotate rotate = new Rotate(angle, this.getCenterX(), this.getCenterY());
        this.rect.getTransforms().add(rotate);
    }

    private double getCenterX() {
        return this.x + width / 2;
    }

    private double getCenterY() {
        return this.y + height / 2;
    }

    public void draw(GraphicsContext graphicsContext) {
        if (!Settings.debugMode) {
            return;
        }

        graphicsContext.save(); // saves the current state on stack, including the current transform

        Rotate r = new Rotate(angle, getCenterX(), getCenterY());
        graphicsContext.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());

        graphicsContext.setStroke(Color.RED);
        graphicsContext.strokeRect(this.x, this.y, this.width, this.height);

        graphicsContext.restore(); // back to original state (before rotation)
    }

    public Rectangle getRect() {
        return rect;
    }
}
