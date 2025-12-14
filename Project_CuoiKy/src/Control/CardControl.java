package Control;

import Model.FlashCard;
import Model.FlashCardManager;

import java.util.List;

public class CardControl {
    private FlashCardManager currentModel;

    public CardControl(FlashCardManager model) {
        this.currentModel = model;
    }

    public void setModel(FlashCardManager model) {
        this.currentModel = model;
    }

    public void CardController(FlashCardManager model){
        this.currentModel = model;
    }
    public boolean addCard(String english, String vietnamese){
        String engligh = english.trim();
        String vietnames = vietnamese.trim();
        if(!english.isEmpty() && !vietnames.isEmpty()){
            return currentModel.addCards(english, vietnames);

        }
        return false;
    }
    public boolean removeCard(String english, String vietnamese){
        String engligh = english.trim();
        String vietnames = vietnamese.trim();
        if(!english.isEmpty() && !vietnames.isEmpty()){
            return currentModel.removeCards(engligh, vietnames);
        }
        return false;
    }
    public String getAllCardsInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- DANH SÁCH THẺ TỪ HIỆN TẠI (")
                .append(currentModel.getCardsCount()).append(") --\n");

        for (FlashCard card : currentModel.getAllCards()) {
            sb.append(card.toString()).append("\n");
        }
        return sb.toString();
    }
    public FlashCard getNextRandomCard(){
        return currentModel.getRandomCard();
    }
}
