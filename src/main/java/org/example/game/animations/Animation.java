package org.example.game.animations;

import org.example.engine.Shader;
import org.example.engine.Texture;

public class Animation {
    private final ActionList actions;

    private float spriteWidth = 0.0f;
    private float spriteHeight = 0.0f;

    // can have .0000 because of movement
    private float currentFrame = 0;

    private float currentStateEnd = 0.0f;
    private float currentStatePosNumber = 0.0f;

    private float internalTextureY = 0.0f;
    private float internalTextureX = 0.0f;

    private final Texture texture;

    public Animation(Texture texture, ActionList actions, float spriteWidth, float spriteHeight){
        this.texture = texture;
        this.actions = actions;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
    }

    public void SetState(String stateName){
        currentFrame = 0;
        float currentStatePosition = actions.getStateCoord(stateName);
        currentStateEnd = actions.getStateEndPos(stateName);
        currentStatePosNumber = actions.getNumberOfPositions(stateName);
        internalTextureY = currentStatePosition * spriteHeight / (float) texture.getHeight();
    }

    private void GetNextFrame(float nextFrameFraction){
        // X represents the frame
        // Y represents the action
        nextFrameFraction = (float) Math.floor((nextFrameFraction % currentStatePosNumber) + 1);
        float texX = Math.max(nextFrameFraction, currentFrame) * spriteWidth / (float) texture.getWidth();

        if(texX >= currentStateEnd){
            internalTextureX = currentFrame = 0;
            return;
        }

        internalTextureX = currentFrame += texX;
    }

    public void SetCurrentSpriteFrame(float frame){
        GetNextFrame(frame);
    }

    public void BindCurrentFrame(String uniformName, Shader boundShader) {
        boundShader.uploadUniformFloat4(uniformName, internalTextureX, internalTextureY, spriteWidth/ (float)texture.getWidth(), spriteHeight / (float)texture.getHeight());
    }
}
