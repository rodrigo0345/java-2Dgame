package org.example.game.characters;

import org.example.engine.*;
import org.example.engine.type.Pair;
import org.example.game.*;
import org.example.game.animations.ActionList;
import org.example.game.animations.Animation;
import org.example.game.entities.Entity;
import org.example.game.entities.EntityInterface;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.ArrayList;

public class Zombie implements EntityInterface {

    Texture texture = new Texture("assets/Zombie.png");
    Entity entity = new Entity(new Square());

    Animation animation;
    float animationSpeed = 3.0f;
    float frameCounter = 0.0f;

    private Vector3f rotation = new Vector3f();
    private float rotationAngle = 0.0f;
    private final Vector3f scale = new Vector3f(1.0f, 1.0f, 1.0f);
    private Vector3f translation = new Vector3f();

    private void InitializeAnimations() {
        float x = 32, y = 32;

        ArrayList<Pair<String, Integer>> data = new ArrayList<>();

        data.add(new Pair<>("idle", 7));    // 0
        data.add(new Pair<>("attack", 6));  // 1
        data.add(new Pair<>("walk", 7));    // 2
        data.add(new Pair<>("u_", 7));      // 3
        data.add(new Pair<>("y_", 7));      // 4
        data.add(new Pair<>("die", 7));     // 5

        animation = new Animation(
                texture,
                new ActionList(data),
                x, // image specific
                y // image specific
        );
    }

    public Zombie(Vector3f spawnLocation) {
        entity = new Entity(new Square());
        entity.getVao().Bind();
        translation = spawnLocation;
        this.InitializeAnimations();
    }

    public void Walk(Vector3f walkTo){
        // TODO: Animate
        translation.add(walkTo);
        animation.SetState("walk");
        frameCounter += animationSpeed * (float)Timestamp.GetDeltaTime();
        animation.SetCurrentSpriteFrame(frameCounter);
    }

    public void Die(){
        // TODO: Animate
    }

    public void Attack(){
        // TODO: Animate
        animation.SetState("attack");
        frameCounter += 5.0 * (float) Timestamp.GetDeltaTime();
        animation.SetCurrentSpriteFrame(frameCounter);
    }

    public void Idle(){
        // TODO: Animate
        animation.SetState("idle");
        frameCounter += animationSpeed * (float)Timestamp.GetDeltaTime();
        animation.SetCurrentSpriteFrame(frameCounter);
    }

    @Override
    public VertexArray getVao() {
        return entity.getVao();
    }

    @Override
    public Shader getShader() {
        return entity.getShader();
    }

    @Override
    public Shape getShape() {
        return entity.getFoundationShape();
    }

    @Override
    public Texture getTexture() {
        return null;
    }

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

    @Override
    public Boolean isColliding(Entity box) {
        return entity.isColliding(box);
    }

    @Override
    public void draw() {
        getShader().bind();
        animation.BindCurrentFrame("u_SpriteIndex", getShader());
        Renderer.Submit(this);
        getShader().unbind();
    }


    @Override
    public void setPosition(Vector3f position) {
        if ((position.x - this.translation.x) < 0) {
            rotation = new Vector3f(0.0f, 1.0f, 0.0f);
            rotationAngle = 180.0f;
        } else {
            rotationAngle = 0.0f;
        }
        translation = position;
        animation.SetState("walk");
        frameCounter += animationSpeed * (float)Timestamp.GetDeltaTime();
        animation.SetCurrentSpriteFrame(frameCounter);
    }

    @Override
    public void resetAnimation() {
        // default
        Idle();
    }
}
