
package View;

import Control.CardControl;
import Model.FlashCardManager;
import Model.User;
import Model.UserManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private ModernButton loginButton, registerButton;
    private UserManager userManager;

    // --- BẢNG MÀU HIỆN ĐẠI ---
    private final Color PRIMARY_COLOR = new Color(52, 152, 219);   // Xanh dương
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);   // Xanh lá
    private final Color BACKGROUND_COLOR = new Color(245, 246, 250); // Xám nhạt nền
    private final Color WHITE_COLOR = Color.WHITE;
    private final Color TEXT_COLOR = new Color(44, 62, 80);
    private final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);

    public LoginView(UserManager userManager){
        this.userManager = userManager;

        setTitle("Đăng Nhập ");
        setSize(700, 600); // Kích thước vừa phải
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);

        initComponents();
        setupLayout();
        setupListeners();

        setVisible(true);
    }

    class ModernButton extends JButton {
        public ModernButton(String text, Color bgColor) {
            super(text);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setForeground(Color.WHITE);
            setBackground(bgColor);
            setFocusPainted(false);
            setBorder(new EmptyBorder(10, 0, 10, 0)); // Padding trên dưới
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Hiệu ứng Hover
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent evt) { setBackground(bgColor.darker()); }
                public void mouseExited(MouseEvent evt) { setBackground(bgColor); }
            });
        }
    }

    private void initComponents() {
        usernameField = createStyledTextField();
        passwordField = createStyledPasswordField();

        loginButton = new ModernButton("Đăng Nhập", PRIMARY_COLOR);
        registerButton = new ModernButton("Đăng Ký Ngay", SUCCESS_COLOR);
    }

    // Helper: Tạo TextField đẹp
    private JTextField createStyledTextField() {
        JTextField tf = new JTextField();
        tf.setFont(MAIN_FONT);
        tf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(8, 10, 8, 10))); // Padding trong
        return tf;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField pf = new JPasswordField();
        pf.setFont(MAIN_FONT);
        pf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(8, 10, 8, 10)));
        return pf;
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(WHITE_COLOR);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(30, 40, 30, 40) // Padding lề rộng
        ));


        JLabel titleLabel = new JLabel("Chào Mừng!");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subTitleLabel = new JLabel("Học từ vựng mỗi ngày");
        subTitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        subTitleLabel.setForeground(Color.GRAY);
        subTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel formPanel = new JPanel(new GridLayout(4, 1, 0, 8)); // 4 dòng, cách nhau 8px
        formPanel.setBackground(WHITE_COLOR);
        formPanel.setBorder(new EmptyBorder(20, 0, 20, 0)); // Cách trên dưới

        JLabel userLbl = new JLabel("Tên đăng nhập:");
        userLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JLabel passLbl = new JLabel("Mật khẩu:");
        passLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));

        formPanel.add(userLbl);
        formPanel.add(usernameField);
        formPanel.add(passLbl);
        formPanel.add(passwordField);

        // Các nút bấm
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 10)); // 2 nút chồng lên nhau
        buttonPanel.setBackground(WHITE_COLOR);
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        cardPanel.add(titleLabel);
        cardPanel.add(subTitleLabel);
        cardPanel.add(formPanel);
        cardPanel.add(buttonPanel);
        mainPanel.add(cardPanel);
        setContentPane(mainPanel);
    }

    private void setupListeners() {
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if(username.isEmpty() || password.isEmpty()){
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            User currentUser = userManager.login(username, password);

            if(currentUser != null){
                FlashCardManager flashCardManager = currentUser.getFlashCardManager();
                CardControl controller = new CardControl(flashCardManager);
                new FlashCardAppView(controller);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Sai tài khoản hoặc mật khẩu!",
                        "Lỗi Đăng Nhập", JOptionPane.ERROR_MESSAGE);
            }
        });

        // đăng ký tk
        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if(username.isEmpty() || password.isEmpty()){
                JOptionPane.showMessageDialog(this,
                        "Vui lòng nhập đầy đủ thông tin!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if(userManager.register(username, password)){
                JOptionPane.showMessageDialog(this,
                        "Đăng ký thành công! Hãy đăng nhập.",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Username đã tồn tại!",
                        "Lỗi Đăng Ký", JOptionPane.ERROR_MESSAGE);
            }
        });

        passwordField.addActionListener(e -> loginButton.doClick());
    }
}