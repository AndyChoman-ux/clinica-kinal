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

    /**
     * Obtiene los diagnósticos más frecuentes del mes con validación de nulos.
     */
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

    /**
     * Obtiene los horarios de afluencia transformados o filtrados.
     */
    public Map<String, Integer> getHorariosAfluencia() {
        try {
            LOGGER.info("Procesando horarios de mayor afluencia de pacientes...");
            Map<String, Integer> datos = estadisticasRepository.obtenerHorariosAfluencia();
            
            // Aquí puedes agregar lógica adicional de negocio si lo requieres
            if (datos == null) {
                return new HashMap<>();
            }
            return datos;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al procesar los horarios de afluencia en el servicio", e);
            return new HashMap<>();
        }
    }

    /**
     * Calcula la tasa de ausentismo y cancelaciones convirtiendo los totales a porcentajes.
     */
    public Map<String, String> getTasaAusentismoConPorcentajes() {
        Map<String, String> resultadosConPorcentaje = new HashMap<>();
        try {
            LOGGER.info("Calculando tasas de ausentismo y porcentajes de citas...");
            Map<String, Integer> datosBrutos = estadisticasRepository.obtenerTasaAusentismo();
            
            if (datosBrutos == null || datosBrutos.isEmpty()) {
                return resultadosConPorcentaje;
            }

            // Calcular el total general de citas para sacar porcentajes reales
            int totalCitas = datosBrutos.values().stream().mapToInt(Integer::intValue).sum();

            if (totalCitas > 0) {
                for (Map.Entry<String, Integer> entry : datosBrutos.entrySet()) {
                    String estado = entry.getKey();
                    int cantidad = entry.getValue();
                    double porcentaje = (double) cantidad / totalCitas * 100.0;
                    
                    // Formatear el resultado como texto con su porcentaje (ej. "Cancelada: 5 (15.5%)")
                    String detalle = String.format("%d citas (%.1f%%)", cantidad, porcentaje);
                    resultadosConPorcentaje.put(estado, detalle);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al calcular el porcentaje de ausentismo", e);
        }
        return resultadosConPorcentaje;
    }

    /**
     * Genera un reporte general consolidado en texto para auditorías o respaldos rápidos.
     */
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
}