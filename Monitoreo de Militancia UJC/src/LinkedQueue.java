public class LinkedQueue<E> implements IQueue<E> {
    private Nodo<E> first;
    private Nodo<E> last;

    public LinkedQueue() { this.first = null; this.last = null; }

    @Override
    public void add(E item) {
        Nodo<E> n = new Nodo<>(item);
        if (isEmpty()) { last = n; first = n; }
        else { last.setNext(n); last = n; }
    }

    @Override
    public E peek() { return isEmpty() ? null : first.getInfo(); }

    @Override
    public E poll() {
        if (isEmpty()) return null;
        E item = first.getInfo();
        first = first.getNext();
        if (first == null) last = null; 
        return item;
    }

    @Override
    public boolean isEmpty() { return first == null; }

    public Nodo<E> getFirst() { return first; }
}