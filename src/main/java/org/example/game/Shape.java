package org.example.game;

import org.example.engine.VertexArray;
import org.example.game.collisions.CollidingBox;

public interface Shape {
    float[] GetVertexData();
    void Init(VertexArray vao);
    boolean IsColliding(Shape box);
    CollidingBox GetCollidingBox();
    void UpdateCollidingBox(float x, float y, float width, float height);
}
