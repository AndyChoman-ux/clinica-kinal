package main.java.com.vyorg.clinica.kinal0.model;

import java.time.LocalDateTime;

public class Notificacion {

    private String icono;
    private String mensaje;
    private LocalDateTime fecha;
    private String destino; // a qué pantalla navega al hacer clic: "CITAS", "EXPEDIENTES", etc.

    public Notificacion(String icono, String mensaje, LocalDateTime fecha, String destino) {
        this.icono = icono;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.destino = destino;
    }

    public String getIcono() { return icono; }
    public String getMensaje() { return mensaje; }
    public LocalDateTime getFecha() { return fecha; }
    public String getDestino() { return destino; }
}