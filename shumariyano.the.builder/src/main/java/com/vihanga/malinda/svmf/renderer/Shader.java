package com.vihanga.malinda.svmf.renderer;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static com.vihanga.malinda.svmf.renderer.ShaderConstant.FRAGMENT_SHADER;
import static com.vihanga.malinda.svmf.renderer.ShaderConstant.VERTEX_SHADER;
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
        // Bind shader program
        glUseProgram(this.shaderProgramId);
    }

    public void detach() {
        glUseProgram(0);
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
