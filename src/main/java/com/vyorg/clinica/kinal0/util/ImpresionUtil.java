package main.java.com.vyorg.clinica.kinal0.util;

import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.geometry.Insets;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.java.com.vyorg.clinica.kinal0.model.Cita;
import main.java.com.vyorg.clinica.kinal0.model.Expediente;
import main.java.com.vyorg.clinica.kinal0.model.Paciente;

public class ImpresionUtil {

    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private ImpresionUtil() {
    }

    public static void mostrarVentana(String titulo, Node documento) {
        VBox contenedor = new VBox(documento);
        contenedor.setStyle("-fx-background-color: white;");
        contenedor.setPadding(new Insets(20));

        ScrollPane scroll = new ScrollPane(contenedor);
        scroll.setFitToWidth(true);

        Button btnImprimir = new Button("🖨 Imprimir");
        btnImprimir.setStyle("-fx-background-color: #3f9e88; -fx-text-fill: white; -fx-font-weight: bold; "
                + "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 20 8 20;");
        btnImprimir.setOnAction(e -> imprimir(documento, btnImprimir));

        HBox barra = new HBox(btnImprimir);
        barra.setPadding(new Insets(10));
        barra.setStyle("-fx-background-color: #e2ece9;");

        BorderPane raiz = new BorderPane();
        raiz.setTop(barra);
        raiz.setCenter(scroll);

        Stage ventana = new Stage();
        ventana.initModality(Modality.APPLICATION_MODAL);
        ventana.setTitle(titulo);
        ventana.setScene(new Scene(raiz, 650, 700));
        ventana.show();
    }

