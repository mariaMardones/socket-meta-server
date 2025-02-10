package meta.server;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private static final Map<String, String> users = new HashMap<>();

    static {
        // Usuarios registrados simulados
        users.put("user1@example.com", "password123");
        users.put("user2@example.com", "mypassword");
    }

    public boolean checkEmail(String email) {
        return users.containsKey(email);
    }

    public boolean validateCredentials(String email, String password) {
        return users.getOrDefault(email, "").equals(password);
    }

    public void registerUser(String email, String password) {
        users.put(email, password);
    }
}
