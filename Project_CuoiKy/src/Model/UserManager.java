
package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserManager implements Serializable {
    private static final long serialVersionUID = 3L;
    private List<User> users;
    private final String FILE_PATH = "users_data.ser";

    public UserManager() {
        users = new ArrayList<>();
        loadUsersFromFile();
    }
    // Đăng ký
    public boolean register(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) return false;
        for (User u : users) {
            if (u.getUsername().equals(username)) return false;
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
    public User getUser(String username) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }
    public void saveUsersToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.err.println("Lỗi khi lưu dữ liệu người dùng: " + e.getMessage());
        }
    }


    // Load user từ file
    private void loadUsersFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            users = (List<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Lỗi khi tải dữ liệu người dùng: " + e.getMessage());
        }
    }
}
