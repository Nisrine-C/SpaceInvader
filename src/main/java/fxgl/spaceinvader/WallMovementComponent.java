package fxgl.spaceinvader;

import com.almasb.fxgl.entity.component.Component;

public class WallMovementComponent extends Component {

    private double speed = 100; // Speed in pixels per second
    private boolean movingRight = true;

//    @Override
//    public void onUpdate(double tpf) {
//        if (movingRight) {
//            entity.translateX(speed * tpf);
//            if (entity.getRightX() >= 800) { // Reverse direction when hitting the right edge
//                movingRight = false;
//            }
//        } else {
//            entity.translateX(-speed * tpf);
//            if (entity.getX() <= 0) { // Reverse direction when hitting the left edge
//                movingRight = true;
//            }
//        }
//    }
}
