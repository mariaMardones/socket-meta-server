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
        		String email = parts[1];
        		String password = parts[2];
        		response = "LOGIN_OK"+email;
        	
        		// Enviar respuesta al cliente
                DataOutputStream out = new DataOutputStream(socketCliente.getOutputStream());
                out.writeUTF(response);
                out.flush();
                System.out.println("Respuesta enviada: " + response);
        	
        	
   
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
}