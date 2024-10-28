package site.nomoreparties.stellarburgers.users;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import site.nomoreparties.stellarburgers.users.NewUser;
import site.nomoreparties.stellarburgers.users.NewUserRegistrationInfo;
import site.nomoreparties.stellarburgers.users.UserChecks;
import site.nomoreparties.stellarburgers.users.UserClient;

public class UserCreateTest {
    private String accessToken;
    private UserClient userClient = new UserClient();
    private UserChecks userChecks = new UserChecks();
    private NewUser newUser;

    @After
    public void deleteUser() {
        if(accessToken != null) {
            if (!accessToken.isBlank()) {
                ValidatableResponse response = userClient.deleteUser(accessToken);
                userChecks.checkDeletedUser(response);
            }
        }
    }

    @Test
    @DisplayName("Создание нового пользователя")
    public void createNewUser() {
        newUser = NewUser.random();
        ValidatableResponse response = userClient.createNewUser(newUser);
        NewUserRegistrationInfo userRegistrationInfo = userClient.getResponseAboutNewUser(response);
        accessToken = userRegistrationInfo.getAccessToken();
        userChecks.checkCreatedUser(userRegistrationInfo, newUser.getEmail(), newUser.getName());
    }

    @Test
    @DisplayName("Создание идентичного пользователя")
    public void createDuplicateUser() {
        newUser = NewUser.random();
        ValidatableResponse response = userClient.createNewUser(newUser);
        NewUserRegistrationInfo userRegistrationInfo = userClient.getResponseAboutNewUser(response);
        accessToken = userRegistrationInfo.getAccessToken();

        ValidatableResponse responseDuplicate = userClient.createNewUser(newUser);
        userChecks.checkDuplicateUser(responseDuplicate);
    }
}