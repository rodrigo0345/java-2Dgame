package org.example.game.animations;

import org.joml.Vector2f;

public class ActionData {
    Vector2f coords;
    boolean isEnd;
    boolean isBegin;

    public ActionData(Vector2f coords, boolean isEnd, boolean isBegin) {
        this.coords = coords;
        this.isEnd = isEnd;
        this.isBegin = isBegin;
    }
}

