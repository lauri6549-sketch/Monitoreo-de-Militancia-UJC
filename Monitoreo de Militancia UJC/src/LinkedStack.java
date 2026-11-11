public class LinkedStack<E> implements IStack<E> {
    private Nodo<E> top;

    public LinkedStack() { this.top = null; }

    @Override
    public void push(E item) { 
        Nodo<E> n = new Nodo<>(item);
        if (isEmpty()) { top = n; }
        else { n.setNext(top); top = n; }
    }

    @Override
    public E top() { return isEmpty() ? null : top.getInfo(); }

    @Override
    public E pop() { 
        if (isEmpty()) return null;
        E aux = top.getInfo();
        top = top.getNext();
        return aux;
    }

    @Override
    public boolean isEmpty() { return top == null; }

    public Nodo<E> getTop() { return top; }
}