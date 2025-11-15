package Main;

import Models.HistoriaClinica;
import Models.HistoriaClinica.GrupoSanguineo;
import Models.Paciente;
import Service.GenericService.ServiceException;
import Service.HistoriaClinicaService;
import Service.PacienteService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Menú de consola principal del sistema.
 *
 * Permite gestionar Pacientes (A) e Historias Clínicas (B) con operaciones CRUD
 * completas, búsqueda por DNI, manejo de entradas inválidas y mensajes claros.
 *
 * @author Grupo
 */
public class AppMenu {

    private final Scanner scanner;
    private final PacienteService pacienteService;
    private final HistoriaClinicaService historiaClinicaService;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public AppMenu() {
        this.scanner = new Scanner(System.in);
        this.pacienteService = new PacienteService();
        this.historiaClinicaService = new HistoriaClinicaService();
    }

    public void start() {
        boolean salir = false;
        while (!salir) {
            mostrarMenuPrincipal();
            String opcion = leerLinea("Seleccione una opción: ").toUpperCase(Locale.ROOT);
            try {
                switch (opcion) {
                    case "1":
                        crearPacienteCompleto();
                        break;
                    case "2":
                        listarPacientes();
                        break;
                    case "3":
                        buscarPacientePorId();
                        break;
                    case "4":
                        actualizarPaciente();
                        break;
                    case "5":
                        eliminarPaciente();
                        break;
                    case "6":
                        buscarPacientePorDni();
                        break;
                    case "7":
                        crearHistoriaClinicaParaPaciente();
                        break;
                    case "8":
                        listarHistoriasClinicas();
                        break;
                    case "9":
                        buscarHistoriaPorId();
                        break;
                    case "10":
                        actualizarHistoriaClinica();
                        break;
                    case "11":
                        eliminarHistoriaClinica();
                        break;
                    case "0":
                        salir = true;
                        System.out.println("\n¡Gracias por usar el sistema!");
                        break;
                    default:
                        System.out.println("⚠️  Opción inválida. Intente nuevamente.");
                }
            } catch (ServiceException e) {
                System.out.println("❌ Error de servicio: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("❌ Error inesperado: " + e.getMessage());
            }
            System.out.println();
        }
    }

    private void mostrarMenuPrincipal() {
        System.out.println("==============================================");
        System.out.println("  Sistema de Gestión Clínica - Menú Principal ");
        System.out.println("==============================================");
        System.out.println("Pacientes (A)");
        System.out.println(" 1) Crear paciente con historia clínica");
        System.out.println(" 2) Listar pacientes");
        System.out.println(" 3) Buscar paciente por ID");
        System.out.println(" 4) Actualizar paciente");
        System.out.println(" 5) Eliminar paciente (lógico)");
        System.out.println(" 6) Buscar paciente por DNI");
        System.out.println("----------------------------------------------");
        System.out.println("Historias Clínicas (B)");
        System.out.println(" 7) Crear historia clínica para paciente");
        System.out.println(" 8) Listar historias clínicas");
        System.out.println(" 9) Buscar historia clínica por ID");
        System.out.println("10) Actualizar historia clínica");
        System.out.println("11) Eliminar historia clínica (lógico)");
        System.out.println("----------------------------------------------");
        System.out.println(" 0) Salir");
        System.out.println("==============================================");
    }

    // ===================== PACIENTES =====================
    private void crearPacienteCompleto() throws ServiceException {
        System.out.println("\n--- Alta de Paciente + Historia Clínica ---");
        Paciente paciente = solicitarDatosPaciente(null);
        HistoriaClinica historia = solicitarDatosHistoria(null);
        paciente.setHistoriaClinica(historia);

        Paciente creado = pacienteService.insertarPacienteCompleto(paciente);
        System.out.println("✅ Paciente creado con ID: " + creado.getId());
    }

    private void listarPacientes() throws ServiceException {
        System.out.println("\n--- Listado de Pacientes ---");
        List<Paciente> pacientes = pacienteService.getAllConHistoria();
        if (pacientes.isEmpty()) {
            System.out.println("No hay pacientes registrados.");
            return;
        }
        for (Paciente p : pacientes) {
            System.out.println(formatearPaciente(p));
        }
    }

    private void buscarPacientePorId() throws ServiceException {
        System.out.println("\n--- Buscar Paciente por ID ---");
        Long id = leerLong("Ingrese el ID del paciente: ");
        Paciente paciente = pacienteService.getByIdConHistoria(id);
        if (paciente == null) {
            System.out.println("⚠️  No se encontró un paciente con ID " + id);
        } else {
            System.out.println(formatearPaciente(paciente));
        }
    }

    private void actualizarPaciente() throws ServiceException {
        System.out.println("\n--- Actualizar Paciente ---");
        Long id = leerLong("Ingrese el ID del paciente: ");
        Paciente paciente = pacienteService.getByIdConHistoria(id);
        if (paciente == null) {
            System.out.println("⚠️  No se encontró el paciente.");
            return;
        }

        System.out.println("Deje el campo vacío para mantener el valor actual.");
        String nombre = leerLinea("Nombre (" + paciente.getNombre() + "): ");
        if (!nombre.trim().isEmpty()) {
            paciente.setNombre(nombre.trim());
        }
        String apellido = leerLinea("Apellido (" + paciente.getApellido() + "): ");
        if (!apellido.trim().isEmpty()) {
            paciente.setApellido(apellido.trim());
        }
        String dni = leerLinea("DNI (" + paciente.getDni() + "): ");
        if (!dni.trim().isEmpty()) {
            paciente.setDni(dni.trim());
        }
        String fecha = leerLinea("Fecha nac. dd/MM/yyyy (" + formatearFecha(paciente.getFechaNacimiento()) + "): ");
        if (!fecha.trim().isEmpty()) {
            LocalDate nuevaFecha = parseFecha(fecha);
            if (nuevaFecha != null) {
                paciente.setFechaNacimiento(nuevaFecha);
            }
        }

        // ¿Actualizar la historia clínica existente?
        if (paciente.getHistoriaClinica() != null) {
            System.out.println("\n¿Desea actualizar la historia clínica asociada? (S/N)");
            if ("S".equalsIgnoreCase(leerLinea("> "))) {
                HistoriaClinica hc = paciente.getHistoriaClinica();
                actualizarHistoriaExistente(hc);
                paciente.setHistoriaClinica(hc);
            }
        }

        pacienteService.actualizar(paciente);
        System.out.println("✅ Paciente actualizado correctamente.");
    }

    private void eliminarPaciente() throws ServiceException {
        System.out.println("\n--- Eliminar Paciente (Borrado Lógico) ---");
        Long id = leerLong("Ingrese el ID del paciente: ");
        pacienteService.eliminar(id);
        System.out.println("✅ Paciente marcado como eliminado.");
    }

    private void buscarPacientePorDni() throws ServiceException {
        System.out.println("\n--- Buscar Paciente por DNI ---");
        String dni = leerLinea("Ingrese el DNI: ").trim();
        Paciente paciente = pacienteService.buscarPorDni(dni);
        if (paciente == null) {
            System.out.println("⚠️  No se encontró un paciente con DNI " + dni);
        } else {
            System.out.println(formatearPaciente(paciente));
        }
    }

    // ===================== HISTORIAS CLÍNICAS =====================
    private void crearHistoriaClinicaParaPaciente() throws ServiceException {
        System.out.println("\n--- Crear Historia Clínica para Paciente ---");
        Long pacienteId = leerLong("Ingrese el ID del paciente: ");
        Paciente paciente = pacienteService.getByIdConHistoria(pacienteId);
        if (paciente == null) {
            System.out.println("⚠️  No existe un paciente con ese ID.");
            return;
        }
        if (paciente.getHistoriaClinica() != null) {
            System.out.println("⚠️  Este paciente ya tiene una historia clínica asociada.");
            return;
        }

        HistoriaClinica hc = solicitarDatosHistoria(null);
        hc.setPaciente(paciente);
        historiaClinicaService.insertar(hc);
        System.out.println("✅ Historia clínica creada correctamente.");
    }

    private void listarHistoriasClinicas() throws ServiceException {
        System.out.println("\n--- Listado de Historias Clínicas ---");
        List<HistoriaClinica> historias = historiaClinicaService.getAll();
        if (historias.isEmpty()) {
            System.out.println("No hay historias clínicas registradas.");
            return;
        }
        for (HistoriaClinica hc : historias) {
            System.out.println(formatearHistoria(hc));
        }
    }

    private void buscarHistoriaPorId() throws ServiceException {
        System.out.println("\n--- Buscar Historia Clínica por ID ---");
        Long id = leerLong("Ingrese el ID de la historia: ");
        HistoriaClinica hc = historiaClinicaService.getById(id);
        if (hc == null) {
            System.out.println("⚠️  No se encontró una historia clínica con ese ID.");
        } else {
            System.out.println(formatearHistoria(hc));
        }
    }

    private void actualizarHistoriaClinica() throws ServiceException {
        System.out.println("\n--- Actualizar Historia Clínica ---");
        Long id = leerLong("Ingrese el ID de la historia clínica: ");
        HistoriaClinica hc = historiaClinicaService.getById(id);
        if (hc == null) {
            System.out.println("⚠️  No se encontró la historia clínica.");
            return;
        }

        actualizarHistoriaExistente(hc);
        historiaClinicaService.actualizar(hc);
        System.out.println("✅ Historia clínica actualizada.");
    }

    private void eliminarHistoriaClinica() throws ServiceException {
        System.out.println("\n--- Eliminar Historia Clínica (Borrado Lógico) ---");
        Long id = leerLong("Ingrese el ID de la historia clínica: ");
        historiaClinicaService.eliminar(id);
        System.out.println("✅ Historia clínica eliminada (lógicamente).");
    }

    // ===================== HELPER METHODS =====================
    private Paciente solicitarDatosPaciente(Paciente existente) {
        Paciente paciente = existente != null ? existente : new Paciente();
        paciente.setNombre(leerNoVacio("Nombre: "));
        paciente.setApellido(leerNoVacio("Apellido: "));
        paciente.setDni(leerNoVacio("DNI: "));
        paciente.setFechaNacimiento(leerFecha("Fecha de nacimiento (dd/MM/yyyy): "));
        paciente.setEliminado(false);
        return paciente;
    }

    private HistoriaClinica solicitarDatosHistoria(HistoriaClinica existente) {
        HistoriaClinica hc = existente != null ? existente : new HistoriaClinica();
        hc.setNroHistoria(leerNoVacio("Número de historia clínica: ").toUpperCase(Locale.ROOT));
        hc.setGrupoSanguineo(seleccionarGrupoSanguineo());
        hc.setAntecedentes(leerLinea("Antecedentes: "));
        hc.setMedicacionActual(leerLinea("Medicación actual: "));
        hc.setObservaciones(leerLinea("Observaciones: "));
        hc.setEliminado(false);
        return hc;
    }

    private void actualizarHistoriaExistente(HistoriaClinica hc) {
        String nro = leerLinea("Nro historia (" + hc.getNroHistoria() + "): ");
        if (!nro.trim().isEmpty()) {
            hc.setNroHistoria(nro.trim().toUpperCase(Locale.ROOT));
        }
        System.out.println("Grupo sanguíneo actual: " + hc.getGrupoSanguineo());
        System.out.println("¿Desea modificarlo? (S/N)");
        if ("S".equalsIgnoreCase(leerLinea("> "))) {
            hc.setGrupoSanguineo(seleccionarGrupoSanguineo());
        }
        String antecedentes = leerLinea("Antecedentes (" + nuloSiVacio(hc.getAntecedentes()) + "): ");
        if (!antecedentes.trim().isEmpty()) {
            hc.setAntecedentes(antecedentes);
        }
        String medicacion = leerLinea("Medicación actual (" + nuloSiVacio(hc.getMedicacionActual()) + "): ");
        if (!medicacion.trim().isEmpty()) {
            hc.setMedicacionActual(medicacion);
        }
        String obs = leerLinea("Observaciones (" + nuloSiVacio(hc.getObservaciones()) + "): ");
        if (!obs.trim().isEmpty()) {
            hc.setObservaciones(obs);
        }
    }

    private GrupoSanguineo seleccionarGrupoSanguineo() {
        System.out.println("Seleccione grupo sanguíneo:");
        GrupoSanguineo[] valores = GrupoSanguineo.values();
        for (int i = 0; i < valores.length; i++) {
            System.out.println(" " + (i + 1) + ") " + valores[i]);
        }
        while (true) {
            int opcion = leerEntero("Opción: ");
            if (opcion >= 1 && opcion <= valores.length) {
                return valores[opcion - 1];
            }
            System.out.println("⚠️  Selección inválida.");
        }
    }

    private String formatearPaciente(Paciente p) {
        String estado = Boolean.TRUE.equals(p.getEliminado()) ? "ELIMINADO" : "ACTIVO";
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(p.getId())
                .append(" | ").append(p.getNombre()).append(" ").append(p.getApellido())
                .append(" | DNI: ").append(p.getDni())
                .append(" | Estado: ").append(estado);
        if (p.getHistoriaClinica() != null) {
            sb.append(" | HC: ").append(p.getHistoriaClinica().getNroHistoria());
        }
        return sb.toString();
    }

    private String formatearHistoria(HistoriaClinica hc) {
        String estado = Boolean.TRUE.equals(hc.getEliminado()) ? "ELIMINADA" : "ACTIVA";
        return "ID: " + hc.getId()
                + " | Nro: " + hc.getNroHistoria()
                + " | Grupo: " + hc.getGrupoSanguineo()
                + " | PacienteID: " + (hc.getPaciente() != null ? hc.getPaciente().getId() : "N/D")
                + " | Estado: " + estado;
    }

    private String leerLinea(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    private String leerNoVacio(String mensaje) {
        while (true) {
            String valor = leerLinea(mensaje).trim();
            if (!valor.isEmpty()) {
                return valor;
            }
            System.out.println("⚠️  Este campo es obligatorio.");
        }
    }

    private Long leerLong(String mensaje) {
        while (true) {
            try {
                return Long.parseLong(leerLinea(mensaje).trim());
            } catch (NumberFormatException e) {
                System.out.println("⚠️  Debe ingresar un número válido.");
            }
        }
    }

    private int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerLinea(mensaje).trim());
            } catch (NumberFormatException e) {
                System.out.println("⚠️  Debe ingresar un número válido.");
            }
        }
    }

    private LocalDate leerFecha(String mensaje) {
        while (true) {
            try {
                return LocalDate.parse(leerLinea(mensaje).trim(), DATE_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.println("⚠️  Formato inválido. Use dd/MM/yyyy.");
            }
        }
    }

    private LocalDate parseFecha(String input) {
        try {
            return LocalDate.parse(input.trim(), DATE_FORMAT);
        } catch (DateTimeParseException e) {
            System.out.println("⚠️  Fecha inválida, se mantiene la original.");
            return null;
        }
    }

    private String formatearFecha(LocalDate fecha) {
        return fecha != null ? fecha.format(DATE_FORMAT) : "N/D";
    }

    private String nuloSiVacio(String valor) {
        return valor == null || valor.isEmpty() ? "-" : valor;
    }
}

