package org.example.engine;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL45.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private int id;
    private int width;
    private int height;
    private int slot = 0;

    public Texture(String filepath) {
        id = loadTexture(filepath);
    }

    private int loadTexture(String filepath) {
        // Create a texture ID

        int internalFormat = GL_RGB;
        int dataFormat = GL_RGB;

        // Load image using STB
        ByteBuffer imageBuffer;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            // Load the image
            STBImage.stbi_set_flip_vertically_on_load(true);
            imageBuffer = STBImage.stbi_load(filepath, width, height, channels, 0);
            if (imageBuffer == null) {
                throw new RuntimeException("Failed to load texture image: " + STBImage.stbi_failure_reason());
            }

            this.width = width.get();
            this.height = height.get();

            if (channels.get() == 4) {
                internalFormat = GL_RGBA8;
                dataFormat = GL_RGBA;
            } else if (channels.get() == 3) {
                internalFormat = GL_RGBA8;
                dataFormat = GL_RGB;
            }
        }

        int textureID = glCreateTextures(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, textureID);

        // Set texture parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTextureStorage2D(textureID, 1, internalFormat, width, height);
        glTextureSubImage2D(textureID, 0, 0, 0, width, height, dataFormat, GL_UNSIGNED_BYTE, imageBuffer);

        // Free image memory
        stbi_image_free(imageBuffer);

        return textureID;
    }

    public void bind(int slot) {
        glActiveTexture(GL_TEXTURE0 + slot);
        glBindTexture(GL_TEXTURE_2D, id);
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void cleanup() {
        glDeleteTextures(id);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}