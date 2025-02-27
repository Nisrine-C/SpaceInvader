package fxgl.spaceinvader.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import fxgl.spaceinvader.SpaceInvaderType;
import fxgl.spaceinvader.component.OwnerComponent;
import fxgl.spaceinvader.component.WallComponent;

public class BulletWallHandler extends CollisionHandler {
    public BulletWallHandler(){
        super(SpaceInvaderType.BULLET,SpaceInvaderType.WALL);
    }

    @Override
    protected void onCollisionBegin(Entity bullet, Entity wall) {
       Object owner = bullet.getComponent(OwnerComponent.class).getValue();

       bullet.removeFromWorld();
       if(owner == SpaceInvaderType.ENEMY){
           wall.getComponent(WallComponent.class).onHit();
       }
    }
}
