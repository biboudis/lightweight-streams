package streams;

import java.util.*;
import java.util.function.*;

public class LPipeline<T> implements LStream<T>  {

    public Consumer<Function<T, Boolean>> getTryAdvanceLambda() {
        return streamf;
    }

    private Consumer<Function<T, Boolean>> streamf;

    public LPipeline(Consumer<Function<T, Boolean>> tryAdvanceFunction) {
        this.streamf = tryAdvanceFunction;
    }

    @Override
    public <R> LStream<R> map(Function<T, R> f) {
        Consumer<Function<R, Boolean>> consumer = (iterf) ->
                streamf.accept(value -> iterf.apply(f.apply(value)));

        return new LPipeline<R>(consumer);
    }

    @Override
    public LStream<T> filter(Function<T, Boolean> predicate) {
        Consumer<Function<T, Boolean>> consumer = (iterf) ->
                streamf.accept(value -> predicate.apply(value) ? iterf.apply(value) : true);

        return new LPipeline<T>(consumer);
    }

    @Override
    public T reduce(T identity, BinaryOperator<T> accumulator) {
        // final AtomicReference<T> ref = new AtomicReference<>(identity);

        final Ref<T> state = new Ref<T>(identity);

        streamf.accept(value -> {
            //ref.getAndAccumulate(identity, accumulator);
            state.setRef(accumulator.apply(state.getRef(), value));
            return true;
        });

        return state.getRef();
    }

    @Override
    public <U> U reduce(U identity, BiFunction<U,? super T,U> accumulator) {
        // final AtomicReference<T> ref = new AtomicReference<>(state);

        final Ref<U> state = new Ref<U>(identity);

        streamf.accept(value -> {
            //ref.getAndAccumulate(value, folder);
            state.setRef(accumulator.apply(state.getRef(), value));
            return true;
        });

        return state.getRef();
    }

    @Override
    public <R> LStream<R> flatMap(Function<T, LStream<R>> f) {
        Consumer<Function<R, Boolean>> consumer = (iterf) ->
                streamf.accept(value -> {
                    LStream<R> streamfInternal = f.apply(value);
                    streamfInternal.getTryAdvanceLambda().accept(iterf);
                    return true;
                });

        return new LPipeline<R>(consumer);
    }

    @Override
    public LStream<T> sorted(Comparator<? super T> comparator) {

        Vector<T> vector = new Vector<>();

        streamf.accept(value -> {
            vector.add(value);
            return true;
        });

        vector.sort(comparator);

        Object arrayV[] = new Object[vector.size()];

        T[] sorted = vector.toArray((T[]) arrayV);

        return LStream.of(sorted);
    }

    @Override
    public T[] toArray(IntFunction<T[]> generator) {
        Vector<T> vector = new Vector<T>();

        vector = reduce(vector, (accList, value) -> {
            accList.addElement(value);
            return accList;
        });

        T arrayV[] = generator.apply(vector.size());

        return vector.toArray(arrayV);
    }
}
