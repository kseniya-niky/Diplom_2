package site.nomoreparties.stellarburgers.users;

import io.qameta.allure.junit4.DisplayName;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import site.nomoreparties.stellarburgers.Constants;
import site.nomoreparties.stellarburgers.users.NewUser;
import site.nomoreparties.stellarburgers.users.UserChecks;
import site.nomoreparties.stellarburgers.users.UserClient;

@RunWith(Parameterized.class)
public class UserCreateParamWithoutDataTest {
    private String email, password, name;
    private UserClient userClient = new UserClient();
    private UserChecks userChecks = new UserChecks();
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
    @DisplayName("Создание пользователя без данных")
    public void createNewUserWithoutData() {
        newUser = new NewUser(email, password, name);
        ValidatableResponse response = userClient.createNewUser(newUser);
        userChecks.checkCreatedUserWithoutParameter(response);
    }
}