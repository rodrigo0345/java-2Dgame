package org.example.engine.shape;

import org.example.engine.buffers.*;
import org.example.engine.collisions.CollidingBox;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Arrays;

public class Square implements Shape {

    private static final int[] indices = {
            0, 1, 2, 2, 3, 0
    };
    private final CollidingBox box = new CollidingBox(-0.5f, -0.5f, 0.5f, 0.5f);
    private final BufferLayout layout = new BufferLayout(
            new ArrayList<BufferElement>(
                    Arrays.asList(
                            new BufferElement("a_Position", BufferElement.ShaderDataType.Float3, false),
                            new BufferElement("a_TexCoord", BufferElement.ShaderDataType.Float2, false)
                    )
            )
    );
    private float[] vertexData = new float[]{
            // position coords  // texture coords
            -0.5f, -0.5f, 0.0f, 0.0f, 0.0f,
            0.5f, -0.5f, 0.0f, 1.0f, 0.0f,
            0.5f, 0.5f, 0.0f, 1.0f, 1.0f,
            -0.5f, 0.5f, 0.0f, 0.0f, 1.0f
    };

    public Square() {
    }

    public void setTexCoordsForRepeatTexture(Vector4f vec, float repeatCount) {
        float startX = vec.x;
        float startY = vec.y;
        float width = vec.z;
        float height = vec.w;

        vertexData = new float[]{
                // position coords  // texture coords
                -0.5f, -0.5f, 0.0f, startX, startY,
                0.5f, -0.5f, 0.0f, (startX + width) / repeatCount, startY,
                0.5f, 0.5f, 0.0f, (startX + width) / repeatCount, (startY + height) / repeatCount,
                -0.5f, 0.5f, 0.0f, startX, (startY + height) / repeatCount
        };
    }

    @Override
    public float[] GetVertexData() {
        return vertexData;
    }

    public void Init(VertexArray vao) {
        vao.Bind();

        IndexBuffer ib = new IndexBuffer(Square.indices);
        VertexBuffer vbo = new VertexBuffer(vertexData);

        vbo.SetLayout(layout);
        vao.AddVertexBuffer(vbo);
        vao.SetIndexBuffer(ib);
    }

    @Override
    public boolean IsColliding(Shape other) {
        return box.isColliding(other.GetCollidingBox());
    }

    @Override
    public CollidingBox GetCollidingBox() {
        return box;
    }

    public void UpdateCollidingBox(float x, float y, float width, float height) {
        box.setX(x);
        box.setY(y);
        box.setHeight(height);
        box.setWidth(width);
    }
}
