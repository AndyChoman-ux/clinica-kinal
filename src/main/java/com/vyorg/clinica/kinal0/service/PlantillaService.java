package main.java.com.vyorg.clinica.kinal0.service;

import main.java.com.vyorg.clinica.kinal0.repository.PlantillaRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servicio encargado de la lógica de negocio para la gestión de plantillas
 * y el autocompletado instantáneo en las consultas médicas.
 */
public class PlantillaService {

    private static final Logger LOGGER = Logger.getLogger(PlantillaService.class.getName());
    private final PlantillaRepository plantillaRepository;

    public PlantillaService() {
        this.plantillaRepository = new PlantillaRepository();
    }

    /**
     * Obtiene y valida la lista de plantillas disponibles para mostrar en la interfaz.
     * @return Lista de nombres de plantillas seguras.
     */
    public List<String> listarNombresPlantillas() {
        try {
            LOGGER.info("Servicio: Procesando solicitud para listar plantillas clínicas.");
            List<String> lista = plantillaRepository.obtenerNombresPlantillas();
            if (lista == null) {
                return new ArrayList<>();
            }
            return lista;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error en el servicio al listar las plantillas", e);
            return new ArrayList<>();
        }
    }

    /**
     * Carga y procesa los detalles de una plantilla seleccionada para realizar el autocompletado.
     * @param nombrePlantilla Nombre de la plantilla elegida por el médico.
     * @return Mapa con las notas y medicamentos listos para autocompletar.
     */
    public Map<String, String> procesarAutocompletado(String nombrePlantilla) {
        Map<String, String> resultadoVacio = new HashMap<>();
        resultadoVacio.put("notasMedicas", "");
        resultadoVacio.put("medicamentos", "");

        if (nombrePlantilla == null || nombrePlantilla.isBlank()) {
            LOGGER.warning("Intento de autocompletado con un nombre de plantilla vacío o nulo.");
            return resultadoVacio;
        }

        try {
            LOGGER.info("Servicio: Procesando autocompletado para la plantilla -> " + nombrePlantilla);
            Map<String, String> datosPlantilla = plantillaRepository.obtenerDetallePlantillaPorNombre(nombrePlantilla.trim());
            
            if (datosPlantilla == null || datosPlantilla.isEmpty()) {
                LOGGER.warning("No se encontraron datos para autocompletar con la plantilla especificada.");
                return resultadoVacio;
            }
            
            return datosPlantilla;
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error crítico procesando el autocompletado clínico", e);
            return resultadoVacio;
        }
    }

    /**
     * Valida los datos y coordina el almacenamiento de una nueva plantilla clínica.
     * @param nombre Nombre de la plantilla.
     * @param notas Notas médicas.
     * @param medicamentos Lista de medicamentos.
     * @return true si el registro fue exitoso.
     */
    public boolean crearNuevaPlantilla(String nombre, String notas, String medicamentos) {
        if (nombre == null || nombre.isBlank()) {
            LOGGER.warning("Validación fallida: El nombre de la plantilla es obligatorio.");
            return false;
        }

        if (notas == null) {
            notas = ""; // Asegurar que no sea nulo
        }

        if (medicamentos == null) {
            medicamentos = ""; // Asegurar que no sea nulo
        }

        try {
            LOGGER.info("Servicio: Guardando nueva plantilla validada: " + nombre);
            return plantillaRepository.guardarNuevaPlantilla(nombre.trim(), notas.trim(), medicamentos.trim());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Excepción en el servicio al crear la plantilla", e);
            return false;
        }
    }

    /**
     * Gestiona la eliminación de una plantilla en el sistema.
     * @param nombrePlantilla Nombre de la plantilla a eliminar.
     * @return true si se eliminó correctamente.
     */
    public boolean borrarPlantilla(String nombrePlantilla) {
        if (nombrePlantilla == null || nombrePlantilla.isBlank()) {
            LOGGER.warning("Intento de borrado fallido: Nombre de plantilla inválido.");
            return false;
        }

        try {
            LOGGER.info("Servicio: Solicitando eliminación de la plantilla: " + nombrePlantilla);
            return plantillaRepository.eliminarPlantilla(nombrePlantilla.trim());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Excepción en el servicio al eliminar la plantilla", e);
            return false;
        }
    }
}
