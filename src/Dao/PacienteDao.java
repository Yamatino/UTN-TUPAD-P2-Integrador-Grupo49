/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import Config.DatabaseConnection;
import Models.Paciente;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para la entidad Paciente
 * Implementa las operaciones CRUD y métodos específicos para Paciente
 * 
 * @author Grupo 49
 */
public class PacienteDao implements GenericDao<Paciente> {
    
    // Consultas SQL preparadas
    private static final String INSERT_SQL = 
        "INSERT INTO paciente (nombre, apellido, dni, fecha_nacimiento, eliminado) VALUES (?, ?, ?, ?, ?)";
    
    private static final String SELECT_BY_ID_SQL = 
        "SELECT id, nombre, apellido, dni, fecha_nacimiento, eliminado FROM paciente WHERE id = ?";
    
    private static final String SELECT_ALL_SQL = 
        "SELECT id, nombre, apellido, dni, fecha_nacimiento, eliminado FROM paciente WHERE eliminado = false";
    
    private static final String UPDATE_SQL = 
        "UPDATE paciente SET nombre = ?, apellido = ?, dni = ?, fecha_nacimiento = ?, eliminado = ? WHERE id = ?";
    
    private static final String DELETE_LOGICAL_SQL = 
        "UPDATE paciente SET eliminado = true WHERE id = ?";
    
    private static final String SELECT_BY_DNI_SQL = 
        "SELECT id, nombre, apellido, dni, fecha_nacimiento, eliminado FROM paciente WHERE dni = ? AND eliminado = false";
    
    /**
     * Crea un nuevo paciente en la base de datos (sin conexión compartida)
     * 
     * @param entity El paciente a crear
     * @return El paciente creado con su ID generado
     * @throws Exception Si ocurre un error durante la creación
     */
    @Override
    public Paciente crear(Paciente entity) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection()) {
            return crear(entity, conn);
        }
    }
    
    /**
     * Crea un nuevo paciente usando una conexión compartida (para transacciones)
     * 
     * @param entity El paciente a crear
     * @param conn La conexión compartida
     * @return El paciente creado con su ID generado
     * @throws Exception Si ocurre un error durante la creación
     */
    @Override
    public Paciente crear(Paciente entity, Connection conn) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setString(1, entity.getNombre());
            pstmt.setString(2, entity.getApellido());
            pstmt.setString(3, entity.getDni());
            
            // Convertir LocalDate a java.sql.Date
            if (entity.getFechaNacimiento() != null) {
                pstmt.setDate(4, Date.valueOf(entity.getFechaNacimiento()));
            } else {
                pstmt.setNull(4, java.sql.Types.DATE);
            }
            
            pstmt.setBoolean(5, Boolean.TRUE.equals(entity.getEliminado()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new Exception("Error al crear el paciente, no se insertó ningún registro.");
            }
            
            // Obtener el ID generado
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getLong(1));
            } else {
                throw new Exception("Error al crear el paciente, no se obtuvo el ID.");
            }
            
            return entity;
            
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }
    }
    
    /**
     * Lee un paciente por su ID
     * IMPORTANTE: Lazy loading - NO carga la HistoriaClinica automáticamente
     * 
     * @param id El ID del paciente a buscar
     * @return El paciente encontrado, o null si no existe
     * @throws Exception Si ocurre un error durante la lectura
     */
    @Override
    public Paciente leer(Long id) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(SELECT_BY_ID_SQL);
            pstmt.setLong(1, id);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPaciente(rs);
            }
            
            return null;
            
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }
    
    /**
     * Lee todos los pacientes NO eliminados
     * 
     * @return Lista de pacientes activos (eliminado = false)
     * @throws Exception Si ocurre un error durante la lectura
     */
    @Override
    public List<Paciente> leerTodos() throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Paciente> pacientes = new ArrayList<>();
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(SELECT_ALL_SQL);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                pacientes.add(mapResultSetToPaciente(rs));
            }
            
            return pacientes;
            
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }
    
    /**
     * Actualiza un paciente existente
     * 
     * @param entity El paciente con los datos actualizados
     * @throws Exception Si ocurre un error durante la actualización
     */
    @Override
    public void actualizar(Paciente entity) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(UPDATE_SQL);
            
            pstmt.setString(1, entity.getNombre());
            pstmt.setString(2, entity.getApellido());
            pstmt.setString(3, entity.getDni());
            
            if (entity.getFechaNacimiento() != null) {
                pstmt.setDate(4, Date.valueOf(entity.getFechaNacimiento()));
            } else {
                pstmt.setNull(4, java.sql.Types.DATE);
            }
            
            pstmt.setBoolean(5, entity.getEliminado());
            pstmt.setLong(6, entity.getId());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new Exception("Error al actualizar el paciente, no se encontró el registro.");
            }
            
        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }
    
    /**
     * Realiza un borrado lógico del paciente (establece eliminado = true)
     * 
     * @param id El ID del paciente a eliminar
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
                throw new Exception("Error al eliminar el paciente, no se encontró el registro.");
            }
            
        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }
    
    /**
     * Busca un paciente por su DNI
     * Método adicional específico de PacienteDao
     * 
     * @param dni El DNI del paciente a buscar
     * @return El paciente encontrado, o null si no existe
     * @throws Exception Si ocurre un error durante la búsqueda
     */
    public Paciente buscarPorDni(String dni) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(SELECT_BY_DNI_SQL);
            pstmt.setString(1, dni);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPaciente(rs);
            }
            
            return null;
            
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }
    
    /**
     * Mapea un ResultSet a un objeto Paciente
     * Método auxiliar privado
     * 
     * @param rs El ResultSet con los datos del paciente
     * @return El objeto Paciente mapeado
     * @throws Exception Si ocurre un error durante el mapeo
     */
    private Paciente mapResultSetToPaciente(ResultSet rs) throws Exception {
        Paciente paciente = new Paciente();
        
        paciente.setId(rs.getLong("id"));
        paciente.setNombre(rs.getString("nombre"));
        paciente.setApellido(rs.getString("apellido"));
        paciente.setDni(rs.getString("dni"));
        
        // Convertir java.sql.Date a LocalDate
        Date fechaNacSQL = rs.getDate("fecha_nacimiento");
        if (fechaNacSQL != null) {
            paciente.setFechaNacimiento(fechaNacSQL.toLocalDate());
        }
        
        paciente.setEliminado(rs.getBoolean("eliminado"));
        
        // Lazy loading: NO cargamos la HistoriaClinica aquí
        // Si se necesita, debe cargarse explícitamente usando HistoriaClinicaDao
        
        return paciente;
    }
}

