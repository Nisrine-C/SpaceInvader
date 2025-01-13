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
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.level.tiled.TMXLevelLoader;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.texture.Texture;
import com.almasb.fxgl.ui.UI;
import fxgl.spaceinvader.collision.BulletEnemyHandler;
import fxgl.spaceinvader.collision.BulletPlayerHandler;
import fxgl.spaceinvader.collision.BulletShieldHandler;
import fxgl.spaceinvader.collision.BulletWallHandler;
import fxgl.spaceinvader.component.PlayerComponent;
import fxgl.spaceinvader.event.GameEvent;
import fxgl.spaceinvader.level.Level1;
import fxgl.spaceinvader.level.SpaceLevel;

import fxgl.spaceinvader.particles.ParticleSystem;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.*;
import java.util.stream.IntStream;

import static com.almasb.fxgl.dsl.FXGL.*;
import static fxgl.spaceinvader.Config.*;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
public class SpaceInvaderApp extends GameApplication {

    private ParticleSystem particleSystem;
    private SpaceLevel currentLevel = null; // Initialize as null
    private List<SpaceLevel> levels;

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
    protected void initInput() {
        Input input = getInput();

        onKey(KeyCode.LEFT, "Move Left", () -> playerComponent.left());
        onKey(KeyCode.RIGHT, "Move Right", () -> playerComponent.right());
        onKey(KeyCode.UP, "Shoot", () -> playerComponent.shoot());
        onKey(KeyCode.SPACE, "Shield Up", () -> playerComponent.shieldUp());
    }

    private Entity player;
    private PlayerComponent playerComponent;

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("score", 0);
        vars.put("level", 1); // Tracks the current level index
        vars.put("lives", START_LIVES);
        vars.put("enemiesKilled", 0);
    }

    @Override
    protected void initGame() {
        levels = Arrays.asList(
                new Level1() // Add more levels as needed
        );

        getGameWorld().addEntityFactory(new SpaceInvaderFactory());
        setLevelFromMap("level1.tmx");
        getGameWorld().getEntitiesByType(SpaceInvaderType.ENEMY).forEach(enemy ->{
            enemy.addComponent(new MoveComponent());
        });
        spawnBackground();
        spawnPlayer();
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new BulletEnemyHandler());
        getPhysicsWorld().addCollisionHandler(new BulletPlayerHandler());
        getPhysicsWorld().addCollisionHandler(new BulletWallHandler());
        getPhysicsWorld().addCollisionHandler(new BulletShieldHandler());
    }

    public void spawnPlayer() {
        player = FXGL.spawn("Player", WIDTH / 2 - 20, HEIGHT - 100);
        playerComponent = player.getComponent(PlayerComponent.class);
    }

    public void spawnBackground() {
        getGameScene().setBackgroundColor(Color.BLACK);
        particleSystem = new ParticleSystem();
        particleSystem.spawnParticles();
    }

    private void startLevel() {
        int levelIndex = geti("level"); // Get the current level index

        // Cleanup previous level
        if (currentLevel != null) {
            currentLevel.destroy();
            cleanupLevel();
        }

        // Load the next level
        if (levelIndex < levels.size()) {
            currentLevel = levels.get(levelIndex);
            currentLevel.init();
        } else {
            System.out.println("No more levels to load. Game Over!");
            endGame();
        }
    }

    @Override
    protected void onUpdate(double tpf) {
        System.out.println(geti("enemiesKilled"));
    }

    private void cleanupLevel() {
        getGameWorld().getEntitiesByType(
                SpaceInvaderType.ENEMY,
                SpaceInvaderType.WALL,
                SpaceInvaderType.BULLET
        ).forEach(Entity::removeFromWorld);
    }

    private void endGame() {
        System.out.println("Game Over! Final Score: " + geti("score"));
        // Add any additional game-over logic, such as showing a UI screen
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
                if (newX <= LEFT_BOUND  || newX >= RIGHT_BOUND - 48) {
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
