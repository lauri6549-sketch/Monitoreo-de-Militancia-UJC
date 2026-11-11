import java.util.Scanner;

public class GestorUJC implements IMultilista {
    private NodoCarrera primeraCarrera;
    private LinkedStack<Expediente> historial;
    private LinkedQueue<SolicitudIngreso> solicitudes;
    private String nombreFacultad;

    public GestorUJC(String nombreFacultad) {
        this.nombreFacultad = nombreFacultad;
        this.primeraCarrera = null;
        this.historial = new LinkedStack<>();
        this.solicitudes = new LinkedQueue<>();
    }

    public void agregarCarrera(String nombre) {
        NodoCarrera nueva = new NodoCarrera(nombre);
        if (primeraCarrera == null) {
            primeraCarrera = nueva;
        } else {
            NodoCarrera actual = primeraCarrera;
            while (actual.getSiguiente() != null) actual = actual.getSiguiente();
            actual.setSiguiente(nueva);
        }
    }

    @Override
    public void agregarEstudiante(Scanner in) {
        String id = validarId(in);
        if (buscarEstudiantePorId(id) != null) {
            System.out.println("ERROR: Ya existe un estudiante con ese ID en el sistema.");
            return;
        } 

        String nombre = validarTexto(in, "Ingrese el nombre del estudiante: ");

        System.out.print("¿Es militante de la UJC? (1=Si / 2=No): ");
        int opMilitante = validarOpcion(in, 1, 2);
        boolean esMilitante = (opMilitante == 1);

        NodoCarrera carrera = seleccionarCarrera(in);
        if (carrera == null) return;

        agregarEstudianteAuxiliar(id, nombre, esMilitante, carrera.getNombreCarrera());
    }

    @Override
    public void darBajaUJC(Scanner in) {
        if (estaVaciaLaFacultad()) {
            System.out.println("ERROR: No hay estudiantes registrados en la facultad.");
            return;
        }

        NodoCarrera carrera = seleccionarCarrera(in);
        if (carrera == null) return;

        String id = validarId(in);
        darBajaUJCAuxiliar(carrera.getNombreCarrera(), id); 
    }

    @Override
    public void darBajaEstudiante(Scanner in) {
        if (estaVaciaLaFacultad()) {
            System.out.println("ERROR: No hay estudiantes registrados en la facultad.");
            return;
        }

        NodoCarrera carrera = seleccionarCarrera(in);
        if (carrera == null) return;

        String id = validarId(in);
        darBajaEstudianteAuxiliar(carrera.getNombreCarrera(), id);
    }


    public void agregarEstudianteAuxiliar(String id, String nombre, boolean esMilitante, String nombreCarrera) {
        if (buscarEstudiantePorId(id) != null) {
            System.out.println("ERROR: Ya existe un estudiante con ID " + id);
            return;
        }

        NodoCarrera carrera = buscarCarrera(nombreCarrera);
        if (carrera == null) {
            System.out.println("ERROR: La carrera " + nombreCarrera + " no existe.");
            return;
        }
        
        NodoGrupo ultimoGrupo = carrera.getPrimerGrupo();
        int cantidadEstudiantesGrupo = 0;

        if (ultimoGrupo == null) {
            ultimoGrupo = new NodoGrupo("Grupo 1");
            carrera.setPrimerGrupo(ultimoGrupo);
        } else {
            while (ultimoGrupo.getSiguiente() != null) {
                ultimoGrupo = ultimoGrupo.getSiguiente();
            }
            cantidadEstudiantesGrupo = contarEstudiantesEnGrupo(ultimoGrupo.getPrimerEstudiante());
            
            if (cantidadEstudiantesGrupo >= 25) {
                NodoGrupo nuevoGrupo = new NodoGrupo("Grupo " + (obtenerCantidadGrupos(carrera) + 1));
                ultimoGrupo.setSiguiente(nuevoGrupo);
                ultimoGrupo = nuevoGrupo;
                cantidadEstudiantesGrupo = 0;
            }
        }

        int numListaAsignado = cantidadEstudiantesGrupo + 1;
        NodoEstudiante nuevoEst = new NodoEstudiante(id, nombre, numListaAsignado, esMilitante);

        if (ultimoGrupo.getPrimerEstudiante() == null) {
            ultimoGrupo.setPrimerEstudiante(nuevoEst);
        } else {
            NodoEstudiante estActual = ultimoGrupo.getPrimerEstudiante();
            while (estActual.getSiguiente() != null) estActual = estActual.getSiguiente();
            estActual.setSiguiente(nuevoEst);
        }

        System.out.println("Estudiante [" + id + "] " + nombre + " agregado correctamente.");
        System.out.println("   -> Asignado al " + ultimoGrupo.getNombreGrupo() + " de la carrera " + carrera.getNombreCarrera());
        System.out.println("   -> Número de lista: " + numListaAsignado);
    }

