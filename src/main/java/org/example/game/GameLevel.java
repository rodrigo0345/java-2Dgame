package org.example.game;

import org.example.engine.entities.EntityInterface;

import java.util.ArrayList;

public class GameLevel {

    public ArrayList<EntityInterface> entities = new ArrayList<>();

    public void AddEntity(EntityInterface entity) {
        entities.add(entity);
    }

    public void RemoveEntity(EntityInterface entity) {
        entities.remove(entity);
    }

    public void DrawEntities() {
        for (EntityInterface entity : entities) {
            entity.draw();
        }
    }

    public void Clear() {
        entities = new ArrayList<>();
    }
}
