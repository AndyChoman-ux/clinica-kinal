package main.java.com.vyorg.clinica.kinal0.service;

import java.sql.SQLException;
import main.java.com.vyorg.clinica.kinal0.dto.request.RegistroDTORequest;
import main.java.com.vyorg.clinica.kinal0.dto.response.RegistroDTOResponse;
import main.java.com.vyorg.clinica.kinal0.repository.RegistroRepository;
import org.mindrot.jbcrypt.BCrypt;

public class RegistroService {

    private final RegistroRepository registroRepository;

    public RegistroService(RegistroRepository registroRepository) {
        this.registroRepository = registroRepository;
    }

    public RegistroDTOResponse registrar(RegistroDTORequest request) throws SQLException {

        if (request == null) {
            throw new RuntimeException("Los datos estan vacios");
        }
        if (request.getCorreo() == null || request.getCorreo().isBlank()) {
            throw new RuntimeException("El correo es obligatorio");
        }
        if (!request.getCorreo().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new RuntimeException("El correo no tiene un formato válido");
        }
        if (request.getNombreUsuario() == null || request.getNombreUsuario().isBlank()) {
            throw new RuntimeException("El nombre de usuario es obligatorio");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new RuntimeException("La contraseña es obligatoria");
        }
        if (request.getPassword().length() < 6) {
            throw new RuntimeException("La contraseña debe tener al menos 6 caracteres");
        }

        String passwordHash = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());

        return registroRepository.registrarUsuario(
                request.getNombreUsuario(), request.getCorreo(), passwordHash);
    }
}