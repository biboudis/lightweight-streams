package streams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;

public interface LStream<T> {

    public static<T> LStream<T> ofArray(T[] values) {
        List<T> src = Arrays.asList(values);
        Consumer<Function<T, Boolean>> consumer = (f) -> {
            int i = 0;
            boolean next = true;
            while (i < src.size() && next) {
                next = f.apply(src.get(i));
                i++;
            }
        };

        return new LPipeline<T>(consumer);
    }

    <R> LStream<R> map (Function<T,R> f);

    LStream<T> filter (Function<T, Boolean> predicate);

    //TODO: Why not use BiFunction<S,T,S> (S->T-S) ?
    T reduce (T state, BinaryOperator<T> folder);

    <R> LStream<R> flatMap (Function<T,LStream<R>> f);

    Consumer<Function<T, Boolean>> getStreamf();
}
