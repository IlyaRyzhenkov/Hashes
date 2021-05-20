package Experiment;

import Algorithms.ResizableHash;
import utils.INode;

public class Test {
    private ResizableHash<Integer> controller;

    public Test(ResizableHash<Integer> c) {
        setUp(c);
    }

    public void setUp(ResizableHash<Integer> c) {
        controller = c;
    }

    public void testAddNode(String nodeName, INode<Integer> node) {
        controller.addNode(nodeName, node);
    }

    public void testGetKey(String key) {
        controller.getKey(key);
    }

    public void testAddKey(String key, int value) {
        controller.addKey(key, value);
    }
}
