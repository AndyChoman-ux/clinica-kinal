package main.java.com.vyorg.clinica.kinal0.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import main.java.com.vyorg.clinica.kinal0.model.Cita;
import main.java.com.vyorg.clinica.kinal0.model.Notificacion;
import main.java.com.vyorg.clinica.kinal0.repository.CitaRepository;

public class NotificacionService {

    private final CitaRepository citaRepository;

    public NotificacionService(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    public List<Notificacion> listar() {
        List<Notificacion> notificaciones = new ArrayList<>();

        LocalDate hoy = LocalDate.now();
        LocalDate manana = hoy.plusDays(1);

        for (Cita cita : citaRepository.findAll()) {
            boolean esHoyOManana = cita.getFecha() != null
                    && (cita.getFecha().isEqual(hoy) || cita.getFecha().isEqual(manana));
            boolean estaPendiente = "Programada".equalsIgnoreCase(cita.getEstado());

            if (esHoyOManana && estaPendiente) {
                String cuando = cita.getFecha().isEqual(hoy) ? "hoy" : "mañana";
                String mensaje = "Cita de " + cita.getNombrePaciente() + " " + cuando +
                        " a las " + cita.getHora() + " por confirmar.";
                LocalDateTime fecha = cita.getFecha().atTime(cita.getHora() != null ? cita.getHora() : java.time.LocalTime.MIDNIGHT);
                notificaciones.add(new Notificacion("🗓", mensaje, fecha, "CITAS"));
            }
        }

        notificaciones.sort((a, b) -> a.getFecha().compareTo(b.getFecha()));
        return notificaciones;

        // Cuando existan las tablas de sala de espera, firma de expedientes y farmacia,
        // se agregan aquí más bloques "for" iguales a este, cada uno con su propio destino.
    }
}