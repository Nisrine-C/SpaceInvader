package fxgl.spaceinvader.component;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.ExpireCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.time.LocalTimer;

import fxgl.spaceinvader.SpaceInvaderType;
import fxgl.spaceinvader.event.GameEvent;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.spawn;

public class EnemyComponent extends Component {

    protected LocalTimer attackTimer;
    protected Duration nextAttack = Duration.seconds(2);

    private final int originalLives;
    private int lives;

    public EnemyComponent(int lives ){
        this.lives = lives;
        originalLives = lives;
    }

    @Override
    public void onAdded() {
        attackTimer = FXGL.newLocalTimer();
        attackTimer.capture();
    }

    @Override
    public void onUpdate(double tpf) {
        if(attackTimer.elapsed(nextAttack)) {
            if(FXGLMath.randomBoolean(0.3f)) {
                shoot();
            }
            nextAttack = Duration.seconds(5*Math.random());
            attackTimer.capture();
        }
    }

    protected void shoot() {
        spawn("Bullet",new SpawnData(0,0).put("owner",getEntity()));
    }

    public void onHit(){
        lives--;
        if (lives == 0) {
            entity.getComponent(CollidableComponent.class).setValue(false);
            this.die();
        } else if (lives == originalLives / 2) {
            entity.getViewComponent().clearChildren();
        }
    }

    public void die() {
        FXGL.fire(new GameEvent(GameEvent.ENEMY_KILLED));
        entity.removeFromWorld();
    }
}
