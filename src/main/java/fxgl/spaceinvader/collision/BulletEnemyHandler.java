package fxgl.spaceinvader.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import fxgl.spaceinvader.SpaceInvaderType;
import fxgl.spaceinvader.component.EnemyComponent;
import fxgl.spaceinvader.component.OwnerComponent;

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


        bullet.removeFromWorld();
        enemy.getComponent(EnemyComponent.class).die();

    }
}
