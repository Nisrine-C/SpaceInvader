package fxgl.spaceinvader.component;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.animationBuilder;
import static com.almasb.fxgl.dsl.FXGL.getAppHeight;

public class WallComponent extends Component {

    private final int originalLives;
    private int lives;

    public WallComponent(int lives) {
        this.lives = lives;
        originalLives = lives;
    }

    public void onHit() {
        lives--;

        animationBuilder()
                .autoReverse(true)
                .repeat(2)
                .interpolator(Interpolators.CIRCULAR.EASE_IN())
                .duration(Duration.seconds(0.33))
                .scale(entity)
                .to(new Point2D(1.2, 1.2))
                .buildAndPlay();

        if (lives == 0) {
            entity.getComponent(CollidableComponent.class).setValue(false);

            animationBuilder()
                    .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())
                    .duration(Duration.seconds(0.8))
                    .onFinished(entity::removeFromWorld)
                    .translate(entity)
                    .from(entity.getPosition())
                    .to(new Point2D(entity.getX(), getAppHeight() + 10))
                    .buildAndPlay();

        } else if (lives == originalLives / 2) {
            entity.getViewComponent().clearChildren();
        }
    }
}