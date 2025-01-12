package fxgl.spaceinvader;

import com.almasb.fxgl.entity.Entity;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGL.*;

public class WallManager {

    public void spawnWalls() {
        // Spawn 3 walls across the screen
//        spawnWall(150, 400);
        spawnWall(350, 400);
//        spawnWall(550, 400);
    }

    private void spawnWall(double x, double y) {
        entityBuilder()
                .at(x, y)
                .type(EntityType.WALL)
                .viewWithBBox(new Rectangle(80, 20, Color.BROWN)) // Wall dimensions and color
                .collidable()
                .with(new WallMovementComponent()) // Add movement logic
                .buildAndAttach();
    }
}
