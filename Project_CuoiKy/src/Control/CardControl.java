package Control;

import Model.FlashCard;
import Model.FlashCardManager;

import java.util.List;

public class CardControl {
    private FlashCardManager model;

    public CardControl(FlashCardManager model) {
        this.model = model;
    }

    public void CardController(FlashCardManager model){
        this.model = model;
    }
    public boolean addCard(String english, String vietnamese){
        String engligh = english.trim();
        String vietnames = vietnamese.trim();
        if(!english.isEmpty() && !vietnames.isEmpty()){
            model.addCards(english, vietnames);
            return true;
        }
        return false;
    }
    public boolean removeCard(String english, String vietnamese){
        String engligh = english.trim();
        String vietnames = vietnamese.trim();
        if(!english.isEmpty() && !vietnames.isEmpty()){
            return model.removeCards(engligh, vietnames);
        }
        return false;
    }
    public String getAllCardsInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- DANH SÁCH THẺ TỪ HIỆN TẠI (")
                .append(model.getCardsCount()).append(") ---\n");

        for (FlashCard card : model.getAllCards()) {
            sb.append(card.toString()).append("\n");
        }
        return sb.toString();
    }
    public FlashCard getNextRandomCard(){
        return model.getRadomCards();
    }
}
