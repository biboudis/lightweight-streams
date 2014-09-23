package streams;

/**
 * Created by bibou on 9/21/14.
 */
class Box<T> {
    T value;

    Box(T value) {
        this.value = value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

}
