package org.openjfx.controls;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import org.openjfx.controllers.GameLoop;
import org.openjfx.interfaces.Updatable;
import org.openjfx.misc.Constants;
import org.openjfx.models.AsteroidObject;
import org.openjfx.models.WorldObject;

import java.util.concurrent.ThreadLocalRandom;

public class AsteroidController implements Updatable {

    private static AsteroidController instance;

    private final WorldObject worldObject;

    private long spawnTimeCounter = 0;

    // Singleton Class
    public static AsteroidController getInstance() {
        if (instance == null) {
            instance = new AsteroidController();
        }
        return instance;
    }

    public AsteroidController() {
        this.worldObject = GameLoop.worldObject;
    }

    @Override
    public void update(long deltaTime) {

        spawnTimeCounter += deltaTime;

        if (spawnTimeCounter > Constants.ASTEROID_SPAWN_TIME) {
            spawnAsteroid();
            spawnTimeCounter = 0;
        }
    }

    private void spawnAsteroid() {
        Point2D coords = randomPointInRect(
                getRandomBorder(
                        getBorderRect(
                                getWindowRect(), -50
                        )
                )
        );
        int x = (int) coords.getX();
        int y = (int) coords.getY();
        double angle = getAngleBetweenTwoPoints(coords, worldObject.getCanvasCenter());
        float speed = getRandomFloatInBorders(Constants.MIN_ASTEROID_SPEED, Constants.MAX_ASTEROID_SPEED);
        float scale = getRandomFloatInBorders(Constants.MIN_ASTEROID_SCALE, Constants.MAX_ASTEROID_SCALE);

        AsteroidObject asteroid = new AsteroidObject(x, y, speed, angle, scale);
        this.worldObject.addGameObject(asteroid);
    }

    private float getRandomFloatInBorders(float min, float max) {
        float result = ThreadLocalRandom.current().nextFloat() * (max - min) + min;
        if (result >= max) // correct for rounding
            result = Float.intBitsToFloat(Float.floatToIntBits(max) - 1);
        return result;
    }


    private double getAngleBetweenTwoPoints(Point2D p1, Point2D p2) {
        double angle = Math.toDegrees(Math.atan2(p2.getY() - p1.getY(), p2.getX() - p1.getX()));
        angle += (angle < 0) ? 360 : 0;
        return angle;
    }


    private Rectangle[] getBorderRect(Rectangle rec, int distance) {
        double x = rec.getX();
        double y = rec.getY();
        double width = rec.getWidth();
        double height = rec.getHeight();
        return new Rectangle[]
                {
                        new Rectangle(x, y, width, distance),    // TOP
                        new Rectangle(x, y + height - distance, width, distance),   // BOTTOM   
                        new Rectangle(x, y, distance, height),                          // LEFT
                        new Rectangle(x + width - distance, y, distance, height)     // RIGHT
                };
    }

    private Rectangle getRandomBorder(Rectangle[] rectangles) {
        return rectangles[(int) Math.floor((Math.random() * rectangles.length))];
    }

    private Point2D randomPointInRect(Rectangle rec) {
        return new Point2D(
                rec.getX() + (int) (Math.random() * rec.getWidth()),
                rec.getY() + (int) (Math.random() * rec.getHeight())
        );
    }

    private Rectangle getWindowRect() {
        return new Rectangle(0, 0, GameLoop.worldObject.getCanvasWidth(), GameLoop.worldObject.getCanvasHeight());
    }

}
