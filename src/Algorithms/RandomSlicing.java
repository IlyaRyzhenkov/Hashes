package Algorithms;

import utils.INode;
import utils.Interval;
import utils.OwnerWeight;
import utils.StringUtils;

import java.util.*;

public class RandomSlicing<T> implements NodeRemovableHashByName<T>, IDataReplaced {
    private final TreeMap<Double, String> floatTree = new TreeMap<>();
    private final TreeMap<String, TreeSet<Interval>> floatMap = new TreeMap<>();
    private final TreeMap<String, Double> nodesIntervalTotalLen = new TreeMap<>();
    private final TreeSet<Interval> freeBlocks = new TreeSet<>();
    private ArrayList<utils.OwnerWeight> ownerWeights = new ArrayList<>();

    private final HashMap<String, INode<T>> nodes = new HashMap<>();
    private int dataReplaced = 0;


    public RandomSlicing() {
        freeBlocks.add(new Interval(0, 1d));
    }

    public RandomSlicing(String[] names, ArrayList<INode<T>> nodes) {
        freeBlocks.add(new Interval(0, 1d));
        for (int i = 0; i < nodes.size(); i++) {
            ownerWeights.add(new OwnerWeight(names[i], 1));
            this.nodes.put(names[i], nodes.get(i));
        }
        makeFloatMap();
    }

    public void addKey(String key, T value) {
        if (nodes.size() == 0)
            return;
        String nodeName = lookup(key);
        nodes.get(nodeName).store(key, value);
    }

    public T getKey(String key) {
        if (nodes.size() == 0)
            return null;
        String nodeName = lookup(key);
        return nodes.get(nodeName).get(key);
    }

    public void removeKey(String key) {
        if (nodes.size() == 0)
            return;
        String nodeName = lookup(key);
        nodes.get(nodeName).remove(key);
    }

    public void addNode(String nodeName, INode<T> node) {
        addNode(nodeName, node, 1);
    }
    
    public void addNode(String nodeName, INode<T> node, double weight) {
        HashMap<String, String> oldLookups = new HashMap<>();
        for (Map.Entry<String, INode<T>> n : nodes.entrySet()) {
            for (Map.Entry<String, T> pair : n.getValue().getAll()) {
                oldLookups.put(pair.getKey(), lookup(pair.getKey()));
            }
        }

        ownerWeights.add(new OwnerWeight(nodeName, weight));
        makeFloatMap();
        nodes.put(nodeName, node);

        dataReplaced = 0;
        for (Map.Entry<String, String> pair : oldLookups.entrySet()) {
            String l = lookup(pair.getKey());
            if (!l.equals(pair.getValue())) {
                T obj = nodes.get(pair.getValue()).get(pair.getKey());
                nodes.get(pair.getValue()).remove(pair.getKey());
                nodes.get(l).store(pair.getKey(), obj);
                dataReplaced++;
            }
        }
    }

    public void removeNode(String nodeName) {
        if (!nodes.containsKey(nodeName))
            return;

        HashSet<Map.Entry<String, T>> toReplace = new HashSet<>(nodes.get(nodeName).getAll());
        nodes.remove(nodeName);

        HashMap<String, String> oldLookups = new HashMap<>();
        for (Map.Entry<String, INode<T>> n : nodes.entrySet()) {
            for (Map.Entry<String, T> pair : n.getValue().getAll()) {
                oldLookups.put(pair.getKey(), n.getKey());
            }
        }
        for (int i = 0; i < ownerWeights.size(); i++) {
            if (ownerWeights.get(i).getName().equals(nodeName)) {
                ownerWeights.remove(i);
                break;
            }
        }

        makeFloatMap();

        dataReplaced = toReplace.size();
        for(Map.Entry<String, T> pair : toReplace) {
            addKey(pair.getKey(), pair.getValue());
        }
        for (Map.Entry<String, String> pair : oldLookups.entrySet()) {
            String l = lookup(pair.getKey());
            if (!l.equals(pair.getValue())) {
                T obj = nodes.get(pair.getValue()).get(pair.getKey());
                nodes.get(pair.getValue()).remove(pair.getKey());
                nodes.get(l).store(pair.getKey(), obj);
                dataReplaced++;
            }
        }
    }

    public void removeNodeUnchecked(String nodeName) {
        if (!nodes.containsKey(nodeName))
            return;
        nodes.remove(nodeName);
        for (int i = 0; i < ownerWeights.size(); i++) {
            if (ownerWeights.get(i).getName().equals(nodeName)) {
                ownerWeights.remove(i);
                break;
            }
        }
        makeFloatMap();
    }

