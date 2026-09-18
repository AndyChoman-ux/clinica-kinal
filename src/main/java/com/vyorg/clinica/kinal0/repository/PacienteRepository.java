package main.java.com.vyorg.clinica.kinal0.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import main.java.com.vyorg.clinica.kinal0.config.DatabaseConnection;
import main.java.com.vyorg.clinica.kinal0.model.Paciente;

public class PacienteRepository {

    private static final String CAMPOS =
            "id_paciente, nombre_completo, fecha_nacimiento, genero, telefono, " +
            "correo, direccion, tipo_sangre, fecha_registro";

    public List<Paciente> findAll() {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "select " + CAMPOS + " from pacientes order by nombre_completo";

        try (PreparedStatement pstm = DatabaseConnection.getDatabaseConnection().prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                pacientes.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar pacientes: " + e.getMessage());
        }
        return pacientes;
    }

    public List<Paciente> buscarPorNombre(String texto) {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "select " + CAMPOS + " from pacientes " +
                     "where nombre_completo like ? order by nombre_completo";

        try (PreparedStatement pstm = DatabaseConnection.getDatabaseConnection().prepareStatement(sql)) {
            pstm.setString(1, "%" + texto + "%");
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    pacientes.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar pacientes: " + e.getMessage());
        }
        return pacientes;
    }

    public boolean insertar(Paciente paciente) {
        String sql = "insert into pacientes (nombre_completo, fecha_nacimiento, genero, telefono, " +
                     "correo, direccion, tipo_sangre) values (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstm = DatabaseConnection.getDatabaseConnection().prepareStatement(sql)) {
            llenarParametros(pstm, paciente);
            return pstm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar paciente: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Paciente paciente) {
        String sql = "update pacientes set nombre_completo = ?, fecha_nacimiento = ?, genero = ?, " +
                     "telefono = ?, correo = ?, direccion = ?, tipo_sangre = ? where id_paciente = ?";

        try (PreparedStatement pstm = DatabaseConnection.getDatabaseConnection().prepareStatement(sql)) {
            llenarParametros(pstm, paciente);
            pstm.setInt(8, paciente.getIdPaciente());
            return pstm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar paciente: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idPaciente) {
        String sql = "delete from pacientes where id_paciente = ?";

        try (PreparedStatement pstm = DatabaseConnection.getDatabaseConnection().prepareStatement(sql)) {
            pstm.setInt(1, idPaciente);
            return pstm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar paciente: " + e.getMessage());
            return false;
        }
    }

    private void llenarParametros(PreparedStatement pstm, Paciente paciente) throws SQLException {
        pstm.setString(1, paciente.getNombreCompleto());
        pstm.setDate(2, paciente.getFechaNacimiento() != null ? Date.valueOf(paciente.getFechaNacimiento()) : null);
        pstm.setString(3, paciente.getGenero());
        pstm.setString(4, paciente.getTelefono());
        pstm.setString(5, paciente.getCorreo());
        pstm.setString(6, paciente.getDireccion());
        pstm.setString(7, paciente.getTipoSangre());
    }

    private Paciente mapRow(ResultSet rs) throws SQLException {
        Date fechaNacimientoSql = rs.getDate("fecha_nacimiento");
        Timestamp fechaRegistroSql = rs.getTimestamp("fecha_registro");

        return new Paciente(
                rs.getInt("id_paciente"),
                rs.getString("nombre_completo"),
                fechaNacimientoSql != null ? fechaNacimientoSql.toLocalDate() : null,
                rs.getString("genero"),
                rs.getString("telefono"),
                rs.getString("correo"),
                rs.getString("direccion"),
                rs.getString("tipo_sangre"),
                fechaRegistroSql != null ? fechaRegistroSql.toLocalDateTime() : null
        );
    }
}