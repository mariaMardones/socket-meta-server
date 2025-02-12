//package meta.server;
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.net.Socket;
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.concurrent.atomic.AtomicLong;
//
//public class MetaService {
//
//    private static final String DELIMITER = "#";
//    private static AtomicLong sessionCounter = new AtomicLong(1); // Para generar IDs de sesión únicos
//
//    private Socket clientSocket;
//    private DataInputStream in;
//    private DataOutputStream out;
//    private MetaAuthentification metaAuthentification;  // Instancia de MetaAutentification para delegar la creación de la sesión
//
//    // Constructor que recibe el socket del cliente
//    public MetaService(Socket clientSocket) {
//        this.clientSocket = clientSocket;
//        //this.metaAuthentification = metaAuthentification;  // Recibe la instancia de MetaAutentification
//        try {
//            this.in = new DataInputStream(clientSocket.getInputStream());
//            this.out = new DataOutputStream(clientSocket.getOutputStream());
//            handleRequest();
//        } catch (IOException e) {
//            System.err.println("# MetaService: IO error during client connection: " + e.getMessage());
//        }
//    }
//
//    private void handleRequest() {
//        try {
//            // Leer la solicitud del cliente
//            String request = in.readUTF();
//            System.out.println(" - MetaService: Received request: " + request);
//
//            // Procesar la solicitud de crear sesión utilizando MetaAutentification
//            String response = metaAuthentification.procesarSolicitudCrearSesion(request, clientSocket);
//
//            // Enviar la respuesta al cliente
//            out.writeUTF(response);
//            out.flush();
//        } catch (IOException e) {
//            System.err.println("# MetaService: IO error while processing request: " + e.getMessage());
//        } finally {
//            try {
//                clientSocket.close();
//            } catch (IOException e) {
//                System.err.println("# MetaService: Error while closing socket: " + e.getMessage());
//            }
//        }
//    }
//}


package meta.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MetaService implements Runnable {
    private Socket clientSocket;
    private MetaAuthentification metaAuth;

    public MetaService(Socket socket) {
        this.clientSocket = socket;
        new Thread(this).start(); // Ejecutar en un hilo separado
    }

    @Override
    public void run() {
        try (DataInputStream in = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())) {

            // Leer solicitud del cliente
            String request = in.readUTF();
            System.out.println("📥 Solicitud recibida: " + request);

            // Procesar la solicitud y generar la respuesta
            String response = metaAuth.procesarSolicitudCrearSesion(request, clientSocket);

            // Enviar respuesta al cliente
            out.writeUTF(response);
            out.flush();
            System.out.println("📤 Respuesta enviada: " + response);

        } catch (IOException e) {
            System.err.println("Error al manejar la conexión: " + e.getMessage());
        }
    }
}

