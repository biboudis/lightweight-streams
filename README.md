# Lightweight Streams for Java.

### Measurements
Benchmark                                                    Mode  Samples       Score  Score error  Units
b.SimplePipelines.cart_Boxed_Long_Baseline                   avgt       10    1656.069       52.841  us/op
b.SimplePipelines.cart_Boxed_Long_Java8Streams               avgt       10    4603.143      260.045  us/op
b.SimplePipelines.cart_Boxed_Long_LStreams                   avgt       10    4148.314       96.394  us/op
b.SimplePipelines.map_filter_fold_Boxed_Long_Baseline        avgt       10  152137.698     8891.963  us/op
b.SimplePipelines.map_filter_fold_Boxed_Long_Java8Streams    avgt       10  190655.016     3159.224  us/op
b.SimplePipelines.map_filter_fold_Boxed_Long_LStreams        avgt       10  233473.979    19700.333  us/op

