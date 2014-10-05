package streams;

import java.util.function.*;

/**
 * Created by bibou on 9/23/14.
 */
public interface IntLStream {

    public static IntLStream of(int... values) {
        int[] src = values;

        Consumer<IntPredicate> streamf = (f) -> {
            int i = 0;
            boolean next = true;
            while (i < src.length && next) {
                next = f.test(src[i]);
                i++;
            }
        };

        return new IntLPipeline (streamf);
    }

    IntLStream map(IntUnaryOperator f);

    LongLStream mapToLong(LongUnaryOperator f);

    <R> LStream<R> mapToObj(IntFunction<R> f);

    IntLStream filter(IntPredicate predicate);

    int reduce(int state, IntBinaryOperator folder);

    IntLStream flatMap (IntFunction<? extends IntLStream> f);

    IntLStream sorted();

    long count();

    int[] toArray();

    Consumer<IntPredicate> getStreamF();
}
