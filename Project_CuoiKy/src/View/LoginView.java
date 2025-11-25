package View;

import javax.swing.*;
import java.awt.*;
import Model.UserManager;
import Model.FlashCardManager;
import Control.CardControl;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;
    private UserManager userManager;

    public LoginView(UserManager userManager){
        this.userManager = userManager;

        setTitle("Login / Register");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3,2,10,10));
        setLocationRelativeTo(null);

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        add(loginButton);
        add(registerButton);

        setVisible(true);

        // --- Login action ---
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if(userManager.login(username, password)){
                // Tạo model và controller
                FlashCardManager model = new FlashCardManager();
                CardControl controller = new CardControl(model);

                // Mở FlashCardAppView với listener đầy đủ
                FlashCardAppView appView = new FlashCardAppView(controller);

                // Thêm listener cho các nút trong FlashCardAppView
                addFlashCardAppListeners(appView, controller);

                dispose(); // đóng LoginView
            } else {
                JOptionPane.showMessageDialog(this,
                        "Sai tài khoản hoặc mật khẩu!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // --- Register action ---
        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if(userManager.register(username, password)){
                JOptionPane.showMessageDialog(this,
                        "Đăng ký thành công!",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Username đã tồn tại hoặc rỗng!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // Hàm thêm listener cho FlashCardAppView
    private void addFlashCardAppListeners(FlashCardAppView view, CardControl controller){
        // Thêm thẻ mới
        view.getAddButton().addActionListener(e -> {
            String english = view.getEnglishInput();
            String vietnamese = view.getVietnameseInput();

            if (controller.addCard(english, vietnamese)) {
                view.clearInputFields();
                view.updateCardListDisplay();
            } else {
                view.showMessage("Vui lòng nhập đầy đủ cả Tiếng Anh và Tiếng Việt!",
                        "Lỗi Nhập liệu", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Hiển thị nghĩa
        view.getShowAnswerButton().addActionListener(e -> view.getCardAnswerLabel().setVisible(true));

        // Thẻ tiếp theo
        view.getNextCardButton().addActionListener(e -> view.resetPracticeCard());
    }
}
