package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FlashCardManager implements Serializable {
    // Thêm ID này để đảm bảo tính nhất quán khi lưu/đọc file
    private static final long serialVersionUID = 1L;

    private List<FlashCard> cards;

    public FlashCardManager() {
        this.cards = new ArrayList<>();
    }

    public boolean addCards(String englishWord, String vietnameseMeaning) {
        String english = englishWord.trim();
        String vietnamese = vietnameseMeaning.trim();

        // Regex kiểm tra
        String regexTiengAnh = "[a-zA-Z ]+";
        if (!englishWord.matches(regexTiengAnh)) {
            return false;
        }
        String regexTiengViet= "^[A-Za-zÀ-ỹ\\s]+$";
        if (!vietnameseMeaning.matches(regexTiengViet)) {
            return false;
        }

        FlashCard newCard = new FlashCard(englishWord, vietnameseMeaning);
        if (cards.contains(newCard)) {
            return false; // Trả về false nếu thẻ đã có trong danh sách
        }
        cards.add(newCard);

        return true;
    }

    public boolean removeCards(String englishWord, String vietnameseMeaning) {
        if (englishWord == null || englishWord.trim().isEmpty()
                || vietnameseMeaning == null || vietnameseMeaning.trim().isEmpty()){
            return false;
        }

        FlashCard cardToRemove = new FlashCard(englishWord, vietnameseMeaning);
        return cards.remove(cardToRemove);
    }

    // Đã sửa tên hàm từ getRadomCards -> getRandomCard
    public FlashCard getRandomCard(){
        if(cards.isEmpty()){
            return null;
        }
        int randomIndex = (int) (Math.random() * cards.size());
        return cards.get(randomIndex);
    }

    public List<FlashCard> getAllCards(){
        return cards;
    }

    public int getCardsCount(){
        return cards.size();
    }
}