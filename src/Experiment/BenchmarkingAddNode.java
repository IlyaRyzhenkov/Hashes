package Experiment;

import Algorithms.*;
import org.openjdk.jmh.annotations.*;
import utils.INode;
import utils.Node;
import utils.StringUtils;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class BenchmarkingAddNode {
    private final int nodesCount = 99;
    private final int keysCount = 1000000;

    private final Random random = new Random();
    private String[] nodeNames;
    private String[] keys;

    private Consistent<Integer> consistent;
    private ModN<Integer> modn;
    private Jump<Integer> jump;
    private Rendezvous<Integer> rendezvous;
    private RandomSlicing<Integer> randomSlicing;

    private AddNodeExpInfo consistentInfo = new AddNodeExpInfo();
    private AddNodeExpInfo modnInfo = new AddNodeExpInfo();
    private AddNodeExpInfo jumpInfo = new AddNodeExpInfo();
    private AddNodeExpInfo rendezvousInfo = new AddNodeExpInfo();
    private AddNodeExpInfo randomSlicingInfo = new AddNodeExpInfo();

    private String currentNodeName;

    @Setup(Level.Iteration)
    public void doFirstSetup() {
        nodeNames = new String[nodesCount];
        keys = new String[keysCount];

        consistentInfo.setUp();
        modnInfo.setUp();
        jumpInfo.setUp();
        rendezvousInfo.setUp();
        randomSlicingInfo.setUp();

        for (int i = 0; i < nodesCount; i++) {
            nodeNames[i] = "n"+i;
            consistentInfo.addNode(nodeNames[i]);
            modnInfo.addNode(nodeNames[i]);
            jumpInfo.addNode(nodeNames[i]);
            rendezvousInfo.addNode(nodeNames[i]);
            randomSlicingInfo.addNode(nodeNames[i]);
        }
        consistent = new Consistent<>(nodeNames, consistentInfo.nodes);
        modn = new ModN<>(nodeNames, modnInfo.nodes);
        jump = new Jump<>(nodeNames, jumpInfo.nodes);
        rendezvous = new Rendezvous<>(nodeNames, rendezvousInfo.nodes);
        randomSlicing = new RandomSlicing<>(nodeNames, randomSlicingInfo.nodes);

        for (int i = 0; i < keysCount; i++) {
            keys[i] = StringUtils.generateRandomString(8);
            int val = random.nextInt();
            consistent.addKey(keys[i], val);
            modn.addKey(keys[i], val);
            jump.addKey(keys[i], val);
            rendezvous.addKey(keys[i], val);
            randomSlicing.addKey(keys[i], val);
        }
    }

    @Setup(Level.Invocation)
    public void doSetup() {
        currentNodeName = StringUtils.generateRandomString(5);
    }

    @TearDown(Level.Invocation)
    public void doTearDown() {
        consistentInfo.dataReplaced = consistent.getDataReplaced();
        modnInfo.dataReplaced = modn.getDataReplaced();
        jumpInfo.dataReplaced = jump.getDataReplaced();
        rendezvousInfo.dataReplaced = rendezvous.getDataReplaced();
        randomSlicingInfo.dataReplaced = randomSlicing.getDataReplaced();

        if (consistentInfo.dataReplaced > 0) {
            consistentInfo.updateDataList();
            consistent.removeNode(currentNodeName);
        }
        if (modnInfo.dataReplaced > 0) {
            modnInfo.updateDataList();
            modn.removeNode(modn.getNumberOfNodes() - 1);
        }
        if (jumpInfo.dataReplaced > 0) {
            jumpInfo.updateDataList();
            jump.removeNode();
        }
        if (rendezvousInfo.dataReplaced > 0) {
            rendezvousInfo.updateDataList();
            rendezvous.removeNode(currentNodeName);
        }
        if (randomSlicingInfo.dataReplaced > 0) {
            randomSlicingInfo.updateDataList();
            randomSlicing.removeNode(currentNodeName);
        }
    }

    @TearDown(Level.Trial)
    public void doLastTearDown() {
        System.out.print("Average keys moved:\n");
        if (!Double.isNaN(consistentInfo.getAverage())) {
            System.out.print("Consistent: " + consistentInfo.getAverage() + "\n");
        }
        if (!Double.isNaN(modnInfo.getAverage())) {
            System.out.print("ModN: " + modnInfo.getAverage() + "\n");
        }
        if (!Double.isNaN(jumpInfo.getAverage())) {
            System.out.print("Jump: " + jumpInfo.getAverage() + "\n");
        }
        if (!Double.isNaN(rendezvousInfo.getAverage())) {
            System.out.print("Rendezvous: " + rendezvousInfo.getAverage() + "\n");
        }
        if (!Double.isNaN(randomSlicingInfo.getAverage())) {
            System.out.print("Random Slicing: " + randomSlicingInfo.getAverage() + "\n");
        }
    }

    @Benchmark
    public void testAddNodeConsistent() {
        Node<Integer> n = new Node<>(currentNodeName);
        consistent.addNode(currentNodeName, n);
    }
    @Benchmark
    public void testAddNodeModN() {
        Node<Integer> n = new Node<>(currentNodeName);
        modn.addNode(currentNodeName, n);
    }
    @Benchmark
    public void testAddNodeJump() {
        Node<Integer> n = new Node<>(currentNodeName);
        jump.addNode(currentNodeName, n);
    }
    @Benchmark
    public void testAddNodeRendezvous() {
        Node<Integer> n = new Node<>(currentNodeName);
        rendezvous.addNode(currentNodeName, n);
    }
    @Benchmark
    public void testAddNodeRandomSlicing() {
        Node<Integer> n = new Node<>(currentNodeName);
        randomSlicing.addNode(currentNodeName, n);
    }
}
