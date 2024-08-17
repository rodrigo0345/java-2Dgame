package org.example.engine.entities;

import org.example.engine.animations.Animation;
import org.example.engine.buffers.VertexArray;
import org.example.engine.shaders.Shader;
import org.example.engine.textures.Texture;
import org.example.engine.type.Pair;
import org.example.engine.shape.Shape;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public interface EntityInterface {
    VertexArray getVao();

    Shader getShader();

    void setVertexShader(String shader);

    void setFragmentShader(String shader);

    Shape getShape();

    Texture getTexture();

    void setTexture(Texture texture);

    Boolean isColliding(Entity box);

    Boolean handleCollision(Entity box);

    // do not use draw without Animation outside this package
    void draw();

    void draw(Animation animation);

    // Model Matrix
    Pair<Float, Vector3f> getRotation();

    void setRotation(float angle, Vector3f vec);

    Vector3f getTranslation();

    void setTranslation(Vector3f vec);

    float getScale();

    void setScale(float scale);

    float[] getTransform();

    Matrix4f getTransformMat4();
}
