package com.vihanga.malinda.svmf.scene;

import com.vihanga.malinda.svmf.listner.KeyListener;
import com.vihanga.malinda.svmf.window.Window;
import org.lwjgl.BufferUtils;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;


public class LevelEditorScene extends Scene{
    private final KeyListener keyListener;

    private boolean isChangingScene;
    private float timeToChangeSceneThreshold ;

    private String vertexShaderSrc ="#version 330 core\n" +
            "    layout (location=0) in vec3 aPos;\n" +
            "    layout (location=1) in vec4 aColor;\n" +
            "\n" +
            "    out vec4 fColor;\n" +
            "\n" +
            "    void main(){\n" +
            "        fColor = aColor;\n" +
            "        gl_Position = vec4(aPos, 1.0);\n" +
            "    }";

    private String fragmentShaderSrc ="#version 330 core\n" +
            "\n" +
            "    in vec4 fColor;\n" +
            "    out vec4 color;\n" +
            "\n" +
            "    void main(){\n" +
            "        color =fColor;\n" +
            "    }";

    private int vertexId;
    private int fragmentId;
    private int shaderProgram;

    private float[] vertexArray={
            //position                    // color
            0.5f,-0.5f,0.0f,              1.0f,0.0f,0.0f,1.0f, // Bottom right  [index of vertex 0]
            -0.5f,0.5f,0.0f,              0.0f,1.0f,0.0f,1.0f, // Top Left      [index of vertex 1]
            0.5f,0.5f,0.0f,               0.0f,0.0f,1.0f,1.0f, // Top right     [index of vertex 2]
            -0.5f,-0.5f,0.0f,             1.0f,1.0f,0.0f,1.0f, // Bottom left   [index of vertex 3]
    };

    /***
     * ==============================================
     * IMPORTANT :: Must be in counter-clockwise order
     *===============================================
     */
    private int[] elementArray ={
            /*
            K,L,P,J are 4 vertexes and square is made up of 2 triangles

                    K        L
                    x--------x
                    |\       |         + 'A' triangle counter-clockwise order should be
                    | \  (A) |          from vertex   L-> K -> P ( Top right -> Top left -> Bottom right)
                    |  \     |
                    |   \    |         + 'B' triangle counter-clockwise order should be
                    |    \   |          from vertex P-> K -> J (Bottom right -> Top Left -> Bottom left)
                    |     \  |
                    | (B)  \ |
                    |       \|
                    x--------x
                    J        P
             */
            2,1,0, // Top right triangle
            0,1,3  // bottom left triangle
    };

    private int vertexArrayObjectId; // vaoId
    private int vertexBufferObjectId; // vboId
    private int elementBufferObjectId; //eboId

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
