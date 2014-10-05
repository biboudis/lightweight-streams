package streams;

import com.google.common.primitives.Longs;

import java.util.ArrayList;
import java.util.function.*;


/**
 * Created by bibou on 9/23/14.
 */
public class LongLPipeline implements LongLStream {
    private Consumer<LongPredicate> streamf;

    public LongLPipeline(Consumer<LongPredicate> streamf) {
        this.streamf = streamf;
    }

    // lazy operations.
    @Override
    public LongLStream filter(LongPredicate predicate) {
        Consumer<LongPredicate> consumer = (iterf) ->
                streamf.accept(value -> !predicate.test(value) || iterf.test(value));

        return new LongLPipeline(consumer);
    }

    @Override
    public LongLStream map(LongUnaryOperator f) {
        Consumer<LongPredicate> consumer = (iterf) ->
                streamf.accept(value -> iterf.test(f.applyAsLong(value)));

        return new LongLPipeline(consumer);
    }

    @Override
    public <R> LStream<R> mapToObj(LongFunction<R> f) {
        Consumer<Predicate<R>> consumer = (iterf) ->
                streamf.accept(value -> iterf.test(f.apply(value)));

        return new LPipeline<R>(consumer);
    }

    @Override
    public LongLStream flatMap(LongFunction<? extends LongLStream> f) {
        Consumer<LongPredicate> consumer = (iterf) ->
                streamf.accept(value -> {
                    LongLStream streamfInternal = f.apply(value);
                    streamfInternal.getStreamF().accept(iterf);
                    return true;
                });

        return new LongLPipeline(consumer);
    }

    // eager operations.
    @Override
    public long reduce(long identity, LongBinaryOperator accumulator) {
        LongCell state = new LongCell(identity);

        streamf.accept(value -> {
            state.value = accumulator.applyAsLong(state.value, value);
            return true;
        });

        return state.value;
    }

    @Override
    public <U> U reduce(U identity, ObjLongConsumer<U> accumulator) {
        final RefCell<U> state = new RefCell<U>(identity);

        streamf.accept(value -> {
            accumulator.accept(state.value, value);
            return true;
        });

        return state.value;
    }

    @Override
    public LongLStream sorted() {
        ArrayList<Long> buffer = new ArrayList<>();

        streamf.accept(value -> {
            buffer.add(value);
            return true;
        });

        buffer.sort(Long::compare);

        long[] values = Longs.toArray(buffer);

        return LongLStream.of(values);
    }

    @Override
    public long[] toArray() {
        ArrayList<Long> buffer = new ArrayList<Long>();

        streamf.accept(value -> {
            buffer.add(value);
            return true;
        });

        return Longs.toArray(buffer);
    }

    @Override
    public long count() {
        LongCell length = new LongCell(0);

        return this.map((value) -> 1).reduce(length.value, Long::sum);
    }

    @Override
    public Consumer<LongPredicate> getStreamF() {
        return streamf;
    }
}
