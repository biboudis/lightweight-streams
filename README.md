## Lightweight Streams for Java

## Test

* Run tests: 
    `mvn -q test`

* Build [benchmarks](https://github.com/biboudis/lightweight-streams/blob/master/src/main/java/benchmarks/): 
    `mvn clean package -Dskiptests`
    
* To run your benchmarks you can run the generated über-jar:
    `java -XX:-TieredCompilation -jar target/microbenchmarks.jar -wi 15 -i 10 -f 1 -gc -tu ms ".*"`
    

### References

* [Clash of the Lambdas: Microbenchmarking collections, via the functional APIs of Java 8, Scala, C#, F#.](http://biboudis.github.io/clashofthelambdas/)
* [Nessos/Streams](https://github.com/nessos/Streams)
* [Java 8 Streams (in JDK9 tip)] (http://hg.openjdk.java.net/jdk9/jdk9/jdk/file/tip/src/java.base/share/classes/java/util/stream)

### Dependencies
* [JMH](http://openjdk.java.net/projects/code-tools/jmh/)
* [junit](http://junit.org/)

