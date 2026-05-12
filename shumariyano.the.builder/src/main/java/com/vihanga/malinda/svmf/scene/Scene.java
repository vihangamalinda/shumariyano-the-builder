package com.vihanga.malinda.svmf.scene;

import com.vihanga.malinda.svmf.camera.Camera;
import com.vihanga.malinda.svmf.window.Window;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public abstract class Scene {
    protected Camera camera;
    public Scene(){

        System.out.println("Scene created");

        this.camera = new Camera(new Vector2f(), new Matrix4f(), new Matrix4f());
    }

    public abstract void update(float delta);
    public abstract void update(Window window,float delta);
    public void init(){

    }
}
