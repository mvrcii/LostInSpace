package org.openjfx.models;


import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.openjfx.enums.GameObjectType;
import org.openjfx.interfaces.Updatable;

import java.util.Objects;

public abstract class GameObject implements Updatable {

    protected HitboxObject hitboxObject;
    protected GameObjectType gameObjectType;
    protected Image image;

    protected double width;
    protected double height;
    protected float scale = 1;
    protected double x,y;
    protected double angle = 0;

    public GameObject(double x, double y, String imageFileName, GameObjectType gameObjectType) {
        this.x = x;
        this.y = y;
        this.setImage(imageFileName);
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.hitboxObject = new HitboxObject(this);
        this.gameObjectType = gameObjectType;
    }

    // Rotates a given point around the center of this game object by a given angle
    protected Point2D rotatePoint(double x, double y, double angle) {
        double radians = Math.toRadians(angle);
        double cx = this.getCenter().getX();
        double cy = this.getCenter().getY();
        return new Point2D(
                (x-cx) * Math.cos(radians) - (y-cy) * Math.sin(radians) + cx,
                (y-cy) * Math.cos(radians) + (x-cx) * Math.sin(radians) + cy
        );
    }

    public abstract void update(long deltaTime);

    public abstract void draw(GraphicsContext graphicsContext);

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getWidth() {
        return width * scale;
    }

    public double getHeight() {
        return height * scale;
    }

    public Point2D getCenter() {
        return new Point2D(x + getWidth() / 2, y + getHeight() / 2);
    }

    public Image getImage() {
        return image;
    }

    private void setImage(String imageFileName) {
        this.image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/" + imageFileName)));
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public double getAngle() {
        return angle;
    }


    public HitboxObject getHitboxObject() {
        return hitboxObject;
    }

    public GameObjectType getGameObjectType() {
        return gameObjectType;
    }

}
