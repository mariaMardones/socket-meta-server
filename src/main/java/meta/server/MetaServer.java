package meta.server;

import java.io.IOException;
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

                    // Crear y manejar la conexión
                    new Thread(new MetaService(clientSocket)).start(); // Crear un hilo para cada cliente

                } catch (IOException e) {
                    System.err.println("# MetaServer: Error al aceptar conexión: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("# MetaServer: Error de IO: " + e.getMessage());
        }
    }
}
