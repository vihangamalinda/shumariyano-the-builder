package com.vihanga.malinda.svmf.configuration;

import com.vihanga.malinda.svmf.listner.KeyListener;
import com.vihanga.malinda.svmf.listner.KeyListenerImpl;
import com.vihanga.malinda.svmf.listner.MouseListener;
import com.vihanga.malinda.svmf.listner.MouseListenerImpl;
import com.vihanga.malinda.svmf.util.TimeUtil;
import com.vihanga.malinda.svmf.util.TimeUtilImpl;
import com.vihanga.malinda.svmf.window.Window;

public class AppConfiguration {
    private final Window window;
    private final int initialWindowWidth;
    private final int initialWindowHeight;
    private final String windowTitle;
    private final MouseListener mouseListener;
    private final KeyListener keyListener;
    private final boolean[] keyPressed;
    private final boolean[] mouseButtonPressed;
    private final TimeUtil timeUtil;

    public AppConfiguration() {
        this.keyPressed = new boolean[350]; // GLFW has 350 key codes
        this.mouseButtonPressed = new boolean[3]; // Left, Right, Middle mouse buttons

        this.mouseListener = new MouseListenerImpl(0.0,
                                                       0.0,
                                                       0.0,
                                                       0.0,
                                                       0.0,
                                                       0.0,
                                                       this.mouseButtonPressed,
                                                       false);
        this.keyListener = new KeyListenerImpl(this.keyPressed);
        this.timeUtil = new TimeUtilImpl();

        this.initialWindowWidth = 1920;
        this.initialWindowHeight = 1080;
        this.windowTitle = "Shumariyo The Builder";
        this.window = new Window(this.initialWindowWidth,
                                 this.initialWindowHeight,
                                 this.windowTitle,
                                 this.mouseListener,
                                 this.keyListener,
                                 this.timeUtil);
    }

    public Window getWindow() {
        return window;
    }
}
