package benchmarks;

import org.openjdk.jmh.annotations.*;
import streams.LStream;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**

mvn -DskipTests clean package

java -Xmx1g -XX:-TieredCompilation -XX:MaxInlineLevel=12 -Dbenchmark.N=1000000 -jar target/microbenchmarks.jar -wi 15 -i 10 -f 1 -gc -tu ms ".*"

 */

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
public class Benchmark_SimpleBoxedPipelines {

    static final int N =  Integer.getInteger("benchmark.N", 1000);
    static Long[] v, v1,v2, v_forSorting_Baseline ,v_forSorting_LStreams, v_forSorting_Java8Streams;

    static {
        // Ok, lets use IntStream for this :P
        v  = IntStream.range(0, N).mapToObj(i -> new Long(i % 1000)).toArray(Long[]::new);
        v1 = IntStream.range(0, 1000).mapToObj(i -> new Long(i % 10)).toArray(Long[]::new);
        v2 = IntStream.range(0, 100).mapToObj(i -> new Long(i % 10)).toArray(Long[]::new);
        v_forSorting_Baseline  = IntStream.range(0, N).mapToObj(i -> new Long(i % 1000)).toArray(Long[]::new);
        v_forSorting_LStreams  = IntStream.range(0, N).mapToObj(i -> new Long(i % 1000)).toArray(Long[]::new);
        v_forSorting_Java8Streams  = IntStream.range(0, N).mapToObj(i -> new Long(i % 1000)).toArray(Long[]::new);
    }

    @Benchmark
    public Long cart_Baseline() {
        Long cart = 0L;
        for (int d = 0 ; d < v1.length ; d++) {
            for (int dp = 0 ; dp < v2.length ; dp++){
                cart += v1[d] * v2[dp];
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
    public Long map_Megamorphic_Baseline() {
        Long acc = 0L;
        for (int i =0 ; i < v.length ; i++) {
            acc += (((((((v[i] + 2L) + 2L) + 2L) + 2L) + 2L) + 2L));
        }
        return acc;
    }

    @Benchmark
    public Long[] sort_Baseline(){
        Arrays.sort(v_forSorting_Baseline);
        return v_forSorting_Baseline;
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
        long cart = Stream.of(v1)
                .flatMap(d -> Stream.of(v2).map(dP -> dP * d))
                .reduce(0L, Long::sum);
        return cart;
    }

    @Benchmark
    public Long cart_LStreams() {
        long cart = LStream.of(v1)
                .flatMap(d -> LStream.of(v2).<Long>map(dP -> dP * d))
                .reduce(0L, Long::sum);
        return cart;
    }

    @Benchmark
    public Long map_Megamorphic_LStreams(){
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
    public Long map_Megamorphic_Java8Streams(){
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
}
