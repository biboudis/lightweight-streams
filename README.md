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
*TL;DR* 

With large MaxInlineLevel, LStreams are slightly better in the 
cartesian product case, they are considerably slower in the sort case 
due to the sorting algorithm and in all other cases, numbers are similar.

You can take a look at the most current measurements, that include baseline, 
LStreams and Java 8 Streams in the [measurements](measurements) file. 
 
### References
* [Clash of the Lambdas](http://biboudis.github.io/clashofthelambdas/)
* [Nessos/Streams](https://github.com/nessos/Streams)
* j.u.stream [code](http://hg.openjdk.java.net/jdk9/jdk9/jdk/file/tip/src/java.base/share/classes/java/util/stream) / [docs](http://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html)

### Dependencies
* [JMH](http://openjdk.java.net/projects/code-tools/jmh/)
* [junit](http://junit.org/)
* [Guava](https://code.google.com/p/guava-libraries/)
