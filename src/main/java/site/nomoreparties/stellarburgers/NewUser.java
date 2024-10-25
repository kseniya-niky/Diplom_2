package site.nomoreparties.stellarburgers;

import java.util.Random;

public class NewUser {
    private String email;
    private String password;
    private String name;


    public NewUser(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    static NewUser random() {
        return new NewUser("Lucky_" + new Random().nextInt() + "@tst.ru",
                "L&%$h" + new Random().nextInt(),
                "UU" + new Random().nextInt());
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }
}