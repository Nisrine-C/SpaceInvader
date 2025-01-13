package fxgl.spaceinvader.level;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.entity.level.tiled.TMXLevelLoader;
import fxgl.spaceinvader.SpaceInvaderType;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;


/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public abstract class SpaceLevel {

    private List<Entity> enemies = new ArrayList<>();

    public SpaceLevel() {

    }

    public abstract void init();

    public void onUpdate(double tpf) {

    }

    public void destroy() {

    }

    public void playInCutscene(Runnable onFinished) {
        onFinished.run();
    }

    public void playOutCutscene(Runnable onFinished) {
        onFinished.run();
    }



    protected void addEnemy(Entity entity) {
        enemies.add(entity);
    }


    public boolean isFinished() {
        return enemies.stream().noneMatch(Entity::isActive);
    }

    protected void loadLevelFromTMX(String tmxFilePath) {
        // Load level from the specified .tmx file
        Level level = getAssetLoader().loadLevel(tmxFilePath, new TMXLevelLoader());

        // Extract entities from the level based on object layers
        level.getEntities().forEach(entity -> {
            if (entity.isType(SpaceInvaderType.ENEMY)) {
                addEnemy(entity);
            }
        });
    }

    public List<Entity> getEnemies(){return enemies;}
}
