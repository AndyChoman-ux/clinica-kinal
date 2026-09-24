package main.java.com.vyorg.clinica.kinal0.repository;

import main.java.com.vyorg.clinica.kinal0.config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class EstadisticasRepository {

    public Map<String, Integer> obtenerDiagnosticosFrecuentesMes() {
        Map<String, Integer> resultados = new LinkedHashMap<>();
        String sql = "SELECT diagnostico, COUNT(*) as total FROM expedientes " +
                "WHERE MONTH(fecha_creacion) = MONTH(CURRENT_DATE()) " +
                "AND YEAR(fecha_creacion) = YEAR(CURRENT_DATE()) " +
                "GROUP BY diagnostico ORDER BY total DESC LIMIT 5";

        try (Connection conn = DatabaseConnection.getDatabaseConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                resultados.put(rs.getString("diagnostico"), rs.getInt("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultados;
    }

    public Map<String, Integer> obtenerHorariosAfluencia() {
        Map<String, Integer> resultados = new LinkedHashMap<>();
        String sql = "SELECT HOUR(hora_cita) as hora, COUNT(*) as total FROM citas " +
                "GROUP BY HOUR(hora_cita) ORDER BY total DESC";

        try (Connection conn = DatabaseConnection.getDatabaseConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                resultados.put(rs.getString("hora") + ":00 hrs", rs.getInt("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultados;
    }

    public Map<String, Integer> obtenerTasaAusentismo() {
        Map<String, Integer> resultados = new LinkedHashMap<>();
        String sql = "SELECT estado, COUNT(*) as total FROM citas GROUP BY estado";

        try (Connection conn = DatabaseConnection.getDatabaseConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                resultados.put(rs.getString("estado"), rs.getInt("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultados;
    }

    public Map<String, Integer> obtenerPacientesPorGenero() {
        Map<String, Integer> resultados = new LinkedHashMap<>();
        String sql = "SELECT genero, COUNT(*) as total FROM pacientes GROUP BY genero ORDER BY total DESC";

        try (Connection conn = DatabaseConnection.getDatabaseConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                resultados.put(rs.getString("genero"), rs.getInt("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultados;
    }

    public Map<String, Integer> obtenerPacientesPorTipoSangre() {
        Map<String, Integer> resultados = new LinkedHashMap<>();
        String sql = "SELECT tipo_sangre, COUNT(*) as total FROM pacientes " +
                "WHERE tipo_sangre IS NOT NULL GROUP BY tipo_sangre ORDER BY total DESC";

        try (Connection conn = DatabaseConnection.getDatabaseConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                resultados.put(rs.getString("tipo_sangre"), rs.getInt("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultados;
    }

    public Map<String, Integer> obtenerCitasPorEstado() {
        Map<String, Integer> resultados = new LinkedHashMap<>();
        String sql = "SELECT estado, COUNT(*) as total FROM citas GROUP BY estado ORDER BY total DESC";

        try (Connection conn = DatabaseConnection.getDatabaseConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                resultados.put(rs.getString("estado"), rs.getInt("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultados;
    }

    public Map<String, Integer> obtenerExpedientesPorEstado() {
        Map<String, Integer> resultados = new LinkedHashMap<>();
        String sql = "SELECT estado, COUNT(*) as total FROM expedientes GROUP BY estado ORDER BY total DESC";

        try (Connection conn = DatabaseConnection.getDatabaseConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                resultados.put(rs.getString("estado"), rs.getInt("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultados;
    }
}