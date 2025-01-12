module fxgl.spaceinvader {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens fxgl.spaceinvader to javafx.fxml;
    opens fxgl.spaceinvader.assets.textures;
    opens fxgl.spaceinvader.assets.levels;


    exports fxgl.spaceinvader;
    exports fxgl.spaceinvader.component to com.almasb.fxgl.core;
    exports fxgl.spaceinvader.collision to com.almasb.fxgl.core;
}