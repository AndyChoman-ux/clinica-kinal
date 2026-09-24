package main.java.com.vyorg.clinica.kinal0.service;

import java.time.LocalDateTime;
import java.util.List;
import main.java.com.vyorg.clinica.kinal0.model.Expediente;
import main.java.com.vyorg.clinica.kinal0.repository.ExpedienteRepository;

public class ExpedienteService {

    private final ExpedienteRepository expedienteRepository;

    public ExpedienteService(ExpedienteRepository expedienteRepository) {
        this.expedienteRepository = expedienteRepository;
    }

    public List<Expediente> listar() {
        return expedienteRepository.findAll();
    }

    public List<Expediente> buscar(String texto) {
        if (texto == null || texto.isBlank()) {
            return listar();
        }
        return expedienteRepository.buscarPorPaciente(texto.trim());
    }

    public int contarNuevosEstaSemana() {
        return expedienteRepository.contarNuevosEstaSemana();
    }

    public void guardar(Expediente expediente) {
        validar(expediente);
        boolean esNuevo = expediente.getIdExpediente() == 0;
        if (esNuevo) {
            expediente.setFechaApertura(LocalDateTime.now());
            if (!expedienteRepository.insertar(expediente)) {
                throw new RuntimeException("No se pudo crear el expediente");
            }
            return;
        }
        if (!expedienteRepository.actualizar(expediente)) {
            throw new RuntimeException("No se pudo actualizar el expediente");
        }
    }

    public void eliminar(int idExpediente) {
        if (!expedienteRepository.eliminar(idExpediente)) {
            throw new RuntimeException("No se pudo eliminar el expediente");
        }
    }

    private void validar(Expediente expediente) {
        if (expediente.getIdPaciente() == 0) {
            throw new RuntimeException("Debes seleccionar un paciente");
        }
        if (expediente.getEstado() == null || expediente.getEstado().isBlank()) {
            expediente.setEstado("ACTIVO");
        }
    }
}