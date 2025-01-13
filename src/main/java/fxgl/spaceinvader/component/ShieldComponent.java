package fxgl.spaceinvader.component;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import fxgl.spaceinvader.SpaceInvaderType;
import javafx.geometry.Point2D;
import javafx.util.Duration;


import static com.almasb.fxgl.dsl.FXGL.*;

public class ShieldComponent extends Component {
   public Entity player;
    private double duration;
    private double created;

    public ShieldComponent(Entity entity,double duration){this.player=entity;this.duration = duration;}

    @Override
    public void onAdded() {
        created = getGameTimer().getNow();
    }

    @Override
    public void onUpdate(double tpf) {
        // Check if the shield has expired
        if (getGameTimer().getNow() - created >= duration) {
            die();
        }

        // Update the shield's position relative to the player
        if (player != null) {
            // Center the shield on the player without an additional offset
            entity.setPosition(player.getX()+24 - entity.getWidth()/2, player.getY() - entity.getHeight()/2);
        }

    }

    public void onHit(){
        animationBuilder()
                .autoReverse(true)
                .repeat(2)
                .interpolator(Interpolators.CIRCULAR.EASE_OUT())
                .duration(Duration.seconds(0.33))
                .scale(entity)
                .to(new Point2D(1.2, 1.2))
                .buildAndPlay();
    }
    public void die(){
        entity.removeFromWorld();
    }
}
