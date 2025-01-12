package fxgl.spaceinvader.component;

import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.Required;

import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import fxgl.spaceinvader.Config;
import fxgl.spaceinvader.SpaceInvaderType;
import fxgl.spaceinvader.event.GameEvent;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;


@Required(InvincibleComponent.class)
public class PlayerComponent extends Component {
    public InvincibleComponent invincibility;

    private double dx = 0;
    private double attackSpeed = Config.PLAYER_ATTACK_SPEED;

    private boolean canShoot = true;
    private double lastTimeShot = 0;

    @Override
    public void onUpdate(double tpf) {
        dx = Config.PLAYER_MOVE_SPEED *tpf;

        if(!canShoot) {
            if((getGameTimer().getNow() - lastTimeShot) >= 1.0 / attackSpeed) {
                canShoot = true;
            }
        }
    }

    public void left(){
        if(getEntity().getX() - dx >= 0)
            getEntity().translateX(-dx);
    }

    public void right() {
        if (getEntity().getX() + getEntity().getWidth() + dx <= Config.WIDTH)
            getEntity().translateX(dx);
    }

    public void shoot(){
        if(!canShoot) return;
        canShoot=false;
        lastTimeShot = getGameTimer().getNow();

        var bullet = entityBuilder()
                .at(getEntity().getCenter().subtract(2.5, 10)) // Position slightly above the player
                .view(new Rectangle(5, 10, Color.YELLOW))      // Small yellow rectangle
                .bbox(new HitBox(BoundingShape.box(5, 10)))    // Collision shape
                .with(new ExpireCleanComponent(Duration.seconds(3))) // Remove bullet after 3 seconds
                .with(new ProjectileComponent(new Point2D(0, -1), 150)) // Move bullet upwards
                .type(SpaceInvaderType.BULLET)               // Mark as bullet type
                .buildAndAttach();


    }

    public void die() {
        entity.removeFromWorld();
        fire(new GameEvent(GameEvent.ENEMY_KILLED));
    }
}
