package site.nomoreparties.stellarburgers;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

public class UserCreateTest {
    private String accessToken;
    private UserClient userClient = new UserClient();
    private NewUser newUser;

    @After
    public void deleteUser() {
        userClient.deleteUser(accessToken);
    }

    @Test
    public void createNewUser() {
        newUser = NewUser.random();
        ValidatableResponse response = userClient.createNewUser(newUser);
        NewUserRegistrationInfo userRegistrationInfo = userClient.getResponseAboutNewUser(response);
        accessToken = userRegistrationInfo.getAccessToken();

        userClient.checkCreatedUser(userRegistrationInfo);
    }

    @Test
    public void createDuplicateUser() {
        newUser = NewUser.random();
        ValidatableResponse response = userClient.createNewUser(newUser);
        NewUserRegistrationInfo userRegistrationInfo = userClient.getResponseAboutNewUser(response);
        accessToken = userRegistrationInfo.getAccessToken();

        ValidatableResponse responseDuplicate = userClient.createNewUser(newUser);
        userClient.checkDuplicateUser(responseDuplicate);
    }
}