package main.java.com.vyorg.clinica.kinal0.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.com.vyorg.clinica.kinal0.model.Cita;
import main.java.com.vyorg.clinica.kinal0.model.Expediente;
import main.java.com.vyorg.clinica.kinal0.model.Paciente;
import main.java.com.vyorg.clinica.kinal0.repository.CitaRepository;
import main.java.com.vyorg.clinica.kinal0.service.ExpedienteService;
import main.java.com.vyorg.clinica.kinal0.service.PacienteService;
import main.java.com.vyorg.clinica.kinal0.util.ImpresionUtil;
import main.java.com.vyorg.clinica.kinal0.util.SceneManager;
import javafx.scene.layout.VBox;

public class ExpedienteController implements Initializable {

    private final ExpedienteService expedienteService;
    private final PacienteService pacienteService;
    private final CitaRepository citaRepository;
    private final SceneManager sceneManager;

    private Expediente expedienteSeleccionado;

    @FXML private TextField campoBuscar;
    @FXML private TableView<Expediente> tablaExpedientes;
    @FXML private TableColumn<Expediente, Integer> colId;
    @FXML private TableColumn<Expediente, String> colPaciente;
    @FXML private TableColumn<Expediente, String> colFechaApertura;
    @FXML private TableColumn<Expediente, String> colEstado;

    @FXML private ComboBox<Paciente> comboPaciente;
    @FXML private ComboBox<String> comboEstado;
    @FXML private TextField campoObservaciones;

    public ExpedienteController(ExpedienteService expedienteService, PacienteService pacienteService,
                                CitaRepository citaRepository, SceneManager sceneManager) {
        this.expedienteService = expedienteService;
        this.pacienteService = pacienteService;
        this.citaRepository = citaRepository;
        this.sceneManager = sceneManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("idExpediente"));
        colPaciente.setCellValueFactory(new PropertyValueFactory<>("nombrePaciente"));
        colFechaApertura.setCellValueFactory(new PropertyValueFactory<>("fechaApertura"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        comboPaciente.setItems(FXCollections.observableArrayList(pacienteService.listar()));
        comboEstado.setItems(FXCollections.observableArrayList("ACTIVO", "INACTIVO"));
        comboEstado.setValue("ACTIVO");

        tablaExpedientes.getSelectionModel().selectedItemProperty().addListener(
                (obs, anterior, seleccionado) -> cargarEnFormulario(seleccionado));

        cargarTabla(expedienteService.listar());
    }

    @FXML
    private void buscarExpedientes() {
        cargarTabla(expedienteService.buscar(campoBuscar.getText()));
    }

    @FXML
    private void nuevoExpediente() {
        expedienteSeleccionado = null;
        limpiarFormulario();
    }

    @FXML
    private void guardarExpediente() {
        try {
            Expediente expediente = expedienteSeleccionado != null ? expedienteSeleccionado : new Expediente();

            Paciente paciente = comboPaciente.getValue();
            expediente.setIdPaciente(paciente != null ? paciente.getIdPaciente() : 0);
            expediente.setEstado(comboEstado.getValue());
            expediente.setObservacionesGenerales(campoObservaciones.getText());

            expedienteService.guardar(expediente);
            sceneManager.showAlertInfo("Listo", "Expediente guardado", "Se guardó correctamente.", AlertType.INFORMATION);

            nuevoExpediente();
            cargarTabla(expedienteService.listar());
        } catch (RuntimeException e) {
            sceneManager.showAlertInfo("No se pudo guardar", "Revisa los datos", e.getMessage(), AlertType.WARNING);
        }
    }

    @FXML
    private void eliminarExpediente() {
        if (expedienteSeleccionado == null) {
            sceneManager.showAlertInfo("Sin selección", "Elige un expediente",
                    "Selecciona un expediente de la tabla para eliminarlo.", AlertType.WARNING);
            return;
        }
        try {
            expedienteService.eliminar(expedienteSeleccionado.getIdExpediente());
            nuevoExpediente();
            cargarTabla(expedienteService.listar());
        } catch (RuntimeException e) {
            sceneManager.showAlertInfo("No se pudo eliminar", "Ocurrió un error", e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void imprimirExpediente() {
        if (expedienteSeleccionado == null) {
            sceneManager.showAlertInfo("Sin selección", "Elige un expediente",
                    "Selecciona un expediente de la tabla para imprimirlo.", AlertType.WARNING);
            return;
        }
        Paciente paciente = comboPaciente.getItems().stream()
                .filter(p -> p.getIdPaciente() == expedienteSeleccionado.getIdPaciente())
                .findFirst().orElse(null);
        List<Cita> historial = citaRepository.findByPaciente(expedienteSeleccionado.getIdPaciente());
        ImpresionUtil.mostrarVentana("Resumen Integral del Expediente Clínico",
                ImpresionUtil.resumenExpediente(expedienteSeleccionado, paciente, historial));
    }

    @FXML
    private void volver() {
        try {
            sceneManager.showDashboardView();
        } catch (Exception e) {
            sceneManager.showAlertInfo("Error", "No se pudo volver al panel principal", e.getMessage(), AlertType.ERROR);
        }
    }

    private void cargarTabla(List<Expediente> expedientes) {
        ObservableList<Expediente> datos = FXCollections.observableArrayList(expedientes);
        tablaExpedientes.setItems(datos);
    }

    private void cargarEnFormulario(Expediente expediente) {
        expedienteSeleccionado = expediente;
        if (expediente == null) {
            limpiarFormulario();
            return;
        }
        comboPaciente.getItems().stream()
                .filter(p -> p.getIdPaciente() == expediente.getIdPaciente())
                .findFirst()
                .ifPresent(comboPaciente::setValue);
        comboEstado.setValue(expediente.getEstado());
        campoObservaciones.setText(expediente.getObservacionesGenerales());
    }

    private void limpiarFormulario() {
        comboPaciente.setValue(null);
        comboEstado.setValue("ACTIVO");
        campoObservaciones.clear();
        tablaExpedientes.getSelectionModel().clearSelection();
    }
    
    @FXML
private void exportarExpedientePDF() {
    if (expedienteSeleccionado == null) {
        sceneManager.showAlertInfo("Sin selección", "Elige un expediente",
                "Selecciona un expediente de la tabla para exportarlo.", AlertType.WARNING);
        return;
    }
    Paciente paciente = comboPaciente.getItems().stream()
            .filter(p -> p.getIdPaciente() == expedienteSeleccionado.getIdPaciente())
            .findFirst().orElse(null);
    List<Cita> historial = citaRepository.findByPaciente(expedienteSeleccionado.getIdPaciente());
    VBox documento = ImpresionUtil.resumenExpediente(expedienteSeleccionado, paciente, historial);
    ImpresionUtil.exportarPDF(documento, campoBuscar);
}
}