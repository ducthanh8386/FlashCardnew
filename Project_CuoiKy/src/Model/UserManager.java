package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private List<User> users;
    private final String FILE_PATH = "users.txt";

    public UserManager() {
        users = new ArrayList<>();
        loadUsersFromFile();
    }

    // Đăng ký
    public boolean register(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) return false;
        for (User u : users) {
            if (u.getUsername().equals(username)) return false; // username đã tồn tại
        }
        User newUser = new User(username, password);
        users.add(newUser);
        saveUsersToFile();
        return true;
    }

    // Đăng nhập
    public boolean login(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    // Lưu user vào file
    private void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (User u : users) {
                writer.write(u.getUsername() + "," + u.getPassword());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load user từ file
    private void loadUsersFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    users.add(new User(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
