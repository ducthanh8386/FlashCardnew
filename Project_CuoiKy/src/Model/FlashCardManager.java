package Model;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class FlashCardManager {
    private List<FlashCard> cards;
    public FlashCardManager() {
        this.cards = new ArrayList<>();
    }

    public boolean addCards(String englishWord, String vietnameseMeaning) {
        String english = englishWord.trim();
        String vietnamese = vietnameseMeaning.trim();
        String regexTiengAnh = "[a-zA-Z ]+";
        if (!englishWord.matches(regexTiengAnh)) {
            return false;
        }
        String regexTiengViet= "^[A-Za-zÀ-ỹ\\s]+$";
        if (!vietnameseMeaning.matches(regexTiengViet)) {
            return false;
        }
        FlashCard newCard = new FlashCard(englishWord, vietnameseMeaning);
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
