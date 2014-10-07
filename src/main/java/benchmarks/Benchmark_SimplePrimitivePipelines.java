package benchmarks;

import org.openjdk.jmh.annotations.*;
import streams.IntLStream;
import streams.LongLStream;

import java.util.Arrays;
import java.util.function.LongUnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
public class Benchmark_SimplePrimitivePipelines {

    // For map, count, operations
    private static int N =  Integer.getInteger("benchmark.N", 1000);
    // For filtering operations
    private static int F =  Integer.getInteger("benchmark.F", 1000);
    // For cartesian product operations
    private static int N_outer =  Integer.getInteger("benchmark.N_outer", 100);
    private static int N_inner =  Integer.getInteger("benchmark.N_inner", 10);

    public long[] v_longs, v_outer, v_inner, v_forSorting_Baseline ,v_forSorting_LStreams, v_forSorting_Java8Streams;
    public int[] v_ints, v_for_megamorphic_filter;

    public int[] fillIntArray(int range){
        int[] array = new int[range];
        for (int i = 0; i < range; i++) {
            array[i] = i % 1000;
        }
        return array;
    }

    public long[] fillLongArray(int range){
        long[] array = new long[range];
        for (int i = 0; i < range; i++) {
            array[i] = i % 1000;
        }
        return array;
    }

    @Setup
    public void setUp() {
        v_longs = fillLongArray(N);
        v_ints  = fillIntArray(N);
        v_outer = fillLongArray(N_outer);
        v_inner = fillLongArray(N_inner);
        v_forSorting_Baseline = fillLongArray(N);
        v_forSorting_Java8Streams = fillLongArray(N);
        v_forSorting_LStreams = fillLongArray(N);
        v_for_megamorphic_filter = fillIntArray(F);
    }

    @Benchmark
    public long cart_Baseline() {
        long cart = 0L;
        for (int d = 0 ; d < v_outer.length ; d++) {
            for (int dp = 0 ; dp < v_inner.length ; dp++){
                cart += v_outer[d] * v_inner[dp];
            }
        }
        return cart;
    }
    @Benchmark
    public long map_filter_fold_Baseline() {
        long acc = 0L;
        for (int i = 0 ; i < v_longs.length ; i++) {
            if (v_longs[i] % 2L == 0L)
                acc += v_longs[i] + 2L;
        }
        return acc;
    }

    @Benchmark
    public long filter_megamorphic_Baseline_6() {
        long acc = 0L;
        for (int i = 0 ; i < v_for_megamorphic_filter.length ; i++) {
            if (v_for_megamorphic_filter[i] > 10
                    && v_for_megamorphic_filter[i] > 11
                    && v_for_megamorphic_filter[i] > 12
                    && v_for_megamorphic_filter[i] > 13
                    && v_for_megamorphic_filter[i] > 14
                    && v_for_megamorphic_filter[i] > 15 )
                acc ++;
        }
        return acc;
    }

    @Benchmark
    public long map_megamorphic_Baseline_6() {
        long acc = 0L;
        for (int i = 0 ; i < v_longs.length ; i++) {
            acc += (v_longs[i] + 2L + 2L + 2L + 2L + 2L + 2L);
        }
        return acc;
    }

    @Benchmark
    public long[] sort_copyOf() {
        return Arrays.copyOf(v_forSorting_Baseline, N);
    }

    @Benchmark
    public long[] sort_Baseline(){
        long[] copyOf = Arrays.copyOf(v_forSorting_Baseline, N);

        Arrays.sort(copyOf);

        return copyOf;
    }

    @Benchmark
    public long map_filter_fold_Java8Streams() {
        long sum = LongStream.of(v_longs)
                .filter(x -> x % 2L == 0L)
                .map(x -> x + 2L)
                .reduce(0L, Long::sum);
        return sum;
    }

    @Benchmark
    public long map_filter_fold_LStreams() {
        long sum = LongLStream.of(v_longs)
                .filter(x -> x % 2L == 0L)
                .map(x -> x + 2L)
                .reduce(0L, Long::sum);
        return sum;
    }

    @Benchmark
    public long cart_Java8Streams() {
        long cart = LongStream.of(v_outer)
                .flatMap(d -> LongStream.of(v_inner).map(dP -> dP * d))
                .reduce(0L, Long::sum);
        return cart;
    }

    @Benchmark
    public long cart_LStreams() {
        long cart = LongLStream.of(v_outer)
                .flatMap(d -> LongLStream.of(v_inner).map(dP -> dP * d))
                .reduce(0L, Long::sum);
        return cart;
    }

