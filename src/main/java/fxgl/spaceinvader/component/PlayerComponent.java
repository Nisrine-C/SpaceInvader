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

    private boolean canGenerateShield=true;
    private double lastTimeGeneratedShield = 0;

    @Override
    public void onUpdate(double tpf) {
        dx = Config.PLAYER_MOVE_SPEED *tpf;

        if(!canShoot) {
            if((getGameTimer().getNow() - lastTimeShot) >= 1.0 / attackSpeed) {
                canShoot = true;
            }
        }

        if(!canGenerateShield) {
            if((getGameTimer().getNow() - lastTimeGeneratedShield) >= Config.SHIELD_COOLDOWN){
                canGenerateShield = true;
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

        canShoot = false;
        lastTimeShot = getGameTimer().getNow();
        spawn("Bullet", new SpawnData(0, 0).put("owner", getEntity()));
    }

    public void shieldUp(){
        if(!canGenerateShield)return;
        canGenerateShield=false;
        lastTimeGeneratedShield = getGameTimer().getNow();
        spawn("Shield", new SpawnData(0,0).put("owner",getEntity()));
    }

    public void enableInvincibility() {
        invincibility.setValue(true);
    }
    public void disableInvincibility(){invincibility.setValue(false);}

    public void die() {
        entity.removeFromWorld();
        fire(new GameEvent(GameEvent.ENEMY_KILLED));
    }
}
