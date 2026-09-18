package main.java.com.vyorg.clinica.kinal0.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.com.vyorg.clinica.kinal0.model.Paciente;
import main.java.com.vyorg.clinica.kinal0.service.PacienteService;
import main.java.com.vyorg.clinica.kinal0.util.SceneManager;

public class PacienteController implements Initializable {

    private final PacienteService pacienteService;
    private final SceneManager sceneManager;

    private Paciente pacienteSeleccionado;

    @FXML private TextField campoBuscar;
    @FXML private TableView<Paciente> tablaPacientes;
    @FXML private TableColumn<Paciente, Integer> colId;
    @FXML private TableColumn<Paciente, String> colNombre;
    @FXML private TableColumn<Paciente, String> colFechaNacimiento;
    @FXML private TableColumn<Paciente, String> colGenero;
    @FXML private TableColumn<Paciente, String> colTelefono;
    @FXML private TableColumn<Paciente, String> colCorreo;
    @FXML private TableColumn<Paciente, String> colTipoSangre;

    @FXML private TextField campoNombre;
    @FXML private DatePicker campoFechaNacimiento;
    @FXML private ComboBox<String> comboGenero;
    @FXML private TextField campoTelefono;
    @FXML private TextField campoCorreo;
    @FXML private TextField campoDireccion;
    @FXML private ComboBox<String> comboTipoSangre;
    
    public PacienteController(PacienteService pacienteService, SceneManager sceneManager) {
        this.pacienteService = pacienteService;
        this.sceneManager = sceneManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("idPaciente"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colFechaNacimiento.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colTipoSangre.setCellValueFactory(new PropertyValueFactory<>("tipoSangre"));

        comboGenero.setItems(FXCollections.observableArrayList("M", "F", "OTRO"));
        comboTipoSangre.setItems(FXCollections.observableArrayList(
        "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"));

        tablaPacientes.getSelectionModel().selectedItemProperty().addListener(
                (obs, anterior, seleccionado) -> cargarEnFormulario(seleccionado));

        cargarTabla(pacienteService.listar());
    }

    @FXML
    private void buscarPacientes() {
        cargarTabla(pacienteService.buscar(campoBuscar.getText()));
    }

    @FXML
    private void nuevoPaciente() {
        pacienteSeleccionado = null;
        limpiarFormulario();
    }

    @FXML
    private void guardarPaciente() {
        try {
            Paciente paciente = pacienteSeleccionado != null ? pacienteSeleccionado : new Paciente();

            paciente.setNombreCompleto(campoNombre.getText());
            paciente.setFechaNacimiento(campoFechaNacimiento.getValue());
            paciente.setGenero(comboGenero.getValue());
            paciente.setTelefono(campoTelefono.getText());
            paciente.setCorreo(campoCorreo.getText());
            paciente.setDireccion(campoDireccion.getText());
            paciente.setTipoSangre(comboTipoSangre.getValue());
            
            pacienteService.guardar(paciente);
            sceneManager.showAlertInfo("Listo", "Paciente guardado", "Los datos se guardaron correctamente.",
                    AlertType.INFORMATION);

            nuevoPaciente();
            cargarTabla(pacienteService.listar());
        } catch (RuntimeException e) {
            sceneManager.showAlertInfo("No se pudo guardar", "Revisa los datos", e.getMessage(), AlertType.WARNING);
        }
    }

    @FXML
    private void eliminarPaciente() {
        if (pacienteSeleccionado == null) {
            sceneManager.showAlertInfo("Sin selección", "Elige un paciente",
                    "Selecciona un paciente de la tabla para eliminarlo.", AlertType.WARNING);
            return;
        }

        try {
            pacienteService.eliminar(pacienteSeleccionado.getIdPaciente());
            nuevoPaciente();
            cargarTabla(pacienteService.listar());
        } catch (RuntimeException e) {
            sceneManager.showAlertInfo("No se pudo eliminar", "Ocurrió un error", e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void volver() {
        try {
            sceneManager.showDashboardView();
        } catch (Exception e) {
            sceneManager.showAlertInfo("Error", "No se pudo volver al panel principal", e.getMessage(),
                    AlertType.ERROR);
        }
    }

    private void cargarTabla(java.util.List<Paciente> pacientes) {
        ObservableList<Paciente> datos = FXCollections.observableArrayList(pacientes);
        tablaPacientes.setItems(datos);
    }

    private void cargarEnFormulario(Paciente paciente) {
        pacienteSeleccionado = paciente;
        if (paciente == null) {
            limpiarFormulario();
            return;
        }
        campoNombre.setText(paciente.getNombreCompleto());
        campoFechaNacimiento.setValue(paciente.getFechaNacimiento());
        comboGenero.setValue(paciente.getGenero());
        campoTelefono.setText(paciente.getTelefono());
        campoCorreo.setText(paciente.getCorreo());
        campoDireccion.setText(paciente.getDireccion());
        comboTipoSangre.setValue(paciente.getTipoSangre());
    }

    private void limpiarFormulario() {
        campoNombre.clear();
        campoFechaNacimiento.setValue(null);
        comboGenero.setValue(null);
        campoTelefono.clear();
        campoCorreo.clear();
        campoDireccion.clear();
        comboTipoSangre.setValue(null);
        tablaPacientes.getSelectionModel().clearSelection();
    }
}