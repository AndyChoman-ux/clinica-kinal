package main.java.com.vyorg.clinica.kinal0.service;

import java.time.LocalDate;
import java.util.List;
import main.java.com.vyorg.clinica.kinal0.model.Cita;
import main.java.com.vyorg.clinica.kinal0.repository.CitaRepository;

public class CitaService {

    private final CitaRepository citaRepository;

    public CitaService(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    public List<Cita> listar() {
        return citaRepository.findAll();
    }

    public List<Cita> buscar(String texto) {
        if (texto == null || texto.isBlank()) {
            return listar();
        }
        return citaRepository.buscarPorPaciente(texto.trim());
    }

    public int contarCitasHoy() {
        return citaRepository.contarCitasHoy();
    }

    public void guardar(Cita cita) {
        validar(cita);
        boolean ok = cita.getIdCita() == 0 ? citaRepository.insertar(cita) : citaRepository.actualizar(cita);
        if (!ok) {
            throw new RuntimeException("No se pudo guardar la cita");
        }
    }

    public void eliminar(int idCita) {
        if (!citaRepository.eliminar(idCita)) {
            throw new RuntimeException("No se pudo eliminar la cita");
        }
    }

    private void validar(Cita cita) {
        if (cita.getIdPaciente() == 0) {
            throw new RuntimeException("Debes seleccionar un paciente");
        }
        if (cita.getFecha() == null) {
            throw new RuntimeException("La fecha es obligatoria");
        }
        if (cita.getFecha().isBefore(LocalDate.now())) {
            throw new RuntimeException("La fecha no puede ser anterior a hoy");
        }
        if (cita.getHora() == null) {
            throw new RuntimeException("La hora es obligatoria");
        }
        if (cita.getMotivo() == null || cita.getMotivo().isBlank()) {
            throw new RuntimeException("El motivo es obligatorio");
        }
        if (cita.getEstado() == null || cita.getEstado().isBlank()) {
            cita.setEstado("Programada");
        }
    }
}