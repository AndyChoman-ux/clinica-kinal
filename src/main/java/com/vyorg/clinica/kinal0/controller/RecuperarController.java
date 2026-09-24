package main.java.com.vyorg.clinica.kinal0.controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.java.com.vyorg.clinica.kinal0.dto.request.RecuperarDTORequest;
import main.java.com.vyorg.clinica.kinal0.dto.request.SolicitarCodigoDTORequest;
import main.java.com.vyorg.clinica.kinal0.service.RecuperarService;
import main.java.com.vyorg.clinica.kinal0.util.SceneManager;

public class RecuperarController implements Initializable {

    private final RecuperarService recuperarService;
    private final SceneManager sceneManager;

    @FXML
    private TextField campoUsuarioOCorreo;

    @FXML
    private TextField campoCodigo;

    @FXML
    private PasswordField campoNuevaPassword;

    @FXML
    private PasswordField campoConfirmarPassword;

    public RecuperarController(RecuperarService recuperarService, SceneManager sceneManager) {
        this.recuperarService = recuperarService;
        this.sceneManager = sceneManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void solicitarCodigo() {
        String usuarioOCorreo = campoUsuarioOCorreo.getText();

        try {
            String codigo = recuperarService.solicitarCodigo(new SolicitarCodigoDTORequest(usuarioOCorreo));

            
            sceneManager.showAlertInfo("Código generado", "Válido por 15 minutos",
                    "Tu código de verificación es: " + codigo, AlertType.INFORMATION);

        } catch (RuntimeException e) {
            sceneManager.showAlertInfo("No se pudo generar el código", "Revisa los datos", e.getMessage(), AlertType.WARNING);
        } catch (SQLException e) {
            sceneManager.showAlertInfo("Error de conexión", "No se pudo conectar a la base de datos",
                    e.getMessage(), AlertType.ERROR);
        } catch (Exception e) {
            sceneManager.showAlertInfo("Error", "No se pudo completar la operación", e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void restablecerPassword() {
        String usuarioOCorreo = campoUsuarioOCorreo.getText();
        String codigo = campoCodigo.getText();
        String nuevaPassword = campoNuevaPassword.getText();
        String confirmarPassword = campoConfirmarPassword.getText();

        try {
            recuperarService.restablecerPassword(
                    new RecuperarDTORequest(usuarioOCorreo, codigo, nuevaPassword, confirmarPassword));

            sceneManager.showAlertInfo("Contraseña actualizada", "Listo",
                    "Ya puedes iniciar sesión con tu nueva contraseña.", AlertType.INFORMATION);

            sceneManager.showLoginView();
        } catch (RuntimeException e) {
            sceneManager.showAlertInfo("No se pudo restablecer", "Revisa los datos", e.getMessage(), AlertType.WARNING);
        } catch (SQLException e) {
            sceneManager.showAlertInfo("Error de conexión", "No se pudo conectar a la base de datos",
                    e.getMessage(), AlertType.ERROR);
        } catch (Exception e) {
            sceneManager.showAlertInfo("Error", "No se pudo completar la operación", e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void volverALogin() throws Exception {
        sceneManager.showLoginView();
    }
}