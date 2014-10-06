import benchmarks.Benchmark_SimpleBoxedPipelines;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class Test_SimpleBoxedPipelines {

    static Benchmark_SimpleBoxedPipelines __benchClass = new Benchmark_SimpleBoxedPipelines();

    @Before
    public void setUp() throws Exception {
        __benchClass.setUp();
    }

    @Test
    public void Test_cart_Boxed(){
        assertEquals(__benchClass.cart_Java8Streams(), __benchClass.cart_LStreams()) ;
        assertEquals (__benchClass.cart_Java8Streams(), __benchClass.cart_Baseline());
    }

    @Test
    public void Test_map_filter_fold_Boxed(){
        assertEquals (__benchClass.map_filter_fold_Java8Streams(), __benchClass.map_filter_fold_LStreams()) ;
        assertEquals (__benchClass.map_filter_fold_Java8Streams(), __benchClass.map_filter_fold_Baseline());
    }

    @Test
    public void Test_map_Megamorphic_Boxed(){
        assertEquals (__benchClass.map_megamorphic_Java8Streams_6(), __benchClass.map_megamorphic_LStreams_6()) ;
        assertEquals (__benchClass.map_megamorphic_Java8Streams_6(), __benchClass.map_megamorphic_Baseline_6());
    }

    @Test
    public void Test_count_PrimitiveInt(){
        assertEquals(__benchClass.count_Java8Streams(), __benchClass.count_LStreams());
    }

    @Test
    public void Test_sort_Boxed_Long(){
        Long[] sort_Boxed_baseline = __benchClass.sort_Baseline();
        Long[] sort_Boxed_lStreams = __benchClass.sort_LStreams();
        Long[] sort_Boxed_java8Streams = __benchClass.sort_Java8Streams();

        assertArrayEquals(sort_Boxed_java8Streams, sort_Boxed_lStreams);
        assertArrayEquals(sort_Boxed_lStreams, sort_Boxed_baseline);
    }
}
