package Model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class UserManager {
    // Đường dẫn tuyệt đối của bạn
    private final String DATA_FOLDER = "src/data/";
    private final String FILE_EXTENSION = ".txt";

    public UserManager() {
        File folder = new File(DATA_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public boolean register(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) return false;
        File file = new File(DATA_FOLDER + username + FILE_EXTENSION);
        if (file.exists()) return false;

        // Dùng UTF-8 để ghi tiếng Việt chuẩn
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(password);
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean login(String username, String password) {
        File file = new File(DATA_FOLDER + username + FILE_EXTENSION);
        if (!file.exists()) return false;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String storedPassword = reader.readLine();
            return storedPassword != null && storedPassword.equals(password);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getUser(String username) {
        File file = new File(DATA_FOLDER + username + FILE_EXTENSION);
        if (!file.exists()) return null;

        User user = null;
        // Dùng UTF-8 để đọc tiếng Việt chuẩn
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String password = reader.readLine();
            user = new User(username, password);

            System.out.println("--- BẮT ĐẦU ĐỌC FILE CỦA: " + username + " ---");
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                // Tách chuỗi: English | Vietnamese
                String[] parts = line.split("\\s*\\|\\s*");

                if (parts.length == 2) {
                    String english = parts[0].trim();
                    String vietnamese = parts[1].trim();

                    // Thử thêm và in kết quả ra màn hình Console
                    boolean ketQua = user.getFlashCardManager().addCards(english, vietnamese);

                    if (ketQua) {
                        System.out.println("Đã nạp thành công: " + english);
                    } else {
                        System.err.println("LỖI: Không thể nạp từ '" + english + "' (Có thể do sai Regex hoặc trùng lặp)");
                    }
                } else {
                    System.err.println("LỖI ĐỊNH DẠNG DÒNG: " + line);
                }
            }
            System.out.println("--- KẾT THÚC ĐỌC FILE ---");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void saveUser(User user) {
        if (user == null) return;
        File file = new File(DATA_FOLDER + user.getUsername() + FILE_EXTENSION);

        // Dùng UTF-8 để ghi
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {

            writer.write(user.getPassword());
            writer.newLine();

            List<FlashCard> cards = user.getFlashCardManager().getAllCards();
            for (FlashCard card : cards) {
                writer.write(card.toFileString());
                writer.newLine();
            }
            System.out.println("Đã lưu " + cards.size() + " thẻ vào file: " + file.getName());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}