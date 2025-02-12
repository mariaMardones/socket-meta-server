package meta.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicLong;

public class MetaAuthentification {

    private static AtomicLong sessionCounter = new AtomicLong(1); // Para generar IDs de sesión únicos

    // Método para procesar la solicitud de creación de sesión
    public String procesarSolicitudCrearSesion(String request, Socket socketCliente) {
        String[] parts = request.split("#");
       

        if (parts.length == 5) {
        	 String email = parts[0];
             double  duration= Double.parseDouble(parts[2]);
             String  distance = parts[1];
             String type = parts[3];

             // Llamar al servicio para crear la sesión
             return crearSesion(email, distance, duration, type, socketCliente);
          
        }
        return "ERROR procesar la solicitud";
       
    }

    // Método para crear una sesión de entrenamiento
    private String crearSesion(String email, String distance, double duration, String type, Socket clientSocket) {

        // Usar la fecha y hora actual como valores predeterminados
        LocalDate fechaInicio = LocalDate.now();  // Fecha actual
        LocalTime horaInicio = LocalTime.now();   // Hora actual

        // Generar un ID único para la sesión
        long sessionId = sessionCounter.getAndIncrement();

        // Simular la creación de la sesión
        System.out.println("📌 MetaAuthentification: Creando sesión para " + email +
                           " | ID: " + sessionId +
                           " | Tipo: " + type +
                           " | Distancia: " + distance +
                           " | Duración: " + duration +
                           " | Fecha Inicio: " + fechaInicio +
                           " | Hora Inicio: " + horaInicio);

        // Preparar la respuesta
        String response = "SESION_CREADA#" + sessionId + "#" + fechaInicio + "#" + horaInicio;

        // Enviar la respuesta al cliente usando DataOutputStream
        try (DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())) {
            out.writeUTF(response);  // Escribir la respuesta al cliente
            out.flush();  // Asegurarse de que los datos sean enviados de inmediato
            System.out.println("📤 Respuesta enviada al cliente: " + response);
        } catch (IOException e) {
            System.err.println("Error al enviar la respuesta al cliente: " + e.getMessage());
        }

        return response; // Devolver la respuesta por si necesitas usarla localmente
    }
}