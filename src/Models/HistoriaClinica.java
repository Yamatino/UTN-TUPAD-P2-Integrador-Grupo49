
package Models;

/**
 *
 * @author matia
 */

public class HistoriaClinica {

    // 1. Enum Requerido para Grupo Sanguíneo (B+)
    public enum GrupoSanguineo {
        AP, AM, BP, BM, ABP, ABM, OP, OM // Correspondiente a A+, A-, B+, B-..
    }

    // 2. Atributos Requeridos
    private Long id; // PK
    private Boolean eliminado = false; 
    private String nroHistoria; // UNIQUE, máx. 20
    private GrupoSanguineo grupoSanguineo; // Enum
    private String antecedentes; // TEXT
    private String medicacionActual; // TEXT
    private String observaciones; // TEXT
    private Paciente paciente; // Relación con Paciente (requerido por Service)

    // 3. Constructores, Getters y Setters
    
    // Constructor Vacío
    public HistoriaClinica() {}

    // Constructor Completo (Opcional, pero recomendado)
    public HistoriaClinica(Long id, Boolean eliminado, String nroHistoria, GrupoSanguineo grupoSanguineo, String antecedentes, String medicacionActual, String observaciones, Paciente paciente) {
        this.id = id;
        this.eliminado = eliminado;
        this.nroHistoria = nroHistoria;
        this.grupoSanguineo = grupoSanguineo;
        this.antecedentes = antecedentes;
        this.medicacionActual = medicacionActual;
        this.observaciones = observaciones;
        this.paciente = paciente;
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
    
    public String getNroHistoria() {
        return nroHistoria;
    }
    
    public void setNroHistoria(String nroHistoria) {
        this.nroHistoria = nroHistoria;
    }
    
    public GrupoSanguineo getGrupoSanguineo() {
        return grupoSanguineo;
    }
    
    public void setGrupoSanguineo(GrupoSanguineo grupoSanguineo) {
        this.grupoSanguineo = grupoSanguineo;
    }
    
    public String getAntecedentes() {
        return antecedentes;
    }
    
    public void setAntecedentes(String antecedentes) {
        this.antecedentes = antecedentes;
    }
    
    public String getMedicacionActual() {
        return medicacionActual;
    }
    
    public void setMedicacionActual(String medicacionActual) {
        this.medicacionActual = medicacionActual;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public Paciente getPaciente() {
        return paciente;
    }
    
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    } 
  
    
    // Método toString() 
    @Override
    public String toString() {
        return "HistoriaClinica{" +
                "nroHistoria='" + nroHistoria + '\'' +
                ", grupoSanguineo=" + grupoSanguineo +
                ", eliminado=" + eliminado +
                '}';
    }
}
