package Model;
import java.sql.*;

public class UserManager {

    public UserManager() {

    }
    public boolean register(String username, String password) {
        if (username.trim().isEmpty() || password.trim().isEmpty()) return false;

        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (conn == null) return false; // Không kết nối được DB

            pstmt.setString(1, username.trim());
            pstmt.setString(2, password.trim());


            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                // Có thể lỗi do trùng username (UNIQUE key) hoặc lỗi khác
                return false;
            }

            System.out.println("Đăng ký thành công người dùng: " + username);
            return true;

        } catch (SQLException e) {
            System.err.println("LỖI ĐĂNG KÝ: " + e.getMessage());
            return false;
        }
    }

    public User login(String username, String password) {
        String sql = "SELECT id, username, password FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) return null; // Không kết nối được DB

            pstmt.setString(1, username.trim());
            pstmt.setString(2, password.trim());


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
}