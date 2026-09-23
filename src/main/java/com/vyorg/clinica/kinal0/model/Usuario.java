package main.java.com.vyorg.clinica.kinal0.model;

public class Usuario {

    private int idUsuario;
    private String nombreCompleto;
    private String correo;
    private String nombreUsuario;
    private int idRol;
    private String nombreRol;
    private boolean activo;

    public Usuario() {
    }

    public Usuario(int idUsuario, String nombreCompleto, String correo, String nombreUsuario,
                    int idRol, String nombreRol, boolean activo) {
        this.idUsuario = idUsuario;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.nombreUsuario = nombreUsuario;
        this.idRol = idRol;
        this.nombreRol = nombreRol;
        this.activo = activo;
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public int getIdRol() { return idRol; }
    public void setIdRol(int idRol) { this.idRol = idRol; }

    public String getNombreRol() { return nombreRol; }
    public void setNombreRol(String nombreRol) { this.nombreRol = nombreRol; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() { return nombreCompleto; }
}