public class Expediente {
    private String nombreEstudiante;
    private String nombreCarrera;
    private String nombreGrupo; 
    private String motivo;

    public Expediente(String nombreEstudiante, String nombreCarrera, String nombreGrupo, String motivo) {
        this.nombreEstudiante = nombreEstudiante;
        this.nombreCarrera = nombreCarrera;
        this.nombreGrupo = nombreGrupo;
        this.motivo = motivo;
    }

    public String getNombreEstudiante() { return nombreEstudiante; }
    public String getNombreCarrera() { return nombreCarrera; }
    public String getNombreGrupo() { return nombreGrupo; }
    public String getMotivo() { return motivo; }

    @Override
    public String toString() {
        return "[" + motivo + "] Estudiante: " + nombreEstudiante + 
               " - Carrera: " + nombreCarrera + 
               " - " + nombreGrupo;
    }
}
