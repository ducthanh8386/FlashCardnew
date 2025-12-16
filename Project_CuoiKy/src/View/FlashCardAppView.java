package View;

import Control.CardControl;
import Model.FlashCard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;

public class FlashCardAppView extends JFrame {
    private final CardControl controller;

    private final Color PRIMARY_COLOR = new Color(52, 152, 219);   // Xanh dương
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);   // Xanh lá
    private final Color DANGER_COLOR = new Color(46, 204, 113);     // Đỏ
    private final Color WARNING_COLOR = new Color(241, 196, 15);   // Vàng cam
    private final Color BACKGROUND_COLOR = new Color(245, 246, 250); // Xám nhạt nền
    private final Color TEXT_COLOR = new Color(26, 38, 49);        // Xám đậm chữ
    private final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);

    // --- CÁC THÀNH PHẦN GIAO DIỆN ---
    private JTextField searchField;
    private JTextField englishInput;
    private JTextField vietnameseInput;
    private ModernButton addButton, updateButton, removeButton, clearButton;
    private ModernButton importButton;
    private JTable cardTable;
    private DefaultTableModel tableModel;
    private JLabel statsLabel;
    private int selectedId = -1;

    // Tabbed Pane
    private JTabbedPane tabbedPane;

    // Tab 1: Flashcard
    private JLabel fcQuestionLabel, fcAnswerLabel;
    private ModernButton fcShowBtn, fcNextBtn;

    // Tab 2: Quiz
    private JLabel quizQuestionLabel;
    private ModernButton[] quizButtons;
    private ModernButton quizNextBtn;
    private FlashCard currentQuizAnswer;

    // Tab 3: Typing
    private JLabel typeQuestionLabel;
    private JTextField typeInput;
    private JLabel typeResultLabel;
    private ModernButton typeCheckBtn, typeNextBtn;
    private FlashCard currentTypeCard;

    public FlashCardAppView(CardControl controller) {
        this.controller = controller;
        setTitle("FlashCard");
        setSize(1300, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Khởi tạo giao diện
        initComponents();
        setupLayout();
        setupListeners();

        // Load dữ liệu
        loadDataToTable(controller.getAllCards());
        updateStats();

        setVisible(true);
    }


    class ModernButton extends JButton {
        public ModernButton(String text, Color bgColor) {
            super(text);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setForeground(Color.WHITE);
            setBackground(bgColor);
            setFocusPainted(false);
            setBorder(new EmptyBorder(10, 20, 10, 20)); // Padding trong nút
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Hiệu ứng Hover đơn giản
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent evt) {
                    setBackground(bgColor.darker());
                }
                public void mouseExited(MouseEvent evt) {
                    setBackground(bgColor);
                }
            });
        }
    }

    private void initComponents() {
        // --- 1. KHỞI TẠO CÁC INPUT VÀ NÚT ---
        searchField = createStyledTextField();
        englishInput = createStyledTextField();
        vietnameseInput = createStyledTextField();

        addButton = new ModernButton("Thêm", SUCCESS_COLOR);
        updateButton = new ModernButton("Sửa", WARNING_COLOR);
        removeButton = new ModernButton("Xóa", DANGER_COLOR);
        clearButton = new ModernButton("Làm mới", Color.GRAY);
        importButton = new ModernButton("Nhập File", new Color(155, 89, 182));

        updateButton.setEnabled(false);
        removeButton.setEnabled(false);

        // --- 2. CẤU HÌNH JTABLE ĐẸP ---
        String[] columnNames = {"ID", "Tiếng Anh", "Tiếng Việt", "Đã thuộc"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if(columnIndex == 3) return Boolean.class;
                return super.getColumnClass(columnIndex);
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        cardTable = new JTable(tableModel);
        cardTable.setFont(MAIN_FONT);
        cardTable.setRowHeight(35); // Dòng cao thoáng
        cardTable.setShowVerticalLines(false);
        cardTable.setSelectionBackground(new Color(220, 230, 241));
        cardTable.setSelectionForeground(Color.BLACK);

        // Ẩn cột ID
        cardTable.removeColumn(cardTable.getColumnModel().getColumn(0));

        // Style Header của bảng
        JTableHeader header = cardTable.getTableHeader();
        header.setFont(HEADER_FONT);
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(100, 40));

        statsLabel = new JLabel("Thống kê: Đang tải...");
        statsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        statsLabel.setForeground(Color.DARK_GRAY);

        // --- 3. KHỞI TẠO TABS ---
        // Tab 1
        fcQuestionLabel = new JLabel("Sẵn sàng?", SwingConstants.CENTER);
        fcQuestionLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        fcQuestionLabel.setForeground(TEXT_COLOR);

        fcAnswerLabel = new JLabel("", SwingConstants.CENTER);
        fcAnswerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        fcAnswerLabel.setForeground(PRIMARY_COLOR);

        fcShowBtn = new ModernButton("Hiện Nghĩa", PRIMARY_COLOR);
        fcNextBtn = new ModernButton("Tiếp Theo >>", SUCCESS_COLOR);

        // Tab 2
        quizQuestionLabel = new JLabel("Câu hỏi", SwingConstants.CENTER);
        quizQuestionLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        quizButtons = new ModernButton[4];
        for (int i = 0; i < 4; i++) {
            quizButtons[i] = new ModernButton("Đáp án " + (i+1), Color.WHITE);
            quizButtons[i].setForeground(TEXT_COLOR); // Chữ đen cho nút trắng
            quizButtons[i].setBorder(new LineBorder(PRIMARY_COLOR, 1));
        }
        quizNextBtn = new ModernButton("Câu tiếp >>", PRIMARY_COLOR);

        // Tab 3
        typeQuestionLabel = new JLabel("Từ vựng", SwingConstants.CENTER);
        typeQuestionLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        typeInput = createStyledTextField();
        typeInput.setHorizontalAlignment(JTextField.CENTER);
        typeInput.setFont(new Font("Segoe UI", Font.BOLD, 18));

        typeResultLabel = new JLabel("Nhập nghĩa và kiểm tra", SwingConstants.CENTER);
        typeResultLabel.setFont(MAIN_FONT);

        typeCheckBtn = new ModernButton("Kiểm tra", WARNING_COLOR);
        typeNextBtn = new ModernButton("Tiếp >>", PRIMARY_COLOR);
    }

    // Helper tạo TextField đẹp
    private JTextField createStyledTextField() {
        JTextField tf = new JTextField();
        tf.setFont(MAIN_FONT);
        tf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(8, 8, 8, 8))); // Padding bên trong
        return tf;
    }

    private void setupLayout() {
        // --- CHIA 2 CỘT ---
        JPanel mainContainer = new JPanel(new GridLayout(1, 2, 20, 0));
        mainContainer.setBackground(BACKGROUND_COLOR);
        mainContainer.setBorder(new EmptyBorder(20, 20, 20, 20)); // Padding toàn màn hình

        // === CỘT TRÁI (QUẢN LÝ) ===
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(15, 15, 15, 15) // Padding trong panel trắng
        ));

        // 1. Header Tìm kiếm
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.setBackground(Color.WHITE);
        JLabel titleLbl = new JLabel("QUẢN LÝ THẺ");
        titleLbl.setFont(HEADER_FONT);
        titleLbl.setForeground(PRIMARY_COLOR);

        searchField.putClientProperty("JTextField.placeholderText", "Tìm kiếm..."); // (Java 9+) hoặc dùng label
        JPanel searchPnl = new JPanel(new BorderLayout());
        searchPnl.setBackground(Color.WHITE);
        searchPnl.add(new JLabel("🔍 "), BorderLayout.WEST);
        searchPnl.add(searchField, BorderLayout.CENTER);

        topPanel.add(titleLbl, BorderLayout.NORTH);
        topPanel.add(searchPnl, BorderLayout.SOUTH);

        // 2. Form nhập liệu
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        formPanel.add(new JLabel("Tiếng Anh:"));
        formPanel.add(englishInput);
        formPanel.add(new JLabel("Tiếng Việt:"));
        formPanel.add(vietnameseInput);

        // 3. Nút bấm
        JPanel btnPanel = new JPanel(new GridLayout(1, 5, 5, 0)); // Grid đều nhau
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(addButton);
        btnPanel.add(updateButton);
        btnPanel.add(removeButton);
        btnPanel.add(clearButton);
        btnPanel.add(importButton);

        // Gom nhóm input
        JPanel inputContainer = new JPanel();
        inputContainer.setLayout(new BoxLayout(inputContainer, BoxLayout.Y_AXIS));
        inputContainer.setBackground(Color.WHITE);
        inputContainer.add(topPanel);
        inputContainer.add(Box.createVerticalStrut(10));
        inputContainer.add(formPanel);
        inputContainer.add(Box.createVerticalStrut(10));
        inputContainer.add(btnPanel);

        leftPanel.add(inputContainer, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(cardTable), BorderLayout.CENTER);
        leftPanel.add(statsLabel, BorderLayout.SOUTH);


        // === CỘT PHẢI (LUYỆN TẬP) ===
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(Color.WHITE);

        // Tạo layout cho các tab
        tabbedPane.addTab("Học Flashcard", createFlashcardTab());
        tabbedPane.addTab("Trắc Nghiệm", createQuizTab());
        tabbedPane.addTab("Luyện Gõ", createTypeTab());

        mainContainer.add(leftPanel);
        mainContainer.add(tabbedPane);

        setContentPane(mainContainer);
    }

    // --- HÀM TẠO GIAO DIỆN TỪNG TAB ---
    private JPanel createFlashcardTab() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel center = new JPanel(new GridLayout(2, 1));
        center.setBackground(Color.WHITE);
        center.add(fcQuestionLabel);
        center.add(fcAnswerLabel);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        bottom.setBackground(Color.WHITE);
        bottom.add(fcShowBtn);
        bottom.add(fcNextBtn);

        p.add(center, BorderLayout.CENTER);
        p.add(bottom, BorderLayout.SOUTH);
        return p;
    }

    private JPanel createQuizTab() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        p.add(quizQuestionLabel, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 2, 15, 15));
        grid.setBackground(Color.WHITE);
        grid.setBorder(new EmptyBorder(20, 0, 20, 0));
        for(JButton b : quizButtons) grid.add(b);
        p.add(grid, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setBackground(Color.WHITE);
        bottom.add(quizNextBtn);
        p.add(bottom, BorderLayout.SOUTH);
        return p;
    }

    private JPanel createTypeTab() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;

        p.add(typeQuestionLabel, gbc);

        gbc.gridy++;
        p.add(typeInput, gbc);

        gbc.gridy++;
        p.add(typeResultLabel, gbc);

        gbc.gridy++;
        JPanel btnP = new JPanel();
        btnP.setBackground(Color.WHITE);
        btnP.add(typeCheckBtn);
        btnP.add(typeNextBtn);
        p.add(btnP, gbc);
        return p;
    }


    // --- LOGIC XỬ LÝ (KHÔNG ĐỔI SO VỚI TRƯỚC) ---
    // Copy nguyên phần logic cũ vào đây
    private void loadDataToTable(List<FlashCard> cards) {
        tableModel.setRowCount(0);
        for (FlashCard c : cards) {
            tableModel.addRow(new Object[]{c.getId(), c.getEnglishWord(), c.getVietnameseMeaning(), c.isLearned()});
        }
    }

    private void updateStats() {
        statsLabel.setText(controller.getStats());
    }

    private void setupListeners() {

        cardTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && cardTable.getSelectedRow() != -1) {
                int row = cardTable.getSelectedRow();
                selectedId = (int) tableModel.getValueAt(row, 0);
                englishInput.setText(tableModel.getValueAt(row, 1).toString());
                vietnameseInput.setText(tableModel.getValueAt(row, 2).toString());
                addButton.setEnabled(false);
                updateButton.setEnabled(true);
                removeButton.setEnabled(true);
            }
        });

        tableModel.addTableModelListener(e -> {
            if (e.getColumn() == 3) {
                int row = e.getFirstRow();
                if (row >= 0 && row < tableModel.getRowCount()) {
                    int id = (int) tableModel.getValueAt(row, 0);
                    boolean isLearned = (boolean) tableModel.getValueAt(row, 3);
                    controller.setLearned(id, isLearned);
                    updateStats();
                }
            }
        });
        importButton.addActionListener(e -> {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Chọn file từ vựng (.txt)");

                    int userSelection = fileChooser.showOpenDialog(this);

                    if (userSelection == JFileChooser.APPROVE_OPTION) {
                        java.io.File fileToImport = fileChooser.getSelectedFile();

                        // Gọi Controller xử lý
                        int count = controller.importFromFile(fileToImport);

                        if (count == -1) {
                            showMessage("Lỗi khi đọc file!", JOptionPane.ERROR_MESSAGE);
                        } else {
                            showMessage("Đã nhập thành công " + count + " từ vựng!", JOptionPane.INFORMATION_MESSAGE);
                            // Refresh lại bảng
                            clearButton.doClick();
                        }
                    }
                });

        clearButton.addActionListener(e -> {
            englishInput.setText("");
            vietnameseInput.setText("");
            cardTable.clearSelection();
            selectedId = -1;
            addButton.setEnabled(true);
            updateButton.setEnabled(false);
            removeButton.setEnabled(false);
            loadDataToTable(controller.getAllCards());
            updateStats();
        });

        addButton.addActionListener(e -> {
            String en = englishInput.getText();
            String vn = vietnameseInput.getText();
            if (!controller.isValidFormat(en, vn)) {
                showMessage("Lỗi: Hãy Nhập Đúng Định Dạng(không bao gồm số và kí tự đặc biệt)", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (controller.addCard(en, vn)) {
                showMessage("Thêm thành công!", JOptionPane.INFORMATION_MESSAGE);
                clearButton.doClick();
            } else {
                showMessage("Thẻ đã tồn tại!", JOptionPane.ERROR_MESSAGE);
            }
        });

        updateButton.addActionListener(e -> {
            if (selectedId == -1) return;
            String en = englishInput.getText();
            String vn = vietnameseInput.getText();
            if (!controller.isValidFormat(en, vn)) {
                showMessage("Lỗi định dạng!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (controller.updateCard(selectedId, en, vn)) {
                showMessage("Cập nhật thành công!", JOptionPane.INFORMATION_MESSAGE);
                clearButton.doClick();
            }
        });

        removeButton.addActionListener(e -> {
            if (selectedId == -1) return;
            if (controller.removeCard(selectedId)) {
                showMessage("Xóa thành công!", JOptionPane.INFORMATION_MESSAGE);
                clearButton.doClick();
            }
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { search(); }
            public void removeUpdate(DocumentEvent e) { search(); }
            public void changedUpdate(DocumentEvent e) { search(); }
            void search() { loadDataToTable(controller.searchCards(searchField.getText())); }
        });

        // Tab Logic Listeners
        fcNextBtn.addActionListener(e -> loadFlashCard());
        fcShowBtn.addActionListener(e -> fcAnswerLabel.setVisible(true));

        quizNextBtn.addActionListener(e -> loadQuiz());

        typeNextBtn.addActionListener(e -> loadTyping());
        typeCheckBtn.addActionListener(e -> checkTyping());
        typeInput.addActionListener(e -> checkTyping());
    }

    private void loadFlashCard() {
        FlashCard card = controller.getRandomCard();
        if(card != null) {
            fcQuestionLabel.setText(card.getEnglishWord());
            fcAnswerLabel.setText(card.getVietnameseMeaning());
            fcAnswerLabel.setVisible(false);
        } else {
            fcQuestionLabel.setText("Chưa có thẻ!");
            fcAnswerLabel.setText("");
        }
    }

    private void loadQuiz() {
        for (JButton btn : quizButtons) {
            btn.setBackground(Color.WHITE);
            btn.setEnabled(true);
        }
        List<FlashCard> quizData = controller.getQuizData();
        if (quizData == null || quizData.size() < 2) {
            quizQuestionLabel.setText("Cần thêm thẻ để chơi!");
            return;
        }
        currentQuizAnswer = quizData.get(0);
        quizQuestionLabel.setText(currentQuizAnswer.getEnglishWord());
        Collections.shuffle(quizData);
        for (int i = 0; i < 4; i++) {
            if (i < quizData.size()) {
                quizButtons[i].setVisible(true);
                quizButtons[i].setText(quizData.get(i).getVietnameseMeaning());
                for (java.awt.event.ActionListener al : quizButtons[i].getActionListeners()) quizButtons[i].removeActionListener(al);
                final int index = i;
                quizButtons[i].addActionListener(e -> checkQuizAnswer(quizButtons[index]));
            } else quizButtons[i].setVisible(false);
        }
    }

    private void checkQuizAnswer(JButton clickedBtn) {
        String selected = clickedBtn.getText();
        if (selected.equals(currentQuizAnswer.getVietnameseMeaning())) {
            clickedBtn.setBackground(SUCCESS_COLOR);
            showMessage("Chính xác!", JOptionPane.INFORMATION_MESSAGE);
        } else {
            clickedBtn.setBackground(DANGER_COLOR);
            showMessage("Sai! Đáp án: " + currentQuizAnswer.getVietnameseMeaning(), JOptionPane.ERROR_MESSAGE);
            for(JButton btn : quizButtons) {
                if(btn.getText().equals(currentQuizAnswer.getVietnameseMeaning())) btn.setBackground(SUCCESS_COLOR);
            }
        }
        for(JButton btn : quizButtons) btn.setEnabled(false);
    }

    private void loadTyping() {
        currentTypeCard = controller.getRandomCard();
        if (currentTypeCard != null) {
            typeQuestionLabel.setText(currentTypeCard.getEnglishWord());
            typeInput.setText("");
            typeInput.setEditable(true);
            typeInput.requestFocus();
            typeResultLabel.setText("Nhập nghĩa tiếng Việt");
            typeResultLabel.setForeground(TEXT_COLOR);
            typeCheckBtn.setEnabled(true);
        }
    }

    private void checkTyping() {
        if (currentTypeCard == null) return;
        String user = typeInput.getText().trim();
        if (user.equalsIgnoreCase(currentTypeCard.getVietnameseMeaning().trim())) {
            typeResultLabel.setText("Chính xác!");
            typeResultLabel.setForeground(SUCCESS_COLOR);
        } else {
            typeResultLabel.setText("Sai! Đáp án: " + currentTypeCard.getVietnameseMeaning());
            typeResultLabel.setForeground(DANGER_COLOR);
        }
        typeInput.setEditable(false);
        typeCheckBtn.setEnabled(false);
    }

    private void showMessage(String msg, int type) {
        JOptionPane.showMessageDialog(this, msg, "Thông báo", type);
    }
}