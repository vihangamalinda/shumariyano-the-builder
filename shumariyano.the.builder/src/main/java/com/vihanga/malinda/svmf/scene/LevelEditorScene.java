package com.vihanga.malinda.svmf.scene;

import com.vihanga.malinda.svmf.listner.KeyListener;
import com.vihanga.malinda.svmf.renderer.Shader;
import com.vihanga.malinda.svmf.renderer.texture.Texture;
import com.vihanga.malinda.svmf.util.TimeUtil;
import com.vihanga.malinda.svmf.window.Window;
import org.lwjgl.BufferUtils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;


public class LevelEditorScene extends Scene {
    private final KeyListener keyListener;

    private boolean isChangingScene;
    private float timeToChangeSceneThreshold;

    private float[] vertexArray = {
            //position                    // color                     UV (texture) coordinates
            100.5f,0.5f,0.0f,              1.0f,0.0f,0.0f,1.0f,                 1,1,           // Bottom right  [index of vertex 0]
            0.5f,100.5f,0.0f,              0.0f,1.0f,0.0f,1.0f,                 0,0,           // Top Left  [index of vertex 1]
            100.5f,100.5f,0.0f,            0.0f,0.0f,1.0f,1.0f,                 1,0,           // Top right     [index of vertex 2]
            0.5f,0.5f,0.0f,                1.0f,1.0f,0.0f,1.0f,                 0,1,           //  Bottom left   [index of vertex 3]
    };

    /***
     * ==============================================
     * IMPORTANT :: Must be in counter-clockwise order
     *===============================================
     */
    private int[] elementArray = {
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
            2, 1, 0, // Top right triangle
            0, 1, 3  // bottom left triangle
    };

    private int vertexArrayObjectId; // vaoId
    private int vertexBufferObjectId; // vboId
    private int elementBufferObjectId; //eboId

    private Shader defaultShader;
    private TimeUtil time;
    private Texture testTexture;

    public LevelEditorScene(KeyListener keyListener,
                            boolean isChangingScene,
                            TimeUtil time) {
        System.out.println("Level Editor Scene created");
        this.keyListener = keyListener;
        this.isChangingScene = isChangingScene;
        this.timeToChangeSceneThreshold = 2.0f;
        this.time = time;
        this.testTexture = new Texture("assets/images/test_all.png");
    }

    @Override
    public void init() {
        this.defaultShader = new Shader("assets/shaders/default.glsl");
        this.defaultShader.compile();

        /*
        ===============================================================
         Generate VAO, VBO, and EBO buffer objects, and send to GPU
        ===============================================================
            + vao -> vertex array object
            + vbo -> vertex buffer object
            + ebo -> element buffer object
         */
        this.vertexArrayObjectId = glGenVertexArrays();
        glBindVertexArray(this.vertexArrayObjectId);

        // create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(this.vertexArray.length);
        vertexBuffer.put(this.vertexArray).flip();

        // create VBO upload the vertex buffer
        this.vertexBufferObjectId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,
                     this.vertexBufferObjectId);
        glBufferData(GL_ARRAY_BUFFER,
                     vertexBuffer,
                     GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        this.elementBufferObjectId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,
                     this.elementBufferObjectId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,
                     elementBuffer,
                     GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionSize = 3;
        int colorSize = 4;
        int uvSize =2;
//        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionSize + colorSize+uvSize) * Float.BYTES;
        glVertexAttribPointer(0,
                              positionSize,
                              GL_FLOAT,
                              false,
                              vertexSizeBytes,
                              0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1,
                              colorSize,
                              GL_FLOAT,
                              false,
                              vertexSizeBytes,
                              positionSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        int offset = (positionSize + colorSize) * Float.BYTES;
        // enabling attribute pointer for uv coordinates (texture coordinates)

        glVertexAttribPointer(2,
                              uvSize,
                              GL_FLOAT,
                              false,
                              vertexSizeBytes,
                              offset);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float delta) {
        if (!isChangingScene && keyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            isChangingScene = true;
        }

        if (isChangingScene && timeToChangeSceneThreshold > 0) {
            timeToChangeSceneThreshold -= delta;
            // RGB colour transition from black to white
        } else if (isChangingScene) {
            // Change to level scene

        }
    }

    @Override
    public void update(Window window,
                       float delta) {
        camera.getPosition().x -= delta * 50.0f;
        camera.getPosition().y -= delta * 50.0f;

        this.defaultShader.use();

        defaultShader.uploadMat4f("uProjection",
                                  this.camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView",
                                  this.camera.getViewMatrix());
        defaultShader.uploadFloat("uTime",
                                  this.time.getElapsedTimeBySeconds());

        // Bind the VertexArrayObject that we are using
        glBindVertexArray(this.vertexArrayObjectId);

        // Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES,
                       this.elementArray.length,
                       GL_UNSIGNED_INT,
                       0);

        // unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        glUseProgram(0);
        this.defaultShader.detach();
    }
}
