package meta.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicLong;

public class MetaAuthentification {
    private static AtomicLong sessionCounter = new AtomicLong(1);

    public String procesarSolicitudCrearSesion(String request, Socket socketCliente) {
        System.out.println("Procesando solicitud: " + request);
        String[] parts = request.split("#");
        String response = "ERROR#Formato de solicitud inválido";

        try {
            // CREAR_SESION_ENTRENAMIENTO#email#distancia#duracion#tipo
            if (parts[0].equals("CREAR_SESION_ENTRENAMIENTO") && parts.length == 5) {
                String email = parts[1];
                String distance = parts[2];
                double duration = Double.parseDouble(parts[3]);
                String type = parts[4];
                
                response = crearSesion(email, distance, duration, type);
                
                // Enviar respuesta al cliente
                DataOutputStream out = new DataOutputStream(socketCliente.getOutputStream());
                out.writeUTF(response);
                out.flush();
                
                System.out.println("Respuesta enviada: " + response);
            }
        } catch (Exception e) {
            System.err.println("Error procesando solicitud: " + e.getMessage());
            try {
                DataOutputStream out = new DataOutputStream(socketCliente.getOutputStream());
                out.writeUTF("ERROR#" + e.getMessage());
                out.flush();
            } catch (IOException ioE) {
                System.err.println("Error enviando respuesta de error: " + ioE.getMessage());
            }
        }
        
        return response;
    }

    private String crearSesion(String email, String distance, double duration, String type) {
        LocalDate fechaInicio = LocalDate.now();
        LocalTime horaInicio = LocalTime.now();
        long sessionId = sessionCounter.getAndIncrement();
        
        System.out.println("📌 Creando sesión: " +
                         "ID=" + sessionId +
                         ", Email=" + email +
                         ", Tipo=" + type +
                         ", Distancia=" + distance +
                         ", Duración=" + duration);
        
        return "SESION_CREADA#" + sessionId + "#" + fechaInicio + "#" + horaInicio;
    }
}