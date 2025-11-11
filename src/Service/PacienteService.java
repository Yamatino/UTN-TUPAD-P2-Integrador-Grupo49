/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import config.DatabaseConnection; 
import dao.HistoriaClinicaDao; 
import dao.PacienteDao;
import entities.HistoriaClinica;
import entities.Paciente;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

// Importamos la excepción custom
import static service.GenericService.ServiceException;

public class PacienteService implements GenericService<Paciente> {

    // Instancias de los DAOs que va a usar
    private PacienteDao pacienteDao;
    private HistoriaClinicaDao historiaClinicaDao;

    public PacienteService() {
        this.pacienteDao = new PacienteDao();
        this.historiaClinicaDao = new HistoriaClinicaDao();
    }

    // Este método crea el Paciente (A) Y su HistoriaClinica (B)
    // juntos, o no crea ninguno.
    
    public Paciente insertarPacienteCompleto(Paciente paciente) throws ServiceException {
        
        // Validaciones de lógica de negocio
        validarPaciente(paciente);
        if (paciente.getHistoriaClinica() == null) {
            throw new ServiceException("El paciente debe tener una historia clínica asociada.");
        }
        validarHistoriaClinica(paciente.getHistoriaClinica());
        
        //Obtener la Conexión (será compartida)
        Connection conn = null;
        Paciente pacienteCreado = null;

        try {
            conn = DatabaseConnection.getConnection(); 

            // --- INICIO DE LA TRANSACCIÓN ---
            conn.setAutoCommit(false);

            // Operación Compuesta (Parte 1: Crear Paciente A)
            // Asumimos que el DAO devuelve el paciente con el ID generado
            pacienteCreado = pacienteDao.crear(paciente, conn);
            
            // Operación Compuesta (Parte 2: Crear HistoriaClinica B)
            HistoriaClinica hc = paciente.getHistoriaClinica();
            
            // Asignamos el Paciente (con su nuevo ID) a la HistoriaClinica
            hc.setPaciente(pacienteCreado); 

            // Llamamos al DAO
            HistoriaClinica hcCreada = historiaClinicaDao.crear(hc, conn);

            // --- FIN DE LA TRANSACCIÓN (COMMIT) ---
            conn.commit();
            
            // Devolvemos el objeto completo
            pacienteCreado.setHistoriaClinica(hcCreada);
            return pacienteCreado;

        } catch (SQLException | ServiceException e) {
            
            // --- ERROR: ROLLBACK ---
            if (conn != null) {
                try {
                    System.err.println("Transacción fallida. Ejecutando rollback...");
                    conn.rollback(); // [Requerimiento 5]
                } catch (SQLException ex) {
                    System.err.println("Error al intentar hacer rollback: " + ex.getMessage());
                }
            }
            // Lanzamos la excepción para que la capa superior se entere
            throw new ServiceException("Error al insertar paciente completo: " + e.getMessage(), e);
            
        } finally {
            // --- RESTABLECER Y CERRAR ---
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // [Requerimiento 5]
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }
    }

    // --- Implementación de Validaciones ---
    
    public void validarPaciente(Paciente paciente) throws ServiceException {
        if (paciente == null) {
            throw new ServiceException("El objeto Paciente no puede ser nulo.");
        }
        if (paciente.getNombre() == null || paciente.getNombre().trim().isEmpty() || paciente.getNombre().length() > 80) { 
            throw new ServiceException("Nombre de paciente inválido.");
        }
        if (paciente.getApellido() == null || paciente.getApellido().trim().isEmpty() || paciente.getApellido().length() > 80) {
            throw new ServiceException("Apellido de paciente inválido.");
        }
        if (paciente.getDni() == null || paciente.getDni().trim().isEmpty() || paciente.getDni().length() > 15) { 
            throw new ServiceException("DNI de paciente inválido.");
        }
        
        // Validación 1:1 (Regla de negocio)
        // El diagrama  pide un 'buscarPorDni'
        // Lo usamos para evitar duplicados (aunque la BD ya tiene un UNIQUE
        // Paciente pExistente = pacienteDao.buscarPorDni(paciente.getDni());
        // if (pExistente != null && (paciente.getId() == null || !pExistente.getId().equals(paciente.getId()))) {
        //     throw new ServiceException("El DNI " + paciente.getDni() + " ya está registrado.");
        // }
    }

    private void validarHistoriaClinica(HistoriaClinica hc) throws ServiceException {
        if (hc.getNroHistoria() == null || hc.getNroHistoria().trim().isEmpty() || hc.getNroHistoria().length() > 20) { // [cite: 19]
            throw new ServiceException("Número de historia clínica inválido.");
        }
        if (hc.getGrupoSanguineo() == null) {
            throw new ServiceException("El grupo sanguíneo es obligatorio.");
        }
        
        // Validación 1:1 (Regla de negocio "impedir más de un B por A")
        // La base de datos ya lo impide con un UNIQUE en 'paciente_id'
        // pero podemos chequearlo aquí si el DAO 'buscarPorNroHistoria' existiera.
    }


    // --- Otros métodos del CRUD (pueden o no ser transaccionales) ---
    // Estos métodos (requeridos por GenericService) pueden llamar a los
    // DAOs simples (sin conexión compartida).

    @Override
    public Paciente insertar(Paciente t) throws ServiceException {
        // NO se debe usar este método para la "inserción completa".
        // Podría usarse solo si se permite crear un paciente sin H.C.
        throw new ServiceException("Use 'insertarPacienteCompleto' para crear un paciente con su H.C.");
    }

    @Override
    public void actualizar(Paciente paciente) throws ServiceException {
        try {
            validarPaciente(paciente);
            // El 'actualizar' del DAO no necesita transacción
            pacienteDao.actualizar(paciente);
            
            // Opcional: ¿Se puede actualizar la H.C. también?
            if (paciente.getHistoriaClinica() != null) {
                validarHistoriaClinica(paciente.getHistoriaClinica());
                historiaClinicaDao.actualizar(paciente.getHistoriaClinica());
            }
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar paciente: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(Long id) throws ServiceException {
        // Esto se refiere al borrado lógico
        try {
            // El diagrama  pide un 'eliminarLogico'
            // que probablemente llame a pacienteDao.eliminar(id)
            pacienteDao.eliminar(id);
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar (lógica) paciente: " + e.getMessage(), e);
        }
    }

    @Override
    public Paciente getById(Long id) throws ServiceException {
        try {
            // Aquí hay que decidir si se trae solo el Paciente
            // o si también se carga su HistoriaClinica (Lazy vs Eager)
            return pacienteDao.leer(id);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar paciente por ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Paciente> getAll() throws ServiceException {
        try {
            // [cite: 47]
            return pacienteDao.leerTodos();
        } catch (Exception e) {
            throw new ServiceException("Error al obtener todos los pacientes: " + e.getMessage(), e);
        }
    }
}