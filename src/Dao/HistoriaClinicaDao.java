/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Config.DatabaseConnection;
import Models.HistoriaClinica;
import Models.HistoriaClinica.GrupoSanguineo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para la entidad HistoriaClinica
 * Implementa las operaciones CRUD y métodos específicos para HistoriaClinica
 * 
 * @author Grupo 49
 */
public class HistoriaClinicaDao implements GenericDao<HistoriaClinica> {
    
    // Consultas SQL preparadas
    private static final String INSERT_SQL = 
        "INSERT INTO historia_clinica (nro_historia, grupo_sanguineo, antecedentes, medicacion_actual, observaciones, paciente_id, eliminado) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    private static final String SELECT_BY_ID_SQL = 
        "SELECT id, nro_historia, grupo_sanguineo, antecedentes, medicacion_actual, observaciones, paciente_id, eliminado " +
        "FROM historia_clinica WHERE id = ?";
    
    private static final String SELECT_ALL_SQL = 
        "SELECT id, nro_historia, grupo_sanguineo, antecedentes, medicacion_actual, observaciones, paciente_id, eliminado " +
        "FROM historia_clinica WHERE eliminado = false";
    
    private static final String UPDATE_SQL = 
        "UPDATE historia_clinica SET nro_historia = ?, grupo_sanguineo = ?, antecedentes = ?, " +
        "medicacion_actual = ?, observaciones = ?, paciente_id = ?, eliminado = ? WHERE id = ?";
    
    private static final String DELETE_LOGICAL_SQL = 
        "UPDATE historia_clinica SET eliminado = true WHERE id = ?";
    
    private static final String SELECT_BY_PACIENTE_ID_SQL = 
        "SELECT id, nro_historia, grupo_sanguineo, antecedentes, medicacion_actual, observaciones, paciente_id, eliminado " +
        "FROM historia_clinica WHERE paciente_id = ? AND eliminado = false";
    
