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
    public LStream<T> filter(Predicate<T> predicate) {
        Consumer<Function<T, Boolean>> consumer = (iterf) ->
                streamf.accept(value -> predicate.test(value) ? iterf.apply(value) : true);

        return new LPipeline<T>(consumer);
    }

    @Override
    public T reduce(T identity, BinaryOperator<T> accumulator) {
        // final AtomicReference<T> ref = new AtomicReference<>(identity);

        final Box<T> state = new Box<T>(identity);

        streamf.accept(value -> {
            //ref.getAndAccumulate(identity, accumulator);
            state.setValue(accumulator.apply(state.getValue(), value));
            return true;
        });

        return state.getValue();
    }

    public <U> U reduce(U identity, BiFunction<U,? super T,U> accumulator) {
        // final AtomicReference<T> ref = new AtomicReference<>(state);

        final Box<U> state = new Box<U>(identity);

        streamf.accept(value -> {
            //ref.getAndAccumulate(value, folder);
            state.setValue(accumulator.apply(state.getValue(), value));
            return true;
        });

        return state.getValue();
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

        ArrayList<T> buffer = new ArrayList<>();

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
    public T[] toArray(IntFunction<T[]> generator) {
        ArrayList<T> buffer = new ArrayList<>();

        buffer = reduce(buffer, (accList, value) -> {
            accList.add(value);
            return accList;
        });

        T arrayV[] = generator.apply(buffer.size());

        return buffer.toArray(arrayV);
    }

    @Override
    public int length() {
        return -1;
    }
}
