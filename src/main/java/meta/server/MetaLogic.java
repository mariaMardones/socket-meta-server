package meta.server;

import java.util.HashMap;
import java.util.Map;

public class MetaLogic {

    // Simulamos una base de datos en memoria
    private Map<String, String> users = new HashMap<>();

    public MetaLogic() {
        // Añadimos usuarios de prueba: email -> password
        users.put("user1@example.com", "password123");
        users.put("user2@example.com", "mypassword");
    }

    // Método para verificar si un email está registrado
    public boolean checkEmail(String email) {
        return users.containsKey(email);
    }

    // Método para validar email y password
    public boolean login(String email, String password) {
        return users.containsKey(email) && users.get(email).equals(password);
    }
}