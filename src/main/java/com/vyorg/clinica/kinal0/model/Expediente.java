package main.java.com.vyorg.clinica.kinal0.model;

import java.time.LocalDateTime;

public class Expediente {

    private int idExpediente;
    private int idPaciente;
    private String nombrePaciente;
    private LocalDateTime fechaApertura;
    private String estado;
    private String observacionesGenerales;

    public Expediente() {
    }

    public Expediente(int idExpediente, int idPaciente, String nombrePaciente, LocalDateTime fechaApertura,
                       String estado, String observacionesGenerales) {
        this.idExpediente = idExpediente;
        this.idPaciente = idPaciente;
        this.nombrePaciente = nombrePaciente;
        this.fechaApertura = fechaApertura;
        this.estado = estado;
        this.observacionesGenerales = observacionesGenerales;
    }

    public int getIdExpediente() { return idExpediente; }
    public void setIdExpediente(int idExpediente) { this.idExpediente = idExpediente; }

    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }

    public String getNombrePaciente() { return nombrePaciente; }
    public void setNombrePaciente(String nombrePaciente) { this.nombrePaciente = nombrePaciente; }

    public LocalDateTime getFechaApertura() { return fechaApertura; }
    public void setFechaApertura(LocalDateTime fechaApertura) { this.fechaApertura = fechaApertura; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getObservacionesGenerales() { return observacionesGenerales; }
    public void setObservacionesGenerales(String observacionesGenerales) { this.observacionesGenerales = observacionesGenerales; }
}