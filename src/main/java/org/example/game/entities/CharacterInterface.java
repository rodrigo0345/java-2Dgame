package org.example.game.entities;

import org.example.engine.entities.Entity;
import org.example.engine.shape.Shape;
import org.joml.Vector3f;

public interface CharacterInterface {
    Shape getShape();

    Boolean isColliding(Entity box);

    void draw();

    void setPosition(Vector3f position);

    void resetAnimation();
}
