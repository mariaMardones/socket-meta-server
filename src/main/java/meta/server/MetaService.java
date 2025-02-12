package meta.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicLong;

public class MetaService {

    private static final String DELIMITER = "#";
    private static AtomicLong sessionCounter = new AtomicLong(1); // Para generar IDs de sesión únicos

    private Socket clientSocket;
    private DataInputStream in;
    private DataOutputStream out;
    private MetaAuthentification metaAuthentification;  // Instancia de MetaAutentification para delegar la creación de la sesión

    // Constructor que recibe el socket del cliente
    public MetaService(Socket clientSocket, MetaAuthentification metaAuthentification) {
        this.clientSocket = clientSocket;
        this.metaAuthentification = metaAuthentification;  // Recibe la instancia de MetaAutentification
        try {
            this.in = new DataInputStream(clientSocket.getInputStream());
            this.out = new DataOutputStream(clientSocket.getOutputStream());
            handleRequest();
        } catch (IOException e) {
            System.err.println("# MetaService: IO error during client connection: " + e.getMessage());
        }
    }

    private void handleRequest() {
        try {
            // Leer la solicitud del cliente
            String request = in.readUTF();
            System.out.println(" - MetaService: Received request: " + request);

            // Procesar la solicitud de crear sesión utilizando MetaAutentification
            String response = metaAuthentification.procesarSolicitudCrearSesion(request);

            // Enviar la respuesta al cliente
            out.writeUTF(response);
            out.flush();
        } catch (IOException e) {
            System.err.println("# MetaService: IO error while processing request: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("# MetaService: Error while closing socket: " + e.getMessage());
            }
        }
    }
}
