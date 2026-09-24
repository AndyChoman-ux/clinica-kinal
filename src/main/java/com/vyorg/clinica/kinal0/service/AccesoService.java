package main.java.com.vyorg.clinica.kinal0.service;

import main.java.com.vyorg.clinica.kinal0.dto.request.LoginDTORequest;
import main.java.com.vyorg.clinica.kinal0.dto.response.LoginDTOResponse;
import main.java.com.vyorg.clinica.kinal0.repository.AccesoRepository;
import org.mindrot.jbcrypt.BCrypt;

public class AccesoService {

    private final AccesoRepository accesoRepository;

    public AccesoService(AccesoRepository accesoRepository) {
        this.accesoRepository = accesoRepository;
    }

    public LoginDTOResponse login(LoginDTORequest loginDTORequest) {

        if (loginDTORequest == null) {
            throw new RuntimeException("Los datos estan vacios");
        } else if (loginDTORequest.getNombreUsuario() == null || loginDTORequest.getPassword() == null) {
            throw new RuntimeException("Uno o los dos campos estan vacios");
        } else if (loginDTORequest.getNombreUsuario().isEmpty() || loginDTORequest.getPassword().isEmpty()) {
            throw new RuntimeException("No puedes dejar campos en blanco");
        }

        LoginDTOResponse response = accesoRepository.findUserByNombreUsuario(loginDTORequest);

        if (response == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        if (!response.isActivo()) {
            throw new RuntimeException("El usuario esta inactivo");
        }

        if (response.getPasswordHash() == null) {
            throw new RuntimeException("Error");
        }

        if (BCrypt.checkpw(loginDTORequest.getPassword(), response.getPasswordHash())) {
            return response;
        }

        throw new RuntimeException("Usuario o contraseña incorrectos");
    }
}