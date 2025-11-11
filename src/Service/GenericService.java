/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.util.List;

// Interfaz genérica
public interface GenericService<T> {
    
    T insertar(T t) throws ServiceException; 
    
    void actualizar(T t) throws ServiceException; 
    
    void eliminar(Long id) throws ServiceException; 
    
    T getById(Long id) throws ServiceException; 
    
    List<T> getAll() throws ServiceException; 
    
    public class ServiceException extends Exception {
        public ServiceException(String message) {
            super(message);
        }
        public ServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}