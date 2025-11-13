/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Dao;

import java.sql.Connection;
import java.util.List;

/**
 * Interfaz genérica para operaciones CRUD (Create, Read, Update, Delete)
 * Esta interfaz define el contrato que deben implementar todos los DAOs
 * 
 * @param <T> El tipo de entidad que manejará el DAO
 * @author Grupo 49
 */
public interface GenericDao<T> {
    
    /**
     * Crea una nueva entidad en la base de datos
     * Método sin conexión compartida (crea su propia conexión)
     * 
     * @param entity La entidad a crear
     * @return La entidad creada con su ID generado
     * @throws Exception Si ocurre un error durante la creación
     */
    T crear(T entity) throws Exception;
    
    /**
     * Crea una nueva entidad en la base de datos usando una conexión compartida
     * Este método es útil para transacciones que involucran múltiples operaciones
     * 
     * @param entity La entidad a crear
     * @param conn La conexión compartida para transacciones
     * @return La entidad creada con su ID generado
     * @throws Exception Si ocurre un error durante la creación
     */
    T crear(T entity, Connection conn) throws Exception;
    
    /**
     * Lee una entidad de la base de datos por su ID
     * 
     * @param id El ID de la entidad a buscar
     * @return La entidad encontrada, o null si no existe
     * @throws Exception Si ocurre un error durante la lectura
     */
    T leer(Long id) throws Exception;
    
    /**
     * Lee todas las entidades de la base de datos
     * IMPORTANTE: Solo retorna entidades con eliminado = false (borrado lógico)
     * 
     * @return Lista de entidades no eliminadas
     * @throws Exception Si ocurre un error durante la lectura
     */
    List<T> leerTodos() throws Exception;
    
    /**
     * Actualiza una entidad existente en la base de datos
     * 
     * @param entity La entidad con los datos actualizados
     * @throws Exception Si ocurre un error durante la actualización
     */
    void actualizar(T entity) throws Exception;
    
    /**
     * Realiza un borrado lógico de la entidad (establece eliminado = true)
     * No elimina físicamente el registro de la base de datos
     * 
     * @param id El ID de la entidad a eliminar lógicamente
     * @throws Exception Si ocurre un error durante la eliminación
     */
    void eliminar(Long id) throws Exception;
}

