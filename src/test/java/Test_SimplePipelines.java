import benchmarks.SimplePipelines;
import org.junit.Test;

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
}
