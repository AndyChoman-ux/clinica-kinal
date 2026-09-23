package main.java.com.vyorg.clinica.kinal0.controller;

import main.java.com.vyorg.clinica.kinal0.util.ValidadorUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.sql.SQLException;
import main.java.com.vyorg.clinica.kinal0.model.Paciente;

public class ValidadorController {

    @FXML
    private TextField txtNombrePaciente;
    
    @FXML
    private TextField txtTelefonoPaciente;

    @FXML
    private void handleGuardarPaciente() {
        // 1. Validaciones de campos vacíos o formato
        if (ValidadorUtil.esVacio(txtNombrePaciente, "Nombre del Paciente")) return;
        if (ValidadorUtil.esVacio(txtTelefonoPaciente, "Teléfono")) return;
        if (ValidadorUtil.esNumero(txtTelefonoPaciente, "Teléfono")) return;

        // 2. Intentar la operación con la Base de Datos (Manejo de Errores)
       try {
    Paciente.guardar(paciente);
    ValidadorUtil.mostrarAlerta("Éxito", "Paciente registrado correctamente.", Alert.AlertType.INFORMATION);
    limpiarCampos();
} catch (SQLException e) {
    ValidadorUtil.mostrarAlerta("Error de Base de Datos", "No se pudo guardar el registro en MySQL. Código: " + e.getErrorCode(), Alert.AlertType.ERROR);
    e.printStackTrace();
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