package main.java.com.vyorg.clinica.kinal0.dto.response;

public class LoginDTOResponse {
   private int idUsuario;
private String nombreCompleto;
private String nombreUsuario;
private String passwordHash;
private int idRol;
private String nombreRol;
private boolean activo;

public LoginDTOResponse(int idUsuario, String nombreCompleto, String nombreUsuario,
                         String passwordHash, int idRol, String nombreRol, boolean activo) {
    this.idUsuario = idUsuario;
    this.nombreCompleto = nombreCompleto;
    this.nombreUsuario = nombreUsuario;
    this.passwordHash = passwordHash;
    this.idRol = idRol;
    this.nombreRol = nombreRol;
    this.activo = activo;
}

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public int getIdRol() {
        return idRol;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    
    
}
