package meta.server;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class MetaServer {

    private static final int PORT = 12345;
    private static final Map<String, String> users = new HashMap<>();

    static {
        // Simulando usuarios registrados
        users.put("user1@example.com", "password123");
        users.put("user2@example.com", "mypassword");
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Meta Server escuchando en el puerto " + PORT);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

                    String request = reader.readLine();
                    String response = processRequest(request);
                    writer.write(response);
                    writer.newLine();
                    writer.flush();

                } catch (IOException e) {
                    System.err.println("Error manejando cliente: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println("No se pudo iniciar el servidor: " + e.getMessage());
        }
    }

    private static String processRequest(String request) {
        if (request.startsWith("CHECK_EMAIL,")) {
            String email = request.split(",")[1];
            return users.containsKey(email) ? "EMAIL_FOUND" : "EMAIL_NOT_FOUND";
        } else if (request.startsWith("VALIDATE_LOGIN,")) {
            String[] parts = request.split(",");
            String email = parts[1];
            String password = parts[2];
            return users.getOrDefault(email, "").equals(password) ? "LOGIN_SUCCESS" : "LOGIN_FAIL";
        } else {
            return "INVALID_COMMAND";
        }
    }
}
