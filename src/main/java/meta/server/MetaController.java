package meta.server;

import meta.server.MetaServiceProxy;

public class MetaController {
    private final MetaServiceProxy metaServiceProxy;

    public MetaController(MetaServiceProxy metaServiceProxy) {
        this.metaServiceProxy = metaServiceProxy;
    }

    public void handleRequest(String command) {
        String response = metaServiceProxy.processRequest(command);
        System.out.println("[MetaController] Respuesta: " + response);
    }
}


