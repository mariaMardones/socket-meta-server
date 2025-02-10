package meta.server;

import meta.server.MetaController;
import meta.server.MetaServiceProxy;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MetaServer {

    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println(" - MetaServer: Esperando conexiones en '" +
                    serverSocket.getInetAddress().getHostAddress() +
                    ":" + serverSocket.getLocalPort() + "' ...");

            while (true) {
                try {
                    // Aceptar nueva conexión
                    Socket clientSocket = serverSocket.accept();
                    System.out.println(" - MetaServer: Nueva conexión aceptada desde " + clientSocket.getInetAddress());

                    // Crear controlador con un nuevo proxy para este cliente
                    MetaController controller = new MetaController(new MetaServiceProxy(clientSocket));

                    // Manejo de la comunicación con el cliente
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                         BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

                        String request = reader.readLine();
                        if (request != null) {
                            controller.handleRequest(request);
                            writer.write("Procesado: " + request);
                            writer.newLine();
                            writer.flush();
                        }

                    } catch (IOException e) {
                        System.err.println("Error manejando cliente: " + e.getMessage());
                    }

                } catch (IOException e) {
                    System.err.println("# MetaServer: Error al aceptar conexión: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("# MetaServer: Error de IO: " + e.getMessage());
        }
    }
}
