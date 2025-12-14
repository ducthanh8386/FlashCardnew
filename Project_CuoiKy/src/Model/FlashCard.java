

package Model;

import java.io.Serializable;
import java.util.Objects;


public class FlashCard implements Serializable {

    private static final long serialVersionUID = 1L;

    private String englishWord;
    private String vietnameseMeaning;

    public FlashCard(String englishWord, String vietnameseMeaning) {
        this.englishWord = englishWord;
        this.vietnameseMeaning = vietnameseMeaning;
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
        return  "Tiếng Anh : " +" " +  englishWord +
                "\nTiếng Việt: " +" "+ vietnameseMeaning +
                "\n " ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlashCard flashCard = (FlashCard) o;
        return Objects.equals(englishWord.toLowerCase(), flashCard.englishWord.toLowerCase()) &&
                Objects.equals(vietnameseMeaning.toLowerCase(), flashCard.vietnameseMeaning.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(englishWord.toLowerCase(), vietnameseMeaning.toLowerCase());
    }
}