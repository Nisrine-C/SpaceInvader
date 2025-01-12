package fxgl.spaceinvader;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.geometry.Point2D;

import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.*;

public class ParticleSystem {


    private Random random = new Random();// Speed in pixels per second

    public void spawnParticles() {
        // Spawn a number of particles at random positions
        for (int i = 0; i < 100; i++) { // 100 particles for example
            spawnParticle(Math.random() * getAppWidth(), Math.random() * getAppHeight());
        }
    }

    private void spawnParticle(double x, double y) {

        double speed = 50 + random.nextDouble() * 150;
        double opacity = 0.1 + random.nextDouble() * 0.9;

        Entity particle = entityBuilder()
                .at(x, y)
                .viewWithBBox(new Circle(2, Color.WHITESMOKE))
                .with(new ParticleMovementComponent(speed, opacity))
                .buildAndAttach();
    }

    public static class ParticleMovementComponent extends Component {


        private double speed;
        private double opacity;

        public ParticleMovementComponent(double speed, double opacity) {
            this.speed = speed;
            this.opacity = opacity;
        }

        @Override
        public void onAdded() {
            // Set the opacity of the particle when it is added
            entity.getViewComponent().getChildren().get(0).setOpacity(opacity);
        }

        public void onUpdate(double tpf) {
            // Only move the particle down (on Y-axis)
            entity.translateY(speed * tpf);

            // Reset the particle to the top of the screen when it moves off the bottom
            if (entity.getY() > getAppHeight()) {
                entity.setY(0); // Reappear at the top
            }
        }
    }
}
