package main.java.com.vyorg.clinica.kinal0.util;

import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import main.java.com.vyorg.clinica.kinal0.model.Notificacion;
import main.java.com.vyorg.clinica.kinal0.util.SceneManager;

public class NotificacionPanel {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM HH:mm");

    private NotificacionPanel() {
    }

    public static void mostrar(Node ancla, List<Notificacion> notificaciones, SceneManager sceneManager) {
        Popup popup = new Popup();
        popup.setAutoHide(true);

        VBox contenedor = new VBox(6);
        contenedor.getStylesheets().add(
                NotificacionPanel.class.getResource("/main/resources/ccs/styles.css").toExternalForm());
        contenedor.getStyleClass().add("panel-notificaciones");
        contenedor.setStyle("-fx-background-color: white;"); // respaldo por si la clase CSS no carga a tiempo
        contenedor.setPadding(new Insets(12));
        contenedor.setPrefWidth(300);

        Label titulo = new Label("Notificaciones");
        titulo.getStyleClass().add("panel-notificaciones-titulo");
        contenedor.getChildren().addAll(titulo, new Separator());

        if (notificaciones.isEmpty()) {
            Label vacio = new Label("No tienes notificaciones pendientes.");
            vacio.getStyleClass().add("panel-notificaciones-vacio");
            vacio.setWrapText(true);
            contenedor.getChildren().add(vacio);
        } else {
            for (Notificacion n : notificaciones) {
                VBox item = new VBox(2);
                item.getStyleClass().add("panel-notificaciones-item");
                item.setPadding(new Insets(8));

                Label mensaje = new Label(n.getIcono() + "  " + n.getMensaje());
                mensaje.setWrapText(true);
                mensaje.getStyleClass().add("panel-notificaciones-mensaje");

                Label fecha = new Label(n.getFecha().format(FORMATO));
                fecha.getStyleClass().add("panel-notificaciones-fecha");

                item.getChildren().addAll(mensaje, fecha);
                item.setOnMouseClicked(e -> {
                    popup.hide();
                    navegar(n.getDestino(), sceneManager);
                });

                contenedor.getChildren().add(item);
            }
        }

        popup.getContent().add(contenedor);
        popup.show(ancla,
                ancla.localToScreen(ancla.getBoundsInLocal()).getMaxX() - 300,
                ancla.localToScreen(ancla.getBoundsInLocal()).getMaxY() + 5);
    }

    private static void navegar(String destino, SceneManager sceneManager) {
        try {
            switch (destino) {
                case "CITAS" -> sceneManager.showCitasView();
                case "EXPEDIENTES" -> sceneManager.showExpedientesView();
                case "PACIENTES" -> sceneManager.showPacientesView();
                default -> sceneManager.showDashboardView();
            }
        } catch (Exception e) {
            System.out.println("Error al navegar desde notificación: " + e.getMessage());
        }
    }
}