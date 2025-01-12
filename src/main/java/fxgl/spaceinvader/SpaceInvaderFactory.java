package fxgl.spaceinvader;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.EffectComponent;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.dsl.components.OffscreenCleanComponent;
import com.almasb.fxgl.dsl.components.ProjectileComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.TimeComponent;
import fxgl.spaceinvader.component.*;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.texture;


public class SpaceInvaderFactory implements EntityFactory {

    @Spawns("Background")
    public Entity newBackground(SpawnData data){
        return FXGL.entityBuilder(data)
                .view(new Rectangle(data.<Integer>get("width"), data.<Integer>get("height"), Color.BLACK))
                .with(new IrremovableComponent())
                .zIndex(-100)
                .build();
    }

    @Spawns("Player")
    public Entity newPlayer(SpawnData data){
        return  FXGL.entityBuilder(data)
                .type(SpaceInvaderType.PLAYER)
                .viewWithBBox(new Rectangle(25,25, Color.BLUE))
                .with(new InvincibleComponent())
                .with(new PlayerComponent())
                .collidable()
                .buildAndAttach();
    }

    @Spawns("Enemy")
    public Entity newEnemy(SpawnData data) {
        return entityBuilder(data)
                .type(SpaceInvaderType.ENEMY)
                .viewWithBBox(new Rectangle(25,25,Color.GREEN))
                .collidable()
                .with(new InvincibleComponent())
                .with(new EnemyComponent())
                .build();
    }

    @Spawns("Bullet")
    public Entity newBullet(SpawnData data){
        Entity owner = data.get("owner");
        return FXGL.entityBuilder()
                .type(SpaceInvaderType.BULLET)
                .at(owner.getCenter().add(3,18))
                .viewWithBBox(new Rectangle(5,13,Color.RED))
                .with(new OwnerComponent(owner.getType()))
                .with(new BulletComponent(300))
                .with(new OffscreenCleanComponent())
                .collidable()
                .with("dead",false)
                .build();
    }
}
