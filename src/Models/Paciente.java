
package Paciente;

/**
 *
 * @author matia
 */
package entities;

import java.time.LocalDate;
import entities.HistoriaClinica; // Importamos la clase B

public class Paciente {

    // 1. Atributos Requeridos
    private Long id; // PK
    private Boolean eliminado = false; 
    private String nombre; // NOT NULL, máx. 80
    private String apellido; // NOT NULL, máx. 80
    private String dni; // NOT NULL, UNIQUE, máx. 15
    private LocalDate fechaNacimiento;
    
    // 2. Relación 1-1 Unidireccional (A referencia a B)
    private HistoriaClinica historiaClinica; 

    // 3. Constructores, Getters y Setters
    
    // Constructor Vacío
    public Paciente() {}
    
    
    
   
    @Override
    public String toString() {
        return "Paciente{" +
                "id=" + id +
                ", dni='" + dni + '\'' +
                ", nombre='" + nombre + " " + apellido + '\'' +
                ", historiaClinica=" + (historiaClinica != null ? historiaClinica.getNroHistoria() : "N/A") +
                ", eliminado=" + eliminado +
                '}';
    }
}
