package main.java.com.vyorg.clinica.kinal0.controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.java.com.vyorg.clinica.kinal0.dto.request.RegistroDTORequest;
import main.java.com.vyorg.clinica.kinal0.dto.response.RegistroDTOResponse;
import main.java.com.vyorg.clinica.kinal0.service.RegistroService;
import main.java.com.vyorg.clinica.kinal0.util.SceneManager;

public class RegistroController implements Initializable {

    private final RegistroService registroService;
    private final SceneManager sceneManager;

    @FXML
    private TextField campoCorreoRegistro;

    @FXML
    private TextField campoNombreUsuarioRegistro;

    @FXML
    private PasswordField campoPasswordRegistro;

    public RegistroController(RegistroService registroService, SceneManager sceneManager) {
        this.registroService = registroService;
        this.sceneManager = sceneManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void registrarUsuario() {
        String correo = campoCorreoRegistro.getText();
        String nombreUsuario = campoNombreUsuarioRegistro.getText();
        String password = campoPasswordRegistro.getText();

        try {
            RegistroDTOResponse response = registroService.registrar(
                    new RegistroDTORequest(correo, nombreUsuario, password));

            sceneManager.showAlertInfo("Registro exitoso",
                    "Cuenta creada como " + response.getNombreRol(),
                    "Ya puedes iniciar sesión con tu usuario: " + response.getNombreUsuario(),
                    AlertType.INFORMATION);

            sceneManager.showLoginView();
        } catch (RuntimeException e) {
            sceneManager.showAlertInfo("No se pudo registrar", "Revisa los datos", e.getMessage(), AlertType.WARNING);
        } catch (SQLException e) {
            sceneManager.showAlertInfo("Error de conexión", "No se pudo conectar a la base de datos",
                    e.getMessage(), AlertType.ERROR);
        } catch (Exception e) {
            sceneManager.showAlertInfo("Error", "No se pudo completar el registro", e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void volverALogin() throws Exception {
        sceneManager.showLoginView();
    }
}