
package Database;

/**
 *
 * @author matia
 */
package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    //  Reemplazar con las credenciales reales MySQL
    private static final String URL = "";
    private static final String USER = ""; 
    private static final String PASS = ""; 

    // Implementa el método getConnection() 
    public static Connection getConnection() throws SQLException {
         
        
        // Retorna la conexión usando DriverManager
        return DriverManager.getConnection(URL, USER, PASS);
    }
}