package main.java.com.vyorg.clinica.kinal0;

import javafx.application.Application;
import javafx.stage.Stage;
import main.java.com.vyorg.clinica.kinal0.util.SceneManager;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        SceneManager sceneManager = new SceneManager(stage);
        sceneManager.showLoginView();
    }

}
