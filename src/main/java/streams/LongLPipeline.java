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

    @Override
    public LongLStream map(LongUnaryOperator f) {
        Consumer<LongPredicate> consumer = (iterf) ->
                streamf.accept(value -> iterf.test(f.applyAsLong(value)));

        return new LongLPipeline(consumer);
    }

    @Override
    public <R> LStream<R> mapToObj(LongFunction<R> f) {
        Consumer<Function<R, Boolean>> consumer = (iterf) ->
                streamf.accept(value -> iterf.apply(f.apply(value)));

        return new LPipeline(consumer);
    }

    @Override
    public LongLStream filter(LongPredicate predicate) {
        Consumer<LongPredicate> consumer = (iterf) ->
                streamf.accept(value -> predicate.test(value) ? iterf.test(value) : true);

        return new LongLPipeline(consumer);
    }

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
    public LongLStream flatMap(LongFunction<? extends LongLStream> f) {
        Consumer<LongPredicate> consumer = (iterf) ->
                streamf.accept(value -> {
                    LongLStream streamfInternal = f.apply(value);
                    streamfInternal.getStreamF().accept(iterf);
                    return true;
                });

        return new LongLPipeline(consumer);
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

        long[] values = Longs.toArray(buffer);

        return values;
    }

    @Override
    public int length() {
        return -1;
    }

    @Override
    public Consumer<LongPredicate> getStreamF() {
        return streamf;
    }
}
