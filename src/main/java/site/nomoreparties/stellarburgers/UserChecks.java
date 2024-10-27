package site.nomoreparties.stellarburgers;

import io.restassured.response.ValidatableResponse;

import static java.net.HttpURLConnection.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class UserChecks {
    private UserClient userClient = new UserClient();

    public void checkCreatedUser(NewUserRegistrationInfo userInfo, String currentEmail, String currentName) {
        assertTrue(userInfo.isSuccess());
        assertEquals("Пользователь создан под другим email", currentEmail.toLowerCase(), userInfo.getUser().getEmail());
        assertEquals("Пользователь создан под другим name", currentName, userInfo.getUser().getName());
    }

    public void checkDeletedUser(ValidatableResponse response) {
        userClient.checkResponseHTTPStatus(response, HTTP_ACCEPTED);
        boolean isSuccess = Boolean.valueOf(userClient.getResponseDataByKey(response, "success"));
        assertTrue(isSuccess);
    }

    public void checkDuplicateUser(ValidatableResponse response) {
        userClient.checkResponseHTTPStatus(response, HTTP_FORBIDDEN);
        boolean isSuccess = Boolean.valueOf(userClient.getResponseDataByKey(response, "success"));
        assertFalse(isSuccess);

        String messageResponse = userClient.getResponseDataByKey(response, "message");
        assertEquals("Пользователь создан еще раз","User already exists", messageResponse);
    }

    public void checkCreatedUserWithoutParameter(ValidatableResponse response) {
        userClient.checkResponseHTTPStatus(response, HTTP_FORBIDDEN);
        boolean isSuccess = Boolean.valueOf(userClient.getResponseDataByKey(response, "success"));
        assertFalse(isSuccess);

        String messageResponse = userClient.getResponseDataByKey(response, "message");
        assertEquals("Пользователь создан без обязательного параметра",
                "Email, password and name are required fields", messageResponse);
    }

    public void checkLoginDefaultUser(UserLoginInfo loginInfo) {
        assertTrue(loginInfo.isSuccess());
        assertEquals("Пользователь авторизован под другим email",
                Constants.DEFAULT_EMAIL, loginInfo.getUser().getEmail());
        assertEquals("Пользователь авторизован под другим name",
                Constants.DEFAULT_NAME, loginInfo.getUser().getName());
    }

    public void checkLoginFakeUser(ValidatableResponse response) {
        userClient.checkResponseHTTPStatus(response, HTTP_UNAUTHORIZED);
        boolean isSuccess = Boolean.valueOf(userClient.getResponseDataByKey(response, "success"));
        assertFalse(isSuccess);

        String messageResponse = userClient.getResponseDataByKey(response, "message");
        assertEquals("Несуществующий пользователь авторизован в системе",
                "email or password are incorrect", messageResponse);
    }

    public void checkLogoutUser(ValidatableResponse response) {
        userClient.checkResponseHTTPStatus(response, HTTP_OK);
        boolean isSuccess = Boolean.valueOf(userClient.getResponseDataByKey(response, "success"));
        assertTrue(isSuccess);

        String messageResponse = userClient.getResponseDataByKey(response, "message");
        assertEquals("Пользователь не вышел из системы", "Successful logout", messageResponse);
    }

    public void checkModifiedDataWithAuth(UserModifiedData modifiedData, String currentEmail, String currentName) {
        assertTrue(modifiedData.isSuccess());
        assertEquals("Данные изменены под другим email",
                currentEmail, modifiedData.getUser().getEmail());
        assertEquals("Данные изменены под другим name",
                currentName, modifiedData.getUser().getName());
    }

    public void checkModiedDataWithoutAuth(ValidatableResponse response) {
        userClient.checkResponseHTTPStatus(response, HTTP_UNAUTHORIZED);
        boolean isSuccess = Boolean.valueOf(userClient.getResponseDataByKey(response, "success"));
        assertFalse(isSuccess);

        String messageResponse = userClient.getResponseDataByKey(response, "message");
        assertEquals("Данные изменены без авторизации", "You should be authorised", messageResponse);
    }

    public void checkModifiedDataSameEmail(ValidatableResponse response) {
        userClient.checkResponseHTTPStatus(response, HTTP_FORBIDDEN);
        boolean isSuccess = Boolean.valueOf(userClient.getResponseDataByKey(response, "success"));
        assertFalse(isSuccess);

        String messageResponse = userClient.getResponseDataByKey(response, "message");
        assertEquals("Данные изменены на существующий email",
                "User with such email already exists", messageResponse);
    }
}