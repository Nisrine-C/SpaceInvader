package fxgl.spaceinvader;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import static com.almasb.fxgl.dsl.FXGL.*;

public class GameApp extends GameApplication {

    private Player player;
    private EnemyManager enemyManager;
    private WallManager wallManager;
    private ParticleSystem particleSystem;



    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setTitle("Space Invaders");
        settings.setVersion("0.1");

    }

    @Override
    protected void initGame() {
        getGameScene().setBackgroundColor(Color.BLACK);

        particleSystem = new ParticleSystem();
        particleSystem.spawnParticles();

        player = new Player();
        player.spawn();

        enemyManager = new EnemyManager();
        enemyManager.spawnEnemies();

        wallManager = new WallManager();
        wallManager.spawnWalls();

    }

    @Override
    protected void initInput() {
        onKey(KeyCode.A, () -> player.moveLeft());
        onKey(KeyCode.D, () -> player.moveRight());
        onKeyDown(KeyCode.SPACE, () -> player.shoot());
    }

    @Override
    protected void initPhysics() {
        onCollisionBegin(EntityType.BULLET, EntityType.ENEMY, (bullet, enemy) -> {
            bullet.removeFromWorld();
            enemy.removeFromWorld();
            inc("score", +100);
        });

        onCollisionBegin(EntityType.BULLET, EntityType.WALL, (bullet, wall) -> {
            bullet.removeFromWorld();
        });
    }

    @Override
    protected void initGameVars(java.util.Map<String, Object> vars) {
        vars.put("score", 0);
    }

    @Override
    protected void initUI() {
        var scoreText = getUIFactoryService().newText("", 24);
        scoreText.textProperty().bind(getWorldProperties().intProperty("score").asString("Score: %d"));
        addUINode(scoreText, 20, 20);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
