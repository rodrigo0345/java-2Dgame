package org.example.engine.buffers;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL45.*;
import org.lwjgl.opengl.GL15.*;
import org.lwjgl.opengl.GL33.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class IndexBuffer {
    private int m_Count = 0;
    private int m_RendererID = 0;

    public IndexBuffer(int[] indices) {
        m_Count = indices.length;
        m_RendererID = glGenBuffers();  // Correctly generate buffer ID
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_RendererID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,
                (IntBuffer) BufferUtils.createIntBuffer(indices.length).put(indices).flip(),
                GL_STATIC_DRAW);
    }

    public void Bind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_RendererID);
    }

    public void Unbind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public int GetCount() {
        return m_Count;
    }
}
