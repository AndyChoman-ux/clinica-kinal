package main.java.com.vyorg.clinica.kinal0.service;

import java.sql.SQLException;
import main.java.com.vyorg.clinica.kinal0.dto.request.RecuperarDTORequest;
import main.java.com.vyorg.clinica.kinal0.dto.request.SolicitarCodigoDTORequest;
import main.java.com.vyorg.clinica.kinal0.repository.RecuperarRepository;
import org.mindrot.jbcrypt.BCrypt;

public class RecuperarService {

    private final RecuperarRepository recuperarRepository;

    public RecuperarService(RecuperarRepository recuperarRepository) {
        this.recuperarRepository = recuperarRepository;
    }

    public String solicitarCodigo(SolicitarCodigoDTORequest request) throws SQLException {
        if (request == null || request.getUsuarioOCorreo() == null || request.getUsuarioOCorreo().isBlank()) {
            throw new RuntimeException("Debes indicar tu usuario o correo");
        }

        String usuarioOCorreo = request.getUsuarioOCorreo().trim();
        String codigo = recuperarRepository.generarCodigo(usuarioOCorreo);

        if (codigo == null) {
            throw new RuntimeException("No se encontró ninguna cuenta activa con ese usuario o correo");
        }

        return codigo;
    }

    public void restablecerPassword(RecuperarDTORequest request) throws SQLException {

        if (request == null) {
            throw new RuntimeException("Los datos estan vacios");
        }
        if (request.getUsuarioOCorreo() == null || request.getUsuarioOCorreo().isBlank()) {
            throw new RuntimeException("Debes indicar tu usuario o correo");
        }
        if (request.getCodigo() == null || request.getCodigo().isBlank()) {
            throw new RuntimeException("Debes ingresar el código de verificación");
        }
        if (request.getNuevaPassword() == null || request.getNuevaPassword().isBlank()) {
            throw new RuntimeException("La nueva contraseña es obligatoria");
        }
        if (request.getNuevaPassword().length() < 6) {
            throw new RuntimeException("La contraseña debe tener al menos 6 caracteres");
        }
        if (!request.getNuevaPassword().equals(request.getConfirmarPassword())) {
            throw new RuntimeException("Las contraseñas no coinciden");
        }

        String usuarioOCorreo = request.getUsuarioOCorreo().trim();
        String codigo = request.getCodigo().trim();

        String hashActual = recuperarRepository.validarCodigoYObtenerHashActual(usuarioOCorreo, codigo);

        if (hashActual == null) {
            throw new RuntimeException("El código es inválido o ya expiró. Solicita uno nuevo.");
        }

        boolean esLaMismaPassword;
        try {
            esLaMismaPassword = BCrypt.checkpw(request.getNuevaPassword(), hashActual);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("No se pudo validar la contraseña. Intenta de nuevo.");
        }

        if (esLaMismaPassword) {
            throw new RuntimeException("La nueva contraseña no puede ser igual a la anterior");
        }

        String nuevoHash = BCrypt.hashpw(request.getNuevaPassword(), BCrypt.gensalt());
        recuperarRepository.actualizarPasswordYLimpiarCodigo(usuarioOCorreo, nuevoHash);
    }
}