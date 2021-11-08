package org.openjfx.controls;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import org.openjfx.interfaces.Updatable;

import java.util.HashSet;

public class KeyController implements Updatable {

    private static KeyController instance;
    private final HashSet<KeyCode> keys;


    // Singleton Class
    public static KeyController getInstance() {
        if (instance == null) {
            instance = new KeyController();
        }
        return instance;
    }

    private KeyController() {
        keys = new HashSet<>();
    }

    public void setScene(Scene scene) {
        scene.setOnKeyPressed((keyEvent -> this.keys.add(keyEvent.getCode())));
        scene.setOnKeyReleased((keyEvent -> this.keys.remove(keyEvent.getCode())));
    }


    @Override
    public void update(long deltaTime) {
    }

    public boolean isKeyPressed(KeyCode keyCode) {
        return keys.contains(keyCode);
    }

}