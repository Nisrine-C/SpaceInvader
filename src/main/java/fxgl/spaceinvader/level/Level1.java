package fxgl.spaceinvader.level;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import fxgl.spaceinvader.Config;
import fxgl.spaceinvader.SpaceInvaderType;
import fxgl.spaceinvader.event.GameEvent;

import static com.almasb.fxgl.dsl.FXGL.*;
import static fxgl.spaceinvader.Config.HEIGHT;

import java.util.List;

public class Level1 extends SpaceLevel {

    private static final double GROUP_SPEED = 20;
    private static final double STEP_DOWN = 16;
    private static final double LEFT_BOUNDARY = 150;
    private static final double RIGHT_BOUNDARY = Config.WIDTH - 150;


    private List<Entity> enemies;

    public Level1(String level) {
        super(level);
    }

    @Override
    public void init() {
        setLevelFromMap(getLevel());
        enemies = getGameWorld().getEntitiesByType(SpaceInvaderType.ENEMY);
        enemies.forEach(enemy -> enemy.addComponent(new MoveComponent(20, 1)));
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
