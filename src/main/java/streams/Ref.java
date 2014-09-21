package streams;

/**
 * Created by bibou on 9/21/14.
 */
class Ref<T> {
    T ref;

    Ref(T ref) {
        this.ref = ref;
    }

    public void setRef(T ref) {
        this.ref = ref;
    }

    public T getRef() {
        return ref;
    }
}
