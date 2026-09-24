package main.java.com.vyorg.clinica.kinal0.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import main.java.com.vyorg.clinica.kinal0.config.DatabaseConnection;
import main.java.com.vyorg.clinica.kinal0.model.Rol;
import main.java.com.vyorg.clinica.kinal0.model.Usuario;

public class UsuarioRepository {

    private static final String CAMPOS =
            "u.id_usuario, u.nombre_completo, u.correo, u.nombre_usuario, u.id_rol, u.activo, r.nombre_rol";
    private static final String FROM =
            " from usuarios u inner join roles r on u.id_rol = r.id_rol ";

    public List<Usuario> findAll() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "select " + CAMPOS + FROM + "order by u.nombre_completo";

        try (PreparedStatement pstm = DatabaseConnection.getDatabaseConnection().prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                usuarios.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    public List<Usuario> buscarPorNombre(String texto) {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "select " + CAMPOS + FROM +
                     "where u.nombre_completo like ? or u.nombre_usuario like ? order by u.nombre_completo";

        try (PreparedStatement pstm = DatabaseConnection.getDatabaseConnection().prepareStatement(sql)) {
            String patron = "%" + texto + "%";
            pstm.setString(1, patron);
            pstm.setString(2, patron);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    public List<Rol> listarRoles() {
        List<Rol> roles = new ArrayList<>();
        String sql = "select id_rol, nombre_rol from roles order by nombre_rol";

        try (PreparedStatement pstm = DatabaseConnection.getDatabaseConnection().prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                roles.add(new Rol(rs.getInt("id_rol"), rs.getString("nombre_rol")));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar roles: " + e.getMessage());
        }
        return roles;
    }

    public boolean insertar(Usuario usuario, String passwordHash) {
        String sql = "insert into usuarios (nombre_completo, correo, nombre_usuario, password_hash, id_rol, activo) " +
                     "values (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstm = DatabaseConnection.getDatabaseConnection().prepareStatement(sql)) {
            pstm.setString(1, usuario.getNombreCompleto());
            pstm.setString(2, usuario.getCorreo());
            pstm.setString(3, usuario.getNombreUsuario());
            pstm.setString(4, passwordHash);
            pstm.setInt(5, usuario.getIdRol());
            pstm.setBoolean(6, usuario.isActivo());
            return pstm.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new RuntimeException("El correo o el nombre de usuario ya están registrados");
        } catch (SQLException e) {
            System.out.println("Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Usuario usuario) {
        String sql = "update usuarios set nombre_completo = ?, correo = ?, nombre_usuario = ?, " +
                     "id_rol = ?, activo = ? where id_usuario = ?";

        try (PreparedStatement pstm = DatabaseConnection.getDatabaseConnection().prepareStatement(sql)) {
            pstm.setString(1, usuario.getNombreCompleto());
            pstm.setString(2, usuario.getCorreo());
            pstm.setString(3, usuario.getNombreUsuario());
            pstm.setInt(4, usuario.getIdRol());
            pstm.setBoolean(5, usuario.isActivo());
            pstm.setInt(6, usuario.getIdUsuario());
            return pstm.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new RuntimeException("El correo o el nombre de usuario ya están registrados");
        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarPassword(int idUsuario, String passwordHash) {
        String sql = "update usuarios set password_hash = ? where id_usuario = ?";

        try (PreparedStatement pstm = DatabaseConnection.getDatabaseConnection().prepareStatement(sql)) {
            pstm.setString(1, passwordHash);
            pstm.setInt(2, idUsuario);
            return pstm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar la contraseña: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idUsuario) {
        String sql = "delete from usuarios where id_usuario = ?";

        try (PreparedStatement pstm = DatabaseConnection.getDatabaseConnection().prepareStatement(sql)) {
            pstm.setInt(1, idUsuario);
            return pstm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    private Usuario mapRow(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getInt("id_usuario"),
                rs.getString("nombre_completo"),
                rs.getString("correo"),
                rs.getString("nombre_usuario"),
                rs.getInt("id_rol"),
                rs.getString("nombre_rol"),
                rs.getBoolean("activo")
        );
    }
}