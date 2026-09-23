package main.java.com.vyorg.clinica.kinal0.dto.request;

public class RecuperarDTORequest {

    private String usuarioOCorreo;
    private String codigo;
    private String nuevaPassword;
    private String confirmarPassword;

    public RecuperarDTORequest(String usuarioOCorreo, String codigo, String nuevaPassword, String confirmarPassword) {
        this.usuarioOCorreo = usuarioOCorreo;
        this.codigo = codigo;
        this.nuevaPassword = nuevaPassword;
        this.confirmarPassword = confirmarPassword;
    }

    public String getUsuarioOCorreo() {
        return usuarioOCorreo;
    }

    public void setUsuarioOCorreo(String usuarioOCorreo) {
        this.usuarioOCorreo = usuarioOCorreo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNuevaPassword() {
        return nuevaPassword;
    }

    public void setNuevaPassword(String nuevaPassword) {
        this.nuevaPassword = nuevaPassword;
    }

    public String getConfirmarPassword() {
        return confirmarPassword;
    }

    public void setConfirmarPassword(String confirmarPassword) {
        this.confirmarPassword = confirmarPassword;
    }
}