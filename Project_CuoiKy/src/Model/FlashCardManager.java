package Model;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class FlashCardManager {
    private List<FlashCard> cards;
    public FlashCardManager() {
        this.cards = new ArrayList<>();
    }

    public void addCards(String englishWord, String vietnameseMeaning) {
        String regexTiengAnh = "[a-zA-Z ]+";
        if (!englishWord.matches(regexTiengAnh)) {
            System.out.println("Lỗi: Từ tiếng Anh không hợp lệ. Đã hủy thêm.");
            return;
        }
        String regexTiengViet= "^[A-Za-zÀ-ỹ\s]+$";
        if (!vietnameseMeaning.matches(regexTiengViet)) {
            System.out.println("Lỗi: Từ tiếng việt không hợp lệ");
        }
        FlashCard newCard = new FlashCard(englishWord, vietnameseMeaning);
        cards.add(newCard);
        System.out.println("Đã thêm card thành công!");

    }
    public boolean removeCards(String englishWord, String vietnameseMeaning) {
        if (englishWord == null || englishWord.trim().isEmpty()
                || vietnameseMeaning == null || vietnameseMeaning.trim().isEmpty()){
            return false;
    }

        FlashCard cardToRemove = new FlashCard(englishWord, vietnameseMeaning);
        return cards.remove(cardToRemove);
    }
    public FlashCard getRadomCards(){
        if(cards.isEmpty()){
            return null;
        }
        int randomIndex = (int) (Math.random()*cards.size());
        return cards.get(randomIndex);
    }
    public List<FlashCard> getAllCards(){
        return cards;
    }
    public int getCardsCount(){
        return cards.size();
    }

}