    public void darBajaUJCAuxiliar(String nombreCarrera, String idEst) {
        NodoCarrera carrera = buscarCarrera(nombreCarrera);
        if (carrera == null) {
            System.out.println("ERROR: La carrera " + nombreCarrera + " no existe.");
            return;
        }

        NodoGrupo grupo = buscarGrupoDeEstudiante(carrera, idEst);
        if (grupo == null) {
            System.out.println("ERROR: Estudiante con ID " + idEst + " no encontrado.");
            return;
        }

        NodoEstudiante est = buscarEstudianteEnGrupo(grupo, idEst);
        if (!est.isEsMilitante()) {
            System.out.println("ERROR: El estudiante no es militante de la UJC actualmente.");
            return;
        }

        est.setEsMilitante(false);
        registrarExpediente(est.getNombre(), carrera.getNombreCarrera(), grupo.getNombreGrupo(), "Baja de la UJC");
        System.out.println("Estudiante [" + idEst + "] dado de baja de la UJC exitosamente.");
    }

    public void darBajaEstudianteAuxiliar(String nombreCarrera, String idEst) {
        NodoCarrera carrera = buscarCarrera(nombreCarrera);
        if (carrera == null) {
            System.out.println("ERROR: La carrera " + nombreCarrera + " no existe.");
            return;
        }

        NodoGrupo grupo = buscarGrupoDeEstudiante(carrera, idEst);
        if (grupo == null) {
            System.out.println("ERROR: Estudiante con ID " + idEst + " no encontrado.");
            return;
        }

        NodoEstudiante est = buscarEstudianteEnGrupo(grupo, idEst);
        registrarExpediente(est.getNombre(), carrera.getNombreCarrera(), grupo.getNombreGrupo(), "Baja de estudiante");

        if (grupo.getPrimerEstudiante() == est) {
            grupo.setPrimerEstudiante(est.getSiguiente());
        } else {
            NodoEstudiante anterior = grupo.getPrimerEstudiante();
            while (anterior.getSiguiente() != est) anterior = anterior.getSiguiente();
            anterior.setSiguiente(est.getSiguiente());
        }
        renumerarGrupo(grupo); 
        System.out.println("Estudiante [" + idEst + "] dado de baja correctamente.");

        if (grupo.getPrimerEstudiante() == null) {
            System.out.println("Nota: El " + grupo.getNombreGrupo() + " ha quedado vacío.");
        }
    }

    // ==========================================
    //  MÉTODOS RECURSIVOS
    // ==========================================

    private int contarEstudiantesEnGrupo(NodoEstudiante est) {
        if (est == null) return 0;                              // caso base
        return 1 + contarEstudiantesEnGrupo(est.getSiguiente()); // este estudiante + los que siguen
    }

    private void contarEstudiantes(NodoEstudiante est, int[] conteo) {
        if (est == null) return;                       // caso base: no hay más estudiantes
        conteo[0]++;                                   // cuenta este estudiante en la matrícula
        if (est.isEsMilitante()) conteo[1]++;          // y si es militante, también en militantes
        contarEstudiantes(est.getSiguiente(), conteo); // sigue con el próximo estudiante
    }

