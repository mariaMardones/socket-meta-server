package meta.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MetaServer {
    private static int numClients = 0;

    public static void main(String[] args) {
        int serverPort = Integer.parseInt("9500");
        MetaAuthentification metaAuth = new MetaAuthentification();

        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            System.out.println(" - MetaServer: Waiting for connections '" + 
                serverSocket.getInetAddress().getHostAddress() + ":" + 
                serverSocket.getLocalPort() + "' ...");

            while (true) {
                try {
                    // Solo aceptamos la conexión y la pasamos a MetaService
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("✅ Cliente conectado. Número de clientes: " + ++numClients);
                    
                    // Crear nueva instancia de MetaService pasando metaAuth
                    new MetaService(clientSocket, metaAuth);
                    
                } catch (IOException e) {
                    System.err.println("Error al manejar la conexión: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("# MetaServer: IO error: " + e.getMessage());
        }
    }
}
