package site.nomoreparties.stellarburgers;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

public class UserChangeDataTest {
    private UserClient userClient = new UserClient();
    private UserChecks userChecks = new UserChecks();
    private String accessTokenCurrentUser;

    @Test
    @DisplayName("")
    public void changeUserDataSameEmailWithAuth() {
        NewUser newUser = new NewUser(Constants.NEW_EMAIL, Constants.NEW_PASSWORD, Constants.NEW_NAME);
        ValidatableResponse responseNewUser = userClient.createNewUser(newUser);
        NewUserRegistrationInfo userRegistrationInfo = userClient.getResponseAboutNewUser(responseNewUser);
        userChecks.checkCreatedUser(userRegistrationInfo, newUser.getEmail(), newUser.getName());

        accessTokenCurrentUser = userRegistrationInfo.getAccessToken();

        UserNewData newDataUser = new UserNewData(Constants.DEFAULT_EMAIL, Constants.NEW_PASSWORD, Constants.NEW_NAME);
        ValidatableResponse responseNewDataUser = userClient.changeUserWithAuth(newDataUser, accessTokenCurrentUser);
        userChecks.checkModifiedDataSameEmail(responseNewDataUser);
    }

    @Test
    @DisplayName("")
    public void changeUserDataSameEmailWithoutAuth() {
        UserNewData newDataUser = new UserNewData(Constants.DEFAULT_EMAIL, Constants.NEW_PASSWORD, Constants.NEW_NAME);
        ValidatableResponse responseNewDataUser = userClient.changeUserWithoutAuth(newDataUser);
        userChecks.checkModifiedDataSameEmail(responseNewDataUser);
    }

    @After
    public void deleteUser() {
        if(accessTokenCurrentUser != null) {
            if(!accessTokenCurrentUser.isBlank()) {
            userClient.deleteUser(accessTokenCurrentUser);
            }
        }
    }
}