package site.nomoreparties.stellarburgers;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

public class UserChangeDataTest {
    private UserClient userClient = new UserClient();
    private String accessTokenCurrentUser;

    @Test
    @DisplayName("")
    public void changeUserDataWithAuth() {
        NewUser newUser = new NewUser(Constants.NEW_EMAIL, Constants.NEW_PASSWORD, Constants.NEW_NAME);
        ValidatableResponse responseNewUser = userClient.createNewUser(newUser);
        NewUserRegistrationInfo userRegistrationInfo = userClient.getResponseAboutNewUser(responseNewUser);
        userClient.checkCreatedUser(userRegistrationInfo);

        accessTokenCurrentUser = userRegistrationInfo.getAccessToken();

        UserNewData newDataUser = new UserNewData(Constants.DEFAULT_EMAIL, Constants.NEW_PASSWORD, Constants.NEW_NAME);
        ValidatableResponse responseNewDataUser = userClient.changeUserWithAuth(newDataUser, accessTokenCurrentUser);
        userClient.checkModifiedDataSameEmail(responseNewDataUser);
    }

    @Test
    @DisplayName("")
    public void changeUserDataWithoutAuth() {
        UserNewData newDataUser = new UserNewData(Constants.DEFAULT_EMAIL, Constants.NEW_PASSWORD, Constants.NEW_NAME);
        ValidatableResponse responseNewDataUser = userClient.changeUserWithoutAuth(newDataUser);
        userClient.checkModifiedDataSameEmail(responseNewDataUser);
    }

    @After
    public void deleteUser() {
        if(!accessTokenCurrentUser.isEmpty()) {
            userClient.deleteUser(accessTokenCurrentUser);
        }
    }
}