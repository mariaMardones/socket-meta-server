package meta.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

//MetaService.java
public class MetaService extends Thread {
 private Socket clientSocket;
 private DataInputStream in;
 private DataOutputStream out;
 private MetaAuthentification metaAuthentification;

 public MetaService(Socket clientSocket, MetaAuthentification metaAuth) {
     this.clientSocket = clientSocket;
     this.metaAuthentification = metaAuth;
     
     try {
         this.in = new DataInputStream(clientSocket.getInputStream());
         this.out = new DataOutputStream(clientSocket.getOutputStream());
         this.start();
     } catch (IOException e) {
         System.err.println("# MetaService: IO error during client connection: " + e.getMessage());
         close();
     }
 }

 public void run() {
     try {
         // Leer la solicitud del cliente
         String request = this.in.readUTF();
         System.out.println(" - MetaService: Received request: " + request);
         System.out.println(clientSocket.getInetAddress().getHostAddress() + ":" + 
                          clientSocket.getPort() + " '->' " + request);
         
         // Procesar la solicitud - No enviamos la respuesta aquí porque MetaAuthentification ya la envía
         metaAuthentification.procesarSolicitudCrearSesion(request, clientSocket);
         
     } catch (IOException e) {
         System.err.println("# MetaService: IO error while processing request: " + e.getMessage());
     } finally {
         close();
     }
 }

 private void close() {
     try {
         if (in != null) in.close();
         if (out != null) out.close();
         if (clientSocket != null) clientSocket.close();
     } catch (IOException e) {
         System.err.println("# MetaService: Error while closing resources: " + e.getMessage());
     }
 }
}