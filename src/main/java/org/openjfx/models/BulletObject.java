package org.openjfx.models;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Rotate;
import org.openjfx.enums.GameObjectType;
import org.openjfx.misc.Constants;

public class BulletObject extends GameObject {


    public BulletObject(double x, double y, float scale, double angle) {
        super(x, y, "bullet.png", GameObjectType.BULLET);

        this.scale = scale;
        this.setOffset();
        this.angle = angle + 90; // offset angle in respect to the player angle
    }

    @Override
    public void update(long deltaTime) {
        move(deltaTime);
        hitboxObject.update();
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

    /* **********************************************************
     *                           POSITION                       *
     ************************************************************/

    private void move(long deltaTime) {
        this.x += Constants.BULLET_SPEED * Math.cos(Math.toRadians(angle)) * deltaTime;
        this.y += Constants.BULLET_SPEED * Math.sin(Math.toRadians(angle)) * deltaTime;
    }

    private void setOffset() {
        this.x -= (this.width * scale)/2;
        this.y -= (this.height * scale)/2;
    }

}
