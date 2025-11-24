package Model;
import java.util.Objects;

public class FlashCard {
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
        return  "Tieng Anh: " +" " +  englishWord +
                "\nTieng Viet: " +" "+ vietnameseMeaning +
                "\n " ;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlashCard flashCard = (FlashCard) o;
        // Thẻ được coi là giống nhau nếu từ Tiếng Anh và Nghĩa Tiếng Việt giống nhau
        return Objects.equals(englishWord, flashCard.englishWord) &&
                Objects.equals(vietnameseMeaning, flashCard.vietnameseMeaning);
    }

    // Lưu ý: Mặc dù không bắt buộc cho remove(), nhưng luôn nên ghi đè cả hashCode()
    // khi ghi đè equals(), đặc biệt nếu sử dụng trong HashMaps/HashSets.
    @Override
    public int hashCode() {
        return Objects.hash(englishWord, vietnameseMeaning);
    }
}
