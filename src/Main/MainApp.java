package Main;

import Config.DatabaseConnection;

/**
 * Punto de entrada de la aplicación.
 *
 * Invoca al menú de consola (AppMenu) luego de verificar la conexión a la base
 * de datos.
 */
public class MainApp {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("  Sistema de Gestión de Pacientes - Clínica");
        System.out.println("=================================================\n");

        System.out.println("Verificando conexión a la base de datos...");
        if (!DatabaseConnection.testConnection()) {
            System.err.println("✗ No se pudo establecer la conexión.");
            System.err.println("Revisa las credenciales en Config/DatabaseConnection.java");
            System.err.println("y asegúrate de que MySQL esté disponible (puerto 3307).");
            return;
        }

        System.out.println("✓ Conexión exitosa. Iniciando menú interactivo...\n");
        AppMenu menu = new AppMenu();
        menu.start();
    }
}

