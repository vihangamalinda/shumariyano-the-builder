package com.vihanga.malinda.svmf.scene;

import com.vihanga.malinda.svmf.window.Window;

public abstract class Scene {
    public Scene(){
        System.out.println("Scene created");
    }

    public abstract void update(float delta);
    public abstract void update(Window window,float delta);
}
