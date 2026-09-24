package main.java.com.vyorg.clinica.kinal0.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import main.java.com.vyorg.clinica.kinal0.config.DatabaseConnection;
import main.java.com.vyorg.clinica.kinal0.model.Expediente;

public class ExpedienteRepository {

    private static final String CAMPOS =
            "e.id_expediente, e.id_paciente, p.nombre_completo, e.fecha_apertura, e.estado, e.observaciones_generales";
    private static final String FROM =
            " from expedientes e inner join pacientes p on e.id_paciente = p.id_paciente ";

    public List<Expediente> findAll() {
        List<Expediente> expedientes = new ArrayList<>();
        String sql = "select " + CAMPOS + FROM + "order by e.fecha_apertura desc";

        try (PreparedStatement pstm = DatabaseConnection.getDatabaseConnection().prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                expedientes.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar expedientes: " + e.getMessage());
        }
        return expedientes;
    }

    public List<Expediente> buscarPorPaciente(String texto) {
        List<Expediente> expedientes = new ArrayList<>();
        String sql = "select " + CAMPOS + FROM + "where p.nombre_completo like ? order by e.fecha_apertura desc";

        try (PreparedStatement pstm = DatabaseConnection.getDatabaseConnection().prepareStatement(sql)) {
            pstm.setString(1, "%" + texto + "%");
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    expedientes.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar expedientes: " + e.getMessage());
        }
        return expedientes;
    }

    public int contarNuevosEstaSemana() {
        String sql = "select count(*) from expedientes where yearweek(fecha_apertura, 1) = yearweek(curdate(), 1)";
        try (PreparedStatement pstm = DatabaseConnection.getDatabaseConnection().prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error al contar expedientes de la semana: " + e.getMessage());
        }
        return 0;
    }

    public boolean insertar(Expediente expediente) {
        String sql = "insert into expedientes (id_paciente, fecha_apertura, estado, observaciones_generales) " +
                     "values (?, ?, ?, ?)";

        try (PreparedStatement pstm = DatabaseConnection.getDatabaseConnection().prepareStatement(sql)) {
            pstm.setInt(1, expediente.getIdPaciente());
            pstm.setTimestamp(2, Timestamp.valueOf(expediente.getFechaApertura()));
            pstm.setString(3, expediente.getEstado());
            pstm.setString(4, expediente.getObservacionesGenerales());
            return pstm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar expediente: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Expediente expediente) {
        String sql = "update expedientes set id_paciente = ?, estado = ?, observaciones_generales = ? " +
                     "where id_expediente = ?";

        try (PreparedStatement pstm = DatabaseConnection.getDatabaseConnection().prepareStatement(sql)) {
            pstm.setInt(1, expediente.getIdPaciente());
            pstm.setString(2, expediente.getEstado());
            pstm.setString(3, expediente.getObservacionesGenerales());
            pstm.setInt(4, expediente.getIdExpediente());
            return pstm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar expediente: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idExpediente) {
        String sql = "delete from expedientes where id_expediente = ?";
        try (PreparedStatement pstm = DatabaseConnection.getDatabaseConnection().prepareStatement(sql)) {
            pstm.setInt(1, idExpediente);
            return pstm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar expediente: " + e.getMessage());
            return false;
        }
    }

    private Expediente mapRow(ResultSet rs) throws SQLException {
        return new Expediente(
                rs.getInt("id_expediente"),
                rs.getInt("id_paciente"),
                rs.getString("nombre_completo"),
                rs.getTimestamp("fecha_apertura").toLocalDateTime(),
                rs.getString("estado"),
                rs.getString("observaciones_generales")
        );
    }
}