module fxgl.spaceinvader {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens fxgl.spaceinvader to javafx.fxml;
    exports fxgl.spaceinvader;
}