    private static void imprimir(Node documento, Node dueño) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job == null) {
            return;
        }
        boolean confirmado = job.showPrintDialog(dueño.getScene().getWindow());
        if (confirmado) {
            boolean ok = job.printPage(documento);
            if (ok) {
                job.endJob();
            }
        }
    }

    public static void exportarPDF(Node documento, Node dueño) {
        Printer impresoraPDF = Printer.getAllPrinters().stream()
                .filter(p -> p.getName().toLowerCase().contains("pdf"))
                .findFirst()
                .orElse(null);
        if (impresoraPDF == null) {
            imprimir(documento, dueño);
            return;
        }
        PrinterJob job = PrinterJob.createPrinterJob(impresoraPDF);
        if (job == null) {
            return;
        }
        boolean ok = job.printPage(documento);
        if (ok) {
            job.endJob(); 
        }
    }

    private static VBox encabezado(String tituloDocumento, String numero) {
        Label clinica = new Label("Clínica Kinal");
        clinica.setFont(Font.font("System", FontWeight.BOLD, 22));
        clinica.setStyle("-fx-text-fill: #16332b;");

        Label subtitulo = new Label(tituloDocumento);
        subtitulo.setFont(Font.font("System", FontWeight.NORMAL, 13));
        subtitulo.setStyle("-fx-text-fill: #4b5b57;");

        Label numeroLabel = new Label(numero);
        numeroLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        numeroLabel.setStyle("-fx-text-fill: #2d5449;");

        VBox caja = new VBox(4, clinica, subtitulo, numeroLabel);
        caja.setPadding(new Insets(0, 0, 10, 0));
        return caja;
    }

    private static void agregarFila(GridPane grid, int fila, String etiqueta, String valor) {
        Label lbl = new Label(etiqueta);
        lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #2d5449; -fx-font-size: 12px;");
        Label val = new Label(valor == null || valor.isBlank() ? "-" : valor);
        val.setStyle("-fx-text-fill: #16332b; -fx-font-size: 12px;");
        val.setWrapText(true);
        grid.add(lbl, 0, fila);
        grid.add(val, 1, fila);
    }

    public static VBox formatoConsulta(Cita cita, Paciente paciente) {
        VBox raiz = new VBox(10);
        raiz.setPadding(new Insets(20));
        raiz.setPrefWidth(560);

        raiz.getChildren().add(encabezado("Formato de Consulta Médica", "No. Cita: #CITA-" + cita.getIdCita()));
        raiz.getChildren().add(new Separator());

        GridPane datos = new GridPane();
        datos.setHgap(20);
        datos.setVgap(6);
        agregarFila(datos, 0, "Paciente:", cita.getNombrePaciente());
        agregarFila(datos, 1, "Fecha:", cita.getFecha() != null ? cita.getFecha().format(FECHA) : "-");
        agregarFila(datos, 2, "Hora:", cita.getHora() != null ? cita.getHora().toString() : "-");
        agregarFila(datos, 3, "Estado:", cita.getEstado());
        if (paciente != null) {
            agregarFila(datos, 4, "Teléfono:", paciente.getTelefono());
            agregarFila(datos, 5, "Correo:", paciente.getCorreo());
            agregarFila(datos, 6, "Tipo de sangre:", paciente.getTipoSangre());
        }
        raiz.getChildren().add(datos);

        Label motivoTitulo = new Label("Motivo de la consulta:");
        motivoTitulo.setStyle("-fx-font-weight: bold; -fx-text-fill: #2d5449; -fx-font-size: 12px;");
        Label motivoTexto = new Label(cita.getMotivo());
        motivoTexto.setWrapText(true);
        motivoTexto.setStyle("-fx-text-fill: #16332b; -fx-font-size: 12px;");

        raiz.getChildren().addAll(new Separator(), motivoTitulo, motivoTexto, new Separator());

        Label pie = new Label("Clínica Kinal — Sistema de Expedientes Clínicos");
        pie.setStyle("-fx-text-fill: #6b7a76; -fx-font-size: 10px;");
        raiz.getChildren().add(pie);

        return raiz;
    }

    public static VBox resumenExpediente(Expediente expediente, Paciente paciente, List<Cita> historial) {
        VBox raiz = new VBox(10);
        raiz.setPadding(new Insets(20));
        raiz.setPrefWidth(560);

        raiz.getChildren().add(encabezado("Resumen Integral del Expediente Clínico",
                "Expediente No.: EXP-" + expediente.getIdExpediente()));
        raiz.getChildren().add(new Separator());

        Label seccionDatos = new Label("1. Datos generales del paciente");
        seccionDatos.setStyle("-fx-font-weight: bold; -fx-text-fill: #16332b; -fx-font-size: 13px;");
        raiz.getChildren().add(seccionDatos);

        GridPane datos = new GridPane();
        datos.setHgap(20);
        datos.setVgap(6);
        if (paciente != null) {
            agregarFila(datos, 0, "Nombre completo:", paciente.getNombreCompleto());
            agregarFila(datos, 1, "Fecha de nacimiento:",
                    paciente.getFechaNacimiento() != null ? paciente.getFechaNacimiento().format(FECHA) : "-");
            agregarFila(datos, 2, "Género:", paciente.getGenero());
            agregarFila(datos, 3, "Teléfono:", paciente.getTelefono());
            agregarFila(datos, 4, "Correo:", paciente.getCorreo());
            agregarFila(datos, 5, "Dirección:", paciente.getDireccion());
            agregarFila(datos, 6, "Tipo de sangre:", paciente.getTipoSangre());
        }
        raiz.getChildren().add(datos);
        raiz.getChildren().add(new Separator());

        Label seccionExpediente = new Label("2. Datos del expediente");
        seccionExpediente.setStyle("-fx-font-weight: bold; -fx-text-fill: #16332b; -fx-font-size: 13px;");
        raiz.getChildren().add(seccionExpediente);

        GridPane datosExp = new GridPane();
        datosExp.setHgap(20);
        datosExp.setVgap(6);
        agregarFila(datosExp, 0, "Fecha de apertura:",
                expediente.getFechaApertura() != null ? expediente.getFechaApertura().format(FECHA_HORA) : "-");
        agregarFila(datosExp, 1, "Estado:", expediente.getEstado());
        agregarFila(datosExp, 2, "Observaciones generales:", expediente.getObservacionesGenerales());
        raiz.getChildren().add(datosExp);
        raiz.getChildren().add(new Separator());

        Label seccionHistorial = new Label("3. Historial de consultas");
        seccionHistorial.setStyle("-fx-font-weight: bold; -fx-text-fill: #16332b; -fx-font-size: 13px;");
        raiz.getChildren().add(seccionHistorial);

        if (historial == null || historial.isEmpty()) {
            Label sinDatos = new Label("Este paciente no tiene consultas registradas.");
            sinDatos.setStyle("-fx-text-fill: #6b7a76; -fx-font-size: 12px;");
            raiz.getChildren().add(sinDatos);
        } else {
            for (Cita c : historial) {
                Label linea = new Label(
                        (c.getFecha() != null ? c.getFecha().format(FECHA) : "-") + "  |  " +
                        c.getMotivo() + "  —  " + c.getEstado());
                linea.setStyle("-fx-text-fill: #16332b; -fx-font-size: 12px;");
                linea.setWrapText(true);
                raiz.getChildren().add(linea);
            }
        }

        raiz.getChildren().add(new Separator());
        Label pie = new Label("Clínica Kinal — Archivo de Expedientes Clínicos — Documento Confidencial");
        pie.setStyle("-fx-text-fill: #6b7a76; -fx-font-size: 10px;");
        raiz.getChildren().add(pie);

        return raiz;
    }
}