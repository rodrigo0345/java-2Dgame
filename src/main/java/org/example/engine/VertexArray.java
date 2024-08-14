package org.example.engine;

import org.example.engine.buffers.BufferElement;
import org.example.engine.buffers.BufferLayout;
import org.example.engine.buffers.IndexBuffer;
import org.example.engine.buffers.VertexBuffer;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL41.glVertexAttribLPointer;
import static org.lwjgl.opengl.GL45.glCreateVertexArrays;

public class VertexArray {

    public static int ShaderDataTypeToOpenGLDataType(BufferElement.ShaderDataType type){
        return switch (type) {
            case None -> 0;
            case Float,
            Float2,
            Float3,
            Float4,
            Mat3,
            Mat4 -> GL_FLOAT;
            case Int,
            Int2,
            Int3,
            Int4 -> GL_INT;
            case Bool -> GL_BOOL;
        };
    }

    private List<VertexBuffer> m_VertexBuffers;
    private IndexBuffer m_IndexBuffer;
    private int m_RendererID;

    public VertexArray(){
        m_RendererID = glCreateVertexArrays();
    }

    public void Destroy() {
        glDeleteVertexArrays(m_RendererID);
    }

    public void AddVertexBuffer(VertexBuffer vb) {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_RendererID);  // Bind the Vertex Array Object
        vb.Bind();  // Bind the Vertex Buffer Object

        assert vb.GetLayout().GetCount() != 0 : "Vertex Buffer has no layout";

        int index = 0;
        BufferLayout layout = vb.GetLayout();
        for (BufferElement el : layout.GetElements()) {
            glEnableVertexAttribArray(index);  // Enable the vertex attribute array at the index

            glVertexAttribPointer(
                    index,  // Attribute index
                    el.GetComponentCount(),  // Number of components (e.g., 3 for vec3)
                    ShaderDataTypeToOpenGLDataType(el.Type),  // Data type (e.g., GL_FLOAT)
                    el.Normalized,  // Whether the data should be normalized
                    layout.GetStride(),  // The total size of one vertex (i.e., the stride)
                    (long)el.Offset  // The offset of this attribute within the vertex
            );

            index++;
        }

        // If this is the first VertexBuffer being added, initialize the list
        if (m_VertexBuffers == null) m_VertexBuffers = new ArrayList<>();
        m_VertexBuffers.add(vb);
    }

    public void SetIndexBuffer(IndexBuffer ib){
        glBindVertexArray(m_RendererID);
        ib.Bind();
        m_IndexBuffer = ib;
    }

    public void Bind(){
        glBindVertexArray(m_RendererID);
    }

    public List<VertexBuffer> GetVertexBuffers() {
        return m_VertexBuffers;
    }

    public IndexBuffer GetIndexBuffer() {
        return m_IndexBuffer;
    }
}
