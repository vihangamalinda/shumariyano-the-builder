package com.vihanga.malinda.svmf.configuration;

import com.vihanga.malinda.svmf.listner.KeyListner;
import com.vihanga.malinda.svmf.listner.MouseListner;
import com.vihanga.malinda.svmf.window.Window;

public class AppConfiguration {
    private final Window window;
    private final int initialWindowWidth;
    private final int initialWindowHeight;
    private final String windowTitle;
    private final MouseListner mouseListner;
    private final KeyListner keyListner;
    private final boolean[] keyPressed;
    private final boolean[] mouseButtonPressed;

    public AppConfiguration() {
        this.keyPressed = new boolean[350]; // GLFW has 350 key codes
        this.mouseButtonPressed = new boolean[3]; // Left, Right, Middle mouse buttons

        this.mouseListner = new MouseListner(0.0,
                                           0.0,
                                           0.0,
                                           0.0,
                                           0.0,
                                           0.0,
                                           this.mouseButtonPressed,
                                           false);
        this.keyListner = new KeyListner(this.keyPressed);

        this.initialWindowWidth = 1920;
        this.initialWindowHeight = 1080;
        this.windowTitle = "Shumariyo The Builder";
        this.window = new Window(this.initialWindowWidth,
                                 this.initialWindowHeight,
                                 this.windowTitle,this.mouseListner,this.keyListner);
    }

    public Window getWindow() {
        return window;
    }
}
