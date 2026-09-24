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
        if (ValidadorUtil.esVacio(txtNombrePaciente, "Nombre del Paciente")) return;
        if (ValidadorUtil.esVacio(txtTelefonoPaciente, "Teléfono")) return;
        if (!ValidadorUtil.esNumero(txtTelefonoPaciente, "Teléfono")) return;

        try {
            Paciente paciente = new Paciente();
            paciente.setNombreCompleto(txtNombrePaciente.getText());
            paciente.setTelefono(txtTelefonoPaciente.getText());
            
            PacienteRepository pacienteRepository = new PacienteRepository();
            pacienteRepository.insertar(paciente);
            
            ValidadorUtil.mostrarAlerta("Éxito", "Paciente registrado correctamente.", Alert.AlertType.INFORMATION);
            limpiarCampos();

        } catch (Exception e) {
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
