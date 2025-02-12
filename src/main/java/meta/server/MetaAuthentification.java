package meta.server;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicLong;

public class MetaAuthentification {

    private static AtomicLong sessionCounter = new AtomicLong(1); // Para generar IDs de sesión únicos

    // Método para procesar la solicitud de creación de sesión
    public String procesarSolicitudCrearSesion(String request) {
        String[] parts = request.split("#");

        if (parts.length != 4) {
            return "ERROR#Formato incorrecto";
        }

        String email = parts[0];
        double distance = Double.parseDouble(parts[1]);
        String duration = parts[2];
        String type = parts[3];

        // Llamar al servicio para crear la sesión
        return crearSesion(email, distance, duration, type);
    }

    // Método para crear una sesión de entrenamiento
    private String crearSesion(String email, double distance, String duration, String type) {

        // Usar la fecha y hora actual como valores predeterminados
        LocalDate fechaInicio = LocalDate.now();  // Fecha actual
        LocalTime horaInicio = LocalTime.now();   // Hora actual

        // Generar un ID único para la sesión
        long sessionId = sessionCounter.getAndIncrement();

        // Simular la creación de la sesión
        System.out.println("📌 MetaAutentification: Creando sesión para " + email + " | ID: " + sessionId + 
                           " | Tipo: " + type + " | Distancia: " + distance + " | Duración: " + duration);

        return "SESION_CREADA#" + sessionId;
    }
}
