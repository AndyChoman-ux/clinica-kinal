package main.java.com.vyorg.clinica.kinal0.service;

import java.time.LocalDate;
import java.util.List;
import main.java.com.vyorg.clinica.kinal0.model.Paciente;
import main.java.com.vyorg.clinica.kinal0.repository.PacienteRepository;
import java.util.regex.Pattern;

public class PacienteService {
    
    private static final Pattern PATRON_CORREO =
        Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}(\\.[a-zA-Z]{2,})?$");

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public List<Paciente> listar() {
        return pacienteRepository.findAll();
    }

    public List<Paciente> buscar(String texto) {
        if (texto == null || texto.isBlank()) {
            return listar();
        }
        return pacienteRepository.buscarPorNombre(texto.trim());
    }

    public void guardar(Paciente paciente) {
        validar(paciente);
        boolean ok = paciente.getIdPaciente() == 0
                ? pacienteRepository.insertar(paciente)
                : pacienteRepository.actualizar(paciente);

        if (!ok) {
            throw new RuntimeException("No se pudo guardar el paciente");
        }
    }

    public void eliminar(int idPaciente) {
        if (!pacienteRepository.eliminar(idPaciente)) {
            throw new RuntimeException("No se pudo eliminar el paciente");
        }
    }

    private void validar(Paciente paciente) {
        if (paciente.getNombreCompleto() == null || paciente.getNombreCompleto().isBlank()) {
            throw new RuntimeException("El nombre completo es obligatorio");
        }
        if (paciente.getFechaNacimiento() == null) {
            throw new RuntimeException("La fecha de nacimiento es obligatoria");
        }
        if (paciente.getFechaNacimiento().isAfter(LocalDate.now())) {
            throw new RuntimeException("La fecha de nacimiento no puede ser futura");
        }
        if (paciente.getGenero() == null || paciente.getGenero().isBlank()) {
            throw new RuntimeException("El género es obligatorio");
        }
        if (paciente.getCorreo() != null && !paciente.getCorreo().isBlank()
        && !PATRON_CORREO.matcher(paciente.getCorreo().trim()).matches()) {
    throw new RuntimeException("El correo debe tener un formato válido, por ejemplo nombre@gmail.com");
}
    }
}