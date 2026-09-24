package main.java.com.vyorg.clinica.kinal0.controller;

import java.net.URL;
import java.time.LocalTime;
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
import main.java.com.vyorg.clinica.kinal0.model.Paciente;
import main.java.com.vyorg.clinica.kinal0.service.CitaService;
import main.java.com.vyorg.clinica.kinal0.service.PacienteService;
import main.java.com.vyorg.clinica.kinal0.util.SceneManager;
import main.java.com.vyorg.clinica.kinal0.util.ImpresionUtil;
import javafx.scene.layout.VBox;

public class CitaController implements Initializable {

    private final CitaService citaService;
    private final PacienteService pacienteService;
    private final SceneManager sceneManager;

    private Cita citaSeleccionada;

    @FXML private TextField campoBuscar;
    @FXML private TableView<Cita> tablaCitas;
    @FXML private TableColumn<Cita, Integer> colId;
    @FXML private TableColumn<Cita, String> colPaciente;
    @FXML private TableColumn<Cita, String> colFecha;
    @FXML private TableColumn<Cita, String> colHora;
    @FXML private TableColumn<Cita, String> colMotivo;
    @FXML private TableColumn<Cita, String> colEstado;

    @FXML private ComboBox<Paciente> comboPaciente;
    @FXML private DatePicker campoFecha;
    @FXML private TextField campoHora;
    @FXML private TextField campoMotivo;
    @FXML private ComboBox<String> comboEstado;

    public CitaController(CitaService citaService, PacienteService pacienteService, SceneManager sceneManager) {
        this.citaService = citaService;
        this.pacienteService = pacienteService;
        this.sceneManager = sceneManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("idCita"));
        colPaciente.setCellValueFactory(new PropertyValueFactory<>("nombrePaciente"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colMotivo.setCellValueFactory(new PropertyValueFactory<>("motivo"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        comboPaciente.setItems(FXCollections.observableArrayList(pacienteService.listar()));
        comboEstado.setItems(FXCollections.observableArrayList("Programada", "Atendida", "Cancelada"));
        comboEstado.setValue("Programada");

        tablaCitas.getSelectionModel().selectedItemProperty().addListener(
                (obs, anterior, seleccionada) -> cargarEnFormulario(seleccionada));

        cargarTabla(citaService.listar());
    }

    @FXML
    private void buscarCitas() {
        cargarTabla(citaService.buscar(campoBuscar.getText()));
    }

    @FXML
    private void nuevaCita() {
        citaSeleccionada = null;
        limpiarFormulario();
    }

    @FXML
    private void guardarCita() {
        try {
            Cita cita = citaSeleccionada != null ? citaSeleccionada : new Cita();

            Paciente paciente = comboPaciente.getValue();
            cita.setIdPaciente(paciente != null ? paciente.getIdPaciente() : 0);
            cita.setFecha(campoFecha.getValue());
            cita.setHora(parsearHora(campoHora.getText()));
            cita.setMotivo(campoMotivo.getText());
            cita.setEstado(comboEstado.getValue());

            citaService.guardar(cita);
            sceneManager.showAlertInfo("Listo", "Cita guardada", "La cita se guardó correctamente.", AlertType.INFORMATION);

            nuevaCita();
            cargarTabla(citaService.listar());
        } catch (RuntimeException e) {
            sceneManager.showAlertInfo("No se pudo guardar", "Revisa los datos", e.getMessage(), AlertType.WARNING);
        }
    }

    @FXML
    private void eliminarCita() {
        if (citaSeleccionada == null) {
            sceneManager.showAlertInfo("Sin selección", "Elige una cita",
                    "Selecciona una cita de la tabla para eliminarla.", AlertType.WARNING);
            return;
        }
        try {
            citaService.eliminar(citaSeleccionada.getIdCita());
            nuevaCita();
            cargarTabla(citaService.listar());
        } catch (RuntimeException e) {
            sceneManager.showAlertInfo("No se pudo eliminar", "Ocurrió un error", e.getMessage(), AlertType.ERROR);
        }
    }
    
    @FXML
private void imprimirCita() {
    if (citaSeleccionada == null) {
        sceneManager.showAlertInfo("Sin selección", "Elige una cita",
                "Selecciona una cita de la tabla para imprimirla.", AlertType.WARNING);
        return;
    }
    Paciente paciente = comboPaciente.getItems().stream()
            .filter(p -> p.getIdPaciente() == citaSeleccionada.getIdPaciente())
            .findFirst().orElse(null);
    ImpresionUtil.mostrarVentana("Formato de Consulta Médica",
            ImpresionUtil.formatoConsulta(citaSeleccionada, paciente));
}

    @FXML
    private void volver() {
        try {
            sceneManager.showDashboardView();
        } catch (Exception e) {
            sceneManager.showAlertInfo("Error", "No se pudo volver al panel principal", e.getMessage(), AlertType.ERROR);
        }
    }

    private LocalTime parsearHora(String texto) {
        try {
            return LocalTime.parse(texto.trim());
        } catch (Exception e) {
            throw new RuntimeException("La hora debe tener formato HH:mm, por ejemplo 14:30");
        }
    }

    private void cargarTabla(List<Cita> citas) {
        ObservableList<Cita> datos = FXCollections.observableArrayList(citas);
        tablaCitas.setItems(datos);
    }

    private void cargarEnFormulario(Cita cita) {
        citaSeleccionada = cita;
        if (cita == null) {
            limpiarFormulario();
            return;
        }
        comboPaciente.getItems().stream()
                .filter(p -> p.getIdPaciente() == cita.getIdPaciente())
                .findFirst()
                .ifPresent(comboPaciente::setValue);
        campoFecha.setValue(cita.getFecha());
        campoHora.setText(cita.getHora() != null ? cita.getHora().toString() : "");
        campoMotivo.setText(cita.getMotivo());
        comboEstado.setValue(cita.getEstado());
    }

    private void limpiarFormulario() {
        comboPaciente.setValue(null);
        campoFecha.setValue(null);
        campoHora.clear();
        campoMotivo.clear();
        comboEstado.setValue("Programada");
        tablaCitas.getSelectionModel().clearSelection();
    }
    
    @FXML
private void exportarCitaPDF() {
    if (citaSeleccionada == null) {
        sceneManager.showAlertInfo("Sin selección", "Elige una cita",
                "Selecciona una cita de la tabla para exportarla.", AlertType.WARNING);
        return;
    }
    Paciente paciente = comboPaciente.getItems().stream()
            .filter(p -> p.getIdPaciente() == citaSeleccionada.getIdPaciente())
            .findFirst().orElse(null);
    VBox documento = ImpresionUtil.formatoConsulta(citaSeleccionada, paciente);
    ImpresionUtil.exportarPDF(documento, campoBuscar);
}
}