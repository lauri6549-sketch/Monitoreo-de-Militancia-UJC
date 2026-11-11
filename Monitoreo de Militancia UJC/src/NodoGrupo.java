public class NodoGrupo {
    private String nombreGrupo;
    private NodoEstudiante primerEstudiante;
    private NodoGrupo siguiente;

    public NodoGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
        this.primerEstudiante = null;
        this.siguiente = null;
    }

    public String getNombreGrupo() { return nombreGrupo; }
    public void setNombreGrupo(String nombreGrupo) { this.nombreGrupo = nombreGrupo; }
    public NodoEstudiante getPrimerEstudiante() { return primerEstudiante; }
    public void setPrimerEstudiante(NodoEstudiante primerEstudiante) { this.primerEstudiante = primerEstudiante; }
    public NodoGrupo getSiguiente() { return siguiente; }
    public void setSiguiente(NodoGrupo siguiente) { this.siguiente = siguiente; }
}