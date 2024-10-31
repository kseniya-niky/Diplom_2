package site.nomoreparties.stellarburgers.users;

import io.qameta.allure.junit4.DisplayName;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import io.restassured.response.ValidatableResponse;
import site.nomoreparties.stellarburgers.Constants;

@RunWith(Parameterized.class)
public class UserChangeDataWithAuthParamTest {
    private String email, password, name;
    private static NewUserRegistrationInfo userRegistrationInfo;
    private static UserClient userClient = new UserClient();
    private static UserChecks userChecks = new UserChecks();

    public UserChangeDataWithAuthParamTest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Parameterized.Parameters
    public static final Object[][] getChangeInfo() {
        return new Object[][] {
                {Constants.NEW_EMAIL, Constants.NEW_PASSWORD, "BG -2"},
                {Constants.NEW_EMAIL, "%test458(&^", "BG -2"},
                {"lms@lms.com", "%test458(&^", "BG -2"},
                {"45_7lms@lms.com", "%test458(&^", "BG -2"},
                {"45_7lms@lms.com", "%test458(&^", "BG +570jdsgfuewy"},
                {"45_7lms@lms.com", "yes&53no937!done", "BG +570jdsgfuewy"},
                {Constants.NEW_EMAIL, Constants.NEW_PASSWORD, Constants.NEW_NAME}
        };
    }

    @BeforeClass
    public static void createUserForChanges() {
        NewUser newUser = new NewUser(Constants.NEW_EMAIL, Constants.NEW_PASSWORD, Constants.NEW_NAME);
        ValidatableResponse response = userClient.createNewUser(newUser);
        userRegistrationInfo = userClient.getResponseAboutNewUser(response);
        userChecks.checkCreatedUser(userRegistrationInfo, newUser);
    }

    @Test
    @DisplayName("Изменение данных авторизованного пользователя")
    public void changeUserDataWithAuth() {
        UserNewData userNewData = new UserNewData(email, password, name);
        ValidatableResponse response = userClient.changeUserWithAuth(userNewData, userRegistrationInfo);
        UserModifiedData userModifiedData = userClient.getResponseAboutModifiedData(response);
        userChecks.checkModifiedDataWithAuth(userModifiedData, userNewData);
    }

    @AfterClass
    public static void deleteUser() {
        if(userRegistrationInfo.getAccessToken() != null) {
            if (!userRegistrationInfo.getAccessToken().isBlank()) {
                userClient.deleteUser(userRegistrationInfo);
            }
        }
    }
}