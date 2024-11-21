package akatsuki;

public class LoginManager {
    // This is a simple mock authentication (for demonstration)
    public boolean authenticate(String username, String password) {
        // Hardcoded credentials for testing
        if ("admin".equals(username) && "password123".equals(password)) {
            return true; // Authentication successful
        }
        return false; // Authentication failed
    }
}