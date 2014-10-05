package streams;

import java.util.*;
import java.util.function.*;

/**
 * Created by bibou on 9/23/14.
 */

public class LPipeline<T> implements LStream<T>  {

    public Consumer<Predicate<T>> getStreamF() {
        return streamf;
    }

    private Consumer<Predicate<T>> streamf;

    public LPipeline(Consumer<Predicate<T>> streamf) {
        this.streamf = streamf;
    }

    // lazy operations.
    @Override
    public <R> LStream<R> map(Function<T, R> f) {
        Consumer<Predicate<R>> consumer = (iterf) ->
                streamf.accept(value -> iterf.test(f.apply(value)));

        return new LPipeline<R>(consumer);
    }

    @Override
    public LStream<T> filter(Predicate<T> predicate) {
        Consumer<Predicate<T>> consumer = (iterf) ->
                streamf.accept(value -> !predicate.test(value) || iterf.test(value));

        return new LPipeline<T>(consumer);
    }

    @Override
    public <R> LStream<R> flatMap(Function<T, LStream<R>> f) {
        Consumer<Predicate<R>> consumer = (iterf) ->
                streamf.accept(value -> {
                    LStream<R> streamfInternal = f.apply(value);
                    streamfInternal.getStreamF().accept(iterf);
                    return true;
                });

        return new LPipeline<R>(consumer);
    }

    // eager operations.
    @Override
    public T reduce(T identity, BinaryOperator<T> accumulator) {
        final RefCell<T> state = new RefCell<T>(identity);

        streamf.accept(value -> {
            state.value = accumulator.apply(state.value, value);
            return true;
        });

        return state.value;    }

    @Override
    public <U> U reduce(U identity, BiFunction<U,? super T,U> accumulator) {
        final RefCell<U> state = new RefCell<U>(identity);

        streamf.accept(value -> {
            state.value = accumulator.apply(state.value, value);
            return true;
        });

        return state.value;
    }

    @Override
    public LStream<T> sorted(Comparator<? super T> comparator) {

        ArrayList<T> buffer = new ArrayList<T>();

        streamf.accept(value -> {
            buffer.add(value);
            return true;
        });

        buffer.sort(comparator);

        Object arrayV[] = new Object[buffer.size()];

        T[] sorted = buffer.toArray((T[]) arrayV);

        return LStream.of(sorted);
    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    public T[] toArray(IntFunction<T[]> generator) {
        ArrayList<T> buffer = new ArrayList<T>();

        buffer = reduce(buffer, (accList, value) -> {
            accList.add(value);
            return accList;
        });

        T arrayV[] = generator.apply(buffer.size());

        return buffer.toArray(arrayV);
    }

}
