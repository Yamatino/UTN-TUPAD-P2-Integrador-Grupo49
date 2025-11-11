
package HistoriaClinica;

/**
 *
 * @author matia
 */
package entities;

import java.time.LocalDate;

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

    // 3. Constructores, Getters y Setters
    
    // Constructor Vacío
    public HistoriaClinica() {}

    // Constructor Completo (Opcional, pero recomendado)
    public HistoriaClinica(Long id, Boolean eliminado, String nroHistoria, GrupoSanguineo grupoSanguineo, String antecedentes, String medicacionActual, String observaciones) {
        this.id = id;
        this.eliminado = eliminado;
        this.nroHistoria = nroHistoria;
        this.grupoSanguineo = grupoSanguineo;
        this.antecedentes = antecedentes;
        this.medicacionActual = medicacionActual;
        this.observaciones = observaciones;
    }
    
    // Getters y Setters 
  
    
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
