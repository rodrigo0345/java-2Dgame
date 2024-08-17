package org.example.game.characters;

import org.example.engine.animations.ActionList;
import org.example.engine.animations.Animation;
import org.example.engine.entities.Entity;
import org.example.engine.textures.Texture;
import org.example.engine.time.Timestamp;
import org.example.engine.type.Pair;
import org.example.engine.shape.Square;
import org.example.game.entities.CharacterInterface;
import org.joml.Vector3f;

import java.util.ArrayList;

public class Zombie extends Entity implements CharacterInterface {

    Vector3f position = new Vector3f();

    Animation animation;
    float animationSpeed = 3.0f;
    float frameCounter = 0.0f;

    public Zombie(Vector3f spawnLocation) {
        super(new Square(), new Texture("assets/Zombie.png"), true, true, null);
        super.getVao().Bind();

        position = spawnLocation;
        setTranslation(position);

        this.InitializeAnimations();
    }

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
                super.getTexture(),
                new ActionList(data),
                x, // image specific
                y // image specific
        );
    }

    public void Walk(Vector3f walkTo) {
        position.add(walkTo);
        super.setTranslation(position);

        animation.SetState("walk");
        frameCounter += animationSpeed * (float) Timestamp.GetDeltaTime();
        animation.SetCurrentSpriteFrame(frameCounter);
    }

    public void Die() {
        // TODO: Animate
    }

    public void Attack() {
        // TODO: Animate
        animation.SetState("attack");
        frameCounter += 5.0 * (float) Timestamp.GetDeltaTime();
        animation.SetCurrentSpriteFrame(frameCounter);
    }

    public void Idle() {
        // TODO: Animate
        animation.SetState("idle");
        frameCounter += animationSpeed * (float) Timestamp.GetDeltaTime();
        animation.SetCurrentSpriteFrame(frameCounter);
    }

    @Override
    public void draw() {
        super.draw(animation);
    }

    public void setPosition(Vector3f position) {
        if ((position.x - this.getTranslation().x) < 0) {
            setRotation(180.0f, new Vector3f(0.0f, 1.0f, 0.0f));
        } else {
            setRotation(0.0f, new Vector3f(0.0f, 1.0f, 0.0f));
        }
        setTranslation(position);

        animation.SetState("walk");
        frameCounter += animationSpeed * (float) Timestamp.GetDeltaTime();
        animation.SetCurrentSpriteFrame(frameCounter);
    }

    public void resetAnimation() {
        // default
        Idle();
    }
}
