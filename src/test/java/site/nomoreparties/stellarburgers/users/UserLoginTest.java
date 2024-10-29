package site.nomoreparties.stellarburgers.users;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import site.nomoreparties.stellarburgers.Constants;

public class UserLoginTest {
    private UserClient userClient = new UserClient();
    private UserChecks userChecks = new UserChecks();
    private UserLoginInfo userLoginInfo;

    @After
    public void logoutUser() {
        if(userLoginInfo.getRefreshToken() != null) {
            if (!userLoginInfo.getRefreshToken().isBlank()) {
                ValidatableResponse response = userClient.logoutUser(userLoginInfo);
                userChecks.checkLogoutUser(response);
            }
        }
    }

    @Test
    @DisplayName("Авторизация существующего пользователя")
    public void loginDefaultUser() {
        UserLogin userLogin = new UserLogin(Constants.DEFAULT_EMAIL, Constants.DEFAULT_PASSWORD);
        ValidatableResponse response = userClient.loginUser(userLogin);
        userLoginInfo = userClient.getResponseAboutLogin(response);
        userChecks.checkLoginDefaultUser(userLoginInfo);
    }
}