package com.vihanga.malinda.svmf.renderer;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static com.vihanga.malinda.svmf.renderer.ShaderConstant.*;
import static com.vihanga.malinda.svmf.renderer.ShaderHelper.getShaderSourceByTypeMap;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private String vertexSource;
    private String fragmentSource;
    private String filePath;
    private int vertexId;
    private int fragmentId;
    private int shaderProgramId;
    private boolean isBeingUsed;


    public Shader(String filepath) {
        this.filePath = filepath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            Map<String, String> shaderSourceByTypeMap = getShaderSourceByTypeMap(source);
            assignShaderSource(shaderSourceByTypeMap);


        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Error: Could not open file for shader.'" + filepath + "'";
        }
    }

    private void assignShaderSource(Map<String, String> shaderSourceByTypeMap) {
        shaderSourceByTypeMap.forEach((key, value) -> this.assignShaderSourceByShaderType(key,
                                                                                          value));
    }

    private void assignShaderSourceByShaderType(String shaderType,
                                                String shaderSource
    ) {
        switch (shaderType) {
            case VERTEX_SHADER -> this.vertexSource = shaderSource;
            case FRAGMENT_SHADER -> this.fragmentSource = shaderSource;
            default ->
                    throw new RuntimeException("Unexpected token. '" + shaderType + "'");
        }
    }

    public void compile() {
        /**
         * Compile and link Shaders
         */
        // 1) create & compile shaders
        this.createAndCompileShaders();
        // 2) link shaders and check for errors
        this.linkShaders();
    }

    public void use() {
        if (!this.isBeingUsed()) {
            // Bind shader program
            glUseProgram(this.shaderProgramId);
            setIsBeingUsed(true);
        }
    }

    private boolean isBeingUsed() {
        return this.isBeingUsed;
    }

    private void setIsBeingUsed(boolean isBeingUsed) {
        this.isBeingUsed = isBeingUsed;
    }

    public void detach() {
        glUseProgram(0);
        setIsBeingUsed(false);
    }

    public void uploadMat4f(String varName,
                            Matrix4f mat4) {
        int varLocation = this.getGetUniformLocation(varName); // get location of the variable for shader program

        this.use(); // using the shader
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(BUFFER_CAPACITY_FOR_FOUR_DIMENSION_MATRIX);
        mat4.get(matBuffer); // flatten it out to 1D array with 16 elements
        glUniformMatrix4fv(varLocation,
                           false,
                           matBuffer);
    }


    public void uploadMat3f(String varName,
                            Matrix3f mat3) {
        int varLocation = this.getGetUniformLocation(varName);

        this.use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(BUFFER_CAPACITY_FOR_THREE_DIMENSION_MATRIX);
        mat3.get(matBuffer); // flatten it out to 1D array with 9 elements
        glUniformMatrix3fv(varLocation,
                           false,
                           matBuffer);
    }


    public void uploadVec4(String varName,
                           Vector4f vec4f) {
        int varLocation = this.getGetUniformLocation(varName);
        this.use();

        glUniform4f(varLocation,
                    vec4f.x,
                    vec4f.y,
                    vec4f.z,
                    vec4f.w);
    }


    public void uploadVec3f(String varName,
                            Vector4f vector4f) {
        int varLocation = this.getGetUniformLocation(varName);
        this.use();
        glUniform3f(varLocation,
                    vector4f.x,
                    vector4f.y,
                    vector4f.z);
    }


    public void uploadFloat(String varName,
                            float value) {
        int varLocation = this.getGetUniformLocation(varName);
        this.use();
        glUniform1f(varLocation,
                    value);
    }

    public void uploadInt(String varName,
                          int value) {
        int varLocation = this.getGetUniformLocation(varName);
        this.use();
        glUniform1i(varLocation,
                    value);
    }
    public void uploadTexture(String varName,int slot){
        int varLocation = this.getGetUniformLocation(varName);
        this.use();
        glUniform1i(varLocation,slot);
    }


    private int getGetUniformLocation(String varName) {
        // get location of the variable for shader program
        return glGetUniformLocation(this.shaderProgramId,
                                    varName);
    }


    private void createAndCompileShaders() {
        this.createAndCompileVertexShader();
        this.createAndCompileFragmentShader();
    }

    private void linkShaders() {
        this.shaderProgramId = glCreateProgram();
        glAttachShader(this.shaderProgramId,
                       this.vertexId);
        glAttachShader(this.shaderProgramId,
                       this.fragmentId);
        glLinkProgram(shaderProgramId);

        // Check for linking errors
        int success = glGetProgrami(this.shaderProgramId,
                                    GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int length = glGetProgrami(this.shaderProgramId,
                                       GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filePath + "'\n\t Program shader linking failed.");
            System.out.println(glGetProgramInfoLog(this.shaderProgramId,
                                                   length));
            assert false : "";
        }
    }

    private void createAndCompileVertexShader() {
        // First load and compile the vertex shader
        this.vertexId = glCreateShader(GL_VERTEX_SHADER);

        // Pass the shader source to the GPU
        glShaderSource(this.vertexId,
                       this.vertexSource);
        glCompileShader(this.vertexId);

        // check for errors in compilation
        int success = glGetShaderi(this.vertexId,
                                   GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int length = glGetShaderi(this.vertexId,
                                      GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + this.filePath + "'\n\t Vertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(this.vertexId,
                                                  length));
            assert false : "";
        }
    }

    private void createAndCompileFragmentShader() {
        // First load and compile the fragmentShader
        this.fragmentId = glCreateShader(GL_FRAGMENT_SHADER);

        // pass the shader source to the GPU
        glShaderSource(this.fragmentId,
                       this.fragmentSource);
        glCompileShader(this.fragmentId);

        // check for errors in compilation
        int success = glGetShaderi(this.fragmentId,
                                   GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int length = glGetShaderi(this.fragmentId,
                                      GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + this.filePath + "'\n\t Fragment shader compilation failed.");
            System.out.println(glGetShaderInfoLog(this.fragmentId,
                                                  length));
            assert false : "";
        }


    }
}
