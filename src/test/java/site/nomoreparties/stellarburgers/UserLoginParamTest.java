package site.nomoreparties.stellarburgers;

import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class UserLoginParamTest {
    private String email, password;
    private UserClient userClient = new UserClient();
    private UserChecks userChecks = new UserChecks();

    public UserLoginParamTest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Parameterized.Parameters
    public static final Object[][] getLoginInfo() {
        return new Object[][]{
                { Constants.DEFAULT_EMAIL, "saldf2903&^$" },
                { "llkgd@tst.doc", Constants.DEFAULT_PASSWORD },
                { "", Constants.DEFAULT_PASSWORD },
                { null, Constants.DEFAULT_PASSWORD },
                { Constants.DEFAULT_EMAIL, "" },
                { Constants.DEFAULT_EMAIL, null }
        };
    }

    @Test(timeout=10000)
    public void loginFakeUser() {
        UserLogin userLogin = new UserLogin(email, password);
        ValidatableResponse response = userClient.loginUser(userLogin);
        userChecks.checkLoginFakeUser(response);
    }
}