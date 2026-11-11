public class Nodo<E> {
    protected E info;
    protected Nodo<E> next;

    public Nodo(E info) {
        this.info = info;
        this.next = null;
    }

    public Nodo(E info, Nodo<E> next) {
        this.info = info;
        this.next = next;
    }

    public E getInfo() { return info; }
    public void setInfo(E info) { this.info = info; }
    public Nodo<E> getNext() { return next; }
    public void setNext(Nodo<E> next) { this.next = next; }
}
