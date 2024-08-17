package org.example.game.map;

import org.example.engine.entities.Entity;
import org.example.engine.shape.Square;
import org.example.engine.textures.Texture;
import org.joml.Vector4f;

public class Treasure extends Entity {
    int width = 200;
    int height = 200;

    private static Texture getInitTexture() {
        return new Texture("assets/props.png");
    }

    private static Vector4f getTexRect() {
        int spriteWidth = 32 * 2;
        int spriteHeight = 32 * 2;
        int spriteX = 1;
        int spriteY = 7;
        Vector4f texRect = new Vector4f();
        texRect.x = spriteX * (float) spriteWidth / (float) getInitTexture().getWidth();
        texRect.y = spriteY * (float) spriteHeight / (float) getInitTexture().getHeight();
        texRect.z = 1.0f / getInitTexture().getWidth() * spriteWidth;
        texRect.w = 1.0f / getInitTexture().getHeight() * spriteHeight;
        return texRect;
    }

    public Treasure() {
        super(new Square(), getInitTexture(), false, false, getTexRect());
    }

    @Override
    public void draw() {
        super.draw(null);
    }
}
