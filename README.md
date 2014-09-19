## Lightweight Streams for Java

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
* Java Streams 
    [code](http://hg.openjdk.java.net/jdk9/jdk9/jdk/file/tip/src/java.base/share/classes/java/util/stream) / [docs](http://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html)

### Dependencies
* [JMH](http://openjdk.java.net/projects/code-tools/jmh/)
* [junit](http://junit.org/)

