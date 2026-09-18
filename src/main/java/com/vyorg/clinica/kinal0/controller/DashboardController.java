package main.java.com.vyorg.clinica.kinal0.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import main.java.com.vyorg.clinica.kinal0.dto.response.LoginDTOResponse;
import main.java.com.vyorg.clinica.kinal0.util.SceneManager;
import main.java.com.vyorg.clinica.kinal0.util.Sesion;

/**
 * Controlador del dashboard (dashboard.fxml).
 * Muestra el usuario en sesión y aplica permisos según su rol.
 */
public class DashboardController implements Initializable {

    private static final int ID_ROL_ADMIN = 1;
    private final SceneManager sceneManager;

    @FXML
    private Label lblUsuario;

    @FXML
    private Label lblRol;

    @FXML
    private Button btnAdministrarUsuarios;

    public DashboardController(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }
    
    @FXML
private void irAPacientes() {
    try {
        sceneManager.showPacientesView();
    } catch (Exception e) {
        sceneManager.showAlertInfo("Error", "No se pudo abrir Pacientes", e.getMessage(), AlertType.ERROR);
    }
}

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LoginDTOResponse usuario = Sesion.getUsuarioActual();

        if (usuario != null) {
            lblUsuario.setText("Usuario: " + usuario.getNombreCompleto());
            lblRol.setText("Rol: " + usuario.getNombreRol());
        }

        // Ejemplo: solo el rol "Administrador" ve este botón.
        // Ajusta el nombre del rol al que exista en tu tabla "roles".
      boolean esAdmin = Sesion.tieneRolId(ID_ROL_ADMIN);
        btnAdministrarUsuarios.setVisible(esAdmin);
        btnAdministrarUsuarios.setManaged(esAdmin);
    }

    @FXML
    private void cerrarSesion() {
        try {
            Sesion.cerrar();
            sceneManager.showLoginView();
        } catch (Exception e) {
            sceneManager.showAlertInfo("Error", "No se pudo volver al login",
                    e.getMessage(), AlertType.ERROR);
        }
    }
}