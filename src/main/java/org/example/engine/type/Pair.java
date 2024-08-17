package org.example.engine.type;

public class Pair<T, V> {
    public T Key;
    public V Value;

    public Pair(T key, V value) {
        this.Value = value;
        this.Key = key;
    }
}
