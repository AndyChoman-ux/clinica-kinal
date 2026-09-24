package main.java.com.vyorg.clinica.kinal0.dto.request;

public class RegistroDTORequest {

    private String correo;
    private String nombreUsuario;
    private String password;

    public RegistroDTORequest(String correo, String nombreUsuario, String password) {
        this.correo = correo;
        this.nombreUsuario = nombreUsuario;
        this.password = password;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}