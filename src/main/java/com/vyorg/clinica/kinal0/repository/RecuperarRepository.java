package main.java.com.vyorg.clinica.kinal0.repository;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import main.java.com.vyorg.clinica.kinal0.config.DatabaseConnection;

public class RecuperarRepository {

    private static final int MINUTOS_VALIDEZ_CODIGO = 15;
    private static final SecureRandom RANDOM = new SecureRandom();

    private static final String SQL_BUSCAR_ID_USUARIO_ACTIVO =
            "SELECT id_usuario FROM usuarios WHERE (nombre_usuario = ? OR correo = ?) AND activo = TRUE";

    private static final String SQL_GUARDAR_CODIGO =
            "UPDATE usuarios SET reset_token = ?, reset_token_expira = ? WHERE id_usuario = ?";

    private static final String SQL_VALIDAR_CODIGO_Y_OBTENER_HASH =
            "SELECT password_hash FROM usuarios " +
            "WHERE (nombre_usuario = ? OR correo = ?) AND activo = TRUE " +
            "AND reset_token = ? AND reset_token_expira > NOW()";

    private static final String SQL_ACTUALIZAR_PASSWORD_Y_LIMPIAR_CODIGO =
            "UPDATE usuarios SET password_hash = ?, reset_token = NULL, reset_token_expira = NULL WHERE id_usuario = ?";

    public String generarCodigo(String usuarioOCorreo) throws SQLException {
        Connection conexion = DatabaseConnection.getDatabaseConnection();

        Integer idUsuario = buscarIdUsuarioActivo(conexion, usuarioOCorreo);
        if (idUsuario == null) {
            return null;
        }

        String codigo = String.format("%06d", RANDOM.nextInt(1_000_000));
        Timestamp expira = Timestamp.valueOf(LocalDateTime.now().plusMinutes(MINUTOS_VALIDEZ_CODIGO));

        try (PreparedStatement pstm = conexion.prepareStatement(SQL_GUARDAR_CODIGO)) {
            pstm.setString(1, codigo);
            pstm.setTimestamp(2, expira);
            pstm.setInt(3, idUsuario);
            pstm.executeUpdate();
        }

        return codigo;
    }

    public String validarCodigoYObtenerHashActual(String usuarioOCorreo, String codigo) throws SQLException {
        Connection conexion = DatabaseConnection.getDatabaseConnection();

        try (PreparedStatement pstm = conexion.prepareStatement(SQL_VALIDAR_CODIGO_Y_OBTENER_HASH)) {
            pstm.setString(1, usuarioOCorreo);
            pstm.setString(2, usuarioOCorreo);
            pstm.setString(3, codigo);

            try (ResultSet rs = pstm.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return rs.getString("password_hash");
            }
        }
    }

    public void actualizarPasswordYLimpiarCodigo(String usuarioOCorreo, String nuevoPasswordHash) throws SQLException {
        Connection conexion = DatabaseConnection.getDatabaseConnection();

        Integer idUsuario = buscarIdUsuarioActivo(conexion, usuarioOCorreo);
        if (idUsuario == null) {
            throw new SQLException("No se encontró el usuario al actualizar la contraseña");
        }

        try (PreparedStatement pstm = conexion.prepareStatement(SQL_ACTUALIZAR_PASSWORD_Y_LIMPIAR_CODIGO)) {
            pstm.setString(1, nuevoPasswordHash);
            pstm.setInt(2, idUsuario);
            pstm.executeUpdate();
        }
    }

    private Integer buscarIdUsuarioActivo(Connection conexion, String usuarioOCorreo) throws SQLException {
        try (PreparedStatement pstm = conexion.prepareStatement(SQL_BUSCAR_ID_USUARIO_ACTIVO)) {
            pstm.setString(1, usuarioOCorreo);
            pstm.setString(2, usuarioOCorreo);
            try (ResultSet rs = pstm.executeQuery()) {
                return rs.next() ? rs.getInt("id_usuario") : null;
            }
        }
    }
}