    public int getNumberOfNodes() {
        return nodes.size();
    }

    public int getDataReplaced() {
        int tmp = dataReplaced;
        dataReplaced = 0;
        return tmp;
    }

    private void makeFloatMap() {
        double newSum = 0;
        for (utils.OwnerWeight ow : ownerWeights) {
            newSum += ow.getWeight();
        }
        TreeMap<String, Double> diffMap = new TreeMap<>();
        if (floatMap.size() == 0) {
            for (utils.OwnerWeight ow : ownerWeights) {
                diffMap.put(ow.getName(), ow.getWeight() / newSum);
            }
        } else {
            HashSet<String> oldOnly = new HashSet<>(floatMap.keySet());
            for (utils.OwnerWeight ow : ownerWeights) {
                oldOnly.remove(ow.getName());
            }
            for (String s : oldOnly) {
                TreeSet<Interval> blocks = floatMap.get(s);
                floatMap.remove(s);
                freeBlocks.addAll(blocks);
            }
            for (utils.OwnerWeight ow : ownerWeights) {
                if (floatMap.containsKey(ow.getName())) {
                    diffMap.put(ow.getName(), (ow.getWeight() / newSum) - nodesIntervalTotalLen.get(ow.getName()));
                } else {
                    diffMap.put(ow.getName(), ow.getWeight() / newSum);
                }
            }
        }
        applyDiffMap(diffMap);
        generateFloatTree();
    }

    private void applyDiffMap(TreeMap<String, Double> diffMap) {
        for (Map.Entry<String, Double> n : diffMap.entrySet()) { //subtract part of a block
            double diff = n.getValue();
            if (diff < 0) {
                diff = -diff;
                double currentSpace = nodesIntervalTotalLen.get(n.getKey());
                TreeSet<Interval> iSet = floatMap.get(n.getKey());
                while (true) {
                    if (iSet.isEmpty()) {
                        break;
                    }
                    Interval i = iSet.first();
                    iSet.remove(i);
                    currentSpace -= i.length;

                    if (i.length == diff) {
                        freeBlocks.add(i);
                    } else if (i.length > diff) {
                        Interval newInterval = new Interval(i.start, i.length-diff);
                        iSet.add(newInterval);
                        currentSpace += newInterval.length;
                        freeBlocks.add(new Interval(newInterval.end, diff));
                        break;
                    } else {
                        freeBlocks.add(i);
                        diff -= i.length;
                    }
                }
                nodesIntervalTotalLen.replace(n.getKey(), currentSpace);
            }
        }

        for (Map.Entry<String, Double> n : diffMap.entrySet()) { //add from empty block to this block
            double diff = n.getValue();
            if (diff > 0) {
                double currentSpace = nodesIntervalTotalLen.containsKey(n.getKey()) ? nodesIntervalTotalLen.get(n.getKey()) : 0;
                if (!floatMap.containsKey(n.getKey()))
                    floatMap.put(n.getKey(), new TreeSet<>());
                TreeSet<Interval> iSet = floatMap.get(n.getKey());
                while (true) {
                    if (freeBlocks.isEmpty()) {
                        break;
                    }
                    Interval i = freeBlocks.first();
                    freeBlocks.remove(i);
                    if (i.length == diff) {
                        currentSpace += diff;
                        iSet.add(i);
                        break;
                    } else if (i.length > diff) {
                        currentSpace += diff;
                        Interval newInt = new Interval(i.start, diff);
                        iSet.add(newInt);
                        freeBlocks.add(new Interval(newInt.end, i.length - diff));
                        break;
                    } else {
                        currentSpace += i.length;
                        diff -= i.length;
                        iSet.add(i);
                    }
                }
                nodesIntervalTotalLen.put(n.getKey(), currentSpace);
            }
        }
    }
    
    private void generateFloatTree() {
        floatTree.clear();
        for (Map.Entry<String, TreeSet<Interval>> pair : floatMap.entrySet()) {
            for (Interval i : pair.getValue()) {
                floatTree.put(i.start, pair.getKey());
            }
        }
    }
    
    private String lookup(String key) {
        double hval = utils.BitsConversions.doubleNorm(StringUtils.hash(key));
        return floatTree.floorEntry(hval).getValue();
    }
}
