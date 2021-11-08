package org.openjfx.controllers;

import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import org.openjfx.animation.GameLoopTimer;
import org.openjfx.controls.AsteroidController;
import org.openjfx.controls.CollisionController;
import org.openjfx.controls.KeyController;
import org.openjfx.interfaces.InputSystem;
import org.openjfx.models.GameObject;
import org.openjfx.models.WorldObject;
import org.openjfx.rendering.Renderer;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class GameLoop implements Initializable {

    public Canvas gameCanvas;
    public AnchorPane gameAnchor;

    public static WorldObject worldObject;
    private Renderer renderer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeGame();

        GameLoopTimer timer = new GameLoopTimer() {
            @Override
            public void tick(long diffNanos) {             // GAMELOOP

                long diffMillis = (long) (diffNanos / 1e6);
                update(diffMillis);                          // UPDATE
                draw();                                      // DRAW
            }
        };
        timer.start();
    }


    private void update(long deltaTime) {
        renderer.prepare();

        for (GameObject gameObject : worldObject.getGameObjects()) {
            if (gameObject instanceof InputSystem) {
                ((InputSystem) gameObject).processInput(deltaTime);
            }
            gameObject.update(deltaTime);
        }

        AsteroidController.getInstance().update(deltaTime);
        CollisionController.getInstance().update(deltaTime);
        KeyController.getInstance().update(deltaTime);

        worldObject.refreshGameObjects();
    }

    private void draw() {
        renderer.render();
    }


    private void initializeGame() {
        KeyController.getInstance();
        worldObject = new WorldObject(gameCanvas);

        initializeCanvas();
        initializeRenderer();
    }

    private void initializeRenderer() {
        this.renderer = new Renderer(this.gameCanvas);
        renderer.setBackground(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/SpaceBackground.jpg"))));
    }

    private void initializeCanvas() {
        gameCanvas.widthProperty().bind(gameAnchor.widthProperty());
        gameCanvas.heightProperty().bind(gameAnchor.heightProperty());
    }

}
