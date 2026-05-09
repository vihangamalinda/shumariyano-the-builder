package com.vihanga.malinda.svmf.scene;

import com.vihanga.malinda.svmf.listner.KeyListener;
import com.vihanga.malinda.svmf.window.Window;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene{
    private final KeyListener keyListener;

    private boolean isChangingScene;
    private float timeToChangeSceneThreshold ;


    public LevelEditorScene(KeyListener keyListener,boolean isChangingScene){
        System.out.println("Level Editor Scene created");
        this.keyListener = keyListener;
        this.isChangingScene = isChangingScene;
        this.timeToChangeSceneThreshold =2.0f;
    }

    @Override
    public void update(float delta) {
        if(!isChangingScene && keyListener.isKeyPressed(KeyEvent.VK_SPACE)){
            isChangingScene = true;
        }

        if (isChangingScene && timeToChangeSceneThreshold>0){
            timeToChangeSceneThreshold -= delta;
            // RGB colour transition from black to white
        } else if(isChangingScene){
            // Change to level scene

        }
    }

    @Override
    public void update(Window window,
                       float delta) {
        System.out.println("Rate of frames per second: " + (1.0f / delta));
        if(!isChangingScene && keyListener.isKeyPressed(KeyEvent.VK_SPACE)){
            isChangingScene = true;
        }

        if (isChangingScene && timeToChangeSceneThreshold>0){
            timeToChangeSceneThreshold -= delta;
            // RGB colour transition from black to white
        } else if(isChangingScene){
            // Change to level scene
            window.changeScene(1);

        }
    }


}
