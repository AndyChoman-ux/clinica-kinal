package main.java.com.vyorg.clinica.kinal0.service;

import main.java.com.vyorg.clinica.kinal0.repository.EstadisticasRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EstadisticasService {

    private static final Logger LOGGER = Logger.getLogger(EstadisticasService.class.getName());
    private final EstadisticasRepository estadisticasRepository;

    public EstadisticasService() {
        this.estadisticasRepository = new EstadisticasRepository();
    }

  
    public Map<String, Integer> getDiagnosticosFrecuentes() {
        try {
            LOGGER.info("Consultando diagnósticos frecuentes del mes...");
            Map<String, Integer> datos = estadisticasRepository.obtenerDiagnosticosFrecuentesMes();
            if (datos == null || datos.isEmpty()) {
                LOGGER.warning("No se encontraron registros de diagnósticos para este mes.");
                return new HashMap<>();
            }
            return datos;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al procesar los diagnósticos frecuentes en el servicio", e);
            return new HashMap<>();
        }
    }

    public Map<String, Integer> getHorariosAfluencia() {
        try {
            LOGGER.info("Procesando horarios de mayor afluencia de pacientes...");
            Map<String, Integer> datos = estadisticasRepository.obtenerHorariosAfluencia();

            if (datos == null) {
                return new HashMap<>();
            }
            return datos;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al procesar los horarios de afluencia en el servicio", e);
            return new HashMap<>();
        }
    }

    public Map<String, String> getTasaAusentismoConPorcentajes() {
        Map<String, String> resultadosConPorcentaje = new HashMap<>();
        try {
            LOGGER.info("Calculando tasas de ausentismo y porcentajes de citas...");
            Map<String, Integer> datosBrutos = estadisticasRepository.obtenerTasaAusentismo();

            if (datosBrutos == null || datosBrutos.isEmpty()) {
                return resultadosConPorcentaje;
            }

            int totalCitas = datosBrutos.values().stream().mapToInt(Integer::intValue).sum();

            if (totalCitas > 0) {
                for (Map.Entry<String, Integer> entry : datosBrutos.entrySet()) {
                    String estado = entry.getKey();
                    int cantidad = entry.getValue();
                    double porcentaje = (double) cantidad / totalCitas * 100.0;

                    String detalle = String.format("%d citas (%.1f%%)", cantidad, porcentaje);
                    resultadosConPorcentaje.put(estado, detalle);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al calcular el porcentaje de ausentismo", e);
        }
        return resultadosConPorcentaje;
    }

    public String generarResumenGeneralTexto() {
        StringBuilder resumen = new StringBuilder();
        resumen.append("=========================================\n");
        resumen.append("   REPORTE CONSOLIDADO DE ESTADÍSTICAS   \n");
        resumen.append("=========================================\n");

        int totalDiagnosticos = getDiagnosticosFrecuentes().values().stream().mapToInt(Integer::intValue).sum();
        resumen.append("Total de casos analizados este mes: ").append(totalDiagnosticos).append("\n");
        resumen.append("Estado del servicio: Operativo y sincronizado.\n");
        resumen.append("=========================================\n");

        return resumen.toString();
    }

   
    public Map<String, Integer> getPacientesPorGenero() {
        try {
            Map<String, Integer> datos = estadisticasRepository.obtenerPacientesPorGenero();
            return datos == null ? new HashMap<>() : datos;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al obtener pacientes por género", e);
            return new HashMap<>();
        }
    }

    
    public Map<String, Integer> getPacientesPorTipoSangre() {
        try {
            Map<String, Integer> datos = estadisticasRepository.obtenerPacientesPorTipoSangre();
            return datos == null ? new HashMap<>() : datos;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al obtener pacientes por tipo de sangre", e);
            return new HashMap<>();
        }
    }

   
    public Map<String, Integer> getCitasPorEstado() {
        try {
            Map<String, Integer> datos = estadisticasRepository.obtenerCitasPorEstado();
            return datos == null ? new HashMap<>() : datos;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al obtener citas por estado", e);
            return new HashMap<>();
        }
    }

 
    public Map<String, Integer> getExpedientesPorEstado() {
        try {
            Map<String, Integer> datos = estadisticasRepository.obtenerExpedientesPorEstado();
            return datos == null ? new HashMap<>() : datos;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al obtener expedientes por estado", e);
            return new HashMap<>();
        }
    }
}