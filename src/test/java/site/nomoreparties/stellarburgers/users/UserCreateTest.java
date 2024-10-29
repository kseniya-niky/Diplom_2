package site.nomoreparties.stellarburgers.users;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

public class UserCreateTest {
    private NewUserRegistrationInfo userRegistrationInfo;
    private UserClient userClient = new UserClient();
    private UserChecks userChecks = new UserChecks();
    private NewUser newUser;

    @After
    public void deleteUser() {
        if(userRegistrationInfo.getAccessToken() != null) {
            if (!userRegistrationInfo.getAccessToken().isBlank()) {
                ValidatableResponse response = userClient.deleteUser(userRegistrationInfo);
                userChecks.checkDeletedUser(response);
            }
        }
    }

    @Test
    @DisplayName("Создание нового пользователя")
    public void createNewUser() {
        newUser = NewUser.random();
        ValidatableResponse response = userClient.createNewUser(newUser);
        userRegistrationInfo = userClient.getResponseAboutNewUser(response);
        userChecks.checkCreatedUser(userRegistrationInfo, newUser.getEmail(), newUser.getName());
    }

    @Test
    @DisplayName("Создание идентичного пользователя")
    public void createDuplicateUser() {
        newUser = NewUser.random();
        ValidatableResponse response = userClient.createNewUser(newUser);
        userRegistrationInfo = userClient.getResponseAboutNewUser(response);

        ValidatableResponse responseDuplicate = userClient.createNewUser(newUser);
        userChecks.checkDuplicateUser(responseDuplicate);
    }
}