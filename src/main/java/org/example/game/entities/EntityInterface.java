package org.example.game.entities;

import org.example.engine.Shader;
import org.example.engine.Texture;
import org.example.engine.VertexArray;
import org.example.game.Shape;
import org.joml.Vector3f;

public interface EntityInterface {
    public VertexArray getVao();
    public Shader getShader();
    public Shape getShape();
    public Texture getTexture();
    public float[] getTransform();
    public Boolean isColliding(Entity box);
    public void draw();
    public void setPosition(Vector3f position);
}
