package utils;

import java.util.Map;
import java.util.Set;

public interface INode<T> {
    void reset();
    void store(String key, T value);
    Set<Map.Entry<String, T>> getAll();
    T get(String key);
    void remove(String key);
}
