package Algorithms;

import java.util.*;
import utils.INode;
import utils.StringUtils;

public class Consistent<T> implements NodeRemovableHashByName<T>, IDataReplaced {
    private final TreeMap<Long, INode<T>> ring = new TreeMap<>();

    private int dataReplaced = 0;

    public Consistent() {

    }

    public Consistent(String[] names, ArrayList<INode<T>> nodes) {
        for (int i = 0; i < nodes.size(); i++) {
            long hash = StringUtils.hash(names[i]);
            ring.put(hash, nodes.get(i));
        }
    }

    public void addKey(String key, T value) {
        if (ring.size() == 0)
            return;

        INode<T> respNode = lookup(key);
        respNode.store(key, value);
    }

    public T getKey(String key) {
        if (ring.size() == 0)
            return null;

        INode<T> respNode = lookup(key);
        return respNode.get(key);
    }

    public void removeKey(String key) {
        if (ring.size() == 0)
            return;
        INode<T> respNode = lookup(key);
        respNode.remove(key);
    }

    public void addNode(String nodeName, utils.INode<T> node) {
        long hash = StringUtils.hash(nodeName);
        if (ring.size() == 0) {
            ring.put(hash, node);
            return;
        }

        SortedMap<Long, INode<T>> tailMap = ring.tailMap(hash);
        INode<T> nextNode = tailMap.isEmpty() ? ring.firstEntry().getValue() : ring.get(tailMap.firstKey());

        ring.put(hash, node);
        dataReplaced = 0;
        ArrayList<String> toRemove = new ArrayList<>();
        for (Map.Entry<String, T> pair : nextNode.getAll()) {
            long keyHash = StringUtils.hash(pair.getKey());
            if (keyHash < hash) {
                node.store(pair.getKey(), pair.getValue());
                toRemove.add(pair.getKey());
                dataReplaced++;
            }
        }
        for (String key : toRemove) {
            nextNode.remove(key);
        }
    }

    public void removeNode(String nodeName) {
        long hash = StringUtils.hash(nodeName);
        if (!ring.containsKey(hash))
            return;
        INode<T> n = ring.get(hash);
        HashSet<Map.Entry<String, T>> toReplace = new HashSet<>(n.getAll());
        ring.remove(hash);
        if (ring.size() == 0)
            return;
        SortedMap<Long, INode<T>> tailMap = ring.tailMap(hash);
        INode<T> nextNode = tailMap.isEmpty() ? ring.firstEntry().getValue() : ring.get(tailMap.firstKey());

        for (Map.Entry<String, T> pair : toReplace) {
            nextNode.store(pair.getKey(), pair.getValue());
        }
        dataReplaced = toReplace.size();
    }

    public void removeNodeUnchecked(String name) {
        ring.remove(StringUtils.hash(name));
    }

    public int getNumberOfNodes() {
        return ring.size();
    }

    public int getDataReplaced() {
        int tmp = dataReplaced;
        dataReplaced = 0;
        return tmp;
    }

    private INode<T> lookup(String key) {
        long hash = StringUtils.hash(key);
        if (!ring.containsKey(hash)) {
            SortedMap<Long, INode<T>> tailMap = ring.tailMap(hash);
            hash = tailMap.isEmpty() ? ring.firstKey() : tailMap.firstKey();
        }
        return ring.get(hash);
    }
}
