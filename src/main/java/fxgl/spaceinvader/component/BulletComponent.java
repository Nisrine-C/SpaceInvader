package fxgl.spaceinvader.component;

import com.almasb.fxgl.entity.component.Component;
import fxgl.spaceinvader.SpaceInvaderType;

public class BulletComponent extends Component {
    private OwnerComponent owner;
    private double speed;

    public BulletComponent(double speed){this.speed = speed;}

    @Override
    public void onUpdate(double tpf) {
        System.out.println("hellooooo???");
        if(SpaceInvaderType.PLAYER == owner.getValue()) System.out.println(-tpf*speed);
        else System.out.println(tpf*speed);
        entity.translateY((owner.getValue() == (SpaceInvaderType.PLAYER)) ? -tpf * speed : tpf * speed);
    }
}