    private void contarEstudiantesDeGrupos(NodoGrupo grupo, int[] conteo) {
        if (grupo == null) return;                                  // caso base: no hay más grupos
        contarEstudiantes(grupo.getPrimerEstudiante(), conteo);     // cuenta los estudiantes de este grupo
        contarEstudiantesDeGrupos(grupo.getSiguiente(), conteo);    // sigue con el próximo grupo
    }

    private int evaluarCarreras(NodoCarrera carrera, boolean imprimir) {
        if (carrera == null) return 0;                  // caso base: no hay más carreras

        int[] cont = new int[2]; // cont[0] -> total de estudiantes, cont[1] -> total de militantes
        contarEstudiantesDeGrupos(carrera.getPrimerGrupo(), cont);

        int esAlta = 0;
        if (cont[0] > 0 && cont[1] * 2 > cont[0]) {
            esAlta = 1;
            if (imprimir) {
                System.out.println("- " + carrera.getNombreCarrera() + " (Militantes: " + cont[1] + " de " + cont[0] + ")");
            }
        }
        return esAlta + evaluarCarreras(carrera.getSiguiente(), imprimir); // suma las carreras que siguen
    }

    @Override
    public void evaluarCarrerasAltaMilitancia() {
        if (estaVaciaLaFacultad()) {
            System.out.println("ERROR: No hay estudiantes registrados para evaluar.");
            return;
        }

        System.out.println("\nCarreras con alta militancia (> 50%): ");
        int total = evaluarCarreras(primeraCarrera, true);
        if (total == 0) System.out.println("No hay carreras con alta militancia.");
    }

    // ==========================================
    //  MÉTODOS DE LA PILA (HISTORIAL)
    // ==========================================

    private void registrarExpediente(String nombre, String carrera, String grupo, String motivo) {
        Expediente exp = new Expediente(nombre, carrera, grupo, motivo);
        historial.push(exp); 
    }

    public void verUltimoExpediente() {
        if (historial.isEmpty()) {
            System.out.println("El historial de expedientes está vacío.");
        } else {
            System.out.println("Último trámite registrado:");
            System.out.println(historial.top().toString()); 
        }
    }

    public void verHistorialCompleto() {
        if (historial.isEmpty()) {
            System.out.println("El historial de expedientes está vacío.");
            return;
        }

        System.out.println("\n--- Historial de expedientes (Más reciente al más antiguo) --- ");
        Nodo<Expediente> actual = historial.getTop();
        
        while (actual != null) {
            System.out.println("- " + actual.getInfo().toString());
            actual = actual.getNext();
        }
        System.out.println("--- Fin del historial ---");
    }

    public void vaciarHistorial() {
        if (historial.isEmpty()) {
            System.out.println("El historial ya está vacío.");
        } else {
            while (!historial.isEmpty()) {
                historial.pop(); 
            }
            System.out.println("El historial de expedientes ha sido vaciado por completo.");
        }
    }

    // ==========================================
    //  MÉTODOS DE LA COLA (SOLICITUDES)
    // ==========================================

    public void solicitarIngreso(Scanner in) {
        if (estaVaciaLaFacultad()) {
            System.out.println("ERROR: No hay estudiantes registrados en la facultad.");
            return;
        }

        NodoCarrera carrera = seleccionarCarrera(in);
        if (carrera == null) return;

        String id = validarId(in);
        NodoEstudiante est = buscarEstudiantePorId(id);

        if (est == null) {
            System.out.println("ERROR: El estudiante con ID " + id + " no existe.");
            return;
        }
    
        if (buscarGrupoDeEstudiante(carrera, id) == null) {
            System.out.println("ERROR: El estudiante con ID " + id + " no pertenece a la carrera " + carrera.getNombreCarrera() + ".");
            return;
        }
        if (est.isEsMilitante()) {
            System.out.println("ERROR: El estudiante ya es militante de la UJC.");
            return;
        }

        Nodo<SolicitudIngreso> actual = solicitudes.getFirst();
        while (actual != null) {
            if (actual.getInfo().getIdEstudiante().equals(id)) {
                System.out.println("ERROR: El estudiante ya tiene una solicitud pendiente en la cola.");
                return;
            }
            actual = actual.getNext();
        }

        solicitarIngresoAuxiliar(id, est.getNombre(), carrera.getNombreCarrera());
        System.out.println("Solicitud de ingreso registrada correctamente en la cola.");
    }

