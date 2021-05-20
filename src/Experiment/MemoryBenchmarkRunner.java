package Experiment;

public class MemoryBenchmarkRunner {
    public static void main(String[] args) {
        int[] tests = {1000000};
        BenchmarkingMemory benchmark = new BenchmarkingMemory(tests);
        benchmark.test();
    }
}
