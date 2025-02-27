package fxgl.spaceinvader.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import fxgl.spaceinvader.SpaceInvaderType;
import fxgl.spaceinvader.component.OwnerComponent;
import fxgl.spaceinvader.component.ShieldComponent;
import fxgl.spaceinvader.component.WallComponent;

import java.util.SortedMap;

public class BulletShieldHandler extends CollisionHandler {
    public BulletShieldHandler() {
        super(SpaceInvaderType.SHIELD, SpaceInvaderType.BULLET);
    }

    @Override
    protected void onCollisionBegin(Entity shield, Entity bullet) {
        Object owner = bullet.getComponent(OwnerComponent.class).getValue();

        if(owner == SpaceInvaderType.ENEMY){
            shield.getComponent(ShieldComponent.class).onHit();
            bullet.removeFromWorld();
        }
    }
}
