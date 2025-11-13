
package Config;

/**
 * Clase para gestionar la conexión a la base de datos MySQL
 * 
 * @author matia
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    
    // ========================================
    // Configuración de la base de datos MySQL
    // ========================================
    
    // IMPORTANTE: Reemplazar con las credenciales reales de tu servidor MySQL
    // URL formato: jdbc:mysql://[host]:[puerto]/[nombre_base_datos]?parámetros
    private static final String URL = "jdbc:mysql://localhost:3307/clinica_db?useSSL=false&serverTimezone=UTC";
    
    // Usuario de MySQL (por defecto "root", cambiar según tu configuración)
    private static final String USER = "root"; 
    
    // Contraseña de MySQL (DEBE configurarse según tu instalación)
    // IMPORTANTE: En producción, nunca dejar contraseñas en el código
    // Considerar usar variables de entorno o archivos de configuración externos
    private static final String PASS = "hola123"; 

    /**
     * Obtiene una conexión a la base de datos
     * 
     * @return Connection objeto de conexión a la base de datos
     * @throws SQLException si ocurre un error al conectar
     */
    public static Connection getConnection() throws SQLException {
        // Retorna la conexión usando DriverManager
        return DriverManager.getConnection(URL, USER, PASS);
    }
    
    /**
     * Método auxiliar para verificar la conexión a la base de datos
     * Útil para pruebas
     * 
     * @return true si la conexión es exitosa, false en caso contrario
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            return false;
        }
    }
}
