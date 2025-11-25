package View;

import javax.swing.*;
import java.awt.*;
import Model.UserManager;
import Model.FlashCardManager;
import Control.CardControl;

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

        // --- Login ---
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if(userManager.login(username, password)){
                // Tạo model + controller
                FlashCardManager model = new FlashCardManager();
                CardControl controller = new CardControl(model);

                // Mở FlashCardAppView
                FlashCardAppView appView = new FlashCardAppView(controller);

                // Thêm listener giữ nguyên logic cũ (trim + regex)
                addFlashCardAppListeners(appView, controller);

                dispose(); // đóng LoginView
            } else {
                JOptionPane.showMessageDialog(this,
                        "Sai tài khoản hoặc mật khẩu!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // --- Register ---
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

    // --- Thêm listener cho FlashCardAppView ---
    private void addFlashCardAppListeners(FlashCardAppView view, CardControl controller){
        // Thêm thẻ mới với kiểm tra regex
        view.getAddButton().addActionListener(e -> {
            String english = view.getEnglishInput().trim();
            String vietnamese = view.getVietnameseInput().trim();

            // Regex kiểm tra tiếng Anh và tiếng Việt
            boolean englishValid = english.matches("[a-zA-Z ]+");
            boolean vietnameseValid = vietnamese.matches("[\\p{L} ]+");

            if(!english.isEmpty() && !vietnamese.isEmpty() && englishValid && vietnameseValid){
                controller.addCard(english, vietnamese);
                view.clearInputFields();
                view.updateCardListDisplay();
            } else {
                view.showMessage("Vui lòng nhập đúng định dạng:\n- Tiếng Anh: chỉ chữ cái và khoảng trắng\n- Tiếng Việt: chỉ chữ Unicode và khoảng trắng",
                        "Lỗi Nhập liệu", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Hiển thị nghĩa
        view.getShowAnswerButton().addActionListener(e -> view.getCardAnswerLabel().setVisible(true));

        // Thẻ tiếp theo
        view.getNextCardButton().addActionListener(e -> view.resetPracticeCard());
    }
}
