package main.java.com.vyorg.clinica.kinal0.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Cita {

    private int idCita;
    private int idPaciente;
    private String nombrePaciente;
    private LocalDate fecha;
    private LocalTime hora;
    private String motivo;
    private String estado;

    public Cita() {
    }

    public Cita(int idCita, int idPaciente, String nombrePaciente, LocalDate fecha,
                LocalTime hora, String motivo, String estado) {
        this.idCita = idCita;
        this.idPaciente = idPaciente;
        this.nombrePaciente = nombrePaciente;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.estado = estado;
    }

    public int getIdCita() { return idCita; }
    public void setIdCita(int idCita) { this.idCita = idCita; }

    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }

    public String getNombrePaciente() { return nombrePaciente; }
    public void setNombrePaciente(String nombrePaciente) { this.nombrePaciente = nombrePaciente; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}