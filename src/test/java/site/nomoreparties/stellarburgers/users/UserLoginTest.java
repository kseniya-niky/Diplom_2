package site.nomoreparties.stellarburgers.users;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import site.nomoreparties.stellarburgers.Constants;
import site.nomoreparties.stellarburgers.users.UserChecks;
import site.nomoreparties.stellarburgers.users.UserClient;
import site.nomoreparties.stellarburgers.users.UserLogin;
import site.nomoreparties.stellarburgers.users.UserLoginInfo;

public class UserLoginTest {
    private String accessToken, refreshToken;
    private UserClient userClient = new UserClient();
    private UserChecks userChecks = new UserChecks();

    @After
    public void logoutUser() {
        if(refreshToken != null) {
            if (!refreshToken.isBlank()) {
                ValidatableResponse response = userClient.logoutUser(accessToken, refreshToken);
                userChecks.checkLogoutUser(response);
            }
        }
    }

    @Test
    @DisplayName("Авторизация существующего пользователя")
    public void loginDefaultUser() {
        UserLogin userLogin = new UserLogin(Constants.DEFAULT_EMAIL, Constants.DEFAULT_PASSWORD);
        ValidatableResponse response = userClient.loginUser(userLogin);
        UserLoginInfo userLoginInfo = userClient.getResponseAboutLogin(response);

        userChecks.checkLoginDefaultUser(userLoginInfo);

        accessToken = userLoginInfo.getAccessToken();
        refreshToken = userLoginInfo.getRefreshToken();
    }
}