package org.example.game.map;

import org.example.engine.entities.Entity;
import org.example.engine.textures.Texture;
import org.example.engine.shape.Square;

public class Ground extends Entity {
    int width = 200;
    int height = 200;

    public Ground() {
        super(new Square(), new Texture("assets/grass.png"), false, false, null);
        super.setScale(15);
    }

    @Override
    public void draw() {
        super.draw(null);
    }
}
