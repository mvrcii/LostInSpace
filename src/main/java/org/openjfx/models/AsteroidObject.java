package org.openjfx.models;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Rotate;
import org.openjfx.enums.GameObjectType;


public class AsteroidObject extends GameObject {

    private final float speed;

    public AsteroidObject(int x, int y, float speed, double angle, float scale) {
        super(x, y, "asteroid.png", GameObjectType.ASTEROID);
        this.speed = speed;
        this.angle = angle;
        this.scale = scale;
    }

    @Override
    public void update(long deltaTime) {
        move(deltaTime);
        hitboxObject.update();
    }

    private void move(long deltaTime) {
        this.x += this.speed * Math.cos(Math.toRadians(angle)) * deltaTime;
        this.y += this.speed * Math.sin(Math.toRadians(angle)) * deltaTime;
    }

    @Override
    public void draw(GraphicsContext graphicsContext) {
        graphicsContext.save(); // saves the current state on stack, including the current transform

        Rotate r = new Rotate(angle, getCenter().getX(), getCenter().getY());
        graphicsContext.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
        graphicsContext.drawImage(this.image, this.x, this.y, this.getWidth(), this.getHeight());

        graphicsContext.restore(); // back to original state (before rotation)

        hitboxObject.draw(graphicsContext);
    }

}
