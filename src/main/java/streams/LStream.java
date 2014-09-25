package streams;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.*;

public interface LStream<T> {

    public static<T> LStream<T> of(T... values) {
        List<T> src = Arrays.asList(values);

        Consumer<Predicate<T>> streamf = (f) -> {
            int i = 0;
            boolean next = true;
            while (i < src.size() && next) {
                next = f.test(src.get(i));
                i++;
            }
        };

        return new LPipeline<T>(streamf);
    }

    <R> LStream<R> map (Function<T,R> f);

    LStream<T> filter (Predicate<T> predicate);

    T reduce (T state, BinaryOperator<T> accumulator);

    <U> U reduce(U identity, BiFunction<U,? super T,U> accumulator);

    <R> LStream<R> flatMap (Function<T,LStream<R>> f);

    LStream<T> sorted (Comparator<? super T> comparator );

    int length ();

    T[] toArray(IntFunction<T[]> generator);

    Consumer<Predicate<T>> getStreamF();
}
