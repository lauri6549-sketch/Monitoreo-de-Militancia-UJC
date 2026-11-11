public interface IStack<E> {
    void push(E item); 
    E top(); 
    E pop(); 
    boolean isEmpty();
}