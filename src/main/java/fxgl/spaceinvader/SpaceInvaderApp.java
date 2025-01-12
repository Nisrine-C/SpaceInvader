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
import fxgl.spaceinvader.collision.BulletEnemyHandler;
import fxgl.spaceinvader.collision.BulletPlayerHandler;
import fxgl.spaceinvader.component.PlayerComponent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new BulletEnemyHandler());
        getPhysicsWorld().addCollisionHandler(new BulletPlayerHandler());

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

    public class MoveComponent extends Component {

        private Point2D direction;
        private Timeline movementTimer;
        private double speed = 300;

        // Boundaries to reverse direction
        private static final double LEFT_BOUND = 50;
        private static final double RIGHT_BOUND = WIDTH-25;


        private static final double DOWN_DROP = 50;
        private boolean movingDown = false;


        @Override
        public void onAdded() {
            // Start by moving to the right initially
            direction = new Point2D(1, 0);

            // Set up the timer to update the movement every 0.5 seconds
            movementTimer = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> updateMovement()));
            movementTimer.setCycleCount(Timeline.INDEFINITE);
            movementTimer.play();
        }

        @Override
        public void onRemoved() {
            if (movementTimer != null) {
                movementTimer.stop();
            }
        }

        private void updateMovement() {
            double currentX = entity.getX();
            double currentY = entity.getY();

            if (!movingDown) {
                double newX = currentX + direction.getX() * speed * 0.1;

                // If enemy hits left or right boundary, reverse direction and move down
                if (newX <= LEFT_BOUND || newX >= RIGHT_BOUND) {
                    direction = direction.multiply(-1); // Reverse the direction horizontally
                    movingDown = true; // After hitting boundary, move down
                } else {
                    // Update the enemy's position horizontally
                    entity.setPosition(newX, currentY);
                }
            } else {
                // If moving down, update vertical position
                double newY = currentY + DOWN_DROP;

                // After moving down, reset movingDown flag and continue horizontal movement
                movingDown = false;
                entity.setPosition(currentX, newY);
            }
        }

    }

}









