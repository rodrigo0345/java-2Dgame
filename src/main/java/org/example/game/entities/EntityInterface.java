package org.example.game.entities;

import org.example.engine.Shader;
import org.example.engine.Texture;
import org.example.engine.VertexArray;
import org.example.game.Shape;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public interface EntityInterface {
    VertexArray getVao();

    Shader getShader();

    Shape getShape();

    Texture getTexture();

    float[] getTransform();

    Matrix4f getTransformMat4();

    Boolean isColliding(Entity box);

    void draw();

    void setPosition(Vector3f position);

    void resetAnimation();
}
