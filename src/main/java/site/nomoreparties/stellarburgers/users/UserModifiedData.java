package site.nomoreparties.stellarburgers.users;

import site.nomoreparties.stellarburgers.users.User;

public class UserModifiedData {
    private boolean success;
    private User user;

    public boolean isSuccess() {
        return success;
    }

    public User getUser() {
        return user;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setUser(User user) {
        this.user = user;
    }
}