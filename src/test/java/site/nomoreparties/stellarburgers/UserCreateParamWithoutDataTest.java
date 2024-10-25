package site.nomoreparties.stellarburgers;

import io.qameta.allure.junit4.DisplayName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

@RunWith(Parameterized.class)
public class UserCreateParamWithoutDataTest {
    private String email, password, name;
    private UserClient userClient = new UserClient();
    private NewUser newUser;

    public UserCreateParamWithoutDataTest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Parameterized.Parameters
    public static final Object[][] getRegistrationInformation() {
        return new Object[][] {
                {"", Constants.DEFAULT_PASSWORD, Constants.DEFAULT_NAME},
                {null, Constants.DEFAULT_PASSWORD, Constants.DEFAULT_NAME},
                {Constants.DEFAULT_EMAIL, "", Constants.DEFAULT_NAME},
                {Constants.DEFAULT_EMAIL, null, Constants.DEFAULT_NAME},
                {Constants.DEFAULT_EMAIL, Constants.DEFAULT_PASSWORD, ""},
                {Constants.DEFAULT_EMAIL, Constants.DEFAULT_PASSWORD, null}
        };
    }

    @Test
    @DisplayName("")
    public void createNewUserWithoutData() {
        newUser = new NewUser(email, password, name);
        ValidatableResponse response = userClient.createNewUser(newUser);
        userClient.checkCreatedUserWithoutParameter(response);
    }
}