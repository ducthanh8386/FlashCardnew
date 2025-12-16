package Model;
import java.util.Objects;


public class FlashCard  {

    private String englishWord;
    private String vietnameseMeaning;
    private int id;
    private boolean isLearned;


    public FlashCard(int id ,String englishWord, String vietnameseMeaning,  boolean isLearned) {
        this.englishWord = englishWord;
        this.vietnameseMeaning = vietnameseMeaning;
        this.id = id;
        this.isLearned = isLearned;
    }

    public FlashCard(String vietnameseMeaning, String englishWord) {
        this.vietnameseMeaning = vietnameseMeaning;
        this.englishWord = englishWord;
        this.isLearned = false;
    }

    //getter setter


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isLearned() {
        return isLearned;
    }

    public void setLearned(boolean learned) {
        isLearned = learned;
    }

    // lay tu tieng viet , tieng anh
    public String getEnglishWord() {
        return englishWord;
    }

    public String getVietnameseMeaning() {
        return vietnameseMeaning;
    }

    public void setEnglishWord(String englishWord) {
        this.englishWord = englishWord;
    }

    public void setVietnameseMeaning(String vietnameseMeaning) {
        this.vietnameseMeaning = vietnameseMeaning;
    }

    @Override
    public String toString() {
        return englishWord + " - " + vietnameseMeaning + (isLearned ? " (Đã thuộc)" : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlashCard flashCard = (FlashCard) o;
        return id== flashCard.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
//    public String toFileString(){
//        return englishWord + "|" + vietnameseMeaning;
//    }
}