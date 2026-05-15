package com.vihanga.malinda.svmf.renderer.texture;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

public class Texture {

    public static final int SINGLE_BUFFER_CAPACITY = 1;
    public static final int THREE_COLOUR_CHANNELS = 3;
    public static final int FOUR_COLOUR_CHANNELS = 4;
    private String filePath;
    private int textId;

    public Texture(String filePath) {
        this.filePath = filePath;

        // Generate texture on GPU
        this.textId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D,
                      this.textId);

        // Set texture parameters
        // Repeat Image in both directions
        glTexParameteri(GL_TEXTURE_2D,
                        GL_TEXTURE_WRAP_S,
                        GL_REPEAT); // this will wrap the object on left and right direction(x plains) if texture gets big
        glTexParameteri(GL_TEXTURE_2D,
                        GL_TEXTURE_WRAP_T,
                        GL_REPEAT);// this will wrap the object on up and down direction(y plains) if texture gets big

        // When stretching  the  image, pixelate
        glTexParameteri(GL_TEXTURE_2D,
                        GL_TEXTURE_MIN_FILTER,
                        GL_NEAREST);
        // When shrinking the image,pixelate
        glTexParameteri(GL_TEXTURE_2D,
                        GL_TEXTURE_MAG_FILTER,
                        GL_NEAREST);


        IntBuffer width = BufferUtils.createIntBuffer(SINGLE_BUFFER_CAPACITY);
        IntBuffer height = BufferUtils.createIntBuffer(SINGLE_BUFFER_CAPACITY);
        IntBuffer channels = BufferUtils.createIntBuffer(SINGLE_BUFFER_CAPACITY);
        ByteBuffer image = stbi_load(this.filePath,
                                     width,
                                     height,
                                     channels,
                                     0);

        if (image != null) {
            uploadImageToGPU(channels,
                             width,
                             height,
                             image);

        } else {
            assert false : "ERROR: (Texture) could not load image'" + filePath + "'";
        }

        stbi_image_free(image);


    }

    private void uploadImageToGPU(IntBuffer channels,
                                  IntBuffer width,
                                  IntBuffer height,
                                  ByteBuffer image) {

        int colourChannelsCount = channels.get(0);
        switch (colourChannelsCount) {
            case THREE_COLOUR_CHANNELS ->
                // Uploading the  RGB image to GPU
                    glTexImage2D(GL_TEXTURE_2D,
                                 0,
                                 GL_RGB,
                                 width.get(0),
                                 height.get(0),
                                 0,
                                 GL_RGB,
                                 GL_UNSIGNED_BYTE,
                                 image);
            case FOUR_COLOUR_CHANNELS ->
                // Uploading the RGBA image to GPU
                    glTexImage2D(GL_TEXTURE_2D,
                                 0,
                                 GL_RGBA,
                                 width.get(0),
                                 height.get(0),
                                 0,
                                 GL_RGBA,
                                 GL_UNSIGNED_BYTE,
                                 image);

            default -> {
                assert false : "Error: (texture) Unknown number of channels. '" + colourChannelsCount + "'";
            }

        }
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D,
                      this.textId);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D,
                      0);
    }
}
