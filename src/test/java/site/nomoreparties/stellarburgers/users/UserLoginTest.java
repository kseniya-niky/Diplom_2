package site.nomoreparties.stellarburgers.users;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserLoginTest {
    private UserClient userClient = new UserClient();
    private UserChecks userChecks = new UserChecks();
    private UserLoginInfo userLoginInfo;
    private NewUser newUser;
    private NewUserRegistrationInfo userRegistrationInfo = new NewUserRegistrationInfo();

    @Before
    public void createUser() {
        newUser = NewUser.random();
        ValidatableResponse response = userClient.createNewUser(newUser);
        userRegistrationInfo = userClient.getResponseAboutNewUser(response);
        userChecks.checkCreatedUser(userRegistrationInfo, newUser);
    }

    @After
    public void deleteUser() {
        if(userRegistrationInfo.getAccessToken() != null) {
            if (!userRegistrationInfo.getAccessToken().isBlank()) {
                userClient.deleteUser(userRegistrationInfo);
            }
        }
    }

    @Test
    @DisplayName("Авторизация существующего пользователя")
    public void loginDefaultUser() {
        UserLogin userLogin = new UserLogin(newUser.getEmail(), newUser.getPassword());
        ValidatableResponse response = userClient.loginUser(userLogin);
        userLoginInfo = userClient.getResponseAboutLogin(response);
        userChecks.checkLoginDefaultUser(userLoginInfo, newUser);
    }
}