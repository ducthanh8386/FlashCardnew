package View;

import javax.swing.*;
import java.awt.*;
import Model.UserManager;
import Model.FlashCardManager;
import Control.CardControl;
import Model.User;

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


            User currentUser = userManager.login(username, password);

            if(currentUser != null){
                FlashCardManager flashCardManager = currentUser.getFlashCardManager();
                CardControl controller = new CardControl(flashCardManager);


                FlashCardAppView appView = new FlashCardAppView(controller);


                addFlashCardAppListeners(appView, controller);


                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Sai tài khoản hoặc mật khẩu!",
                        "Lỗi Đăng Nhập", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if(userManager.register(username, password)){
                JOptionPane.showMessageDialog(this,
                        "Đăng ký thành công! Hãy đăng nhập.",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Username đã tồn tại hoặc rỗng!",
                        "Lỗi Đăng Ký", JOptionPane.ERROR_MESSAGE);
            }
        });
    }


    private void addFlashCardAppListeners(FlashCardAppView view, CardControl controller){

        // --- Nút THÊM THẺ ---
        view.getAddButton().addActionListener(e -> {
            String english = view.getEnglishInput().trim();
            String vietnamese = view.getVietnameseInput().trim();

            boolean added = controller.addCard(english, vietnamese);

            if (added) {
                view.clearInputFields();
                view.updateCardListDisplay();
                view.showMessage("Đã thêm thẻ thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Logic kiểm tra lỗi hiển thị thông báo (Regex kiểm tra đã có trong Controller/Manager chưa?)
                // Nếu Controller chưa check Regex kỹ, có thể check tại đây hoặc View
                if (english.isEmpty() || vietnamese.isEmpty()) {
                    view.showMessage("Lỗi: Vui lòng nhập đầy đủ!", "Lỗi Nhập liệu", JOptionPane.ERROR_MESSAGE);
                } else {
                    view.showMessage("Lỗi: Thẻ trùng hoặc ký tự không hợp lệ!", "Lỗi Thêm Thẻ", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // --- Nút XÓA THẺ (SỬA 5: Tách biệt rõ ràng khỏi nút Add) ---
        view.getRemoveButton().addActionListener(e -> {
            String english = view.getEnglishInput();
            String vietnamese = view.getVietnameseInput();

            boolean isRemoved = controller.removeCard(english, vietnamese);

            if(isRemoved){
                view.clearInputFields();
                view.updateCardListDisplay();
                view.showMessage("Đã xóa thẻ thành công!", "Thành Công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                if (english.trim().isEmpty()) {
                    view.showMessage("Lỗi: Hãy nhập từ Tiếng Anh muốn xóa", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                } else {
                    view.showMessage("Lỗi: Không tìm thấy thẻ cần xóa", "Lỗi xóa thẻ", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        view.getShowAnswerButton().addActionListener(e -> view.getCardAnswerLabel().setVisible(true));
        view.getNextCardButton().addActionListener(e -> view.resetPracticeCard());
    }
}