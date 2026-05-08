package com.vihanga.malinda.svmf.listner;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListenerImpl implements KeyListener{

    private boolean[] keyPressed;

    public KeyListenerImpl(boolean[] keyPressed) {
        // new boolean[350];// GLFW has 350 key codes
        this.keyPressed = keyPressed;
    }

    @Override
    public void keyCallback(long window,
                            int key,
                            int scanCode,
                            int action,
                            int mods) {
        if (action == GLFW_PRESS) {
            this.keyPressed[key] = true;
        } else if (action == GLFW_RELEASE) {
            this.keyPressed[key] = false;
        }
    }

    @Override
    public boolean isKeyPressed(int keyCode) {
        if (keyCode < 0 || keyCode >= this.keyPressed.length) {
            throw new IllegalArgumentException("Invalid key code: " + keyCode + ". Valid range is 0 to " + (this.keyPressed.length - 1));
        }

        return this.keyPressed[keyCode];
    }
}
