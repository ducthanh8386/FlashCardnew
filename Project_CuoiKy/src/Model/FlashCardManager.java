package Model;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class FlashCardManager {

    private final int userId;

    public FlashCardManager(int userId) {
        this.userId = userId;
    }
    public boolean addCards(String englishWord, String vietnameseMeaning) {
//        String english = englishWord.trim();
//        String vietnamese = vietnameseMeaning.trim();
//
//        if (english.isEmpty() || vietnamese.isEmpty()) return false;
        String sql = "INSERT INTO flashcards (user_id, english_word, vietnamese_meaning, is_learned) VALUES (?, ?, ?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) return false;

            pstmt.setInt(1, this.userId);

            pstmt.setString(2, englishWord.trim());
            pstmt.setString(3, vietnameseMeaning.trim());
            pstmt.setBoolean(4, false);

            return pstmt.executeUpdate() >0;

        } catch (SQLException e) {
            e.printStackTrace();
//            System.err.println("LỖI THÊM THẺ: " + e.getMessage());
            return false;
        }
    }

    // Thêm thẻ
    public boolean updateCards(int cardId, String englishWord, String vietnameseMeaning) {
        String sql =  "UPDATE flashcards SET english_word = ?, vietnamese_meaning = ? WHERE card_id = ? AND user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) return false;

            pstmt.setString(1, englishWord.trim());
            pstmt.setString(2, vietnameseMeaning.trim());
            pstmt.setInt(3, cardId);
            pstmt.setInt(4, this.userId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    //  XÓA THẺ
    public boolean deleteCard(int cardId) {
        String sql = "DELETE FROM flashcards WHERE card_id = ? AND user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) return false;

            pstmt.setInt(1, cardId);
            pstmt.setInt(2, this.userId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean toggleLearnedStatus(int cardId, boolean isLearned) {
        String sql = "UPDATE flashcards SET is_learned = ? WHERE card_id = ? AND user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) return false;

            pstmt.setBoolean(1, isLearned);
            pstmt.setInt(2, cardId);
            pstmt.setInt(3, this.userId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<FlashCard> getAllCards() {
        List<FlashCard> list = new ArrayList<>();

        String sql = "SELECT * FROM flashcards WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) return list;
            pstmt.setInt(1, this.userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("card_id");
                    String en = rs.getString("english_word");
                    String vn = rs.getString("vietnamese_meaning");
                    boolean learned = rs.getBoolean("is_learned");

                    list.add(new FlashCard(id, en, vn, learned));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<FlashCard> searchCards(String keyword) {
        List<FlashCard> list = new ArrayList<>();
        String sql = "SELECT * FROM flashcards WHERE user_id = ? AND (english_word LIKE ? OR vietnamese_meaning LIKE ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) return list;

            String searchPattern = "%" + keyword.trim() + "%";

            pstmt.setInt(1, this.userId);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new FlashCard(
                            rs.getInt("card_id"),
                            rs.getString("english_word"),
                            rs.getString("vietnamese_meaning"),
                            rs.getBoolean("is_learned")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // LẤY NGẪU NHIÊN
    public FlashCard getRandomCard() {
        String sql = "SELECT * FROM flashcards WHERE user_id = ? ORDER BY RAND() LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) return null;
            pstmt.setInt(1, this.userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new FlashCard(
                            rs.getInt("card_id"),
                            rs.getString("english_word"),
                            rs.getString("vietnamese_meaning"),
                            rs.getBoolean("is_learned")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //  THỐNG KÊ
    public String getStatistics() {
        int total = 0;
        int learned = 0;
        String sql = "SELECT COUNT(*) as total, SUM(CASE WHEN is_learned = 1 THEN 1 ELSE 0 END) as learned FROM flashcards WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) return "N/A";
            pstmt.setInt(1, this.userId);

            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    total = rs.getInt("total");
                    learned = rs.getInt("learned");
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return "Tổng số thẻ: " + total + " | Đã thuộc: " + learned;
    }
}

