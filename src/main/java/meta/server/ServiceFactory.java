package meta.server;

import meta.server.MetaService;
import java.net.Socket;

public class ServiceFactory {
		public static MetaService createMetaService(Socket socket) {
			return new MetaService(socket);	
		}
        
	

}