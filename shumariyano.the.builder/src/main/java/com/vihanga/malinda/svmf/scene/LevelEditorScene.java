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
    public void init(){
        /**
         * Compile and link Shaders
         */
        // 1) create & compile shaders
        this.createAndCompileShaders();
        // 2) link shaders and check for errors
        this.linkShaders();

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
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionSize + colorSize) * floatSizeBytes;
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
                              positionSize * floatSizeBytes);
        glEnableVertexAttribArray(1);
    }

    private void linkShaders() {
        this.shaderProgram = glCreateProgram();
        glAttachShader(this.shaderProgram,this.vertexId);
        glAttachShader(this.shaderProgram,this.fragmentId);
        glLinkProgram(shaderProgram);

        // Check for linking errors
        int success = glGetProgrami(this.shaderProgram,GL_LINK_STATUS);
        if(success ==GL_FALSE){
            int length = glGetProgrami(this.shaderProgram,GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\t Program shader linking failed.");
            System.out.println(glGetProgramInfoLog(this.shaderProgram,length));
            assert  false:"";
        }
    }

    private void createAndCompileShaders(){
        this.createAndCompileVertexShader();
        this.createAndCompileFragmentShader();
    }

    private void createAndCompileVertexShader() {
        // First load and compile the vertex shader
        this.vertexId= glCreateShader(GL_VERTEX_SHADER);

        // Pass the shader source to the GPU
        glShaderSource(this.vertexId,this.vertexShaderSrc);
        glCompileShader(this.vertexId);

        // check for errors in compilation
        int success = glGetShaderi(this.vertexId,GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int length =glGetShaderi(this.vertexId,GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\t Vertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(this.vertexId,length));
            assert  false:"";
        }
    }

    private void createAndCompileFragmentShader(){
        // First load and compile the fragmentShader
        this.fragmentId = glCreateShader(GL_FRAGMENT_SHADER);

        // pass the shader source to the GPU
        glShaderSource(this.fragmentId,this.fragmentShaderSrc);
        glCompileShader(this.fragmentId);

        // check for errors in compilation
        int success = glGetShaderi(this.fragmentId,GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int length = glGetShaderi(this.fragmentId,GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\t Fragment shader compilation failed.");
            System.out.println(glGetShaderInfoLog(this.fragmentId,length));
            assert  false:"";
        }


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
       // Bind shader program
        glUseProgram(this.shaderProgram);
       // Bind the VertexArrayObject that we are using
        glBindVertexArray(this.vertexArrayObjectId);

        // Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES,this.elementArray.length,GL_UNSIGNED_INT,0);

        // unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        glUseProgram(0);
    }


//    @Override
//    public void update(Window window,
//                       float delta) {
//        System.out.println("Rate of frames per second: " + (1.0f / delta));
//        if(!isChangingScene && keyListener.isKeyPressed(KeyEvent.VK_SPACE)){
//            isChangingScene = true;
//        }
//
//        if (isChangingScene && timeToChangeSceneThreshold>0){
//            timeToChangeSceneThreshold -= delta;
//            // RGB colour transition from black to white
//        } else if(isChangingScene){
//            // Change to level scene
//            window.changeScene(1);
//
//        }
//    }


}
