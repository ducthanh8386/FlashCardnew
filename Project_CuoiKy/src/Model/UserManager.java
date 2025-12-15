package Model;

// Thêm các thư viện JDBC cần thiết

import java.sql.*;
import java.util.List;
// Loại bỏ các import liên quan đến File I/O (File, BufferedReader, BufferedWriter, v.v.)

public class UserManager {

    // Loại bỏ DATA_FOLDER và FILE_EXTENSION

    public UserManager() {
        // Không cần tạo thư mục nữa, logic này đã được loại bỏ
    }

    /**
     * Đăng ký người dùng mới vào cơ sở dữ liệu.
     * @param username Tên đăng nhập.
     * @param password Mật khẩu.
     * @return true nếu đăng ký thành công, false nếu thất bại (trùng tên đăng nhập hoặc lỗi DB).
     */
    public boolean register(String username, String password) {
        if (username.trim().isEmpty() || password.trim().isEmpty()) return false;

        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        // Sử dụng try-with-resources để đảm bảo Connection, PreparedStatement được đóng
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (conn == null) return false; // Không kết nối được DB

            pstmt.setString(1, username.trim());
            pstmt.setString(2, password.trim());

            // Thực thi INSERT
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                // Có thể lỗi do trùng username (UNIQUE key) hoặc lỗi khác
                return false;
            }

            System.out.println("Đăng ký thành công người dùng: " + username);
            return true;

        } catch (SQLException e) {
            // Lỗi SQL (ví dụ: trùng username do constraint UNIQUE)
            System.err.println("LỖI ĐĂNG KÝ: " + e.getMessage());
            return false;
        }
    }

    /**
     * Đăng nhập người dùng.
     * @param username Tên đăng nhập.
     * @param password Mật khẩu.
     * @return Đối tượng User nếu đăng nhập thành công, ngược lại trả về null.
     */
    public User login(String username, String password) {
        String sql = "SELECT id, username, password FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) return null; // Không kết nối được DB

            pstmt.setString(1, username.trim());
            pstmt.setString(2, password.trim());

            // Thực thi SELECT
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Đã tìm thấy người dùng hợp lệ
                    int userId = rs.getInt("id");
                    String userDb = rs.getString("username");
                    String passDb = rs.getString("password");

                    // Tạo đối tượng User với ID từ database
                    System.out.println("Đăng nhập thành công, User ID: " + userId);
                    return new User(userId, userDb, passDb);
                }
            }
        } catch (SQLException e) {
            System.err.println("LỖI ĐĂNG NHẬP: " + e.getMessage());
        }
        return null; // Đăng nhập thất bại
    }

    // Loại bỏ phương thức loadUser và saveUser cũ
    // Logic quản lý FlashCard (load/save) sẽ được chuyển hoàn toàn sang FlashCardManager.

    /**
     * Thay thế cho getUser cũ, tuy nhiên, phương thức login mới đã trả về User object.
     * Giữ lại cho đồng nhất code nếu cần nhưng sẽ không được sử dụng ở View/Controller.
     */
    // public User getUser(String username) { ... } // Không cần thiết nữa
}