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
import com.almasb.fxgl.texture.Texture;
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

    @Spawns("Wall_left")
    public Entity newWallLeft(SpawnData data) {
        return entityBuilder()
                .from(data)
                .type(SpaceInvaderType.WALL)
                .viewWithBBox(texture("wall_left_1.png"))
                .with(new CollidableComponent(true))
                .with(new WallComponent(8,"left"))
                .build();
    }

    @Spawns("Wall_right")
    public Entity newWallRight(SpawnData data) {
        return entityBuilder()
                .from(data)
                .type(SpaceInvaderType.WALL)
                .viewWithBBox(texture("wall_right_1.png"))
                .with(new CollidableComponent(true))
                .with(new WallComponent(8,"right"))
                .build();
    }


    @Spawns("Player")
    public Entity newPlayer(SpawnData data){
        return  FXGL.entityBuilder(data)
                .type(SpaceInvaderType.PLAYER)
                .viewWithBBox("player.png")
                .with(new InvincibleComponent())
                .with(new PlayerComponent())
                .collidable()
                .build();
    }

    @Spawns("Shield")
    public Entity newShield(SpawnData data){

        Entity owner = data.get("owner");
        Texture shieldTexture = FXGL.texture("shield.png");
        double shieldWidth = shieldTexture.getWidth();
        double shieldHeight = shieldTexture.getHeight();
        Point2D spawnPosition = owner.getCenter().subtract(shieldWidth / 2, shieldHeight / 2);
        return FXGL.entityBuilder()
                .type(SpaceInvaderType.SHIELD)
                .at(spawnPosition)
                .viewWithBBox("shield.png")
                .with(new ShieldComponent(owner,2))
                .with(new OffscreenCleanComponent())

                .collidable()
                .build();
    }

    @Spawns("Egg")
    public Entity newEgg(SpawnData data) {
        return entityBuilder(data)
                .type(SpaceInvaderType.ENEMY)
                .viewWithBBox("Egg.png")
                .with(new CollidableComponent(true),new HealthIntComponent(3),new TimeComponent(1))
                .with(new InvincibleComponent())
                .with(new EnemyComponent(3,EnemyType.EGG),new EffectComponent())
                .build();
    }

    @Spawns("Frog")
    public Entity newFrog(SpawnData data) {
        return entityBuilder(data)
                .type(SpaceInvaderType.ENEMY)
                .viewWithBBox("Frog.png")
                .with(new CollidableComponent(true),new HealthIntComponent(3),new TimeComponent(1))
                .with(new InvincibleComponent())
                .with(new EnemyComponent(3,EnemyType.FROG),new EffectComponent())
                .build();
    }

    @Spawns("Demon")
    public Entity newDemon(SpawnData data) {
        return entityBuilder(data)
                .type(SpaceInvaderType.ENEMY)
                .viewWithBBox("Demon.png")
                .with(new CollidableComponent(true),new HealthIntComponent(3),new TimeComponent(1))
                .with(new InvincibleComponent())
                .with(new EnemyComponent(3,EnemyType.DEMON),new EffectComponent())
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
                .with()
                .collidable()
                .with("dead",false)
                .build();
    }

    @Spawns("EnemyBullet")
    public Entity newEnemyBullet(SpawnData data){
        Entity owner = data.get("owner");
        return FXGL.entityBuilder()
                .type(SpaceInvaderType.BULLET)
                .at(owner.getCenter().add(3,18))
                .viewWithBBox("EnemyBullet.png")
                .with(new OwnerComponent(owner.getType()))
                .with(new BulletComponent(200))
                .with(new OffscreenCleanComponent())
                .with()
                .collidable()
                .with("dead",false)
                .build();
    }

    @Spawns("EnemyCluster")
    public Entity newEnemyCluster(SpawnData data){
        Entity owner = data.get("owner");
        return FXGL.entityBuilder()
                .type(SpaceInvaderType.BULLET)
                .at(owner.getCenter().add(3,18))
                .viewWithBBox("EnemyCluster.png")
                .with(new OwnerComponent(owner.getType()))
                .with(new BulletComponent(100))
                .with(new OffscreenCleanComponent())
                .with()
                .collidable()
                .with("dead",false)
                .build();
    }
}
