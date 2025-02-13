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

        private double t = 0;
        private  double speed;
        private double speedIncreaseRate;

        public MoveComponent(double initialSpeed) {
            this(initialSpeed, 0.5); // Default speed increase rate
        }

        public MoveComponent(double initialSpeed, double speedIncreaseRate) {
            this.speed = initialSpeed;
            this.speedIncreaseRate = speedIncreaseRate;
        }
        @Override
        public void onUpdate(double tpf) {
            t += tpf * speed;

            double absoluteSpeed = Math.abs(speed) + speedIncreaseRate * tpf;
            speed = speed < 0 ? -absoluteSpeed : absoluteSpeed;

            double newX = entity.getX() + speed * tpf;
            double newY;
            entity.setPosition(newX, entity.getY());

            if(entity.getX() + entity.getWidth() / 2 >= Config.WIDTH -25 || entity.getX() - entity.getWidth() / 2 <= 0){
                speed=-speed;
                newY = entity.getY() + entity.getHeight() + 16;
                entity.setPosition( entity.getX(),newY);
            }
            if(entity.getY() >= HEIGHT - 284){
                fire(new GameEvent(GameEvent.ENEMY_REACHED_END));
            }
        }

    }
}
