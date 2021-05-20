package Algorithms;


import utils.INode;
import utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Rendezvous<T> implements NodeRemovableHashByName<T>, IDataReplaced {
    private final ArrayList<utils.INode<T>> nodes = new ArrayList<>();
    private final HashMap<String, Integer> nodesMap = new HashMap<>();
    private final ArrayList<String> nodesNames = new ArrayList<>();
    private final ArrayList<Long> nodesHashes = new ArrayList<>();

    private int dataReplaced = 0;

    public Rendezvous() {}

    public Rendezvous(String[] names, ArrayList<INode<T>> nodes) {
        for (int i = 0; i < nodes.size(); i++) {
            this.nodes.add(nodes.get(i));
            nodesMap.put(names[i], i);
            nodesNames.add(names[i]);
            nodesHashes.add(StringUtils.hash(names[i]));
        }
    }

    public void addKey(String key, T value) {
        String bestNodeName = lookup(key);
        if (bestNodeName != null) {
            nodes.get(nodesMap.get(bestNodeName)).store(key, value);
        }
    }

    public T getKey(String key) {
        String respNode = lookup(key);
        if (respNode != null) {
            return nodes.get(nodesMap.get(respNode)).get(key);
        }
        return null;
    }

    public void removeKey(String key) {
        String respNode = lookup(key);
        if (respNode != null) {
            nodes.get(nodesMap.get(respNode)).remove(key);
        }
    }

    public void addNode(String nodeName, utils.INode<T> node) {
        dataReplaced = 0;
        long nodeHash = StringUtils.hash(nodeName);
        for (int i = 0; i < nodes.size(); i++) {
            ArrayList<String> keysToRemove = new ArrayList<>();
            for (Map.Entry<String, T> pair : nodes.get(i).getAll()) {
                long keyHash = StringUtils.hash(pair.getKey());
                long oldHash = xorshiftGen(keyHash ^ nodesHashes.get(i));
                long newHash = xorshiftGen(keyHash ^ nodeHash);
                if (newHash > oldHash) {
                    node.store(pair.getKey(), pair.getValue());
                    keysToRemove.add(pair.getKey());
                    dataReplaced++;
                }
            }

            for (String key : keysToRemove) {
                nodes.get(i).remove(key);
            }
        }

        nodes.add(node);
        nodesMap.put(nodeName, nodesNames.size());
        nodesNames.add(nodeName);
        nodesHashes.add(nodeHash);
    }

    public void removeNode(String nodeName) {
        if (!nodesMap.containsKey(nodeName))
            return;
        int index = nodesMap.get(nodeName);
        Set<Map.Entry<String, T>> toReplace = nodes.get(index).getAll();

        for (String key : nodesMap.keySet()) {
            int i = nodesMap.get(key);
            if (i > index) {
                nodesMap.replace(key, i-1);
            }
        }

        nodesMap.remove(nodeName); //remove node from lists
        nodesNames.remove(index);
        nodesHashes.remove(index);
        nodes.remove(index);

        for(Map.Entry<String, T> pair : toReplace) { //replace assigned nodes
            addKey(pair.getKey(), pair.getValue());
        }
        dataReplaced = toReplace.size();
    }

    public void removeNodeUnchecked(String nodeName) {
        if (!nodesMap.containsKey(nodeName))
            return;
        int index = nodesMap.get(nodeName);
        for (String key : nodesMap.keySet()) {
            int i = nodesMap.get(key);
            if (i > index) {
                nodesMap.replace(key, i-1);
            }
        }

        nodesMap.remove(nodeName); //remove node from lists
        nodesNames.remove(index);
        nodesHashes.remove(index);
        nodes.remove(index);
    }

    public int getNumberOfNodes() {
        return nodes.size();
    }

    public int getDataReplaced() {
        int tmp = dataReplaced;
        dataReplaced = 0;
        return tmp;
    }

    private String lookup(String key) {
        if (nodes.size() == 0)
            return null;
        long keyHash = StringUtils.hash(key);
        int bestIndex = 0;
        long bestHash = xorshiftGen(keyHash ^ nodesHashes.get(0));
        for (int i = 1; i < nodes.size(); i++) {
            long hash = xorshiftGen(keyHash ^ nodesHashes.get(i));
            if (hash > bestHash) {
                bestHash = hash;
                bestIndex = i;
            }
        }
        return nodesNames.get(bestIndex);
    }

    private long xorshiftGen(long x) {
        x ^= (x >> 12);
        x ^= (x << 25);
        x ^= (x >> 27);
        return x * 2685821657736338717L;
    }
}
