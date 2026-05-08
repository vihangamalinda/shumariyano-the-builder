package com.vihanga.malinda.svmf.listner;

public interface KeyListener {
    void keyCallback(long window,
                     int key,
                     int scanCode,
                     int action,
                     int mods);

    boolean isKeyPressed(int keyCode);
}
