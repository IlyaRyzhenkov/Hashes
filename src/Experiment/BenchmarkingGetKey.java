package Experiment;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import Algorithms.*;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import utils.INode;
import utils.Node;
import utils.StringUtils;


@Warmup(iterations = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class BenchmarkingGetKey {

    private final int nodesCount = 50;
    private final int keysCount = 10000;
    private String[] nodeNames;
    private String[] keys;

    private final Random random = new Random();
    private Test consistent;
    private Test modn;
    private Test jump;
    private Test rendezvous;
    private Test randomSlicing;

    private ArrayList<INode<Integer>> na1;
    private ArrayList<INode<Integer>> na2;
    private ArrayList<INode<Integer>> na3;
    private ArrayList<INode<Integer>> na4;
    private ArrayList<INode<Integer>> na5;

    private String currentKey;

    @Setup(Level.Iteration)
    public void doFirstSetup() {

        nodeNames = new String[nodesCount];
        keys = new String[keysCount];

        na1 = new ArrayList<>();
        na2 = new ArrayList<>();
        na3 = new ArrayList<>();
        na4 = new ArrayList<>();
        na5 = new ArrayList<>();

        for (int i = 0; i < nodesCount; i++) {
            nodeNames[i] = "n"+i;
            Node<Integer> n1 = new Node<>(nodeNames[i]);
            Node<Integer> n2 = new Node<>(nodeNames[i]);
            Node<Integer> n3 = new Node<>(nodeNames[i]);
            Node<Integer> n4 = new Node<>(nodeNames[i]);
            Node<Integer> n5 = new Node<>(nodeNames[i]);

            na1.add(n1);
            na2.add(n2);
            na3.add(n3);
            na4.add(n4);
            na5.add(n5);
        }
        consistent = new Test(new Consistent<>(nodeNames, na1));
        modn = new Test(new ModN<>(nodeNames, na2));
        jump = new Test(new Jump<>(nodeNames, na3));
        rendezvous = new Test(new Rendezvous<>(nodeNames, na4));
        randomSlicing = new Test(new RandomSlicing<>(nodeNames, na5));

        for (int i = 0; i < keysCount; i++) {
            keys[i] = StringUtils.generateRandomString(8);
            int val = random.nextInt();
            consistent.testAddKey(keys[i], val);
            modn.testAddKey(keys[i], val);
            jump.testAddKey(keys[i], val);
            rendezvous.testAddKey(keys[i], val);
            randomSlicing.testAddKey(keys[i], val);
        }
    }

    @Setup(Level.Invocation)
    public void doSetup() {
        int i = random.nextInt(keysCount);
        currentKey = keys[i];
    }

    @Benchmark
    public void testGetKeyModN() {
        modn.testGetKey(currentKey);
    }

    @Benchmark
    public void testGetKeyConsistent() {
        consistent.testGetKey(currentKey);
    }

    @Benchmark
    public void testGetKeyJump() {
        jump.testGetKey(currentKey);
    }

    @Benchmark
    public void testGetKeyRendezvous() {
        rendezvous.testGetKey(currentKey);
    }

    @Benchmark
    public void testGetKeyRandomSlicing() {
        randomSlicing.testGetKey(currentKey);
    }

}
