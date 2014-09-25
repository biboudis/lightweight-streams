## Lightweight Streams for Java

[![Build Status](https://travis-ci.org/biboudis/lightweight-streams.svg?branch=master)](https://travis-ci.org/biboudis/lightweight-streams)

This library employs the same pattern that the standard Java 8 library does but
without iterators/spliterators and is based on lambdas/continuation-passing
only. The programming model consists of combinators that wrap a source with a
datatype which implements the ```LStream``` interface. Any combinators that
are applied, effectively declare the transformations needed to be applied when
an eager combinator is met. As in Java, boxing is avoided with hand specialized
combinators (e.g. ```LongLStream```).

``` source - lazy - lazy - lazy - eager ```

The purpose of this library is to help with the study of the inlining decisions
that the JVM makes, when following the j.u.stream implementation strategy.

### Test
```shell 
# run unit tests
mvn -q test

# build benchmarks über-jar
mvn clean package -Dskiptests

# run benchmarks
java -XX:-TieredCompilation -jar target/microbenchmarks.jar -wi 15 -i 10 -f 1 -gc -tu ms ".*"
```
### Measurements
```
java -Xmx1g -XX:-TieredCompilation -XX:MaxInlineLevel=100 -Dbenchmark.N=1000000 -jar target/microbenchmarks.jar -wi 5 -i 5 -f 1 -gc -tu ms ".*"

b.Benchmark_SimpleBoxedPipelines.cart_Baseline                       avgt        5   16.542        2.680  ms/op
b.Benchmark_SimpleBoxedPipelines.cart_Java8Streams                   avgt        5   37.271        0.581  ms/op
b.Benchmark_SimpleBoxedPipelines.cart_LStreams                       avgt        5   32.806        3.898  ms/op
b.Benchmark_SimpleBoxedPipelines.map_Megamorphic_Baseline            avgt        5   12.791        2.292  ms/op
b.Benchmark_SimpleBoxedPipelines.map_Megamorphic_Java8Streams        avgt        5   91.436        0.952  ms/op
b.Benchmark_SimpleBoxedPipelines.map_Megamorphic_LStreams            avgt        5   90.283        3.149  ms/op
b.Benchmark_SimpleBoxedPipelines.map_filter_fold_Baseline            avgt        5   15.358        0.469  ms/op
b.Benchmark_SimpleBoxedPipelines.map_filter_fold_Java8Streams        avgt        5   19.595        2.528  ms/op
b.Benchmark_SimpleBoxedPipelines.map_filter_fold_LStreams            avgt        5   22.807        0.976  ms/op
b.Benchmark_SimpleBoxedPipelines.sort_Baseline                       avgt        5   15.452        1.808  ms/op
b.Benchmark_SimpleBoxedPipelines.sort_Java8Streams                   avgt        5  131.353       28.166  ms/op
b.Benchmark_SimpleBoxedPipelines.sort_LStreams                       avgt        5  163.719        7.499  ms/op
b.Benchmark_SimplePrimitivePipelines.cart_Baseline                   avgt        5    4.488        0.526  ms/op
b.Benchmark_SimplePrimitivePipelines.cart_Java8Streams               avgt        5   17.346        1.508  ms/op
b.Benchmark_SimplePrimitivePipelines.cart_LStreams                   avgt        5    9.920        0.283  ms/op
b.Benchmark_SimplePrimitivePipelines.map_Megamorphic_Baseline        avgt        5    2.183        0.401  ms/op
b.Benchmark_SimplePrimitivePipelines.map_Megamorphic_Java8Streams    avgt        5   51.744        1.627  ms/op
b.Benchmark_SimplePrimitivePipelines.map_Megamorphic_LStreams        avgt        5   59.144        8.579  ms/op
b.Benchmark_SimplePrimitivePipelines.map_filter_fold_Baseline        avgt        5    5.733        0.625  ms/op
b.Benchmark_SimplePrimitivePipelines.map_filter_fold_Java8Streams    avgt        5    7.531        1.100  ms/op
b.Benchmark_SimplePrimitivePipelines.map_filter_fold_LStreams        avgt        5    7.616        0.492  ms/op
b.Benchmark_SimplePrimitivePipelines.sort_Baseline                   avgt        5   34.019        0.565  ms/op
b.Benchmark_SimplePrimitivePipelines.sort_Java8Streams               avgt        5   88.508        3.115  ms/op
b.Benchmark_SimplePrimitivePipelines.sort_LStreams                   avgt        5  176.369       14.164  ms/op
```
 
### References
* [Clash of the Lambdas](http://biboudis.github.io/clashofthelambdas/)
* [Nessos/Streams](https://github.com/nessos/Streams)
* j.u.stream [code](http://hg.openjdk.java.net/jdk9/jdk9/jdk/file/tip/src/java.base/share/classes/java/util/stream) / [docs](http://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html)

### Dependencies
* [JMH](http://openjdk.java.net/projects/code-tools/jmh/)
* [junit](http://junit.org/)
