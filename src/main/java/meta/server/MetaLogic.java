package meta.server;

public class MetaLogic {
    private final UserRepository userRepository = new UserRepository();

    public boolean checkEmail(String email) {
        return userRepository.checkEmail(email);
    }

    public boolean login(String email, String password) {
        return userRepository.validateCredentials(email, password);
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
