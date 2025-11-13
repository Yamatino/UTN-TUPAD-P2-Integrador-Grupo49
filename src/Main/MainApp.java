package Main;

import Config.DatabaseConnection;
import Models.HistoriaClinica;
import Models.HistoriaClinica.GrupoSanguineo;
import Models.Paciente;
import Service.GenericService.ServiceException;
import Service.PacienteService;
import java.time.LocalDate;
import java.util.List;

/**
 * Clase principal para probar el sistema de gestión de pacientes
 * 
 * @author Grupo 49
 */
public class MainApp {
    
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("  Sistema de Gestión de Pacientes - Clínica");
        System.out.println("=================================================\n");
        
        // 1. Probar conexión a la base de datos
        System.out.println("1. Probando conexión a la base de datos...");
        if (DatabaseConnection.testConnection()) {
            System.out.println("   ✓ Conexión exitosa a la base de datos\n");
        } else {
            System.err.println("   ✗ Error: No se pudo conectar a la base de datos");
            System.err.println("   Verifica las credenciales en DatabaseConnection.java");
            System.err.println("   y asegúrate de que el servidor MySQL esté corriendo.\n");
            return;
        }
        
        // 2. Instanciar el servicio
        PacienteService pacienteService = new PacienteService();
        
        try {
            // 3. Crear un nuevo paciente con su historia clínica
            System.out.println("2. Creando un nuevo paciente con historia clínica...");
            
            // Generar un DNI único usando timestamp
            String dniUnico = String.valueOf(System.currentTimeMillis()).substring(3, 11);
            String nroHistoriaUnico = "HC-" + System.currentTimeMillis();
            
            // Crear el paciente
            Paciente nuevoPaciente = new Paciente();
            nuevoPaciente.setNombre("Juan");
            nuevoPaciente.setApellido("Pérez");
            nuevoPaciente.setDni(dniUnico);
            nuevoPaciente.setFechaNacimiento(LocalDate.of(1990, 5, 15));
            nuevoPaciente.setEliminado(false);
            
            // Crear la historia clínica
            HistoriaClinica nuevaHC = new HistoriaClinica();
            nuevaHC.setNroHistoria(nroHistoriaUnico);
            nuevaHC.setGrupoSanguineo(GrupoSanguineo.AP);
            nuevaHC.setAntecedentes("Ninguno");
            nuevaHC.setMedicacionActual("Ninguna");
            nuevaHC.setObservaciones("Paciente en buen estado general");
            nuevaHC.setEliminado(false);
            
            // Asociar la historia clínica al paciente
            nuevoPaciente.setHistoriaClinica(nuevaHC);
            
            // Insertar usando transacción
            Paciente pacienteCreado = pacienteService.insertarPacienteCompleto(nuevoPaciente);
            
            System.out.println("   ✓ Paciente creado con éxito:");
            System.out.println("      ID: " + pacienteCreado.getId());
            System.out.println("      Nombre: " + pacienteCreado.getNombre() + " " + pacienteCreado.getApellido());
            System.out.println("      DNI: " + pacienteCreado.getDni() + " (generado automáticamente)");
            System.out.println("      Historia Clínica: " + pacienteCreado.getHistoriaClinica().getNroHistoria());
            System.out.println();
            
            // 4. Listar todos los pacientes
            System.out.println("3. Listando todos los pacientes...");
            List<Paciente> pacientes = pacienteService.getAll();
            System.out.println("   Total de pacientes: " + pacientes.size());
            for (Paciente p : pacientes) {
                System.out.println("   - " + p.getNombre() + " " + p.getApellido() + " (DNI: " + p.getDni() + ")");
            }
            System.out.println();
            
            // 5. Buscar paciente por ID
            System.out.println("4. Buscando paciente por ID...");
            Paciente pacienteEncontrado = pacienteService.getById(pacienteCreado.getId());
            if (pacienteEncontrado != null) {
                System.out.println("   ✓ Paciente encontrado:");
                System.out.println("      " + pacienteEncontrado.getNombre() + " " + pacienteEncontrado.getApellido());
            }
            System.out.println();
            
            // 6. Actualizar paciente
            System.out.println("5. Actualizando datos del paciente...");
            pacienteEncontrado.setApellido("Pérez Actualizado");
            pacienteService.actualizar(pacienteEncontrado);
            System.out.println("   ✓ Paciente actualizado con éxito\n");
            
            // 7. Eliminar lógicamente
            System.out.println("6. Realizando borrado lógico del paciente...");
            pacienteService.eliminar(pacienteCreado.getId());
            System.out.println("   ✓ Paciente eliminado (borrado lógico)\n");
            
            // 8. Verificar que no aparece en la lista
            System.out.println("7. Verificando que el paciente eliminado no aparece en la lista...");
            List<Paciente> pacientesDespues = pacienteService.getAll();
            System.out.println("   Total de pacientes activos: " + pacientesDespues.size());
            System.out.println("   (El paciente eliminado no debería aparecer)\n");
            
            System.out.println("=================================================");
            System.out.println("  ✓ Todas las pruebas completadas con éxito");
            System.out.println("=================================================");
            
        } catch (ServiceException e) {
            System.err.println("\n✗ Error en el servicio: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("\n✗ Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

