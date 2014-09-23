## Lightweight Streams for Java

This library employs the same pattern that the standard Java 8 library does but
without iterators/spliterators and is based on lambdas/continuation-passing
only. The programming model consists of combinators that wrap a source with a
datatype which implements the ```LStream``` interface. Any combinators that
are applied, effectively declare the transformations needed to be applied when
an eager combinator is met.

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
    
### References
* [Clash of the Lambdas](http://biboudis.github.io/clashofthelambdas/)
* [Nessos/Streams](https://github.com/nessos/Streams)
* j.u.stream [code](http://hg.openjdk.java.net/jdk9/jdk9/jdk/file/tip/src/java.base/share/classes/java/util/stream) / [docs](http://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html)

### Dependencies
* [JMH](http://openjdk.java.net/projects/code-tools/jmh/)
* [junit](http://junit.org/)
