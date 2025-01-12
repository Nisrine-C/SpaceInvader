package fxgl.spaceinvader.collision;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import fxgl.spaceinvader.SpaceInvaderType;
import fxgl.spaceinvader.component.InvincibleComponent;
import fxgl.spaceinvader.component.OwnerComponent;
import fxgl.spaceinvader.event.GameEvent;

public class BulletPlayerHandler extends CollisionHandler {
    public BulletPlayerHandler() {
        super(SpaceInvaderType.BULLET, SpaceInvaderType.PLAYER);
    }

    @Override
    protected void onCollisionBegin(Entity bullet, Entity player) {
        Object owner = bullet.getComponent(OwnerComponent.class).getValue();

        if(owner == SpaceInvaderType.PLAYER
            || player.getComponent(InvincibleComponent.class).getValue()){
            return;
        }

        bullet.removeFromWorld();
        FXGL.getEventBus().fireEvent(new GameEvent(GameEvent.PLAYER_GOT_HIT));
    }
}
