package benchmarks;

import org.openjdk.jmh.annotations.*;
import streams.LStream;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
public class Benchmark_SimpleBoxedPipelines {

    // For map, count, operations
    private static int N =  Integer.getInteger("benchmark.N", 1000);
    // For filtering operations
    private static int F =  Integer.getInteger("benchmark.F", 1000);
    // For cartesian product operations
    private static int N_outer =  Integer.getInteger("benchmark.N_outer", 100);
    private static int N_inner =  Integer.getInteger("benchmark.N_inner", 10);

    public Long[] v, v_outer, v_inner, v_forSorting_Baseline ,v_forSorting_LStreams, v_forSorting_Java8Streams;
    public Long[] v_for_megamorphic_filter;

    public Long[] fillArray(int range){
        Long[] array = new Long[range];
        for (int i = 0; i < range; i++) {
            array[i] = i % 1000L;
        }
        return array;
    }

    @Setup
    public void setUp() {
        v  = fillArray(N);
        v_outer = fillArray(N_outer);
        v_inner = fillArray(N_inner);
        v_forSorting_Baseline = fillArray(N);
        v_forSorting_Java8Streams = fillArray(N);
        v_forSorting_LStreams = fillArray(N);
        v_for_megamorphic_filter = fillArray(F);
    }

    @Benchmark
    public Long cart_Baseline() {
        Long cart = 0L;
        for (int d = 0 ; d < v_outer.length ; d++) {
            for (int dp = 0 ; dp < v_inner.length ; dp++){
                cart += v_outer[d] * v_inner[dp];
            }
        }
        return cart;
    }
    @Benchmark
    public Long map_filter_fold_Baseline() {
        Long acc = 0L;
        for (int i =0 ; i < v.length ; i++) {
            if (v[i] % 2L == 0L)
                acc += v[i] + 2L;
        }
        return acc;
    }

    @Benchmark
    public Long map_megamorphic_Baseline_6() {
        Long acc = 0L;
        for (int i =0 ; i < v.length ; i++) {
            acc += (v[i] + 2L + 2L + 2L + 2L + 2L + 2L);
        }
        return acc;
    }

    @Benchmark
    public Long[] sort_copyOf() {
        return Arrays.copyOf(v_forSorting_Baseline, N);
    }

    @Benchmark
    public Long[] sort_Baseline(){
        Long[] copyOf = Arrays.copyOf(v_forSorting_Baseline, N);

        Arrays.sort(copyOf);

        return copyOf;
    }

    @Benchmark
    public Long map_filter_fold_Java8Streams() {
        Long sum = Stream.of(v)
                .filter(x -> x % 2L == 0L)
                .map(x -> x + 2L)
                .reduce(0L, Long::sum);
        return sum;
    }

    @Benchmark
    public Long map_filter_fold_LStreams() {
        Long sum = LStream.of(v)
                .filter(x -> x % 2L == 0L)
                .map(x -> x + 2L)
                .reduce(0L, Long::sum);

        return sum;
    }

    @Benchmark
    public Long cart_Java8Streams() {
        long cart = Stream.of(v_outer)
                .flatMap(d -> Stream.of(v_inner).map(dP -> dP * d))
                .reduce(0L, Long::sum);
        return cart;
    }

    @Benchmark
    public Long cart_LStreams() {
        long cart = LStream.of(v_outer)
                .flatMap(d -> LStream.of(v_inner).<Long>map(dP -> dP * d))
                .reduce(0L, Long::sum);
        return cart;
    }

    @Benchmark
    public Long map_megamorphic_LStreams_2(){
        Long sum = LStream.of(v)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .reduce(0L, Long::sum);
        return sum;
    }

    @Benchmark
    public Long map_megamorphic_LStreams_4(){
        Long sum = LStream.of(v)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .reduce(0L, Long::sum);
        return sum;
    }

    @Benchmark
    public Long map_megamorphic_LStreams_6(){
        Long sum = LStream.of(v)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .reduce(0L, Long::sum);
        return sum;
    }

    @Benchmark
    public Long map_megamorphic_LStreams_single_lambda_2(){
        UnaryOperator<Long> lambda = x -> x + 2L;
        Long sum = LStream.of(v)
                .map(lambda)
                .map(lambda)
                .reduce(0L, Long::sum);
        return sum;
    }

    @Benchmark
    public Long map_megamorphic_LStreams_single_lambda_4(){
        UnaryOperator<Long> lambda = x -> x + 2L;
        Long sum = LStream.of(v)
                .map(lambda)
                .map(lambda)
                .map(lambda)
                .map(lambda)
                .reduce(0L, Long::sum);
        return sum;
    }

    @Benchmark
    public Long map_megamorphic_LStreams_single_lambda_6(){
        UnaryOperator<Long> lambda = x -> x + 2L;
        Long sum = LStream.of(v)
                .map(lambda)
                .map(lambda)
                .map(lambda)
                .map(lambda)
                .map(lambda)
                .map(lambda)
                .reduce(0L, Long::sum);
        return sum;
    }

    @Benchmark
    public Long map_megamorphic_Java8Streams_2(){
        Long sum = Stream.of(v)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .reduce(0L, Long::sum);
        return sum;
    }

    @Benchmark
    public Long map_megamorphic_Java8Streams_4(){
        Long sum = Stream.of(v)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .reduce(0L, Long::sum);
        return sum;
    }

    @Benchmark
    public Long map_megamorphic_Java8Streams_6(){
        Long sum = Stream.of(v)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .reduce(0L, Long::sum);
        return sum;
    }

    @Benchmark
    public Long map_megamorphic_Java8Streams_single_lambda_2(){
        UnaryOperator<Long> lambda = x -> x + 2L;
        Long sum = Stream.of(v)
                .map(lambda)
                .map(lambda)
                .reduce(0L, Long::sum);
        return sum;
    }

    @Benchmark
    public Long map_megamorphic_Java8Streams_single_lambda_4(){
        UnaryOperator<Long> lambda = x -> x + 2L;
        Long sum = Stream.of(v)
                .map(lambda)
                .map(lambda)
                .map(lambda)
                .map(lambda)
                .reduce(0L, Long::sum);
        return sum;
    }

    @Benchmark
    public Long map_megamorphic_Java8Streams_single_lambda_6(){
        UnaryOperator<Long> lambda = x -> x + 2L;
        Long sum = Stream.of(v)
                .map(lambda)
                .map(lambda)
                .map(lambda)
                .map(lambda)
                .map(lambda)
                .map(lambda)
                .reduce(0L, Long::sum);
        return sum;
    }

    @Benchmark
    public Long[] sort_LStreams(){
        Long[] res = LStream.of(v_forSorting_LStreams)
                .sorted(Comparator.<Long>naturalOrder())
                .toArray(Long[]::new);
        return res;
    }

    @Benchmark
    public Long[] sort_Java8Streams(){
        Long[] res = Stream.of(v_forSorting_Java8Streams)
                .sorted(Comparator.<Long>naturalOrder())
                .toArray(Long[]::new);
        return res;
    }

    @Benchmark
    public long count_LStreams(){
        long res = LStream.of(v).count();
        return res;
    }

    @Benchmark
    public long count_Java8Streams(){
        long res = Stream.of(v).count();
        return res;
    }
}
