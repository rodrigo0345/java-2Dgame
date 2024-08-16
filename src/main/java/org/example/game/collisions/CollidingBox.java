package org.example.game.collisions;

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
}
