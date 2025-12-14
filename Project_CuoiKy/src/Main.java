import Model.FlashCardManager;
import Model.UserManager;
import View.FlashCardAppView;
import Model.FlashCard;
import Control.CardControl;
import View.LoginView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import Model.UserManager;
import View.LoginView;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Chạy trên Event Dispatch Thread (EDT) để đảm bảo an toàn cho Swing
        SwingUtilities.invokeLater(() -> {
            // Khởi tạo UserManager (tự động load dữ liệu cũ nếu có)
            UserManager userManager = new UserManager();

            // Mở màn hình đăng nhập
            new LoginView(userManager);
        });
    }
}