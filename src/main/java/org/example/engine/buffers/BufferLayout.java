package org.example.engine.buffers;

import java.util.List;

public class BufferLayout {
    private List<BufferElement> m_Elements = null;
    private int m_Stride = 0;

    public BufferLayout(List<BufferElement> elements) {
        m_Elements = elements;
        CalculateOffsetsAndStrings();
    }

    public List<BufferElement> GetElements() {
        return m_Elements;
    }

    void CalculateOffsetsAndStrings() {
        int offset = 0;
        m_Stride = 0;

        for (BufferElement el : m_Elements) {
            el.Offset = offset;
            offset += el.Size;
            m_Stride += el.Size;
        }
    }

    public int GetStride() {
        return m_Stride;
    }

    public int GetCount() {
        return m_Elements.size();
    }
}
