## Lightweight Streams for Java

[![Build Status](https://travis-ci.org/biboudis/lightweight-streams.svg?branch=master)](https://travis-ci.org/biboudis/lightweight-streams)

This library employs the same pattern that the standard Java 8 library does but
without iterators/spliterators and is based on lambdas/continuation-passing
only. The programming model consists of combinators that wrap a source with a
datatype which implements the ```LStream``` interface. Any combinators that
are applied, effectively declare the transformations needed to be applied when
an eager combinator is met. 

``` source - lazy - lazy - lazy - eager ```

As in Java, boxing is avoided with hand specialized
combinators (e.g. ```LongLStream```). 

The purpose of this library is to make the study of the lambda inlining decisions
that the JVM makes easier.

### Test
```shell 
# run unit tests
mvn -q test

# build benchmarks über-jar
mvn clean package -Dskiptests

# run benchmarks
java -Xmx2g -XX:-TieredCompilation -XX:MaxInlineLevel=12 \
     -Dbenchmark.N=1000000 \
     -Dbenchmark.F=3000000 \
     -Dbenchmark.N_outer=10000 \
     -Dbenchmark.N_inner=10 \
     -jar target/microbenchmarks.jar \
     -wi 5 -i 5 -f 1 -gc -tu ms ".*"
```
### Measurements
*in-progress* 

In C2, with large MaxInlineLevel, LStreams are slightly better in the 
cartesian product case, they are considerably slower in the sort case 
due to the sorting algorithm and in all other cases, numbers are similar.

In Graal, all megamorphic boxed/primitive LStreams with one lambda 
run 50-100% faster. Nearly all megamorphic primitive LStreams run 
considerably faster!

You can take a look at the most current measurements, that include baseline, 
LStreams and Java 8 Streams in the measurements files for both C2 and Graal: 

* [C2 benchmarks](measurements-c2)
* [Graal benchmarks](measurements-graal)
* and in [spreadsheet](measurements.ods)
 
### References
* Inspired by our work on [Clash of the Lambdas](http://biboudis.github.io/clashofthelambdas/)
* [Nessos/Streams](https://github.com/nessos/Streams) in F#
* [sml-streams](https://github.com/biboudis/sml-streams) in Standard-ML on MLton
* [Graal](http://www.oracle.com/technetwork/oracle-labs/program-languages/overview/index.html)
* j.u.stream [code](http://hg.openjdk.java.net/jdk9/jdk9/jdk/file/tip/src/java.base/share/classes/java/util/stream) / [docs](http://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html)

### Dependencies
* [JMH](http://openjdk.java.net/projects/code-tools/jmh/)
* [junit](http://junit.org/)
* [Guava](https://code.google.com/p/guava-libraries/)
