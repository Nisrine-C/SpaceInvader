package fxgl.spaceinvader.ui;

import com.almasb.fxgl.animation.Animation;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.core.util.EmptyRunnable;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.FXGLButton;
import com.almasb.fxgl.ui.FontFactory;
import com.almasb.fxgl.ui.FontType;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;


public class MenuTest extends FXGLMenu {

    private static final int SIZE = 150;

    private Animation<?> animation;

    public MenuTest() {
        super(MenuType.GAME_MENU);

        // Create a VBox for menu buttons
        VBox menuBox = new VBox(20); // Increased spacing between buttons
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setPadding(new Insets(20)); // Adds extra padding around buttons

        // Load the texture directly (assumes the image is in src/main/resources/assets/textures/)
        Texture backgroundTexture = FXGL.texture("background/main_menu.png");

        // Resize the texture to cover the entire game window.
        backgroundTexture.setFitWidth(FXGL.getAppWidth());
        backgroundTexture.setFitHeight(FXGL.getAppHeight());

        // Add the texture to the content root at index 0 to ensure it's behind other UI elements.
        getContentRoot().getChildren().add(0, backgroundTexture);

        Texture startTexture = FXGL.texture("background/Start.png");
        Button btnStart = new FXGLButton();
        btnStart.setPrefSize(startTexture.getWidth(), startTexture.getHeight());
        btnStart.setStyle("-fx-background-image: url('assets/textures/background/Start.png');");
        btnStart.getStyleClass().add("custom-button");
        btnStart.setOnAction(e -> fireNewGame());

        Texture highTexture = FXGL.texture("background/HighScores.png");
        Button btnHigh = new FXGLButton("");
        btnHigh.setPrefSize(highTexture.getWidth(), highTexture.getHeight());
        btnHigh.setStyle("-fx-background-image: url('assets/textures/background/HighScores.png');");
        btnHigh.getStyleClass().add("custom-button");
        btnHigh.setOnAction(e -> fireExit());

        Texture exitTexture = FXGL.texture("background/Exit.png");
        Button btnExit = new FXGLButton("");
        btnExit.setPrefSize(exitTexture.getWidth(), exitTexture.getHeight());
        btnExit.setStyle("-fx-background-image: url('assets/textures/background/Exit.png');");
        btnExit.getStyleClass().add("custom-button");

        menuBox.getChildren().addAll(btnStart, btnHigh, btnExit);

        // Center the menuBox
        menuBox.setTranslateX(FXGL.getAppWidth() / 2.0 - (highTexture.getWidth()+40)/ 2);
        menuBox.setTranslateY(FXGL.getAppHeight() / 2.0 - (startTexture.getHeight() + exitTexture.getHeight()+highTexture.getHeight() + 40)/2 );

        getContentRoot().getChildren().add(menuBox);
    }


}
