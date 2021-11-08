package org.openjfx.models;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import org.openjfx.enums.BorderCollisionType;

import java.util.ArrayList;


public class WorldObject {

    private final ArrayList<GameObject> gameObjects = new ArrayList<>();
    private final ArrayList<GameObject> gameObjectsToAdd = new ArrayList<>();
    private final ArrayList<GameObject> gameObjectsToRemove = new ArrayList<>();

    private final PlayerObject playerObject;
    private final Canvas gameCanvas;

    public WorldObject(Canvas gameCanvas) {
        this.gameCanvas = gameCanvas;

        this.playerObject = new PlayerObject(350, 200);
        this.playerObject.setScale(1);
        gameObjects.add(playerObject);
    }

    public BorderCollisionType isValidPosition(Point2D point2D, GameObject gameObject) {
        double x = point2D.getX();
        double y = point2D.getY();

        if (x >= 0 && x <= (gameCanvas.getWidth() - gameObject.getWidth())) {
            if (y >= 0 && y <= (gameCanvas.getHeight() - gameObject.getHeight())) {
                return BorderCollisionType.NONE;
            } else if (y < 0) {
                return BorderCollisionType.TOP;
            } else {
                return BorderCollisionType.BOTTOM;
            }
        } else if (x < 0) {
            return BorderCollisionType.LEFT;
        } else {
            return BorderCollisionType.RIGHT;
        }
    }

    public void refreshGameObjects() {
        gameObjects.addAll(gameObjectsToAdd);
        gameObjects.removeAll(gameObjectsToRemove);
        gameObjectsToRemove.clear();
        gameObjectsToAdd.clear();
    }

    public PlayerObject getPlayerObject() {
        return playerObject;
    }

    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    public Canvas getGameCanvas() {
        return gameCanvas;
    }

    public double getCanvasWidth() {
        return gameCanvas.getWidth();
    }

    public double getCanvasHeight() {
        return gameCanvas.getHeight();
    }

    public Point2D getCanvasCenter() {
        return new Point2D(getCanvasWidth() / 2, getCanvasHeight() / 2);
    }

    public void addGameObject(GameObject gameObject) {
        gameObjectsToAdd.add(gameObject);
    }

    public void removeGameObject(GameObject gameObject) {
        gameObjectsToRemove.add(gameObject);
    }

}
