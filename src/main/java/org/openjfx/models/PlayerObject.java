package org.openjfx.models;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.transform.Rotate;
import org.openjfx.controls.KeyController;
import org.openjfx.enums.BorderCollisionType;
import org.openjfx.enums.GameObjectType;
import org.openjfx.interfaces.InputSystem;
import org.openjfx.misc.Settings;

import static org.openjfx.controllers.GameLoop.worldObject;

public class PlayerObject extends GameObject implements InputSystem {

    KeyController keys = KeyController.getInstance();

    private int bulletCounter = 0;

    private boolean debugModeLock = false;
    private float debugLockTime = 100f;
    private float debugLockTimeCounter = 0;

    private final float shotDelayTime = 30f; // 0.1 seconds
    private float lastShot = shotDelayTime;

    public PlayerObject(double x, double y) {
        super(x, y, "ship.png", GameObjectType.PLAYER);
    }

    @Override
    public void processInput(long deltaTime) {
        if (keys.isKeyPressed(KeyCode.UP)) {
            this.addThrust(0.0175 * deltaTime);
        } else if (keys.isKeyPressed(KeyCode.DOWN)) {
            this.addThrust(0.0175 * deltaTime);
        }
        if (keys.isKeyPressed(KeyCode.RIGHT)) {
            this.addTorque(0.025f * deltaTime);
        } else if (keys.isKeyPressed(KeyCode.LEFT)) {
            this.addTorque(-0.025f * deltaTime);
        }

        handleDebugKey(deltaTime);

        lastShot += deltaTime;

        if (lastShot > shotDelayTime) {
            if (keys.isKeyPressed(KeyCode.SPACE)) {
                Point2D rotatedPoint = rotatePoint(getCenter().getX() + this.getWidth()/2 - 2, getCenter().getY() + 17, this.angle);
                BulletObject bulletLeft = new BulletObject(rotatedPoint.getX(), rotatedPoint.getY(), 1, this.angle);
                worldObject.addGameObject(bulletLeft);

                rotatedPoint = rotatePoint(getCenter().getX() - this.getWidth()/2 + 2, getCenter().getY() + 17, this.angle);
                BulletObject bulletRight = new BulletObject(rotatedPoint.getX(), rotatedPoint.getY(), 1, this.angle);
                worldObject.addGameObject(bulletRight);

                bulletCounter+=2;
            }
            lastShot = 0;
        }
    }



    @Override
    public void update(long deltaTime) {
        applyDrag();
        move(currentThrustVector);
        rotate(currentTorqueForce);

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

    private void rotate(double angle) {
        if (this.angle >= 360) {
            this.angle = 0;
        }
        this.angle += angle;
    }

    private void move(Point2D vector) {
        double deltaX = vector.getX();
        double deltaY = vector.getY();

        BorderCollisionType type = worldObject.isValidPosition(new Point2D(x,y).add(vector), this);
        // TODO zu schnelles teleportieren blocken, so dass Übergang von links/rechts und oben/unten flüssig ist
        switch (type) {
            case LEFT:
                this.x += worldObject.getCanvasWidth() - 1;
                break;
            case RIGHT:
                this.x -= worldObject.getCanvasWidth() - this.width + 1;
                break;
            case TOP:
                this.y += worldObject.getCanvasHeight() - 1;
                break;
            case BOTTOM:
                this.y -= worldObject.getCanvasHeight() - this.height + 1;
                break;
            case NONE:
            default:
                this.x += deltaX;
                this.y += deltaY;
                break;
        }
    }

    /* *************************************************************
     *                           MOVEMENT                          *
     ***************************************************************/

    private float MAX_SPEED = 5f;
    private Point2D currentThrustVector = new Point2D(0, 0);

    private float MAX_TORQUE = 5f;
    private float currentTorqueForce = 0;

    public void addTorque(float torqueForce) {
        float newTorque = currentTorqueForce + torqueForce;
        if (torqueForce > 0) {
            currentTorqueForce = Math.min(newTorque, MAX_TORQUE);
        } else {
            currentTorqueForce = Math.max(newTorque, -MAX_TORQUE);
        }
    }

    public void addThrust(double scalar) {
        addThrust(scalar, getAngle());
    }

    private void addThrust(double scalar, double angle) {
        Point2D thrustVector = calculateNewThrustVector(scalar, Math.toRadians(-angle));
        currentThrustVector = currentThrustVector.add(thrustVector);
        currentThrustVector = clampToMaxSpeed(currentThrustVector);
    }

    private Point2D calculateNewThrustVector(double scalar, double angle) {
        return new Point2D(
                (float) (Math.sin(angle) * scalar),
                (float) (Math.cos(angle) * scalar));
    }

    private Point2D clampToMaxSpeed(Point2D thrustVector) {
        if (thrustVector.magnitude() > MAX_SPEED) {
            return currentThrustVector = thrustVector.normalize().multiply(MAX_SPEED);
        } else {
            return currentThrustVector = thrustVector;
        }
    }

    private void applyDrag() {
        float movementDrag = currentThrustVector.magnitude() < 0.5 ? 0.01f : 0.07f;
        float rotationDrag = currentTorqueForce < 0.2f ? 0.05f : 0.1f;

        currentThrustVector = new Point2D(
                reduceTowardsZero((float) currentThrustVector.getX(), movementDrag),
                reduceTowardsZero((float) currentThrustVector.getY(), movementDrag));

        currentTorqueForce = reduceTowardsZero(currentTorqueForce, rotationDrag);
    }

    private float reduceTowardsZero(float value, float modifier) {
        float newValue = 0;
        if (value > modifier) {
            newValue = value - modifier;
        } else if (value < -modifier) {
            newValue = value + modifier;
        }
        return newValue;
    }

    private void handleDebugKey(long deltaTime) {
        if (keys.isKeyPressed(KeyCode.B) && !debugModeLock) {
//            System.out.println("Locked");
            debugModeLock = true;
            debugLockTimeCounter = 0;
            Settings.debugMode = !Settings.debugMode;
        }

        debugLockTimeCounter += deltaTime;
        if ((debugLockTimeCounter > debugLockTime) && debugModeLock) {
//            System.out.println("Lock released");
            debugModeLock = false;  // Release Lock
        }
    }

    public int getBulletCounter() {
        return bulletCounter;
    }
}