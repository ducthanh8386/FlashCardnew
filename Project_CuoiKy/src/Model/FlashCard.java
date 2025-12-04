//package Model;
//import java.util.Objects;
//import java.io.Serializable;
//
//
////public class FlashCard {
////    private String englishWord;
////    private String vietnameseMeaning;
////    public FlashCard(String englishWord, String vietnameseMeaning) {
////        this.englishWord = englishWord;
////        this.vietnameseMeaning = vietnameseMeaning;
////
////    }
//
//public class FlashCard implements Serializable {
//    private static final long serialVersionUID = 1L;
//
//    private String englishWord;
//    private String vietnameseMeaning;
//
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        FlashCard flashCard = (FlashCard) o;
//        return Objects.equals(englishWord.toLowerCase(), flashCard.englishWord.toLowerCase()) &&
//                Objects.equals(vietnameseMeaning.toLowerCase(), flashCard.vietnameseMeaning.toLowerCase());
//    }
//
//    @Override
//    public int hashCode() {
//        // Đồng bộ với equals()
//        return Objects.hash(englishWord.toLowerCase(), vietnameseMeaning.toLowerCase());
//    }
//
//
//
//    // lay tu tieng viet , tieng anh
//
//    public String getEnglishWord() {
//        return englishWord;
//    }
//
//    public String getVietnameseMeaning() {
//        return vietnameseMeaning;
//    }
//
//    public void setEnglishWord(String englishWord) {
//        this.englishWord = englishWord;
//    }
//
//    public void setVietnameseMeaning(String vietnameseMeaning) {
//        this.vietnameseMeaning = vietnameseMeaning;
//    }
//
//    @Override
//    public String toString() {
//        return  "Tiếng Anh : " +" " +  englishWord +
//                "\nTiếng Việt: " +" "+ vietnameseMeaning +
//                "\n " ;
//    }
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        FlashCard flashCard = (FlashCard) o;
//        // Thẻ được coi là giống nhau nếu từ Tiếng Anh và Nghĩa Tiếng Việt giống nhau
//        return Objects.equals(englishWord, flashCard.englishWord) &&
//                Objects.equals(vietnameseMeaning, flashCard.vietnameseMeaning);
//    }
//
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(englishWord, vietnameseMeaning);
//    }
//}


package Model;

import java.io.Serializable;
import java.util.Objects;


public class FlashCard implements Serializable {

    private static final long serialVersionUID = 1L;

    private String englishWord;
    private String vietnameseMeaning;

    // ✅ Giữ lại Constructor
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
        // So sánh không phân biệt chữ hoa/thường (toLowerCase())
        return Objects.equals(englishWord.toLowerCase(), flashCard.englishWord.toLowerCase()) &&
                Objects.equals(vietnameseMeaning.toLowerCase(), flashCard.vietnameseMeaning.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(englishWord.toLowerCase(), vietnameseMeaning.toLowerCase());
    }
}