package main.java.com.vyorg.clinica.kinal0.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import main.java.com.vyorg.clinica.kinal0.util.FondoCover;
import main.java.com.vyorg.clinica.kinal0.util.SceneManager;

public class EnConstruccionController implements Initializable {

    private final SceneManager sceneManager;
    private final String titulo;

    @FXML private StackPane rootPane;
    @FXML private ImageView imagenFondo;
    @FXML private Label lblTitulo;

    public EnConstruccionController(SceneManager sceneManager, String titulo) {
        this.sceneManager = sceneManager;
        this.titulo = titulo;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FondoCover.aplicar(imagenFondo, rootPane);
        lblTitulo.setText(titulo);
    }

    @FXML
    private void volver() {
        try {
            sceneManager.showDashboardView();
        } catch (Exception e) {
            sceneManager.showAlertInfo("Error", "No se pudo volver al panel principal", e.getMessage(), AlertType.ERROR);
        }
    }
}