package fxgl.spaceinvader.ui;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.app.scene.SceneFactory;

public class MySceneFactory extends SceneFactory {



        @Override
        public FXGLMenu newMainMenu() {
            //return new SimpleGameMenu();
            return new MenuTest();
        }

}
