package main.java.com.vyorg.clinica.kinal0.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import main.java.com.vyorg.clinica.kinal0.controller.CitaController;
import main.java.com.vyorg.clinica.kinal0.controller.DashboardController;

import main.java.com.vyorg.clinica.kinal0.controller.EstadisticasController;
import main.java.com.vyorg.clinica.kinal0.controller.ExpedienteController;
import main.java.com.vyorg.clinica.kinal0.controller.LoginController;
import main.java.com.vyorg.clinica.kinal0.controller.PacienteController;
import main.java.com.vyorg.clinica.kinal0.controller.RecuperarController;
import main.java.com.vyorg.clinica.kinal0.controller.RegistroController;
import main.java.com.vyorg.clinica.kinal0.controller.UsuarioController;
import main.java.com.vyorg.clinica.kinal0.repository.AccesoRepository;
import main.java.com.vyorg.clinica.kinal0.repository.CitaRepository;
import main.java.com.vyorg.clinica.kinal0.repository.ExpedienteRepository;
import main.java.com.vyorg.clinica.kinal0.repository.PacienteRepository;
import main.java.com.vyorg.clinica.kinal0.repository.RecuperarRepository;
import main.java.com.vyorg.clinica.kinal0.repository.RegistroRepository;
import main.java.com.vyorg.clinica.kinal0.repository.UsuarioRepository;
import main.java.com.vyorg.clinica.kinal0.service.AccesoService;
import main.java.com.vyorg.clinica.kinal0.service.CitaService;
import main.java.com.vyorg.clinica.kinal0.service.ExpedienteService;
import main.java.com.vyorg.clinica.kinal0.service.PacienteService;
import main.java.com.vyorg.clinica.kinal0.service.RecuperarService;
import main.java.com.vyorg.clinica.kinal0.service.RegistroService;
import main.java.com.vyorg.clinica.kinal0.service.UsuarioService;

public class SceneManager {

    private static final double ANCHO_VENTANA = 1000.0;
    private static final double ALTO_VENTANA = 650.0;
    private final Stage stage;

    public SceneManager(Stage stage) {
        this.stage = stage;
        stage.setMinWidth(820.0);
        stage.setMinHeight(600.0);
    }

    public void showLoginView() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/view/login-view.fxml"));

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
        stage.setScene(new Scene(root, ANCHO_VENTANA, ALTO_VENTANA));
        stage.setTitle("Clínica - Iniciar sesión");
        stage.show();
    }

    public void showRegistroView() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/view/registro-view.fxml"));

        loader.setControllerFactory(clazz -> {
            if (clazz == RegistroController.class) {
                RegistroRepository registroRepository = new RegistroRepository();
                RegistroService registroService = new RegistroService(registroRepository);
                return new RegistroController(registroService, this);
            }
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Error al crear el controlador: " + e.getMessage());
            }
        });

        Parent root = loader.load();
        stage.setScene(new Scene(root, ANCHO_VENTANA, ALTO_VENTANA));
        stage.setTitle("Clínica - Crear cuenta");
        stage.show();
    }

    public void showRecuperarView() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/view/recuperar-view.fxml"));

        loader.setControllerFactory(clazz -> {
            if (clazz == RecuperarController.class) {
                RecuperarRepository recuperarRepository = new RecuperarRepository();
                RecuperarService recuperarService = new RecuperarService(recuperarRepository);
                return new RecuperarController(recuperarService, this);
            }
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Error al crear el controlador: " + e.getMessage());
            }
        });

        Parent root = loader.load();
        stage.setScene(new Scene(root, ANCHO_VENTANA, ALTO_VENTANA));
        stage.setTitle("Clínica - Restablecer contraseña");
        stage.show();
    }

    public void showEnConstruccion(String titulo) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/view/en-construccion.fxml"));


        Parent root = loader.load();
        stage.setScene(new Scene(root, ANCHO_VENTANA, ALTO_VENTANA));
        stage.setTitle("Clínica - " + titulo);
        stage.show();
    }

    public void showDashboardView() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/view/dashboard-view.fxml"));
        loader.setControllerFactory(c -> new DashboardController(this, new PacienteRepository(), new UsuarioRepository(), new CitaRepository()));  
        Parent root = loader.load();
        stage.setScene(new Scene(root, ANCHO_VENTANA, ALTO_VENTANA));
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
        stage.setScene(new Scene(root, ANCHO_VENTANA, ALTO_VENTANA));
        stage.setTitle("Clínica - Pacientes");
        stage.show();
    }

    public void showUsuariosView() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/view/usuario.fxml"));

        loader.setControllerFactory(clazz -> {
            if (clazz == UsuarioController.class) {
                UsuarioRepository usuarioRepository = new UsuarioRepository();
                UsuarioService usuarioService = new UsuarioService(usuarioRepository);
                return new UsuarioController(usuarioService, this);
            }
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Error al crear el controlador: " + e.getMessage());
            }
        });

        Parent root = loader.load();
        stage.setScene(new Scene(root, ANCHO_VENTANA, ALTO_VENTANA));
        stage.setTitle("Clínica - Usuarios");
        stage.show();
    }

    public void showCitasView() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/view/cita.fxml"));
        loader.setControllerFactory(clazz -> {
            if (clazz == CitaController.class) {
                CitaService citaService = new CitaService(new CitaRepository());
                PacienteService pacienteService = new PacienteService(new PacienteRepository());
                return new CitaController(citaService, pacienteService, this);
            }
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Error al crear el controlador: " + e.getMessage());
            }
        });
        Parent root = loader.load();
        stage.setScene(new Scene(root, ANCHO_VENTANA, ALTO_VENTANA));
        stage.setTitle("Clínica - Citas");
        stage.show();
    }

    public void showExpedientesView() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/view/expediente.fxml"));
        loader.setControllerFactory(clazz -> {
            if (clazz == ExpedienteController.class) {
                ExpedienteService expedienteService = new ExpedienteService(new ExpedienteRepository());
                PacienteService pacienteService = new PacienteService(new PacienteRepository());
                CitaRepository citaRepository = new CitaRepository();
                return new ExpedienteController(expedienteService, pacienteService, citaRepository, this);
            }
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Error al crear el controlador: " + e.getMessage());
            }
        });
        Parent root = loader.load();
        stage.setScene(new Scene(root, ANCHO_VENTANA, ALTO_VENTANA));
        stage.setTitle("Clínica - Expedientes");
        stage.show();
    }

    public void showEstadisticasView() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/view/estadisticas.fxml"));
        loader.setControllerFactory(clazz -> new EstadisticasController(this,
                new PacienteRepository(), new UsuarioRepository(), new CitaRepository(), new ExpedienteRepository()));
        Parent root = loader.load();
        stage.setScene(new Scene(root, ANCHO_VENTANA, ALTO_VENTANA));
        stage.setTitle("Clínica - Estadísticas");
        stage.show();
    }

    public void showAlertInfo(String title, String header, String content, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("/main/resources/ccs/styles.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("clinic-alert");
        if (alertType == AlertType.ERROR || alertType == AlertType.WARNING) {
            alert.getDialogPane().getStyleClass().add("clinic-alert-error");
        }

        alert.showAndWait();
    }
}