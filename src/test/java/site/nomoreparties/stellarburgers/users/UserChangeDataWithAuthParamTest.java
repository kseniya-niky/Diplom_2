package site.nomoreparties.stellarburgers.users;

import io.qameta.allure.junit4.DisplayName;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import io.restassured.response.ValidatableResponse;

@RunWith(Parameterized.class)
public class UserChangeDataWithAuthParamTest {
    private String email, password, name;
    private static NewUserRegistrationInfo userRegistrationInfo;
    private static UserClient userClient = new UserClient();
    private static UserChecks userChecks = new UserChecks();

    private static NewUser currentUser = NewUser.random();;

    public UserChangeDataWithAuthParamTest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Parameterized.Parameters
    public static final Object[][] getChangeInfo() {
        return new Object[][] {
                {currentUser.getEmail(), currentUser.getPassword(), "BG -2"},
                {currentUser.getEmail(), "%test458(&^", "BG -2"},
                {"lms@lms.com", "%test458(&^", "BG -2"},
                {"45_7lms@lms.com", "%test458(&^", "BG -2"},
                {"45_7lms@lms.com", "%test458(&^", "BG +570jdsgfuewy"},
                {"45_7lms@lms.com", "yes&53no937!done", "BG +570jdsgfuewy"},
                {currentUser.getEmail(), currentUser.getPassword(), currentUser.getName()}
        };
    }

    @BeforeClass
    public static void createUserForChanges() {
        ValidatableResponse response = userClient.createNewUser(currentUser);
        userRegistrationInfo = userClient.getResponseAboutNewUser(response);
        userChecks.checkCreatedUser(userRegistrationInfo, currentUser);
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