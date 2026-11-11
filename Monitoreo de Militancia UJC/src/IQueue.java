public interface IQueue<E> {
    void add(E item);
    E peek();
    E poll();
    boolean isEmpty();
}