package Algorithms;

public interface NodeRemovableHashByIndex<T> extends ResizableHash<T> {
    void removeNode(int index);
    void removeNodeUnchecked(int index);
}
