package main.java.com.vyorg.clinica.kinal0.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import main.java.com.vyorg.clinica.kinal0.controller.DashboardController;
import main.java.com.vyorg.clinica.kinal0.controller.LoginController;
import main.java.com.vyorg.clinica.kinal0.repository.AccesoRepository;
import main.java.com.vyorg.clinica.kinal0.service.AccesoService;
import main.java.com.vyorg.clinica.kinal0.controller.PacienteController;
import main.java.com.vyorg.clinica.kinal0.repository.PacienteRepository;
import main.java.com.vyorg.clinica.kinal0.service.PacienteService;

public class SceneManager {

    private final Stage stage;

    public SceneManager(Stage stage) {
        this.stage = stage;
    }

    public void showLoginView() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/view/login.fxml"));

        loader.setControllerFactory(clazz -> {
            if (clazz == LoginController.class) {
                AccesoRepository accesoRepository = new AccesoRepository();
                AccesoService accesoService = new AccesoService(accesoRepository);
                return new LoginController(accesoService, this);
            }
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Error al crear el controlador: " + e.getMessage());
            }
        });

        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("Clínica - Iniciar sesión");
        stage.show();
    }

    public void showDashboardView() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/view/dashboard.fxml"));
        loader.setControllerFactory(c -> new DashboardController(this));

        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("Clínica - Panel principal");
        stage.show();
    }
    
    public void showPacientesView() throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/view/paciente.fxml"));

    loader.setControllerFactory(clazz -> {
        if (clazz == PacienteController.class) {
            PacienteRepository pacienteRepository = new PacienteRepository();
            PacienteService pacienteService = new PacienteService(pacienteRepository);
            return new PacienteController(pacienteService, this);
        }
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el controlador: " + e.getMessage());
        }
    });

    Parent root = loader.load();
    stage.setScene(new Scene(root));
    stage.setTitle("Clínica - Pacientes");
    stage.show();
}

    public void showAlertInfo(String title, String header, String content, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}