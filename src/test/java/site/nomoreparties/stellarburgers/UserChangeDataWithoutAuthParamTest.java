package site.nomoreparties.stellarburgers;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class UserChangeDataWithoutAuthParamTest {
    private String email, password, name;
    private static UserClient userClient = new UserClient();
    private UserChecks userChecks = new UserChecks();

    public UserChangeDataWithoutAuthParamTest(String email, String password, String name) {
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

    @Test
    @DisplayName("")
    public void changeUserDataWithoutAuth() {
        UserNewData newDataUser = new UserNewData(email, password, name);
        ValidatableResponse response = userClient.changeUserWithoutAuth(newDataUser);
        userChecks.checkModiedDataWithoutAuth(response);
    }
}