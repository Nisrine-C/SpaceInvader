package fxgl.spaceinvader;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import static com.almasb.fxgl.dsl.FXGL.*;

import java.util.ArrayList;
import java.util.List;

public class EnemyManager {

    private List<EnemyMovementComponent> enemyComponents = new ArrayList<>(); // To store enemy components

    public void spawnEnemies() {
        for (int x = 0; x < 6; x++) { // 5 enemies in a row
            for (int y = 0; y < 3; y++) { // 3 rows of enemies
                spawnEnemy(x * 100 + 50, y * 50 + 50); // Adjust spacing as needed
            }
        }

        EnemyMovementComponent.setAllEnemies(enemyComponents);
    }

    private void spawnEnemy(double x, double y) {
        var enemy = entityBuilder()
                .at(x, y)
                .type(EntityType.ENEMY)
                .viewWithBBox(new Rectangle(40, 40, Color.DIMGRAY)) // Use an enemy sprite
                .collidable()
                .with(new EnemyMovementComponent()) // Attach the movement component
                .buildAndAttach();

        // Add this enemy's movement component to the list
        enemyComponents.add(enemy.getComponent(EnemyMovementComponent.class));
    }
}
