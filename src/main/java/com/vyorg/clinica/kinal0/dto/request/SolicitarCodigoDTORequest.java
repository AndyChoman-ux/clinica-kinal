package main.java.com.vyorg.clinica.kinal0.dto.request;

public class SolicitarCodigoDTORequest {

    private String usuarioOCorreo;

    public SolicitarCodigoDTORequest(String usuarioOCorreo) {
        this.usuarioOCorreo = usuarioOCorreo;
    }

    public String getUsuarioOCorreo() {
        return usuarioOCorreo;
    }

    public void setUsuarioOCorreo(String usuarioOCorreo) {
        this.usuarioOCorreo = usuarioOCorreo;
    }
}