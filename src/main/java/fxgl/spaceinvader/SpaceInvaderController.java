package fxgl.spaceinvader;

import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.FXGLTextFlow;
import com.almasb.fxgl.ui.ProgressBar;
import com.almasb.fxgl.ui.UIController;
import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
public class SpaceInvaderController implements UIController {

    @FXML
    private Label labelScore;

    @FXML
    private Double livesX;

    @FXML
    private Double livesY;

    private List<Texture> lives = new ArrayList<>();

    private GameScene gameScene;

    public SpaceInvaderController(GameScene gameScene) {
        this.gameScene = gameScene;
    }

    @Override
    public void init() {
        labelScore.setFont(getUIFactoryService().newFont(18));

        FXGLTextFlow flow = getUIFactoryService().newTextFlow();
        // TODO: bind dynamically to trigger
        flow.append("Press ", Color.WHITE).append(MouseButton.SECONDARY, Color.BLUE);

    }

    public Label getLabelScore() {
        System.out.println(labelScore);
        if (labelScore == null) {
            System.out.println("labelScore is null!");
        }
        return labelScore;
    }

    public void addLife() {
        int numLives = lives.size();

        Texture texture = getAssetLoader().loadTexture("life.png", 16, 16);
        texture.setTranslateX(livesX + 32 * numLives);
        texture.setTranslateY(livesY);

        lives.add(texture);
        gameScene.addUINode(texture);
    }

    public void loseLife() {
        Texture t = lives.get(lives.size() - 1);

        lives.remove(t);

        Animation animation = getAnimationLoseLife(t);
        animation.setOnFinished(e -> gameScene.removeUINode(t));
        animation.play();

        Viewport viewport = gameScene.getViewport();

        Node flash = new Rectangle(viewport.getWidth(), viewport.getHeight(), Color.rgb(190, 10, 15, 0.5));

        gameScene.addUINode(flash);

        runOnce(() -> gameScene.removeUINode(flash), Duration.seconds(1));
    }

    private Animation getAnimationLoseLife(Texture texture) {
        texture.setFitWidth(64);
        texture.setFitHeight(64);

        Viewport viewport = gameScene.getViewport();

        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.66), texture);
        tt.setToX(viewport.getWidth() / 2 - texture.getFitWidth() / 2);
        tt.setToY(viewport.getHeight() / 2 - texture.getFitHeight() / 2);

        ScaleTransition st = new ScaleTransition(Duration.seconds(0.66), texture);
        st.setToX(0);
        st.setToY(0);

        return new SequentialTransition(tt, st);
    }
}
