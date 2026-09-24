package main.java.com.vyorg.clinica.kinal0.controller;

import main.java.com.vyorg.clinica.kinal0.util.ValidadorUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import main.java.com.vyorg.clinica.kinal0.model.Paciente;
import main.java.com.vyorg.clinica.kinal0.repository.PacienteRepository;

public class ValidadorController {

    @FXML
    private TextField txtNombrePaciente;
    
    @FXML
    private TextField txtTelefonoPaciente;

    @FXML
    private void handleGuardarPaciente() {
        // 1. Validaciones de campos vacíos o formato en la interfaz
        if (ValidadorUtil.esVacio(txtNombrePaciente, "Nombre del Paciente")) return;
        if (ValidadorUtil.esVacio(txtTelefonoPaciente, "Teléfono")) return;
        if (!ValidadorUtil.esNumero(txtTelefonoPaciente, "Teléfono")) return;

        try {
            // 2. Crear el objeto con los datos de la interfaz
            Paciente paciente = new Paciente();
            paciente.setNombreCompleto(txtNombrePaciente.getText());
            paciente.setTelefono(txtTelefonoPaciente.getText());
            
            // 3. Instanciar el repositorio y llamar al método insertar
            PacienteRepository pacienteRepository = new PacienteRepository();
            pacienteRepository.insertar(paciente);
            
            // 4. Mostrar mensaje de éxito y limpiar el formulario
            ValidadorUtil.mostrarAlerta("Éxito", "Paciente registrado correctamente.", Alert.AlertType.INFORMATION);
            limpiarCampos();

        } catch (Exception e) {
            // Captura cualquier error inesperado en el proceso
            ValidadorUtil.mostrarAlerta("Error Inesperado", "Ocurrió un error: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void limpiarCampos() {
        txtNombrePaciente.clear();
        txtTelefonoPaciente.clear();
        txtNombrePaciente.requestFocus();
    }
}
