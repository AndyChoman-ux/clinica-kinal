package main.java.com.vyorg.clinica.kinal0.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.java.com.vyorg.clinica.kinal0.dto.request.LoginDTORequest;
import main.java.com.vyorg.clinica.kinal0.dto.response.LoginDTOResponse;
import main.java.com.vyorg.clinica.kinal0.service.AccesoService;
import main.java.com.vyorg.clinica.kinal0.util.SceneManager;
import main.java.com.vyorg.clinica.kinal0.util.Sesion;

public class LoginController implements Initializable {

    private final AccesoService accesoService;
    private final SceneManager sceneManager;

    @FXML
    private TextField campoUsuario;

    @FXML
    private PasswordField campoPassword;

    public LoginController(AccesoService accesoService, SceneManager sceneManager) {
        this.accesoService = accesoService;
        this.sceneManager = sceneManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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

        try {
            LoginDTOResponse response = accesoService.login(new LoginDTORequest(usuario, password));
           Sesion.iniciar(response);
           sceneManager.showAlertInfo("Bienvenido, " + response.getNombreCompleto(),
              "Inicio de sesión correcto", "Rol: " + response.getNombreRol(), AlertType.INFORMATION);
           sceneManager.showDashboardView();
        } catch (RuntimeException e) {
            sceneManager.showAlertInfo("Acceso denegado", "No se pudo iniciar sesión",
                    e.getMessage(), AlertType.WARNING);
        } catch (Exception e) {
            sceneManager.showAlertInfo("Error", "No se pudo abrir el panel principal",
                    e.getMessage(), AlertType.ERROR);
        }
    }
    
    @FXML
private void irARegistro() throws Exception {
    sceneManager.showRegistroView();
}
}