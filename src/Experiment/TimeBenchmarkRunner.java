package Experiment;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class TimeBenchmarkRunner {
    public static void main(String[] args) throws RunnerException {
        TestType mode = TestType.RemoveNode;
        Options opt;
        switch (mode){
            case GetKey:
                opt = new OptionsBuilder().include(BenchmarkingGetKey.class.getSimpleName()).forks(1).build();
                new Runner(opt).run();
                break;
            case AddNode:
                opt = new OptionsBuilder().include(BenchmarkingAddNode.class.getSimpleName()).forks(1).build();
                new Runner(opt).run();
                break;
            case RemoveNode:
                opt = new OptionsBuilder().include(BenchmarkingRemoveNode.class.getSimpleName()).forks(1).build();
                new Runner(opt).run();
                break;
        }
    }
}

