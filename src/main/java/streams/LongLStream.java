package streams;

import java.util.function.*;

/**
 * Created by bibou on 9/23/14.
 */
public interface LongLStream {

    public static LongLStream of(long... values) {
        long[] src = values;

        Consumer<LongPredicate> streamf = (f) -> {
            int i = 0;
            boolean next = true;
            while (i < src.length && next) {
                next = f.test(src[i]);
                i++;
            }
        };

        return new LongLPipeline (streamf);
    }

    LongLStream map(LongUnaryOperator f);

    <R> LStream<R> mapToObj (LongFunction<R> f);

    LongLStream filter (LongPredicate predicate);

    long reduce (long state, LongBinaryOperator folder);

    <U> U reduce(U identity, ObjLongConsumer<U> accumulator);

    LongLStream flatMap (LongFunction<? extends LongLStream> f);

    LongLStream sorted ();

    long count();

    long[] toArray();

    Consumer<LongPredicate> getStreamF();

}
