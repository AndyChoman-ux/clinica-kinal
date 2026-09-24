package main.java.com.vyorg.clinica.kinal0.controller;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import main.java.com.vyorg.clinica.kinal0.repository.CitaRepository;
import main.java.com.vyorg.clinica.kinal0.repository.ExpedienteRepository;
import main.java.com.vyorg.clinica.kinal0.repository.PacienteRepository;
import main.java.com.vyorg.clinica.kinal0.repository.UsuarioRepository;
import main.java.com.vyorg.clinica.kinal0.service.EstadisticasService;
import main.java.com.vyorg.clinica.kinal0.util.SceneManager;

public class EstadisticasController implements Initializable {

    private static final String OPCION_GENERO = "Pacientes por género";
    private static final String OPCION_TIPO_SANGRE = "Pacientes por tipo de sangre";
    private static final String OPCION_CITAS_ESTADO = "Citas por estado";
    private static final String OPCION_EXPEDIENTES_ESTADO = "Expedientes por estado";

    private final SceneManager sceneManager;
    private final PacienteRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final CitaRepository citaRepository;
    private final ExpedienteRepository expedienteRepository;
    private final EstadisticasService estadisticasService;

    private Map<String, Integer> datosActuales = new LinkedHashMap<>();


    @FXML private Label lblTotalPacientes;
    @FXML private Label lblTotalUsuarios;
    @FXML private Label lblCitasHoy;
    @FXML private Label lblExpedientesSemana;

    @FXML private TextArea txtReporteDiagnosticos;
    @FXML private TextArea txtReporteHorarios;
    @FXML private TextArea txtReporteAusentismo;

    // Gráficas nuevas
    @FXML private ComboBox<String> comboDataset;
    @FXML private BarChart<String, Number> barChart;
    @FXML private PieChart pieChart;

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
       
        if (lblTotalPacientes != null) lblTotalPacientes.setText(String.valueOf(pacienteRepository.findAll().size()));
        if (lblTotalUsuarios != null) lblTotalUsuarios.setText(String.valueOf(usuarioRepository.findAll().size()));
        if (lblCitasHoy != null) lblCitasHoy.setText(String.valueOf(citaRepository.contarCitasHoy()));
        if (lblExpedientesSemana != null) lblExpedientesSemana.setText(String.valueOf(expedienteRepository.contarNuevosEstaSemana()));

        cargarReportesJira();

      
        configurarGraficas();
    }

    private void configurarGraficas() {
        if (comboDataset == null || barChart == null || pieChart == null) {
            return; 
        }

        comboDataset.setItems(FXCollections.observableArrayList(
                OPCION_GENERO, OPCION_TIPO_SANGRE, OPCION_CITAS_ESTADO, OPCION_EXPEDIENTES_ESTADO));

        comboDataset.setOnAction(e -> cargarDatosSeleccionados());
        comboDataset.getSelectionModel().selectFirst();

        pieChart.setVisible(false);
        pieChart.setManaged(false);
        barChart.setVisible(true);
        barChart.setManaged(true);

        cargarDatosSeleccionados();
    }

    private void cargarDatosSeleccionados() {
        String seleccion = comboDataset.getValue();
        if (seleccion == null) {
            return;
        }

        switch (seleccion) {
            case OPCION_GENERO:
                datosActuales = estadisticasService.getPacientesPorGenero();
                break;
            case OPCION_TIPO_SANGRE:
                datosActuales = estadisticasService.getPacientesPorTipoSangre();
                break;
            case OPCION_CITAS_ESTADO:
                datosActuales = estadisticasService.getCitasPorEstado();
                break;
            case OPCION_EXPEDIENTES_ESTADO:
                datosActuales = estadisticasService.getExpedientesPorEstado();
                break;
            default:
                datosActuales = new LinkedHashMap<>();
        }

        redibujarGraficaActiva();
    }

    private void redibujarGraficaActiva() {
        if (barChart.isVisible()) {
            dibujarBarChart();
        } else {
            dibujarPieChart();
        }
    }

    @FXML
    private void mostrarGraficaBarras() {
        barChart.setVisible(true);
        barChart.setManaged(true);
        pieChart.setVisible(false);
        pieChart.setManaged(false);
        dibujarBarChart();
    }

    @FXML
    private void mostrarGraficaPastel() {
        pieChart.setVisible(true);
        pieChart.setManaged(true);
        barChart.setVisible(false);
        barChart.setManaged(false);
        dibujarPieChart();
    }

    private void dibujarBarChart() {
        barChart.getData().clear();

        if (datosActuales.isEmpty()) {
            return;
        }

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName(comboDataset.getValue());

        for (Map.Entry<String, Integer> entry : datosActuales.entrySet()) {
            String etiqueta = entry.getKey() != null ? entry.getKey() : "Sin dato";
            serie.getData().add(new XYChart.Data<>(etiqueta, entry.getValue()));
        }

        barChart.getData().add(serie);
        barChart.setTitle(comboDataset.getValue());
    }

    private void dibujarPieChart() {
        pieChart.getData().clear();

        if (datosActuales.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Integer> entry : datosActuales.entrySet()) {
            String etiqueta = entry.getKey() != null ? entry.getKey() : "Sin dato";
            pieChart.getData().add(new PieChart.Data(etiqueta + " (" + entry.getValue() + ")", entry.getValue()));
        }

        pieChart.setTitle(comboDataset.getValue());
    }

    private void cargarReportesJira() {
        try {
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