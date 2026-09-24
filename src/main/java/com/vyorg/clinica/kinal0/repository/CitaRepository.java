package main.java.com.vyorg.clinica.kinal0.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import main.java.com.vyorg.clinica.kinal0.config.DatabaseConnection;
import main.java.com.vyorg.clinica.kinal0.model.Cita;

public class CitaRepository {

    private static final String CAMPOS =
            "c.id_cita, c.id_paciente, p.nombre_completo, c.fecha, c.hora, c.motivo, c.estado";
    private static final String FROM =
            " from citas c inner join pacientes p on c.id_paciente = p.id_paciente ";

    public List<Cita> findAll() {
        List<Cita> citas = new ArrayList<>();
        String sql = "select " + CAMPOS + FROM + "order by c.fecha desc, c.hora desc";

        try (PreparedStatement pstm = DatabaseConnection.getDatabaseConnection().prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                citas.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar citas: " + e.getMessage());
        }
        return citas;
    }

    public List<Cita> buscarPorPaciente(String texto) {
        List<Cita> citas = new ArrayList<>();
        String sql = "select " + CAMPOS + FROM +
                     "where p.nombre_completo like ? order by c.fecha desc, c.hora desc";

        try (PreparedStatement pstm = DatabaseConnection.getDatabaseConnection().prepareStatement(sql)) {
            pstm.setString(1, "%" + texto + "%");
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    citas.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar citas: " + e.getMessage());
        }
        return citas;
    }

    public int contarCitasHoy() {
        String sql = "select count(*) from citas where fecha = curdate()";
        try (PreparedStatement pstm = DatabaseConnection.getDatabaseConnection().prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error al contar citas de hoy: " + e.getMessage());
        }
        return 0;
    }

    public boolean insertar(Cita cita) {
        String sql = "insert into citas (id_paciente, fecha, hora, motivo, estado) values (?, ?, ?, ?, ?)";

        try (PreparedStatement pstm = DatabaseConnection.getDatabaseConnection().prepareStatement(sql)) {
            pstm.setInt(1, cita.getIdPaciente());
            pstm.setDate(2, java.sql.Date.valueOf(cita.getFecha()));
            pstm.setTime(3, Time.valueOf(cita.getHora()));
            pstm.setString(4, cita.getMotivo());
            pstm.setString(5, cita.getEstado());
            return pstm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar cita: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Cita cita) {
        String sql = "update citas set id_paciente = ?, fecha = ?, hora = ?, motivo = ?, estado = ? where id_cita = ?";

        try (PreparedStatement pstm = DatabaseConnection.getDatabaseConnection().prepareStatement(sql)) {
            pstm.setInt(1, cita.getIdPaciente());
            pstm.setDate(2, java.sql.Date.valueOf(cita.getFecha()));
            pstm.setTime(3, Time.valueOf(cita.getHora()));
            pstm.setString(4, cita.getMotivo());
            pstm.setString(5, cita.getEstado());
            pstm.setInt(6, cita.getIdCita());
            return pstm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar cita: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idCita) {
        String sql = "delete from citas where id_cita = ?";
        try (PreparedStatement pstm = DatabaseConnection.getDatabaseConnection().prepareStatement(sql)) {
            pstm.setInt(1, idCita);
            return pstm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar cita: " + e.getMessage());
            return false;
        }
    }

    private Cita mapRow(ResultSet rs) throws SQLException {
        LocalTime hora = rs.getTime("hora") != null ? rs.getTime("hora").toLocalTime() : null;
        return new Cita(
                rs.getInt("id_cita"),
                rs.getInt("id_paciente"),
                rs.getString("nombre_completo"),
                rs.getDate("fecha").toLocalDate(),
                hora,
                rs.getString("motivo"),
                rs.getString("estado")
        );
    }
    
    public List<Cita> findByPaciente(int idPaciente) {
    List<Cita> citas = new ArrayList<>();
    String sql = "select " + CAMPOS + FROM + "where c.id_paciente = ? order by c.fecha desc, c.hora desc";

    try (PreparedStatement pstm = DatabaseConnection.getDatabaseConnection().prepareStatement(sql)) {
        pstm.setInt(1, idPaciente);
        try (ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                citas.add(mapRow(rs));
            }
        }
    } catch (SQLException e) {
        System.out.println("Error al listar citas del paciente: " + e.getMessage());
    }
    return citas;
}
}