package main.java.com.vyorg.clinica.kinal0.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import main.java.com.vyorg.clinica.kinal0.repository.CitaRepository;
import main.java.com.vyorg.clinica.kinal0.repository.ExpedienteRepository;
import main.java.com.vyorg.clinica.kinal0.repository.PacienteRepository;
import main.java.com.vyorg.clinica.kinal0.repository.UsuarioRepository;
import main.java.com.vyorg.clinica.kinal0.util.SceneManager;

public class EstadisticasController implements Initializable {

    private final SceneManager sceneManager;
    private final PacienteRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final CitaRepository citaRepository;
    private final ExpedienteRepository expedienteRepository;

    @FXML private Label lblTotalPacientes;
    @FXML private Label lblTotalUsuarios;
    @FXML private Label lblCitasHoy;
    @FXML private Label lblExpedientesSemana;

    public EstadisticasController(SceneManager sceneManager, PacienteRepository pacienteRepository,
                                   UsuarioRepository usuarioRepository, CitaRepository citaRepository,
                                   ExpedienteRepository expedienteRepository) {
        this.sceneManager = sceneManager;
        this.pacienteRepository = pacienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.citaRepository = citaRepository;
        this.expedienteRepository = expedienteRepository;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblTotalPacientes.setText(String.valueOf(pacienteRepository.findAll().size()));
        lblTotalUsuarios.setText(String.valueOf(usuarioRepository.findAll().size()));
        lblCitasHoy.setText(String.valueOf(citaRepository.contarCitasHoy()));
        lblExpedientesSemana.setText(String.valueOf(expedienteRepository.contarNuevosEstaSemana()));
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