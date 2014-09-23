package benchmarks;

import org.openjdk.jmh.annotations.*;
import streams.LStream;
import streams.LongLStream;

import java.util.Comparator;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**

mvn -DskipTests clean package

java -Xmx1g -XX:-TieredCompilation -XX:MaxInlineLevel=12 -Dbenchmark.N=1000000 -jar target/microbenchmarks.jar -wi 15 -i 10 -f 1 -gc -tu ms ".*"

 */

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
public class Benchmark_SimplePrimitivePipelines {

    static final int N =  Integer.getInteger("benchmark.N", 1000);
    static long[] v, v1,v2;

    static {
        // Ok, lets use IntStream for this :P
        v  = IntStream.range(0, N).mapToLong(i -> i % 1000).toArray();
        v1 = IntStream.range(0, 1000).mapToLong(i -> i % 10).toArray();
        v2 = IntStream.range(0, 100).mapToLong(i -> i % 10).toArray();
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
        for (int i =0 ; i < v.length ; i++) {
            if (v[i] % 2 == 0)
                acc += v[i] * v[i];
        }
        return acc;
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
    public long map_Megamorphic_LStreams(){
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
    public long map_Megamorphic_Java8Streams(){
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
    public long[] sort_LStreams(){
        long[] res = LongLStream.of(v)
                .sorted()
                .toArray();

        return res;
    }

    @Benchmark
    public long[] sort_Java8Streams(){
        long[] res = LongStream.of(v)
                .sorted()
                .toArray();

        return res;
    }
}
