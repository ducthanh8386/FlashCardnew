
package View;

import javax.swing.*;
import java.awt.*;
import Model.UserManager;
import Model.FlashCardManager;
import Control.CardControl;
import Model.User;
import View.FlashCardAppView;
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
                User currentUser = userManager.getUser(username);
                FlashCardManager userModel = currentUser.getFlashCardManager();
                CardControl controller = new CardControl(userModel);
                FlashCardAppView appView = new FlashCardAppView(controller);
                addFlashCardAppListeners(appView, controller, userManager , currentUser);
                dispose();
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
    private void addFlashCardAppListeners(FlashCardAppView view, CardControl controller, UserManager userManager, User currentUser){
        // Thêm thẻ mới
        view.getAddButton().addActionListener(e -> {
            String english = view.getEnglishInput().trim();
            String vietnamese = view.getVietnameseInput().trim();
            boolean added = controller.addCard(english, vietnamese);
            if (added) {
                view.clearInputFields();
                view.updateCardListDisplay();
                view.showMessage("Đã thêm thẻ thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                userManager.saveUser(currentUser);
            } else {
                String regexTiengAnh = "[a-zA-Z ]+";
                String regexTiengViet= "^[A-Za-zÀ-ỹ\\s]+$";
                if (english.isEmpty() || vietnamese.isEmpty()) {
                    view.showMessage("Lỗi: Vui lòng nhập đầy đủ cả Tiếng Anh và Tiếng Việt!",
                            "Lỗi Nhập liệu", JOptionPane.ERROR_MESSAGE);

                } else if (!english.matches(regexTiengAnh) || !vietnamese.matches(regexTiengViet)) {
                    view.showMessage("Lỗi: Vui lòng không nhập ký tự đặc biệt hoặc số vào từ!",
                            "Lỗi Định dạng", JOptionPane.ERROR_MESSAGE);

                }else {
                    view.showMessage("Lỗi: Thẻ này đã được tồn tại trong danh sách.",
                            "Trùng lặp từ vựng", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


//    private void removeFlashCardAppListeners(FlashCardAppView view, CardControl controller){
        view.getRemoveButton().addActionListener(e -> {
            String english = view.getEnglishInput();
            String vietnamese = view.getVietnameseInput();

            boolean isRemoved = controller.removeCard(english, vietnamese);
            if(isRemoved){
                view.clearInputFields();
                view.updateCardListDisplay();
                view.showMessage("Đã xóa thẻ thành công!"," Thành Công", JOptionPane.INFORMATION_MESSAGE);
                userManager.saveUser(currentUser);
            }else {
                if (english.trim().isEmpty() || vietnamese.trim().isEmpty()) {
                    view.showMessage("Lỗi: Hãy nhập đầy đủ cả Tiếng Anh và Tiếng Việt muốn xóa",
                            "Lỗi nhập liệu ", JOptionPane.ERROR_MESSAGE);
                } else {
                    view.showMessage("Lỗi: Không tìm thấy thẻ cần xóa", "Lỗi xóa thẻ", JOptionPane.ERROR_MESSAGE);
                }
            }

        });


        // Hiển thị nghĩa
        view.getShowAnswerButton().addActionListener(e -> view.getCardAnswerLabel().setVisible(true));

        // Thẻ tiếp theo
        view.getNextCardButton().addActionListener(e -> view.resetPracticeCard());
    }

}