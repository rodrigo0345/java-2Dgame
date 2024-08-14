package org.example.engine;

import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;
import java.nio.FloatBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Shader {
    private final int rendererID;

    // Constructor to create and compile the shader
    public Shader(String vertexSrc, String fragSrc) {
        // Compile vertex shader
        int vertexShader = compileShader(GL20.GL_VERTEX_SHADER, vertexSrc);

        // Compile fragment shader
        int fragmentShader = compileShader(GL20.GL_FRAGMENT_SHADER, fragSrc);

        // Link shaders into a program
        rendererID = linkProgram(vertexShader, fragmentShader);

        // Detach and delete shaders after linking
        GL20.glDetachShader(rendererID, vertexShader);
        GL20.glDetachShader(rendererID, fragmentShader);
        GL20.glDeleteShader(vertexShader);
        GL20.glDeleteShader(fragmentShader);
    }

    // Method to compile a shader
    private int compileShader(int type, String source) {
        int shader = GL20.glCreateShader(type);
        GL20.glShaderSource(shader, source);
        GL20.glCompileShader(shader);

        // Check for compilation errors
        int success = GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS);
        if (success == GL20.GL_FALSE) {
            String infoLog = GL20.glGetShaderInfoLog(shader);
            Logger.getLogger(Shader.class.getName()).log(Level.SEVERE, "Error compiling shader: {0}", infoLog);
            GL20.glDeleteShader(shader);
            throw new RuntimeException("Failed to compile shader");
        }

        return shader;
    }

    // Method to link shaders into a program
    private int linkProgram(int vertexShader, int fragmentShader) {
        int program = GL20.glCreateProgram();
        GL20.glAttachShader(program, vertexShader);
        GL20.glAttachShader(program, fragmentShader);
        GL20.glLinkProgram(program);

        // Check for linking errors
        int success = GL20.glGetProgrami(program, GL20.GL_LINK_STATUS);
        if (success == GL20.GL_FALSE) {
            String infoLog = GL20.glGetProgramInfoLog(program);
            Logger.getLogger(Shader.class.getName()).log(Level.SEVERE, "Error linking shader program: {0}", infoLog);
            GL20.glDeleteProgram(program);
            throw new RuntimeException("Failed to link shader program");
        }

        return program;
    }

    // Method to bind (use) the shader program
    public void bind() {
        GL20.glUseProgram(rendererID);
    }

    // Method to unbind (stop using) the shader program
    public void unbind() {
        GL20.glUseProgram(0);
    }

    // Method to upload a 4x4 matrix uniform
    public void uploadUniformMat4(String uniformName, float[] matrix) {
        int location = GL20.glGetUniformLocation(rendererID, uniformName);
        if (location < 0) {
            Logger.getLogger(Shader.class.getName()).log(Level.SEVERE, "Uniform not found: {0}", uniformName);
            return;
        }
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(16).put(matrix);
            buffer.flip();
            GL20.glUniformMatrix4fv(location, false, buffer);
        }
    }

    // Method to upload a vec4 uniform
    public void uploadUniformFloat4(String uniformName, float x, float y, float z, float w) {
        int location = GL20.glGetUniformLocation(rendererID, uniformName);
        if (location < 0) {
            Logger.getLogger(Shader.class.getName()).log(Level.SEVERE, "Uniform not found: {0}", uniformName);
            return;
        }
        GL20.glUniform4f(location, x, y, z, w);
    }

    public void uploadUniformFloat3(String uniformName, float x, float y, float z) {
        int location = GL20.glGetUniformLocation(rendererID, uniformName);
        if (location < 0) {
            Logger.getLogger(Shader.class.getName()).log(Level.SEVERE, "Uniform not found: {0}", uniformName);
            return;
        }
        GL20.glUniform3f(location, x, y, z);
    }

    // Method to upload a float uniform
    public void uploadUniformFloat(String uniformName, float value) {
        int location = GL20.glGetUniformLocation(rendererID, uniformName);
        if (location < 0) {
            Logger.getLogger(Shader.class.getName()).log(Level.SEVERE, "Uniform not found: {0}", uniformName);
            return;
        }
        GL20.glUniform1f(location, value);
    }

    // Method to upload an integer uniform
    public void uploadUniformInt(String uniformName, int value) {
        int location = GL20.glGetUniformLocation(rendererID, uniformName);
        if (location < 0) {
            Logger.getLogger(Shader.class.getName()).log(Level.SEVERE, "Uniform not found: {0}", uniformName);
            return;
        }
        GL20.glUniform1i(location, value);
    }

    // Clean up the shader program
    public void delete() {
        GL20.glDeleteProgram(rendererID);
    }
}
