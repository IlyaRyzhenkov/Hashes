package Algorithms;

import utils.StringUtils;

import java.util.ArrayList;
import java.util.Map;

public class ModN<T> implements NodeRemovableHashByIndex<T>, IDataReplaced {
    private int numberOfNodes = 0;
    private int dataReplaced = 0;
    private final ArrayList<utils.INode<T>> nodes = new ArrayList<>();

    public ModN() {

    }

    public ModN(String[] names, ArrayList<utils.INode<T>> nodes) {
        this.nodes.addAll(nodes);
        numberOfNodes = this.nodes.size();
    }

    public void addKey(String key, T value) {
        if (numberOfNodes == 0)
            return;
        long hash = StringUtils.hash(key);
        nodes.get((int) Math.abs(hash % numberOfNodes)).store(key, value);
    }

    public T getKey(String key) {
        if (numberOfNodes == 0)
            return null;
        long hash = StringUtils.hash(key);
        return nodes.get((int) Math.abs(hash % numberOfNodes)).get(key);
    }

    public void removeKey(String key) {
        if (numberOfNodes == 0)
            return;
        long hash = StringUtils.hash(key);
        nodes.get((int) Math.abs(hash % numberOfNodes)).remove(key);
    }

    public void addNode(String nodeName, utils.INode<T> node) {
        ArrayList<Map.Entry<String, T>> pairs = new ArrayList<>();
        for(utils.INode<T> n : nodes) {
            pairs.addAll(n.getAll());
            n.reset();
        }

        numberOfNodes++;
        nodes.add(node);

        for(Map.Entry<String, T> pair : pairs) {
            addKey(pair.getKey(), pair.getValue());
        }
        dataReplaced = pairs.size();
    }

    public void removeNode(int index) {
        if (index >= numberOfNodes)
            return;

        ArrayList<Map.Entry<String, T>> pairs = new ArrayList<>();
        for(utils.INode<T> n : nodes) {
            pairs.addAll(n.getAll());
            n.reset();
        }
        nodes.remove(index);
        numberOfNodes--;

        for(Map.Entry<String, T> pair : pairs) {
            addKey(pair.getKey(), pair.getValue());
        }
        dataReplaced = pairs.size();
    }

    public void removeNodeUnchecked(int index) {
        nodes.remove(index);
        numberOfNodes--;
    }

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public int getDataReplaced() {
        int tmp = dataReplaced;
        dataReplaced = 0;
        return tmp;
    }
}
