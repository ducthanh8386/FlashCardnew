package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/flashcard_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "09092006";



    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            return connection;
        } catch (SQLException e) {
            System.err.println("LỖI KẾT NỐI DATABASE: Không thể kết nối đến MySQL.");
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            System.err.println("LỖI DRIVER: Không tìm thấy JDBC Driver. Hãy kiểm tra file JAR.");
            e.printStackTrace();
            return null;
        }
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                // System.out.println("Đã đóng kết nối Database.");
            } catch (SQLException e) {
                System.err.println("LỖI ĐÓNG KẾT NỐI:");
                e.printStackTrace();
            }
        }
    }
}