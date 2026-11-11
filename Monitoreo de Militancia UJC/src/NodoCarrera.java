public class NodoCarrera {
    private String nombreCarrera;
    private NodoGrupo primerGrupo;
    private NodoCarrera siguiente;

    public NodoCarrera(String nombreCarrera) {
        this.nombreCarrera = nombreCarrera;
        this.primerGrupo = null;
        this.siguiente = null;
    }

    public String getNombreCarrera() { return nombreCarrera; }
    public void setNombreCarrera(String nombreCarrera) { this.nombreCarrera = nombreCarrera; }
    public NodoGrupo getPrimerGrupo() { return primerGrupo; }
    public void setPrimerGrupo(NodoGrupo primerGrupo) { this.primerGrupo = primerGrupo; }
    public NodoCarrera getSiguiente() { return siguiente; }
    public void setSiguiente(NodoCarrera siguiente) { this.siguiente = siguiente; }
}