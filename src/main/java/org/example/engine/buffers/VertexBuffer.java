package org.example.engine.buffers;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengles.GLES20.GL_ELEMENT_ARRAY_BUFFER;

public class VertexBuffer {
    private int m_Count = 0;
    private int m_RendererID = 0;
    private BufferLayout m_Layout = null;

    public VertexBuffer(float[] vertexData) {
        m_Count = vertexData.length;

        m_RendererID = glGenBuffers();  // Correctly generate buffer ID
        glBindBuffer(GL_ARRAY_BUFFER, m_RendererID);
        glBufferData(GL_ARRAY_BUFFER, (FloatBuffer) BufferUtils.createFloatBuffer(vertexData.length).put(vertexData).flip(), GL_STATIC_DRAW);
        glEnableClientState(GL_VERTEX_ARRAY);
    }

    public void Destroy(){
        glDeleteBuffers(m_RendererID);
        m_RendererID = 0;
    }

    public void SetLayout(BufferLayout layout){
        m_Layout = layout;
    }

    public BufferLayout GetLayout(){ return m_Layout; }

    public void Bind() {
        assert m_RendererID != 0: "Invalid renderer id, probably destroyed";
        glBindBuffer(GL_ARRAY_BUFFER, m_RendererID);
    }

    public void Unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public int GetCount() {
        return m_Count;
    }
}
