package site.nomoreparties.stellarburgers.users;

import io.qameta.allure.Param;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import site.nomoreparties.stellarburgers.Constants;

import static io.qameta.allure.model.Parameter.Mode.HIDDEN;
import static java.net.HttpURLConnection.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class UserChecks {
    private UserClient userClient = new UserClient();

    @Step("Проверка, что пользователь успешно создан")
    public void checkCreatedUser(NewUserRegistrationInfo userInfo,
                                 @Param(mode=HIDDEN) String currentEmail,
                                 @Param(mode=HIDDEN) String currentName) {
        assertTrue(userInfo.isSuccess());
        assertEquals("Пользователь создан под другим email", currentEmail.toLowerCase(), userInfo.getUser().getEmail());
        assertEquals("Пользователь создан под другим name", currentName, userInfo.getUser().getName());
    }

    @Step("Проверка, что пользователь успешно удален")
    public void checkDeletedUser(ValidatableResponse response) {
        userClient.checkResponseHTTPStatus(response, HTTP_ACCEPTED);
        boolean isSuccess = Boolean.valueOf(userClient.getResponseDataByKey(response, "success"));
        assertTrue(isSuccess);
    }

    @Step("Проверка, что нельзя создать идентичного пользователя")
    public void checkDuplicateUser(ValidatableResponse response) {
        userClient.checkResponseHTTPStatus(response, HTTP_FORBIDDEN);
        boolean isSuccess = Boolean.valueOf(userClient.getResponseDataByKey(response, "success"));
        assertFalse(isSuccess);

        String messageResponse = userClient.getResponseDataByKey(response, "message");
        assertEquals("Пользователь создан еще раз","User already exists", messageResponse);
    }

    @Step("Проверка создания пользователя без параметров")
    public void checkCreatedUserWithoutParameter(ValidatableResponse response) {
        userClient.checkResponseHTTPStatus(response, HTTP_FORBIDDEN);
        boolean isSuccess = Boolean.valueOf(userClient.getResponseDataByKey(response, "success"));
        assertFalse(isSuccess);

        String messageResponse = userClient.getResponseDataByKey(response, "message");
        assertEquals("Пользователь создан без обязательного параметра",
                "Email, password and name are required fields", messageResponse);
    }

    @Step("Проверка входа в систему для существующего пользователя")
    public void checkLoginDefaultUser(UserLoginInfo loginInfo) {
        assertTrue(loginInfo.isSuccess());
        Assert.assertEquals("Пользователь авторизован под другим email",
                Constants.DEFAULT_EMAIL, loginInfo.getUser().getEmail());
        assertEquals("Пользователь авторизован под другим name",
                Constants.DEFAULT_NAME, loginInfo.getUser().getName());
    }

    @Step("Проверка входа в систему для несуществующего пользователя")
    public void checkLoginFakeUser(ValidatableResponse response) {
        userClient.checkResponseHTTPStatus(response, HTTP_UNAUTHORIZED);
        boolean isSuccess = Boolean.valueOf(userClient.getResponseDataByKey(response, "success"));
        assertFalse(isSuccess);

        String messageResponse = userClient.getResponseDataByKey(response, "message");
        assertEquals("Несуществующий пользователь авторизован в системе",
                "email or password are incorrect", messageResponse);
    }

    @Step("Проверка выхода пользователя из системы")
    public void checkLogoutUser(ValidatableResponse response) {
        userClient.checkResponseHTTPStatus(response, HTTP_OK);
        boolean isSuccess = Boolean.valueOf(userClient.getResponseDataByKey(response, "success"));
        assertTrue(isSuccess);

        String messageResponse = userClient.getResponseDataByKey(response, "message");
        assertEquals("Пользователь не вышел из системы", "Successful logout", messageResponse);
    }

    @Step("Проверка изменения данных для авторизованного пользователя")
    public void checkModifiedDataWithAuth(UserModifiedData modifiedData,
                                          @Param(mode=HIDDEN) String currentEmail,
                                          @Param(mode=HIDDEN) String currentName) {
        assertTrue(modifiedData.isSuccess());
        assertEquals("Данные изменены под другим email",
                currentEmail, modifiedData.getUser().getEmail());
        assertEquals("Данные изменены под другим name",
                currentName, modifiedData.getUser().getName());
    }

    @Step("Проверка изменения данных для неавторизованного пользователя")
    public void checkModifiedDataWithoutAuth(ValidatableResponse response) {
        userClient.checkResponseHTTPStatus(response, HTTP_UNAUTHORIZED);
        boolean isSuccess = Boolean.valueOf(userClient.getResponseDataByKey(response, "success"));
        assertFalse(isSuccess);

        String messageResponse = userClient.getResponseDataByKey(response, "message");
        assertEquals("Данные изменены без авторизации", "You should be authorised", messageResponse);
    }

    @Step("Проверка изменения на зарегистрированный email")
    public void checkModifiedDataSameEmail(ValidatableResponse response) {
        userClient.checkResponseHTTPStatus(response, HTTP_FORBIDDEN);
        boolean isSuccess = Boolean.valueOf(userClient.getResponseDataByKey(response, "success"));
        assertFalse(isSuccess);

        String messageResponse = userClient.getResponseDataByKey(response, "message");
        assertEquals("Данные изменены на существующий email",
                "User with such email already exists", messageResponse);
    }
}