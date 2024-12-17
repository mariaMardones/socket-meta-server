package meta.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

public class MetaService extends Thread {
    private DataInputStream in;
    private DataOutputStream out;
    private Socket tcpSocket;

    private MetaLogic metaLogic = new MetaLogic();

    private static String DELIMITER = "#";

    public MetaService(Socket socket) {
        try {
            this.tcpSocket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.start();
        } catch (IOException e) {
            System.err.println("# MetaService - TCPConnection IO error: " + e.getMessage());
        }
    }

    public void run() {
        try {
            String data = this.in.readUTF();
            System.out.println(" - Received data: " + data);

            String response = processRequest(data);
            this.out.writeUTF(response);

            System.out.println(" - Sent response: " + response);
        } catch (IOException e) {
            System.err.println("# MetaService - TCPConnection IO error: " + e.getMessage());
        } finally {
            try {
                tcpSocket.close();
            } catch (IOException e) {
                System.err.println("# MetaService - TCPConnection IO error: " + e.getMessage());
            }
        }
    }

    private String processRequest(String request) {
        StringTokenizer tokenizer = new StringTokenizer(request, DELIMITER);
        String action = tokenizer.nextToken();
        String email = tokenizer.nextToken();
        String password = action.equals("login") ? tokenizer.nextToken() : null;

        switch (action) {
            case "checkEmail":
                return metaLogic.checkEmail(email) ? "OK" : "ERR";
            case "login":
                return metaLogic.login(email, password) ? "OK" : "ERR";
            default:
                return "UNKNOWN_ACTION";
        }
    }
}