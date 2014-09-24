package streams;

import java.util.function.*;

/**
 * Created by bibou on 9/23/14.
 */
public interface LongLStream {

    public static LongLStream of(long... values) {
        long[] src = values;

        Consumer<LongPredicate> tryAdvance = (f) -> {
            int i = 0;
            boolean next = true;
            while (i < src.length && next) {
                next = f.test(src[i]);
                i++;
            }
        };

        return new LongLPipeline (tryAdvance);
    }

    LongLStream map(LongUnaryOperator f);

    <R> LStream<R> mapToObj (LongFunction<R> f);

    LongLStream filter (LongPredicate predicate);

    long reduce (long state, LongBinaryOperator folder);

    LongLStream flatMap (LongFunction<? extends LongLStream> f);

    LongLStream sorted ();

    int length ();

    long[] toArray();

    Consumer<LongPredicate> getTryAdvanceLambda();

}
