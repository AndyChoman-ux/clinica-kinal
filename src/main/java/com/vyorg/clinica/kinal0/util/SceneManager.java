package main.java.com.vyorg.clinica.kinal0.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import main.java.com.vyorg.clinica.kinal0.controller.DashboardController;
import main.java.com.vyorg.clinica.kinal0.controller.LoginController;

/**
 * Se encarga de cargar los FXML y cambiar la escena del stage principal.
 * Los controladores reciben el SceneManager por constructor (mediante
 * setControllerFactory) para poder pedirle que navegue a otra vista.
 */
public class SceneManager {

    private final Stage stage;

    public SceneManager(Stage stage) {
        this.stage = stage;
    }

    public void showLoginView() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/view/login.fxml"));
        loader.setControllerFactory(c -> new LoginController(this));

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

    public void showAlertInfo(String title, String header, String content, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