    @Benchmark
    public long map_megamorphic_LStreams_2(){
        long sum = LongLStream.of(v_longs)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .reduce(0L, Long::sum);
        return sum;
    }

    @Benchmark
    public long map_megamorphic_LStreams_4(){
        long sum = LongLStream.of(v_longs)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .reduce(0L, Long::sum);
        return sum;
    }

    @Benchmark
    public long map_megamorphic_LStreams_6(){
        long sum = LongLStream.of(v_longs)
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
    public long map_megamorphic_LStreams_single_lambda_2(){
        LongUnaryOperator lambda = x -> x + 2L;
        long sum = LongLStream.of(v_longs)
                .map(lambda)
                .map(lambda)
                .reduce(0L, Long::sum);
        return sum;
    }

    @Benchmark
    public long map_megamorphic_LStreams_single_lambda_4(){
        LongUnaryOperator lambda = x -> x + 2L;
        long sum = LongLStream.of(v_longs)
                .map(lambda)
                .map(lambda)
                .map(lambda)
                .map(lambda)
                .reduce(0L, Long::sum);
        return sum;
    }

    @Benchmark
    public long map_megamorphic_LStreams_single_lambda_6(){
        LongUnaryOperator lambda = x -> x + 2L;
        long sum = LongLStream.of(v_longs)
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
    public long map_megamorphic_Java8Streams_2(){
        long sum = LongStream.of(v_longs)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .reduce(0L, Long::sum);
        return sum;
    }

    @Benchmark
    public long map_megamorphic_Java8Streams_4(){
        long sum = LongStream.of(v_longs)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .reduce(0L, Long::sum);
        return sum;
    }

    @Benchmark
    public long map_megamorphic_Java8Streams_6(){
        long sum = LongStream.of(v_longs)
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
    public long map_megamorphic_Java8Streams_single_lambda_2(){
        LongUnaryOperator lambda = x -> x + 2L;
        long sum = LongStream.of(v_longs)
                .map(lambda)
                .map(lambda)
                .reduce(0L, Long::sum);
        return sum;
    }

    @Benchmark
    public long map_megamorphic_Java8Streams_single_lambda_4(){
        LongUnaryOperator lambda = x -> x + 2L;
        long sum = LongStream.of(v_longs)
                .map(lambda)
                .map(lambda)
                .map(lambda)
                .map(lambda)
                .reduce(0L, Long::sum);
        return sum;
    }

    @Benchmark
    public long map_megamorphic_Java8Streams_single_lambda_6(){
        LongUnaryOperator lambda = x -> x + 2L;
        long sum = LongStream.of(v_longs)
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
    public long[] sort_LStreams(){
        long[] res = LongLStream.of(v_forSorting_LStreams)
                .sorted()
                .toArray();
        return res;
    }

    @Benchmark
    public long[] sort_Java8Streams(){
        long[] res = LongStream.of(v_forSorting_Java8Streams)
                .sorted()
                .toArray();
        return res;
    }

    @Benchmark
    public long filter_megamorphic_Java8Streams_2(){
        long res = IntStream.of(v_for_megamorphic_filter)
                .filter(x->x>10)
                .filter(x->x>11)
                .count();
        return res;
    }

    @Benchmark
    public long filter_megamorphic_Java8Streams_4(){
        long res = IntStream.of(v_for_megamorphic_filter)
                .filter(x->x>10)
                .filter(x->x>11)
                .filter(x->x>12)
                .filter(x->x>13)
                .count();
        return res;
    }

    @Benchmark
    public long filter_megamorphic_Java8Streams_6(){
        long res = IntStream.of(v_for_megamorphic_filter)
                .filter(x->x>10)
                .filter(x->x>11)
                .filter(x->x>12)
                .filter(x->x>13)
                .filter(x->x>14)
                .filter(x->x>15)
                .count();
        return res;
    }

    @Benchmark
    public long count_PrimitiveLong_LStreams(){
        long res = LongLStream.of(v_longs).count();
        return res;
    }

    @Benchmark
    public long count_PrimitiveInt_LStreams(){
        long res = IntLStream.of(v_ints).count();
        return res;
    }

    @Benchmark
    public long count_PrimitiveLong_Java8Streams(){
        long res = LongStream.of(v_longs).count();
        return res;
    }

    @Benchmark
    public long count_PrimitiveInt_Java8Streams(){
        long res = IntStream.of(v_ints).count();
        return res;
    }
}
