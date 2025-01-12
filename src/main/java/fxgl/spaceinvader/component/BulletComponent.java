package fxgl.spaceinvader.component;

import com.almasb.fxgl.entity.component.Component;
import fxgl.spaceinvader.SpaceInvaderFactory;
import fxgl.spaceinvader.SpaceInvaderType;

public class BulletComponent extends Component {
    public OwnerComponent owner;
    private double speed;

    public BulletComponent(double speed){this.speed = speed;}

    @Override
    public void onUpdate(double tpf) {
        entity.translateY(owner.getValue() == (SpaceInvaderType.PLAYER) ? -tpf * speed : tpf * speed);
    }
}
