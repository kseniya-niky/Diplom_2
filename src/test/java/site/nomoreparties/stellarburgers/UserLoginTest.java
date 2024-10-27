package site.nomoreparties.stellarburgers;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

public class UserLoginTest {
    private String accessToken, refreshToken;
    private UserClient userClient = new UserClient();
    private UserChecks userChecks = new UserChecks();

    @After
    public void logoutUser() {
        if(!refreshToken.isBlank()) {
            ValidatableResponse response = userClient.logoutUser(accessToken, refreshToken);
            userChecks.checkLogoutUser(response);
        }
    }

    @Test
    public void loginDefaultUser() {
        UserLogin userLogin = new UserLogin(Constants.DEFAULT_EMAIL, Constants.DEFAULT_PASSWORD);
        ValidatableResponse response = userClient.loginUser(userLogin);
        UserLoginInfo userLoginInfo = userClient.getResponseAboutLogin(response);

        userChecks.checkLoginDefaultUser(userLoginInfo);

        accessToken = userLoginInfo.getAccessToken();
        refreshToken = userLoginInfo.getRefreshToken();
    }
}