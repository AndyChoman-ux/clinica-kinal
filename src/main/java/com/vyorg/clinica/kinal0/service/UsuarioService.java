package main.java.com.vyorg.clinica.kinal0.service;

import java.util.List;
import java.util.regex.Pattern;
import main.java.com.vyorg.clinica.kinal0.model.Rol;
import main.java.com.vyorg.clinica.kinal0.model.Usuario;
import main.java.com.vyorg.clinica.kinal0.repository.UsuarioRepository;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioService {

    private static final Pattern PATRON_CORREO =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}(\\.[a-zA-Z]{2,})?$");

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> buscar(String texto) {
        if (texto == null || texto.isBlank()) {
            return listar();
        }
        return usuarioRepository.buscarPorNombre(texto.trim());
    }

    public List<Rol> listarRoles() {
        return usuarioRepository.listarRoles();
    }

    public void guardar(Usuario usuario, String password) {
        validar(usuario);
        boolean esNuevo = usuario.getIdUsuario() == 0;

        if (esNuevo) {
            if (password == null || password.isBlank()) {
                throw new RuntimeException("La contraseña es obligatoria para un usuario nuevo");
            }
            if (password.length() < 6) {
                throw new RuntimeException("La contraseña debe tener al menos 6 caracteres");
            }
            if (!usuarioRepository.insertar(usuario, BCrypt.hashpw(password, BCrypt.gensalt()))) {
                throw new RuntimeException("No se pudo crear el usuario");
            }
            return;
        }

        if (!usuarioRepository.actualizar(usuario)) {
            throw new RuntimeException("No se pudo actualizar el usuario");
        }

        if (password != null && !password.isBlank()) {
            if (password.length() < 6) {
                throw new RuntimeException("La contraseña debe tener al menos 6 caracteres");
            }
            usuarioRepository.actualizarPassword(usuario.getIdUsuario(), BCrypt.hashpw(password, BCrypt.gensalt()));
        }
    }

    public void eliminar(int idUsuario) {
        if (!usuarioRepository.eliminar(idUsuario)) {
            throw new RuntimeException("No se pudo eliminar el usuario");
        }
    }

    private void validar(Usuario usuario) {
        if (usuario.getNombreCompleto() == null || usuario.getNombreCompleto().isBlank()) {
            throw new RuntimeException("El nombre completo es obligatorio");
        }
        if (usuario.getNombreUsuario() == null || usuario.getNombreUsuario().isBlank()) {
            throw new RuntimeException("El nombre de usuario es obligatorio");
        }
        if (usuario.getCorreo() == null || usuario.getCorreo().isBlank()) {
            throw new RuntimeException("El correo es obligatorio");
        }
        if (!PATRON_CORREO.matcher(usuario.getCorreo().trim()).matches()) {
            throw new RuntimeException("El correo debe tener un formato válido, por ejemplo nombre@gmail.com");
        }
        if (usuario.getIdRol() == 0) {
            throw new RuntimeException("Debes seleccionar un rol");
        }
    }
}