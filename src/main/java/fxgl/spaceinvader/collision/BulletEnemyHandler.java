package fxgl.spaceinvader.collision;

import com.almasb.fxgl.dsl.components.Effect;
import com.almasb.fxgl.dsl.components.EffectComponent;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.TimeComponent;
import com.almasb.fxgl.physics.CollisionHandler;
import fxgl.spaceinvader.SpaceInvaderType;
import fxgl.spaceinvader.component.EnemyComponent;
import fxgl.spaceinvader.component.OwnerComponent;
import javafx.util.Duration;


public class BulletEnemyHandler extends CollisionHandler {

    public BulletEnemyHandler(){
        super(SpaceInvaderType.BULLET, SpaceInvaderType.ENEMY);
    }

    @Override
    protected void onCollisionBegin(Entity bullet, Entity enemy) {
        Object owner = bullet.getComponent(OwnerComponent.class).getValue();

        if(owner == SpaceInvaderType.ENEMY) {
            return;
        }
        var hp = enemy.getComponent(HealthIntComponent.class);
        hp.damage(1);

        bullet.removeFromWorld();
        if(hp.isZero()){
            if(enemy.hasComponent(EnemyComponent.class)){
                enemy.getComponent(EnemyComponent.class).die();
            }
        }else{
            enemy.getComponentOptional(EffectComponent.class).ifPresent(e->e.startEffect(new Effect(Duration.seconds(1)) {
                @Override
                public void onStart(Entity entity) {
                    entity.getComponent(TimeComponent.class).setValue(0.15);
                }

                @Override
                public void onEnd(Entity entity) {
                    entity.getComponent(TimeComponent.class).setValue(1);
                }
            }));
        }

        enemy.getComponent(EnemyComponent.class).die();

    }
}
