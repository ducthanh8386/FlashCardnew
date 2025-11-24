package View;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Control.CardControl;
import Model.FlashCard;


public class FlashCardAppView extends JFrame {
    private final CardControl controller;
    private void initializeComponents(){
        // nhaapk lieu
        englishInputField = new JTextField(20);
        vietnameseInputField = new JTextField(20);
        addButton = new JButton("Add");
        removeButton = new JButton("Remove");
        displayArea = new JTextArea(10,40);
        displayArea.setEditable(false);
        // Luyen tap
        cardQuestionLabel = new JLabel("Bat dau luyen tap", SwingConstants.CENTER);
        cardQuestionLabel.setFont(new Font("Serif", Font.BOLD, 24));
        cardAnswerLabel = new JLabel("Nhấn 'Hiện Nghĩa'!", SwingConstants.CENTER);
        cardAnswerLabel.setForeground(Color.BLUE);
        cardAnswerLabel.setVisible(false); // Ẩn nghĩa lúc đầu
        cardAnswerLabel.setFont(new Font("Serif", Font.PLAIN, 18));
        nextCardButton = new JButton("Thẻ Tiếp Theo");
        showAnswerButton = new JButton("Hiện Nghĩa");

    }
    private void setupLayout() {

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));


        // ====== CỘT BÊN TRÁI ======
        JPanel leftColumnPanel = new JPanel();
        leftColumnPanel.setLayout(new BoxLayout(leftColumnPanel, BoxLayout.Y_AXIS));


        // --- Panel Nhập Thẻ ---
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Nhập Thẻ Mới"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.setMaximumSize(new Dimension(350, 180));



        // Label + TextField Tiếng Anh
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Tiếng Anh:"), gbc);

        gbc.gridx = 1;
        englishInputField.setPreferredSize(new Dimension(200, 30));
        inputPanel.add(englishInputField, gbc);


        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Tiếng Việt:"), gbc);

        gbc.gridx = 1;
        vietnameseInputField.setPreferredSize(new Dimension(200, 30));
        inputPanel.add(vietnameseInputField, gbc);


        // --- Panel nút ---
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JPanel buttonInputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));

        addButton.setPreferredSize(new Dimension(100, 35));
        removeButton.setPreferredSize(new Dimension(100, 35));

        buttonInputPanel.add(addButton);
        buttonInputPanel.add(removeButton);

        inputPanel.add(buttonInputPanel, gbc);


        // --- Panel danh sách thẻ ---
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh Sách Thẻ"));
        scrollPane.setPreferredSize(new Dimension(350, 300));


        // Thêm vào cột trái
        leftColumnPanel.add(inputPanel);
        leftColumnPanel.add(Box.createVerticalStrut(10));
        leftColumnPanel.add(scrollPane);

        JPanel practicePanel = new JPanel(new BorderLayout(10, 10));
        practicePanel.setBorder(BorderFactory.createTitledBorder("Luyện Tập Flash Card"));


        JPanel cardDisplayPanel = new JPanel(new GridLayout(2, 1));
        cardDisplayPanel.add(cardQuestionLabel);
        cardDisplayPanel.add(cardAnswerLabel);

        practicePanel.add(cardDisplayPanel, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        showAnswerButton.setPreferredSize(new Dimension(150, 40));
        nextCardButton.setPreferredSize(new Dimension(150, 40));

        buttonPanel.add(showAnswerButton);
        buttonPanel.add(nextCardButton);

        practicePanel.add(buttonPanel, BorderLayout.SOUTH);



        // Thêm 2 cột vào giao diện chính
        mainPanel.add(leftColumnPanel);
        mainPanel.add(practicePanel);

        add(mainPanel, BorderLayout.CENTER);
    }



    //    private void setupLayout(){
