import benchmarks.Benchmark_SimplePrimitivePipelines;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class Test_SimplePrimitivePipelines {

    static Benchmark_SimplePrimitivePipelines __benchClass = new Benchmark_SimplePrimitivePipelines();

    @Test
    public void Test_cart_PrimitiveLong(){
        assertEquals (__benchClass.cart_Java8Streams(), __benchClass.cart_LStreams()) ;
        assertEquals (__benchClass.cart_Java8Streams(), __benchClass.cart_Baseline());
    }

    @Test
    public void Test_map_filter_fold_PrimitiveLong(){
        assertEquals (__benchClass.map_filter_fold_Java8Streams(), __benchClass.map_filter_fold_LStreams()) ;
        assertEquals (__benchClass.map_filter_fold_Java8Streams(), __benchClass.map_filter_fold_Baseline());
    }

    @Test
    public void Test_map_Megamorphic_PrimitiveLong(){
        assertEquals (__benchClass.map_Megamorphic_Java8Streams(), __benchClass.map_Megamorphic_LStreams()) ;
        assertEquals (__benchClass.map_Megamorphic_Java8Streams(), __benchClass.map_Megamorphic_Baseline());
    }

    @Test
    public void Test_sort_Boxed_Long(){
        long[] sort_primitiveLong_baseline = __benchClass.sort_Baseline();
        long[] sort_primitiveLong_lStreams = __benchClass.sort_LStreams();
        long[] sort_primitiveLong_java8Streams = __benchClass.sort_Java8Streams();

        assertArrayEquals(sort_primitiveLong_java8Streams, sort_primitiveLong_lStreams);
        assertArrayEquals(sort_primitiveLong_lStreams, sort_primitiveLong_baseline);
    }
}
