package main.java.com.vyorg.clinica.kinal0.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.java.com.vyorg.clinica.kinal0.config.DatabaseConnection;
import main.java.com.vyorg.clinica.kinal0.dto.request.LoginDTORequest;
import main.java.com.vyorg.clinica.kinal0.dto.response.LoginDTOResponse;

public class AccesoRepository {

    public LoginDTOResponse findUserByNombreUsuario(LoginDTORequest loginDTORequest) {
        String sql = "select u.id_usuario, u.nombre_completo, u.nombre_usuario, " +
             "u.password_hash, u.id_rol, u.activo, r.nombre_rol " +
             "from usuarios as u " +
             "inner join roles as r on u.id_rol = r.id_rol " +
             "where u.nombre_usuario = ? or u.correo = ? ";
        try (PreparedStatement pstm = DatabaseConnection.getDatabaseConnection().prepareStatement(sql)) {
            pstm.setString(1, loginDTORequest.getNombreUsuario());
            pstm.setString(2, loginDTORequest.getNombreUsuario());
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
               return new LoginDTOResponse(
        rs.getInt("id_usuario"),
        rs.getString("nombre_completo"),
        rs.getString("nombre_usuario"),
        rs.getString("password_hash"),
        rs.getInt("id_rol"),
        rs.getString("nombre_rol"),
        rs.getBoolean("activo")
);  
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar el usuario: " + e.getMessage());
        }
        return null;
    }
}