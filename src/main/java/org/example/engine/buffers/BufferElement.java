package org.example.engine.buffers;

public class BufferElement {

    public String Name;
    public ShaderDataType Type;
    public int Offset;
    public int Size;
    public boolean Normalized;

    public BufferElement(String name, ShaderDataType type, boolean normalized) {
        Name = name;
        Type = type;
        Offset = 0;
        Size = ShaderDataTypeSize(type);
        Normalized = normalized;
    }

    public static int ShaderDataTypeSize(ShaderDataType type) {
        return switch (type) {
            case None -> 0;
            case Float, Int -> 4;
            case Float2, Int2 -> 4 * 2;
            case Float3, Int3 -> 4 * 3;
            case Float4, Int4 -> 4 * 4;
            case Mat3 -> 4 * 3 * 3;
            case Mat4 -> 4 * 4 * 4;
            case Bool -> 4;
        };
    }

    public int GetComponentCount() {
        return switch (Type) {
            case None -> 0;
            case Float, Int -> 1;
            case Float2, Int2 -> 2;
            case Float3, Int3 -> 3;
            case Float4, Int4 -> 4;
            case Mat3 -> 3 * 3;
            case Mat4 -> 4 * 4;
            case Bool -> 1;
        };
    }

    public enum ShaderDataType {
        None,
        Float,
        Float2,
        Float3,
        Float4,
        Mat3,
        Mat4,
        Int,
        Int2,
        Int3,
        Int4,
        Bool
    }
}
