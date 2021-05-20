package Algorithms;

import utils.INode;

public interface ResizableHash<T> {
    void addKey(String key, T value);
    T getKey(String key);
    void removeKey(String key);
    void addNode(String nodeName, INode<T> node);
    int getNumberOfNodes();
}
