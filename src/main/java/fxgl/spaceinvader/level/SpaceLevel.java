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

    private String level;
    private List<Entity> enemies;

    public SpaceLevel(String level) {
        this.level=level;
        enemies = new ArrayList<>();
    }

    public void init(){

    };

    public List<Entity> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Entity> enemies) {
        this.enemies = enemies;
    }

    public String getLevel() {
        return level;
    }
}
