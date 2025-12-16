package Control;

import Model.FlashCard;
import Model.FlashCardManager;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

public class CardControl {
    private FlashCardManager currentModel;

    private static final String REGEX_ENGLISH = "[a-zA-Z \\-\\(\\)' ]+";
    private static final String REGEX_VIETNAMESE = "^[A-Za-zÀ-ỹ \\s]+$";

    public CardControl(FlashCardManager model) {
        this.currentModel = model;
    }

    public boolean isValidFormat(String english, String vietnamese) {
        if (english == null || vietnamese == null) return false;
        return english.trim().matches(REGEX_ENGLISH) && vietnamese.trim().matches(REGEX_VIETNAMESE);
    }

    public List<FlashCard> getAllCards() {
        return currentModel.getAllCards();
    }

    public boolean addCard(String english, String vietnamese) {
        String en = english.trim();
        String vn = vietnamese.trim();

        if(en.isEmpty() || vn.isEmpty()) return false;

        if (!isValidFormat(en, vn)) return false;

        return currentModel.addCards(en, vn);
    }

    // SỬA THẺ
    public boolean updateCard(int id, String english, String vietnamese) {
        String en = english.trim();
        String vn = vietnamese.trim();

        if(en.isEmpty() || vn.isEmpty()) return false;
        if (!isValidFormat(en, vn)) return false;

        return currentModel.updateCards(id, en, vn);
    }


    public boolean removeCard(int id) {
        return currentModel.deleteCard(id);
    }


    public List<FlashCard> searchCards(String keyword) {
        return currentModel.searchCards(keyword);
    }

    public boolean setLearned(int id, boolean status) {
        return currentModel.toggleLearnedStatus(id, status);
    }

    public String getStats() {
        return currentModel.getStatistics();
    }

    public List<FlashCard> getQuizData() {
        List<FlashCard> quizSet = new ArrayList<>();
        FlashCard correctAnswer = currentModel.getRandomCard();
        if (correctAnswer == null) return null;
        quizSet.add(correctAnswer);

        List<FlashCard> allCards = currentModel.getAllCards();
        Collections.shuffle(allCards);

        int count = 0;
        for (FlashCard card : allCards) {
            if (card.getId() != correctAnswer.getId()) {
                quizSet.add(card);
                count++;
            }
            if (count == 3) break;
        }
        return quizSet;
    }

    public FlashCard getRandomCard() {
        return currentModel.getRandomCard();
    }
    public int importFromFile(java.io.File file) {
        int successCount = 0;

        try (java.io.BufferedReader br = new java.io.BufferedReader(
                new java.io.InputStreamReader(new java.io.FileInputStream(file), java.nio.charset.StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                // Bỏ qua dòng trống
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|");

                if (parts.length >= 2) {
                    String en = parts[0].trim();
                    String vn = parts[1].trim();

                    // Kiểm tra hợp lệ và thêm vào DB
                    // (Tái sử dụng hàm addCard đã có logic regex và check trùng)
                    if (isValidFormat(en, vn)) {
                        boolean added = currentModel.addCards(en, vn);
                        if (added) {
                            successCount++;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Trả về -1 nếu lỗi file
        }

        return successCount;
    }
}