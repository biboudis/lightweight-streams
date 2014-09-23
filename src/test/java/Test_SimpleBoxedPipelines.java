import benchmarks.Benchmark_SimpleBoxedPipelines;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;

public class Test_SimpleBoxedPipelines {

    static Benchmark_SimpleBoxedPipelines __benchClass = new Benchmark_SimpleBoxedPipelines();

    @Test
    public void Test_cart_BoxedLong(){
        assert (__benchClass.cart_BoxedLong_LStreams().equals(__benchClass.cart_BoxedLong_Java8Streams()));
    }

    @Test
    public void Test_map_filter_fold_BoxedLong(){
        assert (__benchClass.map_filter_fold_BoxedLong_LStreams().equals(__benchClass.map_filter_fold_BoxedLong_Java8Streams()));
    }

    @Test
    public void Test_map_Megamorphic_BoxedLong(){
        assert (__benchClass.map_Megamorphic_BoxedLong_LStreams().equals(__benchClass.map_Megamorphic_BoxedLong_Java8Streams()));
    }

    @Test
    public void Test_sort_BoxedLong(){
        Long[] sort_boxed_long_lStreams = __benchClass.sort_BoxedLong_LStreams();
        Long[] sort_boxed_long_java8Streams = __benchClass.sort_BoxedLong_Java8Streams();
        assertArrayEquals(sort_boxed_long_java8Streams, sort_boxed_long_lStreams);
    }
}
