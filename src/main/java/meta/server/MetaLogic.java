package meta.server;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MetaLogic {

    private Map<String, String> users = new HashMap<>();

    public MetaLogic() {
        users.put("user1@example.com", "password123");
        users.put("user2@example.com", "mypassword");
    }

    public boolean checkEmail(String email) {
        return users.containsKey(email);
    }

    public boolean login(String email, String password) {
        return users.containsKey(email) && users.get(email).equals(password);
    }

    public String processRequest(String request) {
        String[] parts = request.split(",");
        if (parts.length < 2) {
            return "ERROR,Invalid request format";
        }

        String command = parts[0];
        String email = parts[1];
        String password = parts.length > 2 ? parts[2] : "";

        switch (command) {
            case "CHECK_EMAIL":
                return checkEmail(email) ? "OK,EMAIL_FOUND" : "ERROR,EMAIL_NOT_FOUND";
            case "VALIDATE_LOGIN":
                return login(email, password) ? "OK,LOGIN_SUCCESS" : "ERROR,INVALID_CREDENTIALS";
            default:
                return "ERROR,UNKNOWN_COMMAND";
        }
    }
}
