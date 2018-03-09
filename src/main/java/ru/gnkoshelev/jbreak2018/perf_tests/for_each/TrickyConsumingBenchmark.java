package ru.gnkoshelev.jbreak2018.perf_tests.for_each;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by kgn on 09.03.2018.
 */
@Fork(value = 1, warmups = 0)
@Warmup(iterations = 5, time = 2_000, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 2_000, timeUnit = TimeUnit.MILLISECONDS)
@OutputTimeUnit(value = TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
public class TrickyConsumingBenchmark {
    @Param(value = {"100", "10000"})
    public int N;

    private List<Integer> values;

    @Setup
    public void setup() {
        Random rand = new Random(12345);

        int size = N;
        values = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            values.add(rand.nextInt());
        }
    }

    @Benchmark
    public void forEachListBenchmark() {
        forEachList(values);
    }

    @Benchmark
    public void forEachStreamBenchmark() {
        forEachStream(values);
    }

    public void forEachList(List<Integer> values) {
        values.forEach(TrickyConsumer::consume);
    }

    public void forEachStream(List<Integer> values) {
        values.stream().forEach(TrickyConsumer::consume);
    }

    public static class TrickyConsumer {
        public static Integer value;

        public static void consume(Integer value) {
            if (! (value % 100 == 0)) {
                TrickyConsumer.value = value;
            }
        }
    }
}
