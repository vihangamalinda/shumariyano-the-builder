package com.vihanga.malinda.svmf.window;

public class WindowInformation {
    private int width;
    private int height;
    private String title;

    public WindowInformation(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getTitle() {
        return title;
    }
}
