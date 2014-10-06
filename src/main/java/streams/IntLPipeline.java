package streams;

import com.google.common.primitives.Ints;

import java.util.ArrayList;
import java.util.function.*;


/**
 * Created by bibou on 9/23/14.
 */
public class IntLPipeline implements IntLStream {
    private Consumer<IntPredicate> streamf;

    public IntLPipeline(Consumer<IntPredicate> streamf) {
        this.streamf = streamf;
    }

    // lazy operations.
    @Override
    public IntLStream filter(IntPredicate predicate) {
        Consumer<IntPredicate> consumer = (iterf) ->
                streamf.accept(value -> !predicate.test(value) || iterf.test(value));

        return new IntLPipeline(consumer);
    }

    @Override
    public IntLStream map(IntUnaryOperator f) {
        Consumer<IntPredicate> consumer = (iterf) ->
                streamf.accept(value -> iterf.test(f.applyAsInt(value)));

        return new IntLPipeline(consumer);
    }

    @Override
    public LongLStream mapToLong(IntToLongFunction f) {
        Consumer<LongPredicate> consumer = (iterf) ->
                streamf.accept(value -> iterf.test(f.applyAsLong(value)));

        return new LongLPipeline(consumer);
    }

    @Override
    public <R> LStream<R> mapToObj(IntFunction<R> f) {
        Consumer<Predicate<R>> consumer = (iterf) ->
                streamf.accept(value -> iterf.test(f.apply(value)));

        return new LPipeline<R>(consumer);
    }

    @Override
    public IntLStream flatMap(IntFunction<? extends IntLStream> f) {
        Consumer<IntPredicate> consumer = (iterf) ->
                streamf.accept(value -> {
                    IntLStream streamfInternal = f.apply(value);
                    streamfInternal.getStreamF().accept(iterf);
                    return true;
                });

        return new IntLPipeline(consumer);
    }

    // eager operations.
    @Override
    public int reduce(int identity, IntBinaryOperator accumulator) {
        IntCell state = new IntCell(identity);

        streamf.accept(value -> {
            state.value = accumulator.applyAsInt(state.value, value);
            return true;
        });

        return state.value;
    }

    @Override
    public IntLStream sorted() {
        ArrayList<Integer> buffer = new ArrayList<Integer>();

        streamf.accept(value -> {
            buffer.add(value);
            return true;
        });

        buffer.sort(Integer::compare);

        int[] values = Ints.toArray(buffer);

        return IntLStream.of(values);
    }

    @Override
    public int[] toArray() {
        ArrayList<Integer> buffer = new ArrayList<Integer>();

        streamf.accept(value -> {
            buffer.add(value);
            return true;
        });

        return Ints.toArray(buffer);
    }

    @Override
    public long count() {
        LongCell length = new LongCell(0);

        return this.mapToLong(value -> 1L).reduce(length.value, Long::sum);
    }

    @Override
    public Consumer<IntPredicate> getStreamF() {
        return streamf;
    }
}
