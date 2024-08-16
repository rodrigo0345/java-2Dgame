package org.example.game.animations;

import org.example.engine.type.Pair;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public class ActionList {
    // e.g. "walk" -> 8 horizontal sprites
    private ArrayList<Pair<String, Integer>> actions = new ArrayList<>();

    public ActionList(ArrayList<Pair<String, Integer>> actions){
        this.actions = actions;
    }

    private ArrayList<String> keyList() {
        ArrayList<String> keys = new ArrayList<>();
        for(Pair<String, Integer> action: actions){
            keys.add(action.Key);
        }
        return keys;
    }

    private Integer get(String key){
        return actions.stream().parallel().filter(action -> {
            return action.Key == key;
        }).findFirst().orElseThrow().Value;
    }

    public float getStateCoord(String actionName){
        // reverse the hashmap keyset
        // to make it more intuitive
        ArrayList<String> keySet = this.keyList();
        float count = keySet.size() - 1;
        for(String name: keySet){
            if(name.equals(actionName)) return count;
            count--;
        }
        assert false: "Invalid action name set " + actionName;
        return 0;
    }

    public float getStateEndPos(String actionName){
        return get(actionName) - 1;
    }

    public int getNumberOfPositions(String actionName){
        return get(actionName);
    }
}
