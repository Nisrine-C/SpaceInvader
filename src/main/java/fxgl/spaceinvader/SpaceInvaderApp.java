package fxgl.spaceinvader;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.ui.FontFactory;
import fxgl.spaceinvader.event.GameEvent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.Input;
import fxgl.spaceinvader.collision.BulletEnemyHandler;
import fxgl.spaceinvader.collision.BulletPlayerHandler;
import fxgl.spaceinvader.collision.BulletShieldHandler;
import fxgl.spaceinvader.collision.BulletWallHandler;
import fxgl.spaceinvader.component.PlayerComponent;
import fxgl.spaceinvader.level.Level1;
import fxgl.spaceinvader.level.SpaceLevel;

import fxgl.spaceinvader.ui.MySceneFactory;
import fxgl.spaceinvader.particles.ParticleSystem;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.*;

import static com.almasb.fxgl.dsl.FXGL.*;
import static fxgl.spaceinvader.Config.*;

public class SpaceInvaderApp extends GameApplication {

    private ParticleSystem particleSystem;
    private List<SpaceLevel> levels;


    List<String> cssList = List.of("fxgl_dark.css","ui.css");
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Space Invaders");
        settings.setVersion("1.0.1");
        settings.setWidth(WIDTH);
        settings.setHeight(HEIGHT);
        settings.setProfilingEnabled(false);
        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(true);
        settings.setSceneFactory(new MySceneFactory());
        settings.setManualResizeEnabled(true);
        settings.setPreserveResizeRatio(true);
        settings.setIntroEnabled(false);
        settings.setApplicationMode(ApplicationMode.RELEASE);
        settings.setCSSList(cssList);
        settings.setFontUI("ARCADECLASSIC.TTF");
        settings.setFontGame("ARCADECLASSIC.TTF");
        settings.setFontMono("ARCADECLASSIC.TTF");
        settings.setFontText("ARCADECLASSIC.TTF");
    }

    @Override
    protected void onPreInit() {
        getSettings().setGlobalSoundVolume(0.1);
        getSettings().setGlobalMusicVolume(0.5);

        loopBGM("bgm.mp3");

        onEvent(GameEvent.ENEMY_KILLED,this::onEnemyKilled);
        onEvent(GameEvent.ENEMY_REACHED_END,this::onEnemyReachedEnd);
        onEvent(GameEvent.PLAYER_GOT_HIT,this::onPlayerGotHit);
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

    private int highScore;
    private String highScoreName;
    private SaveData savedData = null;

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("score", 0);
        vars.put("level", 0); // Tracks the current level index
        vars.put("lives", START_LIVES);
        vars.put("enemiesKilled", 0);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new SpaceInvaderFactory());

        getFileSystemService().<SaveData>readDataTask(SAVE_DATA_NAME)
                .onSuccess(data -> savedData = data)
                .onFailure(ignore -> {})
                .run();

        initGame(savedData == null
                ? new SaveData("CPU", 1000)
                : savedData);

    }

    @Override
    protected void initUI() {
        System.out.println(FXGL.getAssetLoader().loadCSS("ui.css"));
        Label scoreLabel = new Label();
        scoreLabel.setFont(getUIFactoryService().newFont(18));
        scoreLabel.setTextFill(Color.WHITE);
        scoreLabel.textProperty().bind(FXGL.getip("score").asString("Score\n %d"));
        FXGL.addUINode(scoreLabel, 45, 20);

        Label highScoreLabel = new Label();
        highScoreLabel.setFont(getUIFactoryService().newFont(18));
        highScoreLabel.setTextFill(Color.WHITE);
        highScoreLabel.setText("HiScore \n" + highScore );
        FXGL.addUINode(highScoreLabel,WIDTH-125,20);

    }


    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new BulletEnemyHandler());
        getPhysicsWorld().addCollisionHandler(new BulletPlayerHandler());
        getPhysicsWorld().addCollisionHandler(new BulletWallHandler());
        getPhysicsWorld().addCollisionHandler(new BulletShieldHandler());
    }

    private void initGame(SaveData data) {
        highScoreName = data.getName();
        highScore = data.getHighScore();
        levels = Arrays.asList(
                new Level1("level1.tmx"),
                new Level1("level2.tmx")
        );

        startLevel();
        spawnBackground();
        spawnPlayer();

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
        levels.get(geti("level")).init();
    }

    @Override
    protected void onUpdate(double tpf) {
        List<Entity> enemies = getGameWorld().getEntitiesByType(SpaceInvaderType.ENEMY);
        if(enemies.size() == 0){
            inc("level",+1);
            nextLevel();
        }
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
        showGameOver();
    }

    private void nextLevel(){
        cleanupLevel();
        if(levels.size()==geti("level")){
            endGame();
            return;
        }
        startLevel();
        spawnBackground();
        spawnPlayer();
    }

    public void onEnemyKilled(GameEvent event){
        FXGL.inc("score",+100);
        FXGL.inc("enemiesKilled",+1);
    }

    public void onEnemyReachedEnd(GameEvent event){
        showGameOver();
    }

    private void showGameOver() {
        getDialogService().showConfirmationBox("Game Over. Play Again?", yes -> {
            if (yes) {
                getGameWorld().getEntitiesCopy().forEach(Entity::removeFromWorld);
                getGameController().startNewGame();
            } else {
                int score = geti("score");
                if (score > highScore) {
                    getDialogService().showInputBox("High Score! Enter your name", playerName -> {

                        getFileSystemService().writeDataTask(new SaveData(playerName, score), SAVE_DATA_NAME).run();

                        getGameController().exit();
                    });
                } else {
                    getGameController().exit();
                }
            }
        });
    }

    private void onPlayerGotHit(GameEvent event) {
        getGameScene().getViewport().shakeTranslational(9.5);

        inc("lives", -1);

        playerComponent.enableInvincibility();
        runOnce(playerComponent::disableInvincibility, Duration.seconds(INVINCIBILITY_TIME));


        if (geti("lives") == 0)
            showGameOver();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
