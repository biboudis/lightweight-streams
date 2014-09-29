package benchmarks;

import org.openjdk.jmh.annotations.*;
import streams.LongLStream;

import java.util.Arrays;
import java.util.function.LongUnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**

mvn -DskipTests clean package

java -Xmx1g -XX:-TieredCompilation -XX:MaxInlineLevel=12 -Dbenchmark.N=1000000 -Dbenchmark.F=300000000 -jar target/microbenchmarks.jar -wi 15 -i 10 -f 1 -gc -tu ms ".*"

 */

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
public class Benchmark_SimplePrimitivePipelines {

    private static int N =  Integer.getInteger("benchmark.N", 1000);
    private static int F =  Integer.getInteger("benchmark.F", 1000);

    public long[] v, v1,v2, v_forSorting_Baseline ,v_forSorting_LStreams, v_forSorting_Java8Streams;
    public int[] v_for_megamorphic_filter;

    @Setup
    public void setUp() {
        // Ok, lets use IntStream for this :P
        v  = IntStream.range(0, N).mapToLong(i -> i % 1000).toArray();
        v1 = IntStream.range(0, 100000).mapToLong(i -> i % 10).toArray();
        v2 = IntStream.range(0, 10).mapToLong(i -> i % 10).toArray();
        v_forSorting_Baseline  = IntStream.range(0, N).mapToLong(i -> i % 1000).toArray();
        v_forSorting_LStreams  = IntStream.range(0, N).mapToLong(i -> i % 1000).toArray();
        v_forSorting_Java8Streams  = IntStream.range(0, N).mapToLong(i -> i % 1000).toArray();
        v_for_megamorphic_filter = IntStream.range(0, F).toArray();
    }

    @Benchmark
    public long cart_Baseline() {
        long cart = 0L;
        for (int d = 0 ; d < v1.length ; d++) {
            for (int dp = 0 ; dp < v2.length ; dp++){
                cart += v1[d] * v2[dp];
            }
        }
        return cart;
    }
    @Benchmark
    public long map_filter_fold_Baseline() {
        long acc = 0L;
        for (int i = 0 ; i < v.length ; i++) {
            if (v[i] % 2L == 0L)
                acc += v[i] + 2L;
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
        for (int i = 0 ; i < v.length ; i++) {
            acc += (v[i] + 2L + 2L + 2L + 2L + 2L + 2L);
        }
        return acc;
    }

    @Benchmark
    public long[] sort_Baseline(){
        Arrays.sort(v_forSorting_Baseline);
        return v_forSorting_Baseline;
    }

    @Benchmark
    public long map_filter_fold_Java8Streams() {
        long sum = LongStream.of(v)
                .filter(x -> x % 2L == 0L)
                .map(x -> x + 2L)
                .reduce(0L, Long::sum);
        return sum;
    }

    @Benchmark
    public long map_filter_fold_LStreams() {
        long sum = LongLStream.of(v)
                .filter(x -> x % 2L == 0L)
                .map(x -> x + 2L)
                .reduce(0L, Long::sum);
        return sum;
    }

    @Benchmark
    public long cart_Java8Streams() {
        long cart = LongStream.of(v1)
                .flatMap(d -> LongStream.of(v2).map(dP -> dP * d))
                .reduce(0L, Long::sum);
        return cart;
    }

    @Benchmark
    public long cart_LStreams() {
        long cart = LongLStream.of(v1)
                .flatMap(d -> LongLStream.of(v2).map(dP -> dP * d))
                .reduce(0L, Long::sum);
        return cart;
    }

    @Benchmark
    public long map_megamorphic_LStreams_2(){
        long sum = LongLStream.of(v)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .reduce(0L, Long::sum);
        return sum;
    }

    @Benchmark
    public long map_megamorphic_LStreams_4(){
        long sum = LongLStream.of(v)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .reduce(0L, Long::sum);
        return sum;
    }

    @Benchmark
    public long map_megamorphic_LStreams_6(){
        long sum = LongLStream.of(v)
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
        long sum = LongLStream.of(v)
                .map(lambda)
                .map(lambda)
                .reduce(0L, Long::sum);
        return sum;
    }

    @Benchmark
    public long map_megamorphic_LStreams_single_lambda_4(){
        LongUnaryOperator lambda = x -> x + 2L;
        long sum = LongLStream.of(v)
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
        long sum = LongLStream.of(v)
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
        long sum = LongStream.of(v)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .reduce(0L, Long::sum);
        return sum;
    }

    @Benchmark
    public long map_megamorphic_Java8Streams_4(){
        long sum = LongStream.of(v)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .map(x -> x + 2L)
                .reduce(0L, Long::sum);
        return sum;
    }

    @Benchmark
    public long map_megamorphic_Java8Streams_6(){
        long sum = LongStream.of(v)
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
        long sum = LongStream.of(v)
                .map(lambda)
                .map(lambda)
                .reduce(0L, Long::sum);
        return sum;
    }

    @Benchmark
    public long map_megamorphic_Java8Streams_single_lambda_4(){
        LongUnaryOperator lambda = x -> x + 2L;
        long sum = LongStream.of(v)
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
        long sum = LongStream.of(v)
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
}
