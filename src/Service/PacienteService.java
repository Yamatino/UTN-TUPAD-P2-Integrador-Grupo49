/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Config.DatabaseConnection;
import Dao.HistoriaClinicaDao; 
import Dao.PacienteDao;
import Models.HistoriaClinica;
import Models.Paciente;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PacienteService implements GenericService<Paciente> {

    // Instancias de los DAOs que va a usar
    private final PacienteDao pacienteDao;
    private final HistoriaClinicaDao historiaClinicaDao;

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
            try {
                pacienteCreado = pacienteDao.crear(paciente, conn);
            } catch (Exception e) {
                throw new ServiceException("Error al crear el paciente: " + e.getMessage(), e);
            }
            
            // Operación Compuesta (Parte 2: Crear HistoriaClinica B)
            HistoriaClinica hc = paciente.getHistoriaClinica();
            
            // Asignamos el Paciente (con su nuevo ID) a la HistoriaClinica
            hc.setPaciente(pacienteCreado); 

            // Llamamos al DAO
            HistoriaClinica hcCreada;
            try {
                hcCreada = historiaClinicaDao.crear(hc, conn);
            } catch (Exception e) {
                throw new ServiceException("Error al crear la historia clínica: " + e.getMessage(), e);
            }

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

    /**
     * Obtiene un paciente por su ID e incluye su historia clínica (si existe).
     *
     * @param id identificador del paciente
     * @return paciente con la historia clínica cargada
     * @throws ServiceException si ocurre un error
     */
    public Paciente getByIdConHistoria(Long id) throws ServiceException {
        Paciente paciente = getById(id);
        if (paciente != null) {
            adjuntarHistoriaClinica(paciente);
        }
        return paciente;
    }

    /**
     * Obtiene la lista completa de pacientes con sus historias clínicas (si
     * existen).
     *
     * @return listado de pacientes
     * @throws ServiceException si ocurre un error
     */
    public List<Paciente> getAllConHistoria() throws ServiceException {
        List<Paciente> pacientes = getAll();
        for (Paciente paciente : pacientes) {
            adjuntarHistoriaClinica(paciente);
        }
        return pacientes;
    }

    /**
     * Busca un paciente por su DNI.
     *
     * @param dni documento a buscar
     * @return paciente encontrado o null
     * @throws ServiceException si ocurre un error
     */
    public Paciente buscarPorDni(String dni) throws ServiceException {
        if (dni == null || dni.trim().isEmpty()) {
            throw new ServiceException("El DNI no puede estar vacío.");
        }
        try {
            Paciente paciente = pacienteDao.buscarPorDni(dni.trim());
            if (paciente != null) {
                adjuntarHistoriaClinica(paciente);
            }
            return paciente;
        } catch (Exception e) {
            throw new ServiceException("Error al buscar paciente por DNI: " + e.getMessage(), e);
        }
    }

    /**
     * Verifica y adjunta la historia clínica asociada al paciente (lazy
     * loading).
     */
    private void adjuntarHistoriaClinica(Paciente paciente) throws ServiceException {
        if (paciente == null || paciente.getId() == null) {
            return;
        }
        try {
            HistoriaClinica hc = historiaClinicaDao.buscarPorPacienteId(paciente.getId());
            paciente.setHistoriaClinica(hc);
        } catch (Exception e) {
            throw new ServiceException("Error al cargar la historia clínica del paciente: " + e.getMessage(), e);
        }
    }
}