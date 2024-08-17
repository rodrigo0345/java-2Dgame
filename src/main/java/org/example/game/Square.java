package org.example.game;

import org.example.engine.VertexArray;
import org.example.engine.buffers.BufferElement;
import org.example.engine.buffers.BufferLayout;
import org.example.engine.buffers.IndexBuffer;
import org.example.engine.buffers.VertexBuffer;
import org.example.game.collisions.CollidingBox;

import java.util.ArrayList;
import java.util.Arrays;

public class Square implements Shape {

    private final CollidingBox box = new CollidingBox(-0.5f, -0.5f, 0.5f, 0.5f);

    private static final float[] vertexData = new float[]{
            // position coords  // texture coords
            -0.5f, -0.5f, 0.0f, 0.0f, 0.0f,
            0.5f, -0.5f, 0.0f,  1.0f, 0.0f,
            0.5f, 0.5f, 0.0f,   1.0f, 1.0f,
            -0.5f, 0.5f, 0.0f,  0.0f, 1.0f
    };

    private static final int[] indices = {
            0, 1, 2, 2, 3, 0
    };

    private final BufferLayout layout = new BufferLayout(
            new ArrayList<BufferElement>(
                    Arrays.asList(
                            new BufferElement("a_Position", BufferElement.ShaderDataType.Float3, false),
                            new BufferElement("a_TexCoord", BufferElement.ShaderDataType.Float2, false)
                    )
            )
    );

    public Square() {
    }

    @Override
    public float[] GetVertexData() {
        return vertexData;
    }

    public void Init(VertexArray vao){
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

    public void UpdateCollidingBox(float x, float y, float width, float height){
        box.setX(x); box.setY(y); box.setHeight(height); box.setWidth(width);
    }
}
