package Model;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class FlashCardManager {

    private final int userId; // ID của người dùng hiện tại

    /**
     * Constructor mới nhận ID của người dùng.
     * @param userId ID người dùng.
     */
    public FlashCardManager(int userId) {
        this.userId = userId;
        // Không cần load dữ liệu tại đây, các phương thức sẽ tự động truy vấn DB
    }

    /**
     * Thêm thẻ mới vào cơ sở dữ liệu.
     * @return true nếu thêm thành công, false nếu thất bại (trùng lặp, lỗi DB).
     */
    public boolean addCards(String englishWord, String vietnameseMeaning) {
        String english = englishWord.trim();
        String vietnamese = vietnameseMeaning.trim();

        if (english.isEmpty() || vietnamese.isEmpty()) return false;

        // Bỏ qua kiểm tra Regex ở đây, bạn có thể tự thêm lại sau nếu cần.
        // Hoặc kiểm tra ở tầng Controller (CardControl).

        // SQL: INSERT INTO flashcards (user_id, english_word, vietnamese_meaning) VALUES (?, ?, ?)
        String sql = "INSERT INTO flashcards (user_id, english_word, vietnamese_meaning) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) return false;

            pstmt.setInt(1, this.userId);
            pstmt.setString(2, english);
            pstmt.setString(3, vietnamese);

            // Thực thi INSERT
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            // Lỗi SQL, có thể do trùng UNIQUE KEY (user_id, english_word)
            System.err.println("LỖI THÊM THẺ: " + e.getMessage());
            return false;
        }
    }

    /**
     * Xóa thẻ khỏi cơ sở dữ liệu.
     * @return true nếu xóa thành công, false nếu không tìm thấy hoặc lỗi.
     */
    public boolean removeCards(String englishWord, String vietnameseMeaning) {
        if (englishWord == null || englishWord.trim().isEmpty()){
            return false;
        }

        // Chúng ta chỉ cần từ Tiếng Anh và user_id để xóa vì đã có UNIQUE KEY
        String sql = "DELETE FROM flashcards WHERE user_id = ? AND english_word = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) return false;

            pstmt.setInt(1, this.userId);
            pstmt.setString(2, englishWord.trim());

            // Thực thi DELETE
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0; // Trả về true nếu có ít nhất 1 dòng bị ảnh hưởng

        } catch (SQLException e) {
            System.err.println("LỖI XÓA THẺ: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lấy tất cả các thẻ của người dùng hiện tại từ cơ sở dữ liệu.
     */
    public List<FlashCard> getAllCards() {
        List<FlashCard> cards = new ArrayList<>();
        // SQL: SELECT english_word, vietnamese_meaning FROM flashcards WHERE user_id = ?
        String sql = "SELECT english_word, vietnamese_meaning FROM flashcards WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) return cards;

            pstmt.setInt(1, this.userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String english = rs.getString("english_word");
                    String vietnamese = rs.getString("vietnamese_meaning");
                    cards.add(new FlashCard(english, vietnamese));
                }
            }
        } catch (SQLException e) {
            System.err.println("LỖI LẤY THẺ: " + e.getMessage());
        }
        return cards;
    }

    /**
     * Lấy một thẻ ngẫu nhiên từ cơ sở dữ liệu.
     */
    public FlashCard getRandomCard() {
        // Cách tối ưu hóa: thay vì lấy tất cả rồi chọn ngẫu nhiên, ta dùng ORDER BY RAND() LIMIT 1
        String sql = "SELECT english_word, vietnamese_meaning FROM flashcards WHERE user_id = ? ORDER BY RAND() LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) return null;

            pstmt.setInt(1, this.userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String english = rs.getString("english_word");
                    String vietnamese = rs.getString("vietnamese_meaning");
                    return new FlashCard(english, vietnamese);
                }
            }
        } catch (SQLException e) {
            System.err.println("LỖI LẤY THẺ NGẪU NHIÊN: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lấy số lượng thẻ hiện có.
     */
    public int getCardsCount() {
        String sql = "SELECT COUNT(*) FROM flashcards WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) return 0;

            pstmt.setInt(1, this.userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("LỖI ĐẾM THẺ: " + e.getMessage());
        }
        return 0;
    }

    // Các phương thức khác (như toFileString) có thể bị loại bỏ
    // ...
}