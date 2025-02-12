package meta.server;


import java.io.IOException;
import java.net.ServerSocket;

public class MetaServer {

    private static int numClients = 0;

    public static void main(String[] args) {
        // Asegúrate de que se pasa un puerto como argumento
        if (args.length < 1) {
            System.err.println(" # Usage: MetaServer [PORT]");
            System.exit(1);
        }

        // Obtener el puerto del primer argumento
        int serverPort = Integer.parseInt(args[0]);
        MetaAuthentification metaAuth= new  MetaAuthentification() ;
        
        
        
        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            System.out.println(" - MetaServer: Waiting for connections '" + serverSocket.getInetAddress().getHostAddress() + ":" + serverSocket.getLocalPort() + "' ...");

            // Bucle para aceptar conexiones
            while (true) {
                // Esperar una conexión de un cliente
            	 System.out.println("⏳ Esperando conexión de cliente...");
            	 System.out.println(serverPort);
            	 
                 new MetaService(serverSocket.accept());
                 System.out.println("✅ Cliente conectado. Número de clientes: " + ++numClients);
            }

        } catch (IOException e) {
            System.err.println("# MetaServer: IO error: " + e.getMessage());
        }
    }
}
