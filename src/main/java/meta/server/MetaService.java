package meta.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

public class MetaService extends Thread {
    private DataInputStream in;
    private DataOutputStream out;
    private Socket tcpSocket;

    private static final String DELIMITER = "#";
    private MetaLogic metaLogic = new MetaLogic();

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
            while (true) {
                String data = this.in.readUTF();
                System.out.println(" - MetaService - Received data from '" + tcpSocket.getInetAddress().getHostAddress() 
                                    + ":" + tcpSocket.getPort() + "' -> '" + data + "'");

                String response = processRequest(data);

                this.out.writeUTF(response);
                this.out.flush();
                System.out.println(" - MetaService - Sent response to '" + tcpSocket.getInetAddress().getHostAddress() 
                                    + ":" + tcpSocket.getPort() + "' -> '" + response + "'");
            }
        } catch (EOFException e) {
            System.out.println("# MetaService - Client disconnected.");
        } catch (IOException e) {
            System.err.println("# MetaService - Error: " + e.getMessage());
        } finally {
            try {
                tcpSocket.close();
            } catch (IOException e) {
                System.err.println("# MetaService - Socket close error: " + e.getMessage());
            }
        }
    }

    public String processRequest(String request) {
        StringTokenizer tokenizer = new StringTokenizer(request, DELIMITER);
        
        if (!tokenizer.hasMoreTokens()) return "ERR#INVALID_REQUEST";

        String action = tokenizer.nextToken();
        String email = tokenizer.hasMoreTokens() ? tokenizer.nextToken() : null;
        String password = (action.equals("login") && tokenizer.hasMoreTokens()) ? tokenizer.nextToken() : null;

        if (email == null) return "ERR#MISSING_EMAIL";

        switch (action) {
            case "checkEmail":
                return metaLogic.checkEmail(email) ? "OK#EMAIL_FOUND" : "ERR#EMAIL_NOT_FOUND";
            case "login":
                return (password != null && metaLogic.login(email, password)) ? "OK#LOGIN_SUCCESS" : "ERR#LOGIN_FAIL";
            default:
                return "ERR#UNKNOWN_ACTION";
        }
    }
}
