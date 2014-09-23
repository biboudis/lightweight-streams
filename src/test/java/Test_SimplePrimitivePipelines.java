import benchmarks.Benchmark_SimpleBoxedPipelines;
import benchmarks.Benchmark_SimplePrimitivePipelines;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class Test_SimplePrimitivePipelines {

    static Benchmark_SimplePrimitivePipelines __benchClass = new Benchmark_SimplePrimitivePipelines();

    @Test
    public void Test_cart_PrimitiveLong(){
        assert (__benchClass.cart_LStreams() ==__benchClass.cart_Java8Streams());
    }

    @Test
    public void Test_map_filter_fold_PrimitiveLong(){
        assert (__benchClass.map_filter_fold_LStreams() == __benchClass.map_filter_fold_Java8Streams());
    }

    @Test
    public void Test_map_Megamorphic_PrimitiveLong(){
        assert (__benchClass.map_Megamorphic_LStreams() == __benchClass.map_Megamorphic_Java8Streams());
    }

    @Test
    public void Test_sort_Boxed_Long(){
        long[] sort_primitiveLong_lStreams = __benchClass.sort_LStreams();
        long[] sort_primitiveLong_java8Streams = __benchClass.sort_Java8Streams();
        assertArrayEquals(sort_primitiveLong_lStreams, sort_primitiveLong_java8Streams);
    }
}
