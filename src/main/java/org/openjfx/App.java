package org.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.openjfx.controls.KeyController;

import java.util.Objects;

/**
 * JavaFX App
 */
public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }



    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load up the FXML file, create the View and link it with the Controller
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/MainView.fxml")));

        Scene scene = new Scene(root);

        // Register the key event listener on the scene
        KeyController.getInstance().setScene(scene);

        // Tell JavaFX to show that View in the Window
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        // Fullscreen mode
//      primaryStage.setFullScreen(true);
//      primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        // Make style changes to the Window
        primaryStage.setTitle("Lost In Space");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/ship.png"))));

        // Show the Window
        primaryStage.show();
    }

}