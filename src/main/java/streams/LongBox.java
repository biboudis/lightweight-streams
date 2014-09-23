package streams;

/**
 * Created by bibou on 9/23/14.
 */
public class LongBox {
    long value;

    LongBox(long value) {
        this.value = value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
