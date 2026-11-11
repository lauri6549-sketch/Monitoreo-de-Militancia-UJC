public class SolicitudIngreso {
    private String idEstudiante;
    private String nombreEstudiante;
    private String carrera;

    public SolicitudIngreso(String idEstudiante, String nombreEstudiante, String carrera) {
        this.idEstudiante = idEstudiante;
        this.nombreEstudiante = nombreEstudiante;
        this.carrera = carrera;
    }

    public String getIdEstudiante() { return idEstudiante; }
    public String getNombreEstudiante() { return nombreEstudiante; }
    public String getCarrera() { return carrera; }

    @Override
    public String toString() {
        return "Solicitud -> ID: " + idEstudiante + " - Nombre: " + nombreEstudiante + " - Carrera: " + carrera;
    }
}