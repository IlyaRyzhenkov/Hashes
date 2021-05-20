package Algorithms;

import utils.INode;
import utils.StringUtils;

import java.util.*;

public class Jump<T> implements NodeRemovableHash<T>, IDataReplaced {
    private static final long JUMP = 1L << 31;
    private static final long CONSTANT = Long.parseUnsignedLong("2862933555777941757");

    private final ArrayList<INode<T>> nodes = new ArrayList<>();

    private int dataReplaced = 0;

    public Jump() {

    }

    public Jump(String[] names, ArrayList<INode<T>> nodes) {
        this.nodes.addAll(nodes);
    }

    public void addKey(String key, T value) {
        if (nodes.size() == 0)
            return;

        long hash = StringUtils.hash(key);
        int bucket = jump(hash, nodes.size());
        nodes.get(bucket).store(key, value);
    }

    public T getKey(String key) {
        if (nodes.size() == 0)
            return null;

        long hash = StringUtils.hash(key);
        int bucket = jump(hash, nodes.size());
        return nodes.get(bucket).get(key);
    }

    public void removeKey(String key) {
        if (nodes.size() == 0)
            return;
        long hash = StringUtils.hash(key);
        int bucket = jump(hash, nodes.size());
        nodes.get(bucket).remove(key);
    }

    public void addNode(String nodeName, INode<T> node) {
        if (nodes.size() == 0) {
            nodes.add(node);
            return;
        }

        HashMap<String, Integer> toReplace = new HashMap<>();
        for (int i = 0; i < nodes.size(); i++) {
            for (Map.Entry<String, T> pair : nodes.get(i).getAll()){
                toReplace.put(pair.getKey(), i);
            }
        }

        nodes.add(node);
        dataReplaced = 0;
        for (Map.Entry<String, Integer> pair : toReplace.entrySet()) {
            long hash = StringUtils.hash(pair.getKey());
            int newNode = jump(hash, nodes.size());
            if (newNode != pair.getValue())
            {
                T obj = nodes.get(pair.getValue()).get(pair.getKey());
                nodes.get(pair.getValue()).remove(pair.getKey());
                nodes.get(nodes.size() - 1).store(pair.getKey(), obj);
                dataReplaced++;
            }
        }
    }
    //Removes last node
    public void removeNode() {
        if (nodes.size() == 0)
            return;
        int last = nodes.size() - 1;
        HashSet<Map.Entry<String, T>> toReplace = new HashSet<>(nodes.get(last).getAll());
        nodes.remove(last);
        for (Map.Entry<String, T> pair : toReplace) {
            addKey(pair.getKey(), pair.getValue());
        }
        dataReplaced = toReplace.size();
    }

    public void removeNodeUnchecked() {
        if (nodes.size() == 0)
            return;
        nodes.remove(nodes.size() - 1);
    }

    public int getNumberOfNodes() {
        return nodes.size();
    }

    public int getDataReplaced() {
        int tmp = dataReplaced;
        dataReplaced = 0;
        return tmp;
    }

    private static int jump(long key, int bucketsCount) {
        long b = -1;
        long j = 0;

        while (j < bucketsCount) {
            b = j;
            key = key * CONSTANT + 1L;

            j = (long) ((b + 1L) * (JUMP / utils.BitsConversions.toDouble((key >>> 33) + 1L)));
        }
        return (int) b;
    }
}
