package fxgl.spaceinvader;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.EffectComponent;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.TimeComponent;
import fxgl.spaceinvader.component.EnemyComponent;
import fxgl.spaceinvader.component.InvincibleComponent;
import fxgl.spaceinvader.component.PlayerComponent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.entityBuilder;
import static com.almasb.fxgl.dsl.FXGL.texture;


public class SpaceInvaderFactory implements EntityFactory {

    @Spawns("Player")
    public Entity newPlayer(SpawnData data){
        return  FXGL.entityBuilder(data)
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
        return entityBuilder()
                .from(data)
                .type(SpaceInvaderType.ENEMY)
                .viewWithBBox(
                        texture("enemy" + ((int)(Math.random() * 3) + 1) + ".png")
                                .outline(Color.BLACK)
                                .toAnimatedTexture(2, Duration.seconds(2))
                                .loop()
                )
                .with(new CollidableComponent(true))
                .with(new InvincibleComponent())
                .with(new EnemyComponent())
                .build();
    }
}
