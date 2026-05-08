package com.vihanga.malinda.svmf.window;

import com.vihanga.malinda.svmf.listner.KeyListner;
import com.vihanga.malinda.svmf.listner.MouseListner;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

//import static java.sql.Types.NULL;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.glfw.GLFW.*;

public class Window {

    private int width;
    private int height;
    private String title;
    private long glfwWindow;
    private final MouseListner mouseListner;
    private final KeyListner keyListner;

    public Window(int width, int height, String title, MouseListner mouseListner,KeyListner keyListner) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.mouseListner = mouseListner;
        this.keyListner = keyListner;
    }

    public void run() {
        System.out.println("Running window: " + title + " with dimensions: " + width + "x" + height);
        init();
        loop();

        releaseMemory();
    }

    private void releaseMemory() {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(this.glfwWindow);
        glfwDestroyWindow(this.glfwWindow);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init(){
        // Setup an error call-back
        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        configureGLFW();

        // Create the window
        this.glfwWindow =  glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(this.glfwWindow == NULL){
            throw new RuntimeException("Failed to create the GLFW window");
        }

        //configure callbacks
        configureCallbacks();

        // Make the OpenGL context current
        glfwMakeContextCurrent(this.glfwWindow);

        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(this.glfwWindow);



    }

    private void configureCallbacks() {
        configureMouseListnerCallbacks();
        configureKeyListnerCallbacks();
    }

    private void configureKeyListnerCallbacks() {
        glfwSetKeyCallback(this.glfwWindow,keyListner::keyCallback);
    }

    private void configureMouseListnerCallbacks() {
        glfwSetCursorPosCallback(this.glfwWindow,mouseListner::mousePositionCallBack);
        glfwSetMouseButtonCallback(this.glfwWindow, mouseListner::mouseButtonCallBack);
        glfwSetScrollCallback(this.glfwWindow, mouseListner::mouseScrollCallBack);
    }

    private void configureGLFW() {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE); // the window will be maximized
    }

    public void loop(){
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();


        // Set the clear color
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(this.glfwWindow) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            glfwSwapBuffers(this.glfwWindow); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }

    }
}
