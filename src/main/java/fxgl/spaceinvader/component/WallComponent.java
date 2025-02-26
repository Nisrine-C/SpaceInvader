package fxgl.spaceinvader.component;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.animationBuilder;


public class WallComponent extends Component {

    private String wallType;

    private final int originalLives;
    private int lives;


    public WallComponent(int lives,String wallType) {
        this.lives = lives;
        originalLives = lives;
        this.wallType = wallType;
    }

    public void onHit() {
        lives--;



        if (lives == 0) {
            entity.getComponent(CollidableComponent.class).setValue(false);
            entity.removeFromWorld();
        }else{
            updateTexture();
        }
    }

    private void updateTexture() {
        String textureName;

        // Determine the texture based on remaining lives
        if (lives > originalLives / 2) {
            textureName= wallType.equals("right") ? "wall_right_2.png" : "wall_left_2.png";
        } else if (lives > 0) {
            textureName= wallType.equals("right") ? "wall_right_3.png" : "wall_left_3.png";
        } else {
            textureName= wallType.equals("right") ? "wall_right_3.png" : "wall_left_3.png";
        }

        // Clear the current view and set the new texture
        entity.getViewComponent().clearChildren();
        Texture newTexture = FXGL.texture(textureName);
        entity.getViewComponent().addChild(newTexture);
    }
}