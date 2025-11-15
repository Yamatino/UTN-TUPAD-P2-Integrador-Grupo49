/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Dao.HistoriaClinicaDao;
import Models.HistoriaClinica;
import Models.Paciente;
import java.util.List;

/**
 * Servicio para gestionar la lógica de negocio de las Historias Clínicas.
 *
 * Permite realizar operaciones CRUD, validaciones y búsquedas específicas
 * relacionadas con la entidad HistoriaClinica.
 *
 * @author Grupo 49
 */
public class HistoriaClinicaService implements GenericService<HistoriaClinica> {

    private final HistoriaClinicaDao historiaClinicaDao;

    public HistoriaClinicaService() {
        this.historiaClinicaDao = new HistoriaClinicaDao();
    }

    @Override
    public HistoriaClinica insertar(HistoriaClinica historiaClinica) throws ServiceException {
        validarHistoriaClinica(historiaClinica, true);
        try {
            return historiaClinicaDao.crear(historiaClinica);
        } catch (Exception e) {
            throw new ServiceException("Error al crear la historia clínica: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizar(HistoriaClinica historiaClinica) throws ServiceException {
        validarHistoriaClinica(historiaClinica, false);
        try {
            historiaClinicaDao.actualizar(historiaClinica);
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar la historia clínica: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(Long id) throws ServiceException {
        if (id == null || id <= 0) {
            throw new ServiceException("El ID de la historia clínica no es válido.");
        }
        try {
            historiaClinicaDao.eliminar(id);
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar la historia clínica: " + e.getMessage(), e);
        }
    }

    @Override
    public HistoriaClinica getById(Long id) throws ServiceException {
        if (id == null || id <= 0) {
            throw new ServiceException("El ID de la historia clínica no es válido.");
        }
        try {
            return historiaClinicaDao.leer(id);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar la historia clínica: " + e.getMessage(), e);
        }
    }

    @Override
    public List<HistoriaClinica> getAll() throws ServiceException {
        try {
            return historiaClinicaDao.leerTodos();
        } catch (Exception e) {
            throw new ServiceException("Error al obtener las historias clínicas: " + e.getMessage(), e);
        }
    }

    /**
     * Busca una historia clínica asociada a un paciente específico.
     *
     * @param pacienteId ID del paciente
     * @return Historia clínica encontrada o null
     * @throws ServiceException si ocurre un error
     */
    public HistoriaClinica buscarPorPacienteId(Long pacienteId) throws ServiceException {
        if (pacienteId == null || pacienteId <= 0) {
            throw new ServiceException("El ID de paciente no es válido.");
        }
        try {
            return historiaClinicaDao.buscarPorPacienteId(pacienteId);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar la historia clínica por paciente: " + e.getMessage(), e);
        }
    }

    private void validarHistoriaClinica(HistoriaClinica hc, boolean esCreacion) throws ServiceException {
        if (hc == null) {
            throw new ServiceException("La historia clínica no puede ser nula.");
        }

        if (hc.getNroHistoria() == null || hc.getNroHistoria().trim().isEmpty() || hc.getNroHistoria().length() > 20) {
            throw new ServiceException("El número de historia clínica es obligatorio y debe tener hasta 20 caracteres.");
        }

        if (hc.getGrupoSanguineo() == null) {
            throw new ServiceException("El grupo sanguíneo es obligatorio.");
        }

        Paciente paciente = hc.getPaciente();
        if (paciente == null || paciente.getId() == null) {
            throw new ServiceException("La historia clínica debe estar asociada a un paciente existente.");
        }

        if (!esCreacion && hc.getId() == null) {
            throw new ServiceException("El ID de la historia clínica es obligatorio para actualizar.");
        }
    }
}

