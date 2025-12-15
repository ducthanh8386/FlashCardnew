package Model;



public class User {
    private final int id;
    private final String username;
    private String password;
    private final FlashCardManager flashCardManager;

    public User(int id, String username, String password){
        this.id = id;
        this.username = username;
        this.password = password;

        this.flashCardManager = new FlashCardManager(id);
    }

    public int getId() { return id; } // Thêm getter cho ID

    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public FlashCardManager getFlashCardManager() {
        return flashCardManager;
    }

    public void setPassword(String password) { this.password = password; }
}