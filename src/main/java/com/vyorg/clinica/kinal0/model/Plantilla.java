package main.java.com.vyorg.clinica.kinal0.model;

/**
 * Modelo que representa una plantilla clínica de diagnósticos y recetas frecuentes.
 */
public class Plantilla {

    private int idPlantilla;
    private String nombrePlantilla;
    private String notasMedicas;
    private String medicamentos;

    // Constructor vacío
    public Plantilla() {
    }

    // Constructor con parámetros
    public Plantilla(int idPlantilla, String nombrePlantilla, String notasMedicas, String medicamentos) {
        this.idPlantilla = idPlantilla;
        this.nombrePlantilla = nombrePlantilla;
        this.notasMedicas = notasMedicas;
        this.medicamentos = medicamentos;
    }

    // Getters y Setters
    public int getIdPlantilla() {
        return idPlantilla;
    }

    public void setIdPlantilla(int idPlantilla) {
        this.idPlantilla = idPlantilla;
    }

    public String getNombrePlantilla() {
        return nombrePlantilla;
    }

    public void setNombrePlantilla(String nombrePlantilla) {
        this.nombrePlantilla = nombrePlantilla;
    }

    public String getNotasMedicas() {
        return notasMedicas;
    }

    public void setNotasMedicas(String notasMedicas) {
        this.notasMedicas = notasMedicas;
    }

    public String getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(String medicamentos) {
        this.medicamentos = medicamentos;
    }

    @Override
    public String toString() {
        return "Plantilla{" +
                "idPlantilla=" + idPlantilla +
                ", nombrePlantilla='" + nombrePlantilla + '\'' +
                '}';
    }
}
