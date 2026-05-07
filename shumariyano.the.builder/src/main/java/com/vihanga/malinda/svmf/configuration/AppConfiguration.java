package com.vihanga.malinda.svmf.configuration;

import com.vihanga.malinda.svmf.window.Window;

public class AppConfiguration {
    private final Window window;
    private final int initialWindowWidth;
    private final int initialWindowHeight;
    private final String windowTitle;

    public AppConfiguration() {
        this.initialWindowWidth = 1920;
        this.initialWindowHeight = 1080;
        this.windowTitle = "Shumariyo The Builder";
        this.window = new Window(this.initialWindowWidth,
                                 this.initialWindowHeight,
                                 this.windowTitle);
    }

    public Window getWindow() {
        return window;
    }
}
