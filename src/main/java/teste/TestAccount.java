public class TestAccount<T> {
    private T item;

    public void insert(T item) {
        this.item = item;
    }

    public T get() {
        return this.item;
    }
}
