package fxgl.spaceinvader.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import fxgl.spaceinvader.SpaceInvaderType;
import fxgl.spaceinvader.component.WallComponent;

public class EnemyEnemyHandler extends CollisionHandler {
    public EnemyEnemyHandler() {
        super(SpaceInvaderType.ENEMY, SpaceInvaderType.ENEMY);
    }

    @Override
    protected void onCollisionBegin(Entity enemy1, Entity enemy2) {

    }
}
