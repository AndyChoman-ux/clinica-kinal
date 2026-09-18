package main.java.com.vyorg.clinica.kinal0.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import main.java.com.vyorg.clinica.kinal0.config.DatabaseConnection;
import main.java.com.vyorg.clinica.kinal0.dto.response.RegistroDTOResponse;

public class RegistroRepository {

    private static final String SQL_OBTENER_ID_ROL_DOCTOR =
            "SELECT id_rol FROM roles WHERE nombre_rol = 'DOCTOR'";

    private static final String SQL_INSERTAR_USUARIO =
            "INSERT INTO usuarios (nombre_completo, correo, nombre_usuario, password_hash, id_rol, activo) " +
            "VALUES (?, ?, ?, ?, ?, TRUE)";

    public RegistroDTOResponse registrarUsuario(String nombreUsuario, String correo, String passwordHash) throws SQLException {
        Connection conexion = DatabaseConnection.getDatabaseConnection();
        int idRolDoctor = obtenerIdRolDoctor(conexion);

        try (PreparedStatement pstm = conexion.prepareStatement(SQL_INSERTAR_USUARIO, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, nombreUsuario); // nombre_completo por defecto = nombre_usuario
            pstm.setString(2, correo);
            pstm.setString(3, nombreUsuario);
            pstm.setString(4, passwordHash);
            pstm.setInt(5, idRolDoctor);
            pstm.executeUpdate();

            try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new RegistroDTOResponse(generatedKeys.getInt(1), nombreUsuario, correo, "DOCTOR");
                }
                throw new SQLException("No se pudo obtener el id del usuario creado");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new RuntimeException("El correo o el nombre de usuario ya están registrados");
        }
    }

    private int obtenerIdRolDoctor(Connection conexion) throws SQLException {
        try (PreparedStatement pstm = conexion.prepareStatement(SQL_OBTENER_ID_ROL_DOCTOR);
             ResultSet rs = pstm.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("id_rol");
            }
            throw new SQLException("No existe el rol DOCTOR en la tabla roles");
        }
    }
}