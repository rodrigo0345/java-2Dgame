package org.example.engine.shape;

import org.example.engine.buffers.VertexArray;
import org.example.engine.collisions.CollidingBox;
import org.joml.Vector4f;

public interface Shape {
    float[] GetVertexData();

    void Init(VertexArray vao);

    boolean IsColliding(Shape box);

    CollidingBox GetCollidingBox();

    void setTexCoordsForRepeatTexture(Vector4f vec, float repeatCount);

    void UpdateCollidingBox(float x, float y, float width, float height);
}
