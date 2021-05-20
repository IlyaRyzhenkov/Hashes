package Experiment;

import utils.INode;
import utils.Node;

import java.util.ArrayList;

public class AddNodeExpInfo {
    public int dataReplaced;
    public ArrayList<Integer> dataList = new ArrayList<>();
    public ArrayList<INode<Integer>> nodes;

    private long AllSum = 0;
    private long AllCount = 0;

    public void setUp() {
        for (int d : dataList)
            AllSum += d;
        AllCount += dataList.size();
        dataReplaced = 0;
        dataList = new ArrayList<>();
        nodes = new ArrayList<>();
    }

    public void addNode(String nodeName) {
        nodes.add(new Node<Integer>(nodeName));
    }

    public void updateDataList() {
        dataList.add(dataReplaced);
    }

    public double getAverage() {
        return AllSum / (double)AllCount;
    }
}
