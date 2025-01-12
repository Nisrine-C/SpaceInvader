package fxgl.spaceinvader;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.MenuItem;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.input.Input;
import fxgl.spaceinvader.component.PlayerComponent;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Arrays;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;
import static fxgl.spaceinvader.Config.*;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class SpaceInvaderApp extends GameApplication {

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Space Invaders");
        settings.setVersion("1.0.1");
        settings.setWidth(WIDTH);
        settings.setHeight(HEIGHT);
        settings.setProfilingEnabled(false);
        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(true);
        settings.setManualResizeEnabled(true);
        settings.setPreserveResizeRatio(true);
        settings.setIntroEnabled(false);
        settings.setApplicationMode(ApplicationMode.RELEASE);
    }

    @Override
    protected void initGame() {
        FXGL.getGameWorld().addEntityFactory(new SpaceInvaderFactory());
        spawnBackground();
        spawnPlayer();
        spawnEnemies();
    }

    @Override
    protected void initInput() {
        Input input = getInput();

        onKey(KeyCode.LEFT,"Move Left",() -> playerComponent.left());
        onKey(KeyCode.RIGHT,"Move Right",() -> playerComponent.right());
        onKey(KeyCode.UP,"Shoot",() -> playerComponent.shoot());

    }


    private Entity player;
    private List<Entity> enemies = new ArrayList<>();

    private PlayerComponent playerComponent;

    public void spawnPlayer () {
        player = FXGL.spawn("Player",WIDTH/2-20,HEIGHT-100);
        playerComponent = player.getComponent(PlayerComponent.class);
    }
    public void spawnBackground(){
        FXGL.spawn("Background", new SpawnData(0, 0).put("width", WIDTH).put("height", HEIGHT));
    }

    protected void addEnemy(Entity entity) {
        enemies.add(entity);
    }

    public Entity spawnEnemy(double x, double y) {
        Entity enemy = spawn("Enemy", x, y);

        addEnemy(enemy);

        animationBuilder()
                .interpolator(Interpolators.ELASTIC.EASE_OUT())
                .duration(Duration.seconds(FXGLMath.random(0.0, 1.0) * 2))
                .scale(enemy)
                .from(new Point2D(0, 0))
                .to(new Point2D(1, 1))
                .buildAndPlay();

        return enemy;
    }
    public void spawnEnemies(){
        double t = 0;

        for (int y = 0; y < ENEMY_ROWS; y++) {
            for (int x = 0; x < ENEMIES_PER_ROW; x++) {

                runOnce(() -> {
                    Entity enemy = spawnEnemy(50, 50);
                    enemy.addComponent(new MoveComponent());
                }, Duration.seconds(t));

                t += 0.1;
            }
        }
    }
    public static void main(String[] args) {
        launch(args);
    }

    private static class MoveComponent extends Component {

        private double t = 0;

        @Override
        public void onUpdate(double tpf) {
            entity.setPosition(curveFunction().add(WIDTH / 2, HEIGHT / 2 - 100));

            t += tpf;
        }

        private Point2D curveFunction() {
            double x = cos(5*t) - cos(2*t);
            double y = sin(3*t) - sin(t);

            return new Point2D(x, -y).multiply(85);
        }
    }
}