    public void procesarSolicitudIngreso() {
        if (solicitudes.isEmpty()) {
            System.out.println("No hay solicitudes de ingreso pendientes.");
            return;
        }

        SolicitudIngreso sol = solicitudes.poll();
        NodoCarrera carrera = buscarCarrera(sol.getCarrera());

        if (carrera == null) {
            System.out.println("ERROR: No se pudo procesar la solicitud. La carrera " + sol.getCarrera() + " no existe.");
            return;
        }

        NodoGrupo grupo = buscarGrupoDeEstudiante(carrera, sol.getIdEstudiante());
        if (grupo == null) {
            System.out.println("ERROR: No se pudo procesar la solicitud. El estudiante con ID " + sol.getIdEstudiante() + " no se encontró en la carrera " + carrera.getNombreCarrera() + ".");
            return;
        }

        NodoEstudiante est = buscarEstudianteEnGrupo(grupo, sol.getIdEstudiante());
        est.setEsMilitante(true);
        registrarExpediente(est.getNombre(), carrera.getNombreCarrera(), grupo.getNombreGrupo(), "Ingreso aprobado a la UJC");
        System.out.println(est.getNombre() + " es ahora militante oficial de la UJC.");
    }

    public void verSiguienteSolicitud() {
        if (solicitudes.isEmpty()) System.out.println("No hay solicitudes pendientes.");
        else System.out.println("Siguiente en espera: " + solicitudes.peek().toString());
    }

    public void solicitarIngresoAuxiliar(String id, String nombre, String carrera) {
        solicitudes.add(new SolicitudIngreso(id, nombre, carrera));
    }

    // ==========================================
    
    private NodoCarrera seleccionarCarrera(Scanner in) {
        if (primeraCarrera == null) {
            System.out.println("ERROR: No hay carreras registradas.");
            return null;
        }

        System.out.println("\nCarreras disponibles: ");
        NodoCarrera actual = primeraCarrera;
        int cant = 0;
        
        while (actual != null) {
            cant++;
            System.out.println(cant + ". " + actual.getNombreCarrera());
            actual = actual.getSiguiente();
        }
        
        System.out.print("Seleccione el número de la carrera: ");
        int opcion = validarOpcion(in, 1, cant);

        actual = primeraCarrera;
        for (int i = 1; i < opcion; i++) {
            actual = actual.getSiguiente();
        }
        
        return actual;
    }

    private boolean estaVaciaLaFacultad() {
        return totalEstudiantes() == 0;
    }

    private void renumerarGrupo(NodoGrupo grupo) {
        int n = 1;
        NodoEstudiante e = grupo.getPrimerEstudiante();
        while (e != null) {
            e.setNumeroLista(n++);
            e = e.getSiguiente();
        }
    }

    private NodoCarrera buscarCarrera(String nombre) {
        NodoCarrera actual = primeraCarrera;
        while (actual != null) {
            if (actual.getNombreCarrera().equalsIgnoreCase(nombre)) return actual;
            actual = actual.getSiguiente();
        }
        return null;
    }

    private NodoEstudiante buscarEstudianteEnGrupo(NodoGrupo grupo, String id) {
        NodoEstudiante est = grupo.getPrimerEstudiante();
        while (est != null) {
            if (est.getId().equals(id)) return est;
            est = est.getSiguiente();
        }
        return null;
    }

    private NodoGrupo buscarGrupoDeEstudiante(NodoCarrera carrera, String id) {
        NodoGrupo grupo = carrera.getPrimerGrupo();
        while (grupo != null) {
            if (buscarEstudianteEnGrupo(grupo, id) != null) return grupo;
            grupo = grupo.getSiguiente();
        }
        return null;
    }

    private NodoEstudiante buscarEstudiantePorId(String id) {
        NodoCarrera carrera = primeraCarrera;
        while (carrera != null) {
            NodoGrupo grupo = buscarGrupoDeEstudiante(carrera, id);
            if (grupo != null) return buscarEstudianteEnGrupo(grupo, id);
            carrera = carrera.getSiguiente();
        }
        return null;
    }

