package meta.server;

import java.io.IOException;
import java.net.ServerSocket;

public class MetaServer {

    private static int numClients = 0;

    
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println(" # Usage: MetaServer [PORT]");
            System.exit(1);
        }

        int serverPort = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            System.out.println(" - MetaServer: Waiting for connections at " +
                    serverSocket.getInetAddress().getHostAddress() + ":" + serverSocket.getLocalPort());

            while (true) {
                new MetaService(serverSocket.accept());
                System.out.println(" - MetaServer: New client connected. Client number: " + ++numClients);
            }
        } catch (IOException e) {
            System.err.println("# MetaServer: IO error: " + e.getMessage());
        }
    }
}