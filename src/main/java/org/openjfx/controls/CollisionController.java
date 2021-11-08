package org.openjfx.controls;

import javafx.scene.shape.Rectangle;
import org.openjfx.controllers.GameLoop;
import org.openjfx.enums.GameObjectType;
import org.openjfx.interfaces.Updatable;
import org.openjfx.models.GameObject;

import java.util.ArrayList;

public class CollisionController implements Updatable {

    private static CollisionController instance;

    private ArrayList<GameObject> gameObjects;

    // Singleton Class
    public static CollisionController getInstance() {
        if (instance == null) {
            instance = new CollisionController();
        }
        return instance;
    }

    private CollisionController() {
        this.gameObjects = GameLoop.worldObject.getGameObjects();
    }

    @Override
    public void update(long deltaTime) {

        for (GameObject object : gameObjects) {
            Rectangle rect = object.getHitboxObject().getRect();
            for (GameObject object2 : gameObjects) {
                if (object == object2) {
                    continue;
                } else if (object.getGameObjectType().equals(GameObjectType.BULLET) && object2.getGameObjectType().equals(GameObjectType.BULLET)) {
                    continue;
                }

                Rectangle rect2 = object2.getHitboxObject().getRect();
                if (rect.intersects(rect2.getBoundsInLocal())) {
                    // Collision between BULLET and ASTEROID
                    if (object.getGameObjectType() == GameObjectType.BULLET && object2.getGameObjectType() == GameObjectType.ASTEROID
                            || object.getGameObjectType() == GameObjectType.ASTEROID && object2.getGameObjectType() == GameObjectType.BULLET) {
                        GameLoop.worldObject.removeGameObject(object);
                        GameLoop.worldObject.removeGameObject(object2);
                    }
                }
            }
        }
    }
}
