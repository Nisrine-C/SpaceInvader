package fxgl.spaceinvader;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.MenuItem;
import com.almasb.fxgl.app.GameSettings;
import fxgl.spaceinvader.event.GameEvent;
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
    protected void onPreInit() {
        onEvent(GameEvent.ENEMY_KILLED,this::onEnemyKilled);
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
        vars.put("level", 0); // Tracks the current level index
        vars.put("lives", START_LIVES);
        vars.put("enemiesKilled", 0);
    }

    @Override
    protected void initGame() {
        levels = Arrays.asList(
                new Level1() // Add more levels as needed
        );

        getGameWorld().addEntityFactory(new SpaceInvaderFactory());
        startLevel();
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
        levels.getFirst().init();

    }

    @Override
    protected void onUpdate(double tpf) {
        List<Entity> enemies = getGameWorld().getEntitiesByType(SpaceInvaderType.ENEMY);
        if(enemies.size() == 0){
            endGame();
        }
    }

    private void cleanupLevel() {
        getGameWorld().getEntitiesByType(
                SpaceInvaderType.ENEMY,
                SpaceInvaderType.WALL,
                SpaceInvaderType.BULLET,
                SpaceInvaderType.PLAYER
        ).forEach(Entity::removeFromWorld);
    }

    private void endGame() {
        System.out.println("Game Over! Final Score: " + geti("score"));
        cleanupLevel();
        setLevelFromMap("level2.tmx");
        spawnBackground();
        spawnPlayer();
    }

    public void onEnemyKilled(GameEvent event){
        System.out.println("Hello");
        FXGL.inc("score",+100);
        FXGL.inc("enemiesKilled",+1);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
