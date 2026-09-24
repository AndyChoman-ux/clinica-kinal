package main.java.com.vyorg.clinica.kinal0.service;

import main.java.com.vyorg.clinica.kinal0.repository.PlantillaRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlantillaService {

    private static final Logger LOGGER = Logger.getLogger(PlantillaService.class.getName());
    private final PlantillaRepository plantillaRepository;

    public PlantillaService() {
        this.plantillaRepository = new PlantillaRepository();
    }

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

   
    public boolean crearNuevaPlantilla(String nombre, String notas, String medicamentos) {
        if (nombre == null || nombre.isBlank()) {
            LOGGER.warning("Validación fallida: El nombre de la plantilla es obligatorio.");
            return false;
        }

        if (notas == null) {
            notas = ""; 
        }

        if (medicamentos == null) {
            medicamentos = ""; 
        }

        try {
            LOGGER.info("Servicio: Guardando nueva plantilla validada: " + nombre);
            return plantillaRepository.guardarNuevaPlantilla(nombre.trim(), notas.trim(), medicamentos.trim());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Excepción en el servicio al crear la plantilla", e);
            return false;
        }
    }

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
