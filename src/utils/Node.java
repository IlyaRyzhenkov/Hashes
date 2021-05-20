package utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Node<T> implements INode<T> {
    public final String name;
    private final HashMap<String, T> map;
    public Node (String name){
        map = new HashMap<>();
        this.name = name;
    }

    public void store(String key, T value) {
        map.put(key, value);
    }

    public T get(String key) {
        if (map.containsKey(key))
            return map.get(key);
        return null;
    }

    public Set<Map.Entry<String, T>> getAll() {
        return map.entrySet();
    }

    public void reset() {
        map.clear();
    }

    public void remove(String key) {
        map.remove(key);
    }
}
