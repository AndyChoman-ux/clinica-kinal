package main.java.com.vyorg.clinica.kinal0.util;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class ValidadorUtil {

    // Valida si un campo de texto está vacío
    public static boolean esVacio(TextField textField, String nombreCampo) {
        if (textField.getText() == null || textField.getText().trim().isEmpty()) {
            mostrarAlerta("Error de validación", "El campo '" + nombreCampo + "' no puede estar vacío.", Alert.AlertType.WARNING);
            textField.requestFocus();
            return true;
        }
        return false;
    }

    // Valida que un campo contenga solo números (ej. teléfono o edad)
    public static boolean esNumero(TextField textField, String nombreCampo) {
        try {
            Double.parseDouble(textField.getText().trim());
            return false;
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de validación", "El campo '" + nombreCampo + "' debe ser un valor numérico válido.", Alert.AlertType.WARNING);
            textField.requestFocus();
            return true;
        }
    }

    // Método auxiliar para mostrar alertas gráficas en JavaFX
    public static void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
