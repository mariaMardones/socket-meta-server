package meta.server;

import java.net.Socket;

public class MetaServiceProxy extends MetaService {

	public MetaServiceProxy(Socket socket) {
		super(socket);
	}
	
	@Override
    public String processRequest(String request) {
        System.out.println("[MetaServiceProxy] Procesando solicitud: " + request);
        return super.processRequest(request);
    }
}