//        // Tạo Panel chính (CENTER) với bố cục hai cột
//        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));
//
//        JPanel leftColumnPanel = new JPanel();
//        leftColumnPanel.setLayout(new BoxLayout(leftColumnPanel, BoxLayout.Y_AXIS));
//
//        // Nhập thẻ
//        JPanel inputPanel = new JPanel();
//        inputPanel.setLayout(new GridLayout());
//        inputPanel.setBorder(BorderFactory.createTitledBorder("Nhập Thẻ Mới"));
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(5, 5, 5, 5);
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//
//        inputPanel.add(new JLabel("Tiếng Anh:"));
//        inputPanel.add(englishInputField);
//        inputPanel.add(new JLabel("Tiếng Việt:"));
//        inputPanel.add(vietnameseInputField);
//
//        // Panel cho các nút Add/Remove
//        JPanel buttonInputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
//        buttonInputPanel.add(addButton);
//        buttonInputPanel.add(removeButton);
//        inputPanel.add(addButton);
//        inputPanel.add(removeButton);
//
//        // Panel Hiển thị danh sách (SOUTH)
//        JScrollPane scrollPane = new JScrollPane(displayArea);
//        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh Sách Thẻ"));
//
//        // Thêm các thành phần vào cột bên trái
//        leftColumnPanel.add(inputPanel);
//        leftColumnPanel.add(Box.createVerticalStrut(10));
//        leftColumnPanel.add(scrollPane);
//
//
//        // luyen tap
//        JPanel practicePanel = new JPanel(new BorderLayout(10, 10));
//        practicePanel.setBorder(BorderFactory.createTitledBorder("Luyện Tập Flash Card"));
//
//        // Panel hiển thị Thẻ
//        JPanel cardDisplayPanel = new JPanel(new GridLayout(2, 1));
//        cardDisplayPanel.add(cardQuestionLabel);
//        cardDisplayPanel.add(cardAnswerLabel);
//        practicePanel.add(cardDisplayPanel, BorderLayout.CENTER);
//
//        // Panel Nút bấm
//        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 200, 10));
//        buttonPanel.add(showAnswerButton);
//        buttonPanel.add(nextCardButton);
//        practicePanel.add(buttonPanel, BorderLayout.SOUTH);
//
//
//
//        // Thêm hai cột vào panel chính
//        mainPanel.add(leftColumnPanel);
//        mainPanel.add(practicePanel);
//        add(mainPanel, BorderLayout.CENTER);
//    }
    private void updateCardListDisPlay(){
        displayArea.setText(controller.getAllCardsInfo());
    }
    public void resetPracticeCard(){FlashCard card = controller.getNextRandomCard();
        if (card != null) {
            cardQuestionLabel.setText(card.getEnglishWord());
            cardQuestionLabel.setToolTipText("Nghĩa: " + card.getVietnameseMeaning()); // Thêm tooltip
            cardAnswerLabel.setText(card.getVietnameseMeaning());
            cardAnswerLabel.setVisible(false);
        } else {
            cardQuestionLabel.setText("Chưa có thẻ nào. Vui lòng thêm thẻ!");
            cardAnswerLabel.setText("");
        }}

    // Thành phần cho phần Nhập liệu
    private JTextField englishInputField;
    private JTextField vietnameseInputField;
    private JTextArea displayArea;
    private JButton addButton;
    private JButton removeButton;
    private JButton nextCardButton;

    // Thành phần cho phần Luyện tập
    private JLabel cardQuestionLabel; // Hiển thị từ (câu hỏi)
    private JLabel cardAnswerLabel;   // Hiển thị nghĩa (câu trả lời)

    private JButton showAnswerButton;

    public FlashCardAppView(CardControl controller){
        this.controller = controller;
        setTitle("Flash Card App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,800);
        setLayout(new BorderLayout(10,10));
        setVisible(true);
        initializeComponents();
        setupLayout();
        updateCardListDisPlay();
        resetPracticeCard();
    }
    public String getEnglishInput() { return englishInputField.getText(); }
    public String getAddCardButtonText() { return addButton.getText(); }
    public JButton getRemoveButton() { return removeButton; }
    public String getVietnameseInput() { return vietnameseInputField.getText(); }
    public void clearInputFields() { englishInputField.setText(""); vietnameseInputField.setText(""); }
    public JButton getAddButton() { return addButton; }
    public JButton getNextCardButton() { return nextCardButton; }
    public JButton getShowAnswerButton() { return showAnswerButton; }
    public JLabel getCardAnswerLabel() { return cardAnswerLabel; }

    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
    public void updateCardListDisplay(){
        displayArea.setText(controller.getAllCardsInfo());
    }



}
