package main.java.com.vyorg.clinica.kinal0.controller;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import main.java.com.vyorg.clinica.kinal0.repository.CitaRepository;
import main.java.com.vyorg.clinica.kinal0.repository.ExpedienteRepository;
import main.java.com.vyorg.clinica.kinal0.repository.PacienteRepository;
import main.java.com.vyorg.clinica.kinal0.repository.UsuarioRepository;
import main.java.com.vyorg.clinica.kinal0.service.EstadisticasService;
import main.java.com.vyorg.clinica.kinal0.util.SceneManager;

public class EstadisticasController implements Initializable {

    private final SceneManager sceneManager;
    private final PacienteRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final CitaRepository citaRepository;
    private final ExpedienteRepository expedienteRepository;
    private final EstadisticasService estadisticasService;

    // Tus etiquetas originales
    @FXML private Label lblTotalPacientes;
    @FXML private Label lblTotalUsuarios;
    @FXML private Label lblCitasHoy;
    @FXML private Label lblExpedientesSemana;

    // Nuevos campos para mostrar los reportes de Jira (pueden ser TextArea o Labels según tu FXML)
    @FXML private TextArea txtReporteDiagnosticos;
    @FXML private TextArea txtReporteHorarios;
    @FXML private TextArea txtReporteAusentismo;

    public EstadisticasController(SceneManager sceneManager, PacienteRepository pacienteRepository,
                                   UsuarioRepository usuarioRepository, CitaRepository citaRepository,
                                   ExpedienteRepository expedienteRepository) {
        this.sceneManager = sceneManager;
        this.pacienteRepository = pacienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.citaRepository = citaRepository;
        this.expedienteRepository = expedienteRepository;
        this.estadisticasService = new EstadisticasService();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Cargar tus métricas originales
        if (lblTotalPacientes != null) lblTotalPacientes.setText(String.valueOf(pacienteRepository.findAll().size()));
        if (lblTotalUsuarios != null) lblTotalUsuarios.setText(String.valueOf(usuarioRepository.findAll().size()));
        if (lblCitasHoy != null) lblCitasHoy.setText(String.valueOf(citaRepository.contarCitasHoy()));
        if (lblExpedientesSemana != null) lblExpedientesSemana.setText(String.valueOf(expedienteRepository.contarNuevosEstaSemana()));

        // Cargar los nuevos reportes solicitados en Jira
        cargarReportesJira();
    }

    private void cargarReportesJira() {
        try {
            // 1. Diagnósticos frecuentes del mes
            if (txtReporteDiagnosticos != null) {
                Map<String, Integer> diagnosticos = estadisticasService.getDiagnosticosFrecuentes();
                StringBuilder sb = new StringBuilder("--- DIAGNÓSTICOS FRECUENTES ---\n");
                if (diagnosticos.isEmpty()) {
                    sb.append("Sin registros este mes.\n");
                } else {
                    diagnosticos.forEach((k, v) -> sb.append("• ").append(k).append(": ").append(v).append(" casos\n"));
                }
                txtReporteDiagnosticos.setText(sb.toString());
            }

            // 2. Horarios de mayor afluencia
            if (txtReporteHorarios != null) {
                Map<String, Integer> horarios = estadisticasService.getHorariosAfluencia();
                StringBuilder sb = new StringBuilder("--- HORARIOS DE AFLUENCIA ---\n");
                if (horarios.isEmpty()) {
                    sb.append("Sin registros disponibles.\n");
                } else {
                    horarios.forEach((k, v) -> sb.append("• ").append(k).append(": ").append(v).append(" citas\n"));
                }
                txtReporteHorarios.setText(sb.toString());
            }

            // 3. Tasa de ausentismo / cancelaciones con porcentajes
            if (txtReporteAusentismo != null) {
                Map<String, String> ausentismo = estadisticasService.getTasaAusentismoConPorcentajes();
                StringBuilder sb = new StringBuilder("--- TASA DE AUSENTISMO / ESTADOS ---\n");
                if (ausentismo.isEmpty()) {
                    sb.append("Sin registros disponibles.\n");
                } else {
                    ausentismo.forEach((k, v) -> sb.append("• ").append(k).append(": ").append(v).append("\n"));
                }
                txtReporteAusentismo.setText(sb.toString());
            }

        } catch (Exception e) {
            System.err.println("Error al cargar reportes estadísticos: " + e.getMessage());
        }
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