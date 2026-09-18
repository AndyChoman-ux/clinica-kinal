package main.java.com.vyorg.clinica.kinal0.util;

import main.java.com.vyorg.clinica.kinal0.dto.response.LoginDTOResponse;

public class Sesion {

    private static LoginDTOResponse usuarioActual;

    private Sesion() {
    }

    public static void iniciar(LoginDTOResponse usuario) {
        usuarioActual = usuario;
    }

    public static LoginDTOResponse getUsuarioActual() {
        return usuarioActual;
    }

    public static boolean tieneRol(String nombreRol) {
        return usuarioActual != null
                && usuarioActual.getNombreRol() != null
                && usuarioActual.getNombreRol().equalsIgnoreCase(nombreRol);
    }

    public static void cerrar() {
        usuarioActual = null;
    }
    
    public static boolean tieneRolId(int idRol) {
    return usuarioActual != null && usuarioActual.getIdRol() == idRol;
}
}