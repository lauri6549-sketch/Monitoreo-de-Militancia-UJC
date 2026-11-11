import java.util.Scanner;

public class Pruebas {
    private int total;
    private int correctas;

    public Pruebas() {
        this.total = 0;
        this.correctas = 0;
    }

    private void verificar(String descripcion, boolean condicion) {
        total++;
        if (condicion) {
            correctas++;
            System.out.println("   -> OK: " + descripcion);
        } else {
            System.out.println("   -> FALLO: " + descripcion);
        }
    }

    public void ejecutar() {
        total = 0;
        correctas = 0;

        System.out.println("\n---- Casos de Pruebas ----");
        pruebas();

        System.out.println("\n---- Fin de los Casos de Pruebas ----");
        System.out.println("Resultado: " + correctas + " de " + total + " pruebas correctas.");
    }

    private void pruebas() {
        System.out.println("\n--- 1. Pruebas Positivas ---");
        GestorUJC g1 = new GestorUJC("Facultad de Ciencias Técnicas");
        g1.agregarCarrera("Ing. Informática");
        g1.agregarCarrera("Ing. Mecánica");

        System.out.println("\n[1.1] Agregando 26 estudiantes (el 26 debe crear el Grupo 2):");
        for (int i = 1; i <= 25; i++) {
            g1.agregarEstudianteAuxiliar(String.valueOf(1000 + i), "Estudiante " + i, (i % 2 == 0), "Ing. Informática");
        }
        verificar("Con 25 estudiantes solo existe el Grupo 1", g1.cantidadGrupos("Ing. Informática") == 1);

        g1.agregarEstudianteAuxiliar("1026", "Estudiante 26", true, "Ing. Informática");
        verificar("Con 26 estudiantes existen 2 grupos", g1.cantidadGrupos("Ing. Informática") == 2);
        verificar("El estudiante 26 tiene el número de lista 1", g1.obtenerNumeroLista("1026") == 1);

        System.out.println("\n[1.2] Evaluando alta militancia (13 de 26 = 50%, NO debe salir):");
        g1.evaluarCarrerasAltaMilitancia();
        verificar("Ninguna carrera tiene alta militancia", g1.contarCarrerasAltaMilitancia() == 0);

        System.out.println("\n[1.3] Agregando un militante más (14 de 27 > 50%):");
        g1.agregarEstudianteAuxiliar("1027", "Estudiante 27", true, "Ing. Informática");
        g1.evaluarCarrerasAltaMilitancia();
        verificar("Ahora hay 1 carrera con alta militancia", g1.contarCarrerasAltaMilitancia() == 1);

        System.out.println("\n[1.4] Probando Cola (Solicitudes) y alta a la UJC:");
        g1.solicitarIngresoAuxiliar("1001", "Estudiante 1", "Ing. Informática");
        g1.solicitarIngresoAuxiliar("1003", "Estudiante 3", "Ing. Informática");
        verificar("La primera solicitud es la de 1001 (FIFO)", "1001".equals(g1.siguienteSolicitudId()));

        g1.procesarSolicitudIngreso();
        verificar("1001 ya es militante", g1.esMilitante("1001"));
        verificar("Ahora la siguiente solicitud es la de 1003", "1003".equals(g1.siguienteSolicitudId()));

        System.out.println("\n[1.5] Probando Dar de Baja de la UJC:");
        g1.darBajaUJCAuxiliar("Ing. Informática", "1001");
        verificar("1001 ya no es militante", !g1.esMilitante("1001"));

        System.out.println("\n[1.6] Probando Pila (Historial):");
        g1.verUltimoExpediente();
        g1.verHistorialCompleto();
        verificar("El último expediente es la baja de la UJC (LIFO)", "Baja de la UJC".equals(g1.ultimoMotivo()));

        System.out.println("\n[1.7] Probando Dar de Baja de la Facultad:");
        g1.darBajaEstudianteAuxiliar("Ing. Informática", "1027");
        verificar("1027 ya no existe", !g1.existeEstudiante("1027"));

        System.out.println("\n[1.8] Vaciando el historial:");
        g1.vaciarHistorial();
        verificar("La pila quedó vacía", g1.ultimoMotivo() == null);

        System.out.println("\n[1.9] Número de lista después de una baja en el medio:");
        g1.agregarEstudianteAuxiliar("2001", "Pedro", false, "Ing. Mecánica");
        g1.agregarEstudianteAuxiliar("2002", "Ana", false, "Ing. Mecánica");
        g1.agregarEstudianteAuxiliar("2003", "Luis", false, "Ing. Mecánica");
        g1.darBajaEstudianteAuxiliar("Ing. Mecánica", "2002");
        verificar("Luis pasa de la lista 3 a la lista 2", g1.obtenerNumeroLista("2003") == 2);

        g1.agregarEstudianteAuxiliar("2004", "Marta", false, "Ing. Mecánica");
        verificar("Marta recibe la lista 3 (no se repite ningún número)", g1.obtenerNumeroLista("2004") == 3);

        System.out.println("\n[1.10] Agregando un estudiante por el menú:");
        g1.agregarEstudiante(new Scanner("4000\nJuan\n2\n2\n"));
        verificar("Juan fue agregado", g1.existeEstudiante("4000"));

        System.out.println("\n\n--- 2. Pruebas Negativas ---");
        GestorUJC g2 = new GestorUJC("Facultad de Ciencias Técnicas");
        g2.agregarCarrera("Ing. Informática");

        System.out.println("\n[2.1] Intentando evaluar sin estudiantes:");
        g2.evaluarCarrerasAltaMilitancia();
        verificar("No hay carreras con alta militancia", g2.contarCarrerasAltaMilitancia() == 0);

        System.out.println("\n[2.2] Intentando procesar y ver una solicitud con la cola vacía:");
        g2.procesarSolicitudIngreso();
        g2.verSiguienteSolicitud();
        verificar("No hay siguiente solicitud", g2.siguienteSolicitudId() == null);

        System.out.println("\n[2.3] Intentando ver el historial con pila vacía:");
        g2.verUltimoExpediente();
        g2.verHistorialCompleto();
        verificar("No hay último expediente", g2.ultimoMotivo() == null);

        System.out.println("\n[2.4] Intentando dar de baja de la UJC a un ID inexistente:");
        g2.darBajaUJCAuxiliar("Ing. Informática", "9999");
        verificar("No se registró ningún expediente", g2.ultimoMotivo() == null);

        g2.agregarEstudianteAuxiliar("1001", "Pedro", false, "Ing. Informática");

        System.out.println("\n[2.5] Intentando dar de baja de la UJC a un estudiante que no es militante:");
        g2.darBajaUJCAuxiliar("Ing. Informática", "1001");
        verificar("No se registró ningún expediente", g2.ultimoMotivo() == null);

        System.out.println("\n[2.6] Intentando dar de baja de la Facultad a un ID inexistente:");
        g2.darBajaEstudianteAuxiliar("Ing. Informática", "9999");
        verificar("Sigue existiendo 1 estudiante", g2.totalEstudiantes() == 1);

        System.out.println("\n[2.7] Intentando agregar un ID duplicado:");
        g2.agregarEstudianteAuxiliar("1001", "Otro", true, "Ing. Informática");
        verificar("Sigue existiendo 1 estudiante", g2.totalEstudiantes() == 1);

        System.out.println("\n[2.8] Intentando agregar en una carrera que no existe:");
        g2.agregarEstudianteAuxiliar("1002", "Ana", true, "Carrera Falsa");
        verificar("Sigue existiendo 1 estudiante", g2.totalEstudiantes() == 1);

        System.out.println("\n[2.9] Intentando procesar una solicitud de un ID que no existe:");
        g2.solicitarIngresoAuxiliar("9999", "Luis", "Ing. Informática");
        g2.procesarSolicitudIngreso();
        verificar("No se registró ningún expediente", g2.ultimoMotivo() == null);

        System.out.println("\n[2.10] Intentando procesar una solicitud de una carrera que no existe:");
        g2.solicitarIngresoAuxiliar("1001", "Pedro", "Carrera Falsa");
        g2.procesarSolicitudIngreso();
        verificar("El estudiante no se hizo militante", !g2.esMilitante("1001"));

        System.out.println("\n[2.11] Intentando solicitar ingreso dos veces seguidas:");
        g2.solicitarIngreso(new Scanner("1\n1001\n"));
        g2.solicitarIngreso(new Scanner("1\n1001\n"));
        verificar("Solo hay 1 solicitud en la cola", g2.tamanoCola() == 1);

        System.out.println("\n[2.12] Intentando que un militante solicite ingreso:");
        g2.agregarEstudianteAuxiliar("1003", "Marta", true, "Ing. Informática");
        g2.solicitarIngreso(new Scanner("1\n1003\n"));
        verificar("Sigue habiendo 1 solicitud en la cola", g2.tamanoCola() == 1);

        System.out.println("\n[2.13] Intentando solicitar ingreso eligiendo la carrera equivocada:");
        GestorUJC g3 = new GestorUJC("Facultad de Ciencias Técnicas");
        g3.agregarCarrera("Ing. Informática");
        g3.agregarCarrera("Ing. Mecánica");
        g3.agregarEstudianteAuxiliar("5001", "Carlos", false, "Ing. Mecánica");
        g3.solicitarIngreso(new Scanner("1\n5001\n"));
        verificar("La solicitud no se registró", g3.tamanoCola() == 0);

        System.out.println("\n[2.14] Intentando solicitar ingreso con un ID que no existe:");
        g3.solicitarIngreso(new Scanner("2\n7777\n"));
        verificar("La solicitud no se registró", g3.tamanoCola() == 0);

        System.out.println("\n[2.15] Escribiendo opciones inválidas en el menú (letras, fuera de rango, negativo):");
        int opcion = g3.validarOpcion(new Scanner("abc\n99\n-4\n2\n"), 1, 5);
        verificar("Se repite la pregunta hasta recibir la opción válida (2)", opcion == 2);

        System.out.println("\n[2.16] Escribiendo un ID inválido y un nombre vacío:");
        g3.agregarEstudiante(new Scanner("12a\n123\n1234\n\nAna\n2\n1\n"));
        verificar("Al final se agregó el estudiante con los datos correctos", g3.existeEstudiante("1234"));

        System.out.println("\n\n--- 3. Pruebas de Límite ---");

        System.out.println("\n[3.1] Carreras sin estudiantes junto a otras con estudiantes:");
        GestorUJC g4 = new GestorUJC("Facultad de Ciencias Técnicas");
        g4.agregarCarrera("Ing. Informática");
        g4.agregarCarrera("Ing. Mecánica");
        g4.agregarCarrera("Otra");
        g4.agregarEstudianteAuxiliar("1001", "Ana", true, "Ing. Informática");
        g4.agregarEstudianteAuxiliar("1002", "Luis", true, "Ing. Informática");
        g4.agregarEstudianteAuxiliar("3001", "Pedro", false, "Otra");
        verificar("Solo cuenta Informática (Mecánica vacía no da error)", g4.contarCarrerasAltaMilitancia() == 1);

        System.out.println("\n[3.2] Reutilización de un grupo vacío:");
        GestorUJC g5 = new GestorUJC("Facultad de Ciencias Técnicas");
        g5.agregarCarrera("Ing. Mecánica");
        g5.agregarEstudianteAuxiliar("6001", "Juan", true, "Ing. Mecánica");
        g5.darBajaEstudianteAuxiliar("Ing. Mecánica", "6001"); // Grupo queda vacío
        g5.agregarEstudianteAuxiliar("6002", "Juana", false, "Ing. Mecánica");
        verificar("Juana cayó en el mismo grupo (solo existe 1 grupo)", g5.cantidadGrupos("Ing. Mecánica") == 1);
        verificar("Juana tiene el número de lista 1", g5.obtenerNumeroLista("6002") == 1);

        System.out.println("\n[3.3] Usando la cola de nuevo después de vaciarla:");
        g5.agregarEstudianteAuxiliar("6003", "Luis", false, "Ing. Mecánica");
        g5.solicitarIngresoAuxiliar("6002", "Juana", "Ing. Mecánica");
        g5.procesarSolicitudIngreso(); // la cola queda vacía
        g5.solicitarIngresoAuxiliar("6003", "Luis", "Ing. Mecánica");
        verificar("La nueva solicitud entra bien (la siguiente es la de 6003)", "6003".equals(g5.siguienteSolicitudId()));

        System.out.println("\n[3.4] Usando la pila de nuevo después de vaciarla:");
        g5.vaciarHistorial();
        g5.darBajaEstudianteAuxiliar("Ing. Mecánica", "6003");
        verificar("La pila volvió a guardar un expediente", "Baja de estudiante".equals(g5.ultimoMotivo()));
    }

    public static void main(String[] args) {
        new Pruebas().ejecutar();
    }
}