    private int obtenerCantidadGrupos(NodoCarrera carrera) {
        int cont = 0;
        NodoGrupo g = carrera.getPrimerGrupo();
        while (g != null) { cont++; g = g.getSiguiente(); }
        return cont;
    }

    @Override
    public void imprimirFacultad() {
        System.out.println("\n--- " + nombreFacultad + " ---");
        NodoCarrera carrera = primeraCarrera;
        while (carrera != null) {
            System.out.println("\nCarrera: " + carrera.getNombreCarrera());
            NodoGrupo grupo = carrera.getPrimerGrupo();
            while (grupo != null) {
                System.out.println("  " + grupo.getNombreGrupo());
                NodoEstudiante est = grupo.getPrimerEstudiante();
                while (est != null) {
                    System.out.println("    - ID: " + est.getId() + " | " + est.getNombre() + " | Lista: " + est.getNumeroLista() + " | Militante: " + (est.isEsMilitante() ? "Sí" : "No"));
                    est = est.getSiguiente();
                }
                grupo = grupo.getSiguiente();
            }
            carrera = carrera.getSiguiente();
        }
    }

    private String validarId(Scanner in) {
        String id;
        while (true) {
            id = validarTexto(in, "Ingrese el ID del estudiante (4 dígitos): ");
            
            if (!id.matches("^[0-9]{4}$")) {
                System.out.println("ERROR: El ID debe tener exactamente 4 dígitos numéricos.");
            } else {
                break;
            }
        }
        return id;
    }

    private String validarTexto(Scanner in, String mensaje) {
        String texto;
        while (true) {
            System.out.print(mensaje);
            texto = in.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println("ERROR: El campo no puede estar vacío.");
            } else {
                return texto;
            }
        }
    }

    public int validarOpcion(Scanner in, int min, int max) {
        int op;
        while (true) {
            if (in.hasNextInt()) {
                op = in.nextInt();
                in.nextLine();
                if (op >= min && op <= max) return op;
                System.out.println("ERROR: El número debe estar entre " + min + " y " + max);
            } else {
                System.out.print("ERROR: Ingrese un número válido: ");
                in.next();
            }
        }
    }

    // ==========================================
    //  MÉTODOS DE CONSULTA PARA LAS PRUEBAS
    // ==========================================

    public boolean existeEstudiante(String id) {
        return buscarEstudiantePorId(id) != null;
    }

    public boolean esMilitante(String id) {
        NodoEstudiante est = buscarEstudiantePorId(id);
        return est != null && est.isEsMilitante();
    }

    public int obtenerNumeroLista(String id) {
        NodoEstudiante est = buscarEstudiantePorId(id);
        if (est == null) return -1;
        return est.getNumeroLista();
    }

    public int cantidadGrupos(String nombreCarrera) {
        NodoCarrera carrera = buscarCarrera(nombreCarrera);
        if (carrera == null) return 0;
        return obtenerCantidadGrupos(carrera);
    }

    public int contarCarrerasAltaMilitancia() {
        return evaluarCarreras(primeraCarrera, false);
    }

    public int totalEstudiantes() {
        return contarMatriculaCarreras(primeraCarrera);
    }

    private int contarMatriculaCarreras(NodoCarrera carrera) {
        if (carrera == null) return 0;
        int[] cont = new int[2];
        contarEstudiantesDeGrupos(carrera.getPrimerGrupo(), cont);
        return cont[0] + contarMatriculaCarreras(carrera.getSiguiente());
    }

    public String ultimoMotivo() {
        if (historial.isEmpty()) return null;
        return historial.top().getMotivo();
    }

    public int tamanoCola() {
        int cont = 0;
        Nodo<SolicitudIngreso> actual = solicitudes.getFirst();
        while (actual != null) { cont++; actual = actual.getNext(); }
        return cont;
    }

    public String siguienteSolicitudId() {
        if (solicitudes.isEmpty()) return null;
        return solicitudes.peek().getIdEstudiante();
    }
}
