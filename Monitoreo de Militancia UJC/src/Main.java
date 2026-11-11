import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        GestorUJC gestor = new GestorUJC("Facultad de Ciencias Técnicas");

        gestor.agregarCarrera("Ing. Informática");
        gestor.agregarCarrera("Ing. Telecomunicaciones y Electrónica");
        gestor.agregarCarrera("Ing. Mecánica");
        gestor.agregarCarrera("Ing. Geológica");
        gestor.agregarCarrera("Ing. Industrial");
        gestor.agregarCarrera("Otra");

        boolean salir = false;

        try {
            while (!salir) {
                System.out.println("\n--- MENÚ ---");
                System.out.println("1. Agregar Estudiante");
                System.out.println("2. Dar de baja de la UJC");
                System.out.println("3. Dar de baja a un Estudiante");
                System.out.println("4. Ver Facultad");
                System.out.println("5. Evaluar Carreras con Alta Militancia");
                System.out.println("6. Ver último trámite registrado");
                System.out.println("7. Ver historial completo");
                System.out.println("8. Vaciar historial");
                System.out.println("9. Solicitar ingreso a la UJC");
                System.out.println("10. Procesar solicitud de ingreso a la UJC");
                System.out.println("11. Ver siguiente solicitud de ingreso");
                System.out.println("12. Ejecutar casos de prueba");
                System.out.println("13. Salir");
                System.out.print("Opción: ");
                int opcion = gestor.validarOpcion(in, 1, 13);

                switch (opcion) {
                    case 1:
                        gestor.agregarEstudiante(in);
                        break;
                    case 2:
                        gestor.darBajaUJC(in);
                        break;
                    case 3:
                        gestor.darBajaEstudiante(in);
                        break;
                    case 4:
                        gestor.imprimirFacultad();
                        break;
                    case 5:
                        gestor.evaluarCarrerasAltaMilitancia();
                        break;
                    case 6:
                        gestor.verUltimoExpediente();
                        break;
                    case 7:
                        gestor.verHistorialCompleto();
                        break;
                    case 8:
                        gestor.vaciarHistorial();
                        break;
                    case 9:
                        gestor.solicitarIngreso(in);
                        break;
                    case 10:
                        gestor.procesarSolicitudIngreso();
                        break;
                    case 11:
                        gestor.verSiguienteSolicitud();
                        break;
                    case 12:
                        new Pruebas().ejecutar();
                        break;
                    case 13:
                        salir = true;
                        break;
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("\nFin de la entrada de datos. Cerrando el programa.");
        }

        System.out.println("Hasta luego.");
        in.close();
    }
}
