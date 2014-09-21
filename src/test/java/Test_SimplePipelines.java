import benchmarks.SimplePipelines;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;

public class Test_SimplePipelines {

    static SimplePipelines __benchClass = new SimplePipelines();

    @Test
    public void Test_cart_Boxed_Long(){
        assert (__benchClass.cart_Boxed_Long_LStreams().equals(__benchClass.cart_Boxed_Long_Java8Streams()));
    }

    @Test
    public void Test_map_filter_fold_Boxed_Long(){
        assert (__benchClass.map_filter_fold_Boxed_Long_LStreams().equals(__benchClass.map_filter_fold_Boxed_Long_Java8Streams()));
    }

    @Test
    public void Test_map_Megamorphic_Boxed_Long(){
        assert (__benchClass.map_Megamorphic_Boxed_Long_LStreams().equals(__benchClass.map_Megamorphic_Boxed_Long_Java8Streams()));
    }

    @Test
    public void Test_sort_Boxed_Long(){
        Long[] sort_boxed_long_lStreams = __benchClass.sort_Boxed_Long_LStreams();
        Long[] sort_boxed_long_java8Streams = __benchClass.sort_Boxed_Long_Java8Streams();
        assertArrayEquals(sort_boxed_long_java8Streams, sort_boxed_long_lStreams);
    }
}
