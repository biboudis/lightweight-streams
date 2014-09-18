import streams.LStream;

import java.util.ArrayList;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Main {

    static final int N = 10000000;
    static Long[] v;

    static {
        // Ok, lets use IntStream for this :P
        v = IntStream.range(0, N).mapToObj(i -> new Long(i % 1000)).toArray(Long[]::new);
    }

    public static void main(String[] args) {

        Long sum = LStream.ofArray(v)
                .filter(x -> x % 2L == 0L)
                .map(x -> x + 2L)
                .reduce(0L, Long::sum);

        System.out.println("Lightweight Streams Result: " + sum);

        Long sum2 = Stream.of(v)
                .filter(x -> x % 2L == 0L)
                .map(x -> x + 2L)
                .reduce(0L, Long::sum);

        System.out.println("Java 8 Streams Result: " + sum2);

    }
}