    /**
     * Crea una nueva historia clínica en la base de datos (sin conexión compartida)
     * 
     * @param entity La historia clínica a crear
     * @return La historia clínica creada con su ID generado
     * @throws Exception Si ocurre un error durante la creación
     */
    @Override
    public HistoriaClinica crear(HistoriaClinica entity) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return crear(entity, conn);
        }
    }
    
    /**
     * Crea una nueva historia clínica usando una conexión compartida (para transacciones)
     * 
     * @param entity La historia clínica a crear
     * @param conn La conexión compartida
     * @return La historia clínica creada con su ID generado
     * @throws Exception Si ocurre un error durante la creación
     */
    @Override
    public HistoriaClinica crear(HistoriaClinica entity, Connection conn) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setString(1, entity.getNroHistoria());
            
            // Convertir el ENUM a String para MySQL
            if (entity.getGrupoSanguineo() != null) {
                pstmt.setString(2, entity.getGrupoSanguineo().name());
            } else {
                throw new Exception("El grupo sanguíneo es obligatorio.");
            }
            
            pstmt.setString(3, entity.getAntecedentes());
            pstmt.setString(4, entity.getMedicacionActual());
            pstmt.setString(5, entity.getObservaciones());
            
            // paciente_id es obligatorio
            if (entity.getPaciente() != null && entity.getPaciente().getId() != null) {
                pstmt.setLong(6, entity.getPaciente().getId());
            } else {
                throw new Exception("El ID del paciente es obligatorio para crear una historia clínica.");
            }
            
            pstmt.setBoolean(7, Boolean.TRUE.equals(entity.getEliminado()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new Exception("Error al crear la historia clínica, no se insertó ningún registro.");
            }
            
            // Obtener el ID generado
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getLong(1));
            } else {
                throw new Exception("Error al crear la historia clínica, no se obtuvo el ID.");
            }
            
            return entity;
            
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }
    }
    
    /**
     * Lee una historia clínica por su ID
     * 
     * @param id El ID de la historia clínica a buscar
     * @return La historia clínica encontrada, o null si no existe
     * @throws Exception Si ocurre un error durante la lectura
     */
    @Override
    public HistoriaClinica leer(Long id) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(SELECT_BY_ID_SQL);
            pstmt.setLong(1, id);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToHistoriaClinica(rs);
            }
            
            return null;
            
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }
    
    /**
     * Lee todas las historias clínicas NO eliminadas
     * 
     * @return Lista de historias clínicas activas (eliminado = false)
     * @throws Exception Si ocurre un error durante la lectura
     */
    @Override
    public List<HistoriaClinica> leerTodos() throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<HistoriaClinica> historiasClinicas = new ArrayList<>();
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(SELECT_ALL_SQL);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                historiasClinicas.add(mapResultSetToHistoriaClinica(rs));
            }
            
            return historiasClinicas;
            
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }
    
    /**
     * Actualiza una historia clínica existente
     * 
     * @param entity La historia clínica con los datos actualizados
     * @throws Exception Si ocurre un error durante la actualización
     */
    @Override
    public void actualizar(HistoriaClinica entity) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(UPDATE_SQL);
            
            pstmt.setString(1, entity.getNroHistoria());
            
            // Convertir el ENUM a String para MySQL
            if (entity.getGrupoSanguineo() != null) {
                pstmt.setString(2, entity.getGrupoSanguineo().name());
            } else {
                throw new Exception("El grupo sanguíneo es obligatorio.");
            }
            
            pstmt.setString(3, entity.getAntecedentes());
            pstmt.setString(4, entity.getMedicacionActual());
            pstmt.setString(5, entity.getObservaciones());
            
            if (entity.getPaciente() != null && entity.getPaciente().getId() != null) {
                pstmt.setLong(6, entity.getPaciente().getId());
            } else {
                throw new Exception("El ID del paciente es obligatorio.");
            }
            
            pstmt.setBoolean(7, entity.getEliminado());
            pstmt.setLong(8, entity.getId());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new Exception("Error al actualizar la historia clínica, no se encontró el registro.");
            }
            
        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }
    
    /**
     * Realiza un borrado lógico de la historia clínica (establece eliminado = true)
     * 
     * @param id El ID de la historia clínica a eliminar
     * @throws Exception Si ocurre un error durante la eliminación
     */
    @Override
    public void eliminar(Long id) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(DELETE_LOGICAL_SQL);
            pstmt.setLong(1, id);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new Exception("Error al eliminar la historia clínica, no se encontró el registro.");
            }
            
        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }
    
    /**
     * Busca una historia clínica por el ID del paciente
     * Método adicional específico de HistoriaClinicaDao
     * 
     * @param pacienteId El ID del paciente
     * @return La historia clínica encontrada, o null si no existe
     * @throws Exception Si ocurre un error durante la búsqueda
     */
    public HistoriaClinica buscarPorPacienteId(Long pacienteId) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(SELECT_BY_PACIENTE_ID_SQL);
            pstmt.setLong(1, pacienteId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToHistoriaClinica(rs);
            }
            
            return null;
            
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }
    
    /**
     * Mapea un ResultSet a un objeto HistoriaClinica
     * Método auxiliar privado
     * 
     * @param rs El ResultSet con los datos de la historia clínica
     * @return El objeto HistoriaClinica mapeado
     * @throws Exception Si ocurre un error durante el mapeo
     */
    private HistoriaClinica mapResultSetToHistoriaClinica(ResultSet rs) throws Exception {
        HistoriaClinica hc = new HistoriaClinica();
        
        hc.setId(rs.getLong("id"));
        hc.setNroHistoria(rs.getString("nro_historia"));
        
        // Convertir String de MySQL a ENUM de Java
        String grupoSangStr = rs.getString("grupo_sanguineo");
        if (grupoSangStr != null) {
            hc.setGrupoSanguineo(GrupoSanguineo.valueOf(grupoSangStr));
        }
        
        hc.setAntecedentes(rs.getString("antecedentes"));
        hc.setMedicacionActual(rs.getString("medicacion_actual"));
        hc.setObservaciones(rs.getString("observaciones"));
        hc.setEliminado(rs.getBoolean("eliminado"));
        
        // Obtener el paciente_id (sin cargar el objeto Paciente completo - lazy loading)
        // Si se necesita el objeto Paciente completo, debe obtenerse por separado
        Long pacienteId = rs.getLong("paciente_id");
        if (pacienteId > 0) {
            // Crear un objeto Paciente solo con el ID
            Models.Paciente paciente = new Models.Paciente();
            paciente.setId(pacienteId);
            hc.setPaciente(paciente);
        }
        
        return hc;
    }
}

