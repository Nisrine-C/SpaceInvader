package fxgl.spaceinvader;

import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.*;

public class Player {
    private Entity playerEntity;

    public void spawn() {
        playerEntity = entityBuilder()
                .at(getAppWidth() / 2.0 - 20, getAppHeight() - 50)
                .type(EntityType.PLAYER)
                .viewWithBBox(new Rectangle(40, 20, Color.BLUE))
                .collidable()
                .buildAndAttach();
    }

    public void moveLeft() {
        if (playerEntity.getX() > 0) {
            playerEntity.translateX(-5);
        }
    }

    public void moveRight() {
        if (playerEntity.getRightX() < getAppWidth()) {
            playerEntity.translateX(5);
        }
    }

    public void shoot() {
        entityBuilder()
                .at(playerEntity.getX() + 20, playerEntity.getY() - 10)
                .type(EntityType.BULLET)
                .viewWithBBox(new Rectangle(5, 10, Color.WHITE))
                .with(new ProjectileComponent(new javafx.geometry.Point2D(0, -1), 300))
                .collidable()
                .buildAndAttach();
    }
}
