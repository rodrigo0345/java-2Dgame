package org.example.game.entities;

import org.example.engine.Renderer;
import org.example.engine.Shader;
import org.example.engine.Texture;
import org.example.engine.VertexArray;
import org.example.game.Shape;
import org.example.game.Square;
import org.joml.*;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

public class Entity extends Square {
    private Matrix4f ModelMatrix = new Matrix4f();
    private final Shape foundationShape;
    private final VertexArray vao = new VertexArray();
    private Shader shader = new Shader(defaultEntityVertexShader, defaultEntityFragmentShader);

    public Entity(Shape foundationShape) {
        this.foundationShape = foundationShape;

        vao.Bind();
        foundationShape.Init(vao);
    }

    public boolean isColliding(Entity entity){
        return this.foundationShape.IsColliding(entity.getFoundationShape());
    }

    public Matrix4f getModelMatrix(){
        return ModelMatrix;
    }

    public void resetModelMatrix() {
        ModelMatrix = new Matrix4f();
    }

    // ------------------ Projection Matrix -----------------
    public void translate(Vector3f translate){
        ModelMatrix.translate(translate);
    }
    public void rotate(float angle, Vector3f axis){
        ModelMatrix.rotate(angle, axis);
    }
    public void scale(Vector3f scale){
        ModelMatrix.scale(scale);
    }
    // ------------------ Projection Matrix -----------------

    // ------------------ Getters -----------------
    public Shape getFoundationShape(){
        return foundationShape;
    }
    public VertexArray getVao() {return vao;}
    public Shader getShader() {return shader;}

    public void setShader(Shader shader) {
        this.shader = shader;
    }
    // ------------------ Getters -----------------

    private final static String defaultEntityVertexShader = """
          #version 330 core
          layout(location = 0) in vec3 a_Position;
          layout(location = 1) in vec2 a_TexCoord;

          out vec2 v_TexCoord;
          uniform mat4 u_ViewProjection;
          uniform mat4 u_Transform;
          uniform vec4 u_SpriteIndex;

          void main() {
            v_TexCoord = a_TexCoord * u_SpriteIndex.zw + u_SpriteIndex.xy;
            gl_Position = u_ViewProjection * u_Transform * vec4(a_Position, 1.0);
          }
     """;

        private final static String defaultEntityFragmentShader = """
          #version 330 core

          layout(location = 0) out vec4 o_Color;
          in vec2 v_TexCoord;
          uniform sampler2D u_Texture;

          void main() {
            o_Color = texture(u_Texture, v_TexCoord);
          }
        """;
}
