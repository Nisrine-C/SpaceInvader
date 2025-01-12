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
import fxgl.spaceinvader.component.EnemyComponent;
import fxgl.spaceinvader.component.InvincibleComponent;
import fxgl.spaceinvader.component.OwnerComponent;
import fxgl.spaceinvader.component.PlayerComponent;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.texture;


public class SpaceInvaderFactory implements EntityFactory {

    @Spawns("Player")
    public Entity newPlayer(SpawnData data){
        return  FXGL.entityBuilder(data)
                .type(SpaceInvaderType.PLAYER)
                .view(new Rectangle(25,25, Color.BLUE))
                .with(new CollidableComponent(true))
                .with(new InvincibleComponent())
                .with(new PlayerComponent())
                .build();
    }

    @Spawns("Background")
    public Entity newBackground(SpawnData data){
        return FXGL.entityBuilder(data)
                .view(new Rectangle(data.<Integer>get("width"), data.<Integer>get("height"), Color.BLACK))
                .with(new IrremovableComponent())
                .zIndex(-100)
                .build();
    }

    @Spawns("Enemy")
    public Entity newEnemy(SpawnData data) {
        return entityBuilder(data)
                .type(SpaceInvaderType.ENEMY)
                .viewWithBBox(new Rectangle(25,25,Color.GREEN)
                )
                .with(new CollidableComponent(true))
                .with(new InvincibleComponent())
                .with(new EnemyComponent())
                .build();
    }

    @Spawns("Bullet")
    public Entity newBullet(SpawnData data){
        Entity owner = data.get("owner");
        System.out.println(owner);
        return FXGL.entityBuilder()
                .type(SpaceInvaderType.BULLET)
                .at(owner.getCenter().add(-3,18))
                .viewWithBBox(new Rectangle(5,10,Color.RED))
                .collidable()
                .with(new OwnerComponent(owner.getType()))
                .with(new ProjectileComponent(new Point2D(0,1),600).allowRotation(false))
                .with(new OffscreenCleanComponent())
                .with("dead",false)
                .build();
    }
}
