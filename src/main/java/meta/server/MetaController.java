package meta.server;

import meta.server.MetaService;

public class MetaController {
    private final MetaService metaService;

    public MetaController(MetaService metaService) {
        this.metaService = metaService;
    }

    public void handleRequest(String command) {
        String response = metaService.processRequest(command);
        System.out.println("[MetaController] Respuesta: " + response);
    }
}