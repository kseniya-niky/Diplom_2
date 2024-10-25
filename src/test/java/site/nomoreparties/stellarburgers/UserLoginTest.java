package site.nomoreparties.stellarburgers;

import io.restassured.response.ValidatableResponse;
import org.junit.Test;

public class UserLoginTest {
    private String accessToken, refreshToken;
    private UserClient userClient = new UserClient();
    private UserLogin userLogin;

    // @After
    //public void logoutUser() {
    //    if(!refreshToken.isEmpty()) {
    //        client.logoutUser(accessToken, refreshToken);
    //    }
    //}

    @Test
    public void loginDefaultUser() {
        userLogin = new UserLogin(Constants.DEFAULT_EMAIL, Constants.DEFAULT_PASSWORD);
        ValidatableResponse response = userClient.loginUser(userLogin);
        UserLoginInfo userLoginInfo = userClient.getResponseAboutLogin(response);

        accessToken = userLoginInfo.getAccessToken();
        refreshToken = userLoginInfo.getRefreshToken();

        userClient.checkLoginDefaultUser(userLoginInfo);
    }
}