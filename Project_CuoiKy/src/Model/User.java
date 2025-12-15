package Model;

import java.io.Serializable;


public class User implements Serializable {
    private static final long serialVersionUID = 2L;
    private String username;
    private String password;

    private FlashCardManager flashCardManager;

    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.flashCardManager = new FlashCardManager();
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public FlashCardManager getFlashCardManager() {
        return flashCardManager;
    }

    public void setPassword(String password) { this.password = password; }
}


