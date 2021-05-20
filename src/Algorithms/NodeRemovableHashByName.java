package Algorithms;

public interface NodeRemovableHashByName<T> extends ResizableHash<T> {
    void removeNode(String name);
    void removeNodeUnchecked(String name);
}
