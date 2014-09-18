package streams;

import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;

public class LPipeline<T> implements LStream<T> {

    public Consumer<Function<T, Boolean>> getStreamf() {
        return streamf;
    }

    private Consumer<Function<T, Boolean>> streamf;

    public LPipeline(Consumer<Function<T, Boolean>> streamf) {
        this.streamf = streamf;
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
    public T reduce(T state, BinaryOperator<T> folder) {
        // final AtomicReference<T> accumulator = new AtomicReference<>(state);

        class Acc<T> {
            T accumulator;

            Acc(T accumulator) {
                this.accumulator = accumulator;
            }

            public void setAccumulator(T accumulator) {
                this.accumulator = accumulator;
            }

            public T getAccumulator() {
                return accumulator;
            }
        }

        final Acc<T> acc = new Acc<T>(state);

        streamf.accept(value -> {
            //accumulator.getAndAccumulate(value, folder);
            acc.setAccumulator(folder.apply(value, acc.getAccumulator()));
            return true;
        });

        return acc.getAccumulator();
    }

    @Override
    public <R> LStream<R> flatMap(Function<T, LStream<R>> f) {
        Consumer<Function<R, Boolean>> consumer = (iterf) ->
                streamf.accept(value -> {
                    LStream<R> streamfInternal = f.apply(value);
                    streamfInternal.getStreamf().accept(iterf);
                    return true;
                });

        return new LPipeline<R>(consumer);
    }
}
