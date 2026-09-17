package main.java.com.vyorg.clinica.kinal0.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.java.com.vyorg.clinica.kinal0.util.SceneManager;

/**
 * Controlador de la vista de login (login.fxml).
 *
 * Por ahora solo valida que los campos no vengan vacíos y navega al
 * dashboard mediante el SceneManager. La autenticación real contra la
 * base de datos (usando las capas service/repository/model y BCrypt,
 * que ya están en el proyecto) se implementará más adelante, cuando se
 * trabaje el resto de requisitos del proyecto (expedientes clínicos).
 */
public class LoginController {

    private final SceneManager sceneManager;

    @FXML
    private TextField campoUsuario;

    @FXML
    private PasswordField campoPassword;

    public LoginController(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @FXML
    private void iniciarSesion() {
        String usuario = campoUsuario.getText();
        String password = campoPassword.getText();

        if (usuario == null || usuario.isBlank() || password == null || password.isBlank()) {
            sceneManager.showAlertInfo("Campos incompletos", "Faltan datos",
                    "Debes ingresar usuario y contraseña.", AlertType.WARNING);
            return;
        }

        // TODO: reemplazar esta validación temporal por la autenticación real
        // (AuthService -> UsuarioRepository -> DatabaseConnection + BCrypt).
        try {
            sceneManager.showDashboardView();
        } catch (Exception e) {
            sceneManager.showAlertInfo("Error", "No se pudo abrir el panel principal",
                    e.getMessage(), AlertType.ERROR);
        }
    }
}
