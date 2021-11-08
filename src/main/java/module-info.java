/**
 *
 */
module org.openjfx {
    requires javafx.fxml;
    requires javafx.controls;

    opens org.openjfx.controllers to javafx.fxml;

    exports org.openjfx;
}