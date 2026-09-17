package main.java.com.vyorg.clinica.kinal0.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import main.java.com.vyorg.clinica.kinal0.util.SceneManager;

/**
 * Controlador del dashboard (dashboard.fxml).
 * Vista base sin diseño: solo permite volver al login.
 */
public class DashboardController {

    private final SceneManager sceneManager;

    public DashboardController(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @FXML
    private void cerrarSesion() {
        try {
            sceneManager.showLoginView();
        } catch (Exception e) {
            sceneManager.showAlertInfo("Error", "No se pudo volver al login",
                    e.getMessage(), AlertType.ERROR);
        }
    }
}
