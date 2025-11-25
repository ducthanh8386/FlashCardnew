import Model.FlashCardManager;
import Model.UserManager;
import View.FlashCardAppView;
import Model.FlashCard;
import Control.CardControl;
import View.LoginView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

    public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserManager userManager = new UserManager();
            new LoginView(userManager);
        });
    }
}

//    public static void main(String[] args) {
//        FlashCardManager model = new FlashCardManager();
//        CardControl controller = new CardControl(model);
//        SwingUtilities.invokeLater(() -> {
//            FlashCardAppView view = new FlashCardAppView(controller);
//            view.getAddButton().addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    String english = view.getEnglishInput();
//                    String vietnamese = view.getVietnameseInput();
//
//                    if (controller.addCard(english, vietnamese)) {
//                        view.clearInputFields();
//                        view.updateCardListDisplay();
//                        view.showMessage("Đã thêm thẻ thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
//                    }
//                    else {
//                        view.showMessage("Lỗi: Vui lòng nhập đầy đủ cả Tiếng Anh và Tiếng Việt!",
//                                "Lỗi Nhập liệu", JOptionPane.ERROR_MESSAGE);
//                    }
//                }
//            });
//
//
//            // Xóa thẻ
//            view.getRemoveButton().addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    String english = view.getEnglishInput();
//                    String vietnamese = view.getVietnameseInput();
//
//                    boolean isRemoved = controller.removeCard(english, vietnamese);
//
//
//                    if (isRemoved) {
//                        view.clearInputFields();
//                        view.updateCardListDisplay();
//                        view.showMessage("Đã xóa thẻ thành công!", "Thành Công", JOptionPane.INFORMATION_MESSAGE);
//                    } else {
//                        if(english.trim().isEmpty() || vietnamese.trim().isEmpty()){
//                            view.showMessage("Lỗi: Hãy nhập đầy đủ cả Tiếng Anh và Tiếng Việt muốn xóa",
//                            "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
//                        }
//                        else {
//                            view.showMessage("Lỗi: Không tìm thấy thẻ cần xóa","Lỗi xóa thẻ",JOptionPane.ERROR_MESSAGE);
//                        }
//                    }
//                }
//            });
//
//            // hiện nghĩa
//            view.getShowAnswerButton().addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    view.getCardAnswerLabel().setVisible(true);
//                }
//            });
//
//
//            view.getNextCardButton().addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    view.resetPracticeCard();
//                }
//            });
//        });
//    }


