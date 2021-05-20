package Algorithms;

public interface NodeRemovableHash<T> extends ResizableHash<T> {
    void removeNode();
    void removeNodeUnchecked();
}
