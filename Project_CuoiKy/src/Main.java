import javax.swing.*;
import Model.UserManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserManager userManager = new UserManager();
            new View.LoginView(userManager);
        });
    }
}
