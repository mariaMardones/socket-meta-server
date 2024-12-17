package es.deusto.ingenieria.sd.translation.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

public class TranslationService extends Thread {
	private DataInputStream in;
	private DataOutputStream out;
	private Socket tcpSocket;

	private static String DELIMITER = "#";
	
	public TranslationService(Socket socket) {
		try {
			this.tcpSocket = socket;
		    this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());
			this.start();
		} catch (IOException e) {
			System.err.println("# TranslationService - TCPConnection IO error:" + e.getMessage());
		}
	}

	public void run() {
		try {
			String data = this.in.readUTF();			
			System.out.println("   - TranslationService - Received data from '" + tcpSocket.getInetAddress().getHostAddress() + ":" + tcpSocket.getPort() + "' -> '" + data + "'");					
			
			data = this.translate(data);
					
			this.out.writeUTF(data);					
			System.out.println("   - TranslationService - Sent data to '" + tcpSocket.getInetAddress().getHostAddress() + ":" + tcpSocket.getPort() + "' -> '" + data.toUpperCase() + "'");
		} catch (EOFException e) {
			System.err.println("   # TranslationService - TCPConnection EOF error" + e.getMessage());
		} catch (IOException e) {
			System.err.println("   # TranslationService - TCPConnection IO error:" + e.getMessage());
		} finally {
			try {
				tcpSocket.close();
			} catch (IOException e) {
				System.err.println("   # TranslationService - TCPConnection IO error:" + e.getMessage());
			}
		}
	}
	
	public String translate(String msg) {
		String translation = null;
		
		if (msg != null && !msg.trim().isEmpty()) {
			try {
				StringTokenizer tokenizer = new StringTokenizer(msg, DELIMITER);		
				String langFrom = tokenizer.nextToken();
				String langTo = tokenizer.nextToken();
				String text = tokenizer.nextToken();
				System.out.println("   - Starting translation of " + text + " from: " + langFrom + " to " + langTo);
		
				if (langFrom != null && langTo != null && text != null && !text.trim().isEmpty()) {
					GoogleTranslator gt = new GoogleTranslator();
					translation = gt.translate(langFrom, langTo, text);
					System.out.println("   - Google Translator result: " + translation);
				}
			} catch (Exception e) {
				System.err.println("   # TranslationService - Translation API error:" + e.getMessage());
				translation = null;
			}
		}
		
		return (translation == null) ? "ERR" : "OK" + DELIMITER + translation;
	}
}