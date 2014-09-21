package streams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.*;

public interface LStream<T> {

    public static<T> LStream<T> of(T... values) {
        List<T> src = Arrays.asList(values);

        Consumer<Function<T, Boolean>> tryAdvance = (f) -> {
            int i = 0;
            boolean next = true;
            while (i < src.size() && next) {
                next = f.apply(src.get(i));
                i++;
            }
        };

        return new LPipeline<T>(tryAdvance);
    }

    <R> LStream<R> map (Function<T,R> f);

    LStream<T> filter (Function<T, Boolean> predicate);

    T reduce (T state, BinaryOperator<T> folder);

    <U> U reduce(U identity, BiFunction<U,? super T,U> accumulator);

    <R> LStream<R> flatMap (Function<T,LStream<R>> f);

    LStream<T> sorted (Comparator<? super T> comparator );

    T[] toArray(IntFunction<T[]> generator);

    Consumer<Function<T, Boolean>> getTryAdvanceLambda();
}
