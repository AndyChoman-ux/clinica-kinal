package main.java.com.vyorg.clinica.kinal0.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import main.java.com.vyorg.clinica.kinal0.dto.response.LoginDTOResponse;
import main.java.com.vyorg.clinica.kinal0.model.Notificacion;
import main.java.com.vyorg.clinica.kinal0.repository.CitaRepository;
import main.java.com.vyorg.clinica.kinal0.repository.PacienteRepository;
import main.java.com.vyorg.clinica.kinal0.repository.UsuarioRepository;
import main.java.com.vyorg.clinica.kinal0.service.NotificacionService;
import main.java.com.vyorg.clinica.kinal0.util.FondoCover;
import main.java.com.vyorg.clinica.kinal0.util.NotificacionPanel;
import main.java.com.vyorg.clinica.kinal0.util.SceneManager;
import main.java.com.vyorg.clinica.kinal0.util.Sesion;

public class DashboardController implements Initializable {

    private static final int ID_ROL_ADMIN = 1;
    private final SceneManager sceneManager;
    private final PacienteRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacionService notificacionService;

    @FXML
    private Label lblUsuario;

    @FXML
    private Label lblRol;

    @FXML
    private Label lblTotalPacientes;

    @FXML
    private Label lblTotalUsuarios;

    @FXML
    private Button btnAdministrarUsuarios;

    @FXML
    private Button btnMenuUsuarios;

    @FXML
    private StackPane rootPane;

    @FXML
    private ImageView imagenFondo;

    @FXML
    private VBox cajaAdministrarUsuarios;

    @FXML
    private StackPane cajaCampana;

    @FXML
    private Label lblBadgeNotificaciones;

    public DashboardController(SceneManager sceneManager, PacienteRepository pacienteRepository,
                               UsuarioRepository usuarioRepository, CitaRepository citaRepository) {
        this.sceneManager = sceneManager;
        this.pacienteRepository = pacienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificacionService = new NotificacionService(citaRepository);
    }

    @FXML
    private void irAPacientes() {
        try {
            sceneManager.showPacientesView();
        } catch (Exception e) {
            sceneManager.showAlertInfo("Error", "No se pudo abrir Pacientes", e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void irACitas() {
        try {
            sceneManager.showCitasView();
        } catch (Exception e) {
            sceneManager.showAlertInfo("Error", "No se pudo abrir Citas", e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void abrirNotificaciones() {
        NotificacionPanel.mostrar(cajaCampana, notificacionService.listar(), sceneManager);
    }

    @FXML
    private void irAExpedientes() {
        try {
            sceneManager.showExpedientesView();
        } catch (Exception e) {
            sceneManager.showAlertInfo("Error", "No se pudo abrir Expedientes", e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void irAEstadisticas() {
        try {
            sceneManager.showEstadisticasView();
        } catch (Exception e) {
            sceneManager.showAlertInfo("Error", "No se pudo abrir Estadísticas", e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void irAUsuarios() {
        try {
            sceneManager.showUsuariosView();
        } catch (Exception e) {
            sceneManager.showAlertInfo("Error", "No se pudo abrir Administrar usuarios", e.getMessage(), AlertType.ERROR);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FondoCover.aplicar(imagenFondo, rootPane);
        LoginDTOResponse usuario = Sesion.getUsuarioActual();

        if (usuario != null) {
            lblUsuario.setText("Usuario: " + usuario.getNombreCompleto());
            lblRol.setText("Rol: " + usuario.getNombreRol());
        }

        // Ejemplo: solo el rol "Administrador" ve estos botones/cajas.
        boolean esAdmin = Sesion.tieneRolId(ID_ROL_ADMIN);
        cajaAdministrarUsuarios.setVisible(esAdmin);
        cajaAdministrarUsuarios.setManaged(esAdmin);
        btnMenuUsuarios.setVisible(esAdmin);
        btnMenuUsuarios.setManaged(esAdmin);

        lblTotalPacientes.setText(String.valueOf(pacienteRepository.findAll().size()));
        lblTotalUsuarios.setText(String.valueOf(usuarioRepository.findAll().size()));

        List<Notificacion> notificaciones = notificacionService.listar();
        if (!notificaciones.isEmpty()) {
            lblBadgeNotificaciones.setText(String.valueOf(notificaciones.size()));
            lblBadgeNotificaciones.setVisible(true);
            lblBadgeNotificaciones.setManaged(true);
        }
    }

    @FXML
    private void cerrarSesion() {
        try {
            Sesion.cerrar();
            sceneManager.showLoginView();
        } catch (Exception e) {
            sceneManager.showAlertInfo("Error", "No se pudo volver al login",
                    e.getMessage(), AlertType.ERROR);
        }
    }
}