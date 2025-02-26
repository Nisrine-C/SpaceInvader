package fxgl.spaceinvader.level;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import fxgl.spaceinvader.Config;
import fxgl.spaceinvader.SpaceInvaderType;
import fxgl.spaceinvader.event.GameEvent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.fire;
import static fxgl.spaceinvader.Config.HEIGHT;
import static fxgl.spaceinvader.Config.WIDTH;

public class Level2 extends SpaceLevel{
    public Level2(String level) {
        super(level);
    }

    @Override
    public void init() {
        FXGL.setLevelFromMap("level2.tmx");
        FXGL.getGameWorld().getEntitiesByType(SpaceInvaderType.ENEMY).forEach(enemy->{
            enemy.addComponent(new MoveComponent(20,3));
        });

    }

    private static class MoveComponent extends Component {
        private double movementInterval=100;
        private double speed;
        private double speedIncreaseRate;
        private double direction = 1;
        private double initialX = 0;


        public MoveComponent(double initialSpeed, double speedIncreaseRate) {
            this.speed = initialSpeed;
            this.speedIncreaseRate = speedIncreaseRate;
        }

        @Override
        public void onUpdate(double tpf) {
            if (initialX == 0){
                initialX = entity.getX();
            }else {
                speed += speedIncreaseRate * tpf;
                double moveX = direction * speed * tpf;
                entity.translateX(moveX);

                if (entity.getX() >= initialX + movementInterval || entity.getX() <= initialX - movementInterval) {
                    direction *= -1;
                    entity.translateY(32);
                }

                if (entity.getY() >= HEIGHT - 100) {
                    fire(new GameEvent(GameEvent.ENEMY_REACHED_END));
                }
            }
        }
    }
}
