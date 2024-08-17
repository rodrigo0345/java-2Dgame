package org.example.game.collisions;

import org.example.engine.Renderer;
import org.example.engine.Shader;
import org.example.engine.Texture;
import org.example.engine.VertexArray;
import org.example.engine.buffers.BufferElement;
import org.example.engine.buffers.BufferLayout;
import org.example.engine.buffers.IndexBuffer;
import org.example.engine.buffers.VertexBuffer;
import org.example.game.Shape;
import org.example.game.Square;
import org.example.game.entities.Entity;
import org.example.game.entities.EntityInterface;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollidingBox {
    private float x = 0;
    private float y = 0;
    private float width = 0;
    private float height = 0;


    public CollidingBox(float x, float y, float width, float height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean isColliding(CollidingBox other) {
        return !(other.x + other.width < x ||
                other.x > x + width ||
                other.y + other.height < y ||
                other.y > y + height);
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    private static final int[] indices = {
            0, 1, 2, 2, 3, 0
    };

    private final BufferLayout layout = new BufferLayout(
            new ArrayList<BufferElement>(
                    List.of(
                            new BufferElement("a_Position", BufferElement.ShaderDataType.Float3, false)
                    )
            )
    );

    // this is really only meant to be used in debug
    public void draw(final float[] viewProjection, final float[] transform) {
        if (!Renderer.debug) {
        }

    }

    private final static String defaultCollidingVertShader = """
                 #version 330 core
                 layout(location = 0) in vec3 a_Position;
            
                 uniform mat4 u_ViewProjection;
                 uniform mat4 u_Transform;
            
                 void main() {
                   gl_Position = u_ViewProjection * u_Transform * vec4(a_Position, 1.0);
                 }
            """;

    private final static String defaultCollidingFragShader = """
              #version 330 core
            
              layout(location = 0) out vec4 o_Color;
            
              void main() {
                o_Color = vec4(0.85, 0.1, 0.13, 1.0f);
              }
            """;

}
