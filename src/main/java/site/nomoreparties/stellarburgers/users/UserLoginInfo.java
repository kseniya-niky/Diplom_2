package site.nomoreparties.stellarburgers.users;

public class UserLoginInfo {
    private boolean success;
    private String accessToken;
    private String refreshToken;
    private User user;

    public boolean isSuccess() {
        return success;
    }

    public User getUser() {
        return user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}