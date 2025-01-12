package fxgl.spaceinvader;

import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.List;

public class EnemyMovementComponent extends Component {

    private Point2D direction;
    private static List<EnemyMovementComponent> allEnemies;
    private Timeline movementTimer;
    private double speed = 300;

    // Boundaries to reverse direction
    private static final double LEFT_BOUND = 50;
    private static final double RIGHT_BOUND = 700;


    private static final double DOWN_DROP = 50;
    private boolean movingDown = false;

    public static void setAllEnemies(List<EnemyMovementComponent> allEnemies) {
        EnemyMovementComponent.allEnemies = allEnemies;
    }

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
        // Stop the timer when the component is removed
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
