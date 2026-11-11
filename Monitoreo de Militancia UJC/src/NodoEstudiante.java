public class NodoEstudiante {
    private String id;
    private String nombre;
    private int numeroLista;
    private boolean esMilitante;
    private NodoEstudiante siguiente;

    public NodoEstudiante(String id, String nombre, int numeroLista, boolean esMilitante) {
        this.id = id;
        this.nombre = nombre;
        this.numeroLista = numeroLista;
        this.esMilitante = esMilitante;
        this.siguiente = null;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getNumeroLista() { return numeroLista; }
    public void setNumeroLista(int numeroLista) { this.numeroLista = numeroLista; }
    public boolean isEsMilitante() { return esMilitante; }
    public void setEsMilitante(boolean esMilitante) { this.esMilitante = esMilitante; }
    public NodoEstudiante getSiguiente() { return siguiente; }
    public void setSiguiente(NodoEstudiante siguiente) { this.siguiente = siguiente; }
}