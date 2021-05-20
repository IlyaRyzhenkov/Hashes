package Experiment;

import Algorithms.*;
import org.github.jamm.MemoryMeter;
import utils.INode;
import utils.Node;
import utils.StringUtils;

import java.util.ArrayList;
import java.util.Random;

public class BenchmarkingMemory {
    private final int[] nodesCount;

    private final Random random = new Random();
    private MemoryMeter meter = new MemoryMeter();

    private Consistent<Integer> consistent;
    private ModN<Integer> modn;
    private Jump<Integer> jump;
    private Rendezvous<Integer> rendezvous;
    private RandomSlicing<Integer> randomSlicing;

    private ArrayList<INode<Integer>> na1;
    private ArrayList<INode<Integer>> na2;
    private ArrayList<INode<Integer>> na3;
    private ArrayList<INode<Integer>> na4;
    private ArrayList<INode<Integer>> na5;

    public BenchmarkingMemory() {
        nodesCount = new int[]{5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 75, 100, 1000,
                5000, 10000, 20000, 30000, 40000, 50000, 75000, 100000, 200000, 300000, 400000, 500000};
    }

    public BenchmarkingMemory(int[] tests) {
        nodesCount = tests;
    }

    public void test() {
        for (int nc : nodesCount) {
            setUp(nc);
            StringBuilder builder = new StringBuilder();
            builder.append(nc).append("\n")
                    .append(meter.measureDeep(consistent)).append(" -Consistent\n")
                    .append(meter.measureDeep(modn)).append(" -ModN\n")
                    .append(meter.measureDeep(jump)).append(" -Jump\n")
                    .append(meter.measureDeep(rendezvous)).append(" -Rendezvous\n")
                    .append(meter.measureDeep(randomSlicing)).append(" -RandomSlicing\n\n");
            System.out.print(builder.toString());
        }
    }

    private void setUp(int nodesCount) {
        na1 = new ArrayList<>();
        na2 = new ArrayList<>();
        na3 = new ArrayList<>();
        na4 = new ArrayList<>();
        na5 = new ArrayList<>();
        String[] nodeNames = new String[nodesCount];

        for (int i = 0; i < nodesCount; i++) {
            String name = StringUtils.generateRandomString(5);
            nodeNames[i] = name;
            na1.add(new Node<>(name));
            na2.add(new Node<>(name));
            na3.add(new Node<>(name));
            na4.add(new Node<>(name));
            na5.add(new Node<>(name));
        }
        consistent = new Consistent<>(nodeNames, na1);
        modn = new ModN<>(nodeNames, na2);
        jump = new Jump<>(nodeNames, na3);
        rendezvous = new Rendezvous<>(nodeNames, na4);
        randomSlicing = new RandomSlicing<>(nodeNames, na5);
    }
}
