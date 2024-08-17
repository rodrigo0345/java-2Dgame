package org.example.engine.entities;

import org.example.engine.animations.Animation;
import org.example.engine.buffers.VertexArray;
import org.example.engine.renderer.Renderer;
import org.example.engine.shaders.Shader;
import org.example.engine.textures.Texture;
import org.example.engine.type.Pair;
import org.example.engine.shape.Shape;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

public class Entity implements EntityInterface {

    private final static String defaultEntityVertexShader = "assets/vertexShaders/texturedEntity.vert";
    private final static String defaultEntityFragmentShader = "assets/fragmentShaders/texturedEntity.frag";
    private final Shape foundationShape;
    private final VertexArray vao = new VertexArray();
    private final boolean isCollideable;
    private final boolean isSprite;
    private Texture texture;
    private String vertexShader = defaultEntityVertexShader;
    private String fragmentShader = defaultEntityFragmentShader;
    private Shader shaderCache = null;
    private Vector3f rotation = new Vector3f();
    private float rotationAngle = 0.0f;
    private Vector3f scale = new Vector3f(1.0f, 1.0f, 1.0f);
    private Vector3f translation = new Vector3f();

    public Entity(Shape foundationShape,
                  Texture texture,
                  boolean isCollideable,
                  boolean isSprite,
                  Vector4f texRect) {
        this.foundationShape = foundationShape;
        this.isCollideable = isCollideable;
        this.isSprite = isSprite;
        this.texture = texture;

        if (isSprite) {
            vertexShader = "assets/vertexShaders/texturedSpriteEntity.vert";
            fragmentShader = "assets/fragmentShaders/texturedEntity.frag";
        }

        vao.Bind();

        // adjust the texCoords in case the texture needs to repeat
        // this is all because dealing with textures is kind of hard
        float count = texture.getRepeadtCount();
        if (texRect == null) {
            texRect = new Vector4f();
            texRect.x = 0.0f;
            texRect.y = 0.0f;
            texRect.z = 1.0f;
            texRect.w = 1.0f;
        }
        foundationShape.setTexCoordsForRepeatTexture(texRect, count);

        shaderCache = new Shader(vertexShader, fragmentShader, true);
        foundationShape.Init(vao);
    }

    // VAO and Shaders
    public VertexArray getVao() {
        return vao;
    }

    public Boolean isColliding(Entity entity) {
        if (!isCollideable) return false;
        return this.foundationShape.IsColliding(entity.getFoundationShape());
    }

    @Override
    public Boolean handleCollision(Entity box) {
        // TODO
        return null;
    }

    // Model Matrix /////////////////////////////////////

    @Override
    public void draw() {
        assert false : "This method draw should not be used and shall be override";
    }

    // expects the scene to have started
    @Override
    public void draw(Animation animation) {
        if (isSprite) {
            assert animation != null : "isSprite is set to true and animation is missing";
            animation.BindCurrentFrame("u_SpriteIndex", getShader());
        }
        this.getTexture().bind(0);
        Renderer.Submit(this);
    }

    @Override
    public Pair<Float, Vector3f> getRotation() {
        return new Pair<>(rotationAngle, rotation);
    }

    @Override
    public void setRotation(float angle, Vector3f vec) {
        rotation = vec;
        rotationAngle = angle;
    }

    @Override
    public Vector3f getTranslation() {
        return translation;
    }

    @Override
    public void setTranslation(Vector3f vec) {
        translation = vec;
    }

    @Override
    public float getScale() {
        // assuming x = y = z
        return scale.x;
    }

    @Override
    public void setScale(float scale) {
        this.scale = new Vector3f(scale, scale, scale);
    }

    // Model Matrix /////////////////////////////////////

    @Override
    public Matrix4f getTransformMat4() {
        Matrix4f model = new Matrix4f().identity();

        // Then, apply the rotation

        // Finally, move the quad back to its original position
        model.translate(translation);

        // If scaling is needed, you can apply it here
        model.scale(scale);

        model.rotate((float) Math.toRadians(rotationAngle), rotation);

        return model;
    }

    @Override
    public float[] getTransform() {
        Matrix4f model = getTransformMat4();
        float[] transformData = new float[16];
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Allocate a float buffer with 16 floats on the stack
            FloatBuffer buffer = stack.mallocFloat(16);
            // Put the matrix data into the buffer
            model.get(buffer);
            // Extract the data from the buffer into the array
            buffer.get(transformData);
        }
        return transformData;
    }

    public Shape getFoundationShape() {
        return foundationShape;
    }

    @Override
    public Shape getShape() {
        return foundationShape;
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    // Shaders /////////////////////////////////////////////////////////////////////////////////
    public Shader getShader() {
        return shaderCache;
    }

    @Override
    public void setVertexShader(String shader) {
        this.vertexShader = shader;
        vao.Bind();
        shaderCache = new Shader(vertexShader, fragmentShader, true);
    }

    @Override
    public void setFragmentShader(String shader) {
        this.fragmentShader = shader;
        vao.Bind();
        shaderCache = new Shader(vertexShader, fragmentShader, true);
    }
    // Shaders /////////////////////////////////////////////////////////////////////////////////
}
