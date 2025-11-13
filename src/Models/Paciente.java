
package Models;

/**
 *
 * @author matia
 */

import java.time.LocalDate;

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
    
    // Constructor con parámetros
    public Paciente(Long id, Boolean eliminado, String nombre, String apellido, String dni, LocalDate fechaNacimiento, HistoriaClinica historiaClinica) {
        this.id = id;
        this.eliminado = eliminado;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.historiaClinica = historiaClinica;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Boolean getEliminado() {
        return eliminado;
    }
    
    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getApellido() {
        return apellido;
    }
    
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    public String getDni() {
        return dni;
    }
    
    public void setDni(String dni) {
        this.dni = dni;
    }
    
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    
    public HistoriaClinica getHistoriaClinica() {
        return historiaClinica;
    }
    
    public void setHistoriaClinica(HistoriaClinica historiaClinica) {
        this.historiaClinica = historiaClinica;
    }
   
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
