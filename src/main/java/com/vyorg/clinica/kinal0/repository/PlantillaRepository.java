package main.java.com.vyorg.clinica.kinal0.repository;

import main.java.com.vyorg.clinica.kinal0.config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlantillaRepository {

    private static final Logger LOGGER = Logger.getLogger(PlantillaRepository.class.getName());

    public List<String> obtenerNombresPlantillas() {
        List<String> plantillas = new ArrayList<>();
        String sql = "SELECT nombre_plantilla FROM plantillas_clinicas ORDER BY nombre_plantilla ASC";

        LOGGER.info("Ejecutando consulta para obtener nombres de plantillas clínicas.");

        try (Connection conn = DatabaseConnection.getDatabaseConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String nombre = rs.getString("nombre_plantilla");
                if (nombre != null && !nombre.isBlank()) {
                    plantillas.add(nombre);
                }
            }
            LOGGER.info("Se encontraron " + plantillas.size() + " plantillas registradas.");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de SQL al obtener los nombres de las plantillas", e);
        }
        return plantillas;
    }

    public Map<String, String> obtenerDetallePlantillaPorNombre(String nombrePlantilla) {
        Map<String, String> detallePlantilla = new HashMap<>();
        String sql = "SELECT notas_medicas, medicamentos FROM plantillas_clinicas WHERE nombre_plantilla = ?";

        LOGGER.info("Buscando detalles para la plantilla: " + nombrePlantilla);

        try (Connection conn = DatabaseConnection.getDatabaseConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombrePlantilla);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    detallePlantilla.put("notasMedicas", rs.getString("notas_medicas"));
                    detallePlantilla.put("medicamentos", rs.getString("medicamentos"));
                    LOGGER.info("Plantilla encontrada exitosamente.");
                } else {
                    LOGGER.warning("No se encontró ninguna plantilla con el nombre: " + nombrePlantilla);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de SQL al consultar el detalle de la plantilla", e);
        }
        return detallePlantilla;
    }

    public boolean guardarNuevaPlantilla(String nombre, String notasMedicas, String medicamentos) {
        String sql = "INSERT INTO plantillas_clinicas (nombre_plantilla, notas_medicas, medicamentos) VALUES (?, ?, ?)";

        LOGGER.info("Intentando registrar una nueva plantilla clínica: " + nombre);

        try (Connection conn = DatabaseConnection.getDatabaseConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, notasMedicas);
            stmt.setString(3, medicamentos);

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                LOGGER.info("Plantilla guardada correctamente en la base de datos.");
                return true;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al intentar guardar la nueva plantilla clínica", e);
        }
        return false;
    }

    public boolean eliminarPlantilla(String nombrePlantilla) {
        String sql = "DELETE FROM plantillas_clinicas WHERE nombre_plantilla = ?";

        LOGGER.info("Intentando eliminar la plantilla: " + nombrePlantilla);

        try (Connection conn = DatabaseConnection.getDatabaseConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombrePlantilla);
            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                LOGGER.info("Plantilla eliminada exitosamente.");
                return true;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error de SQL al eliminar la plantilla clínica", e);
        }
        return false;
    }
}