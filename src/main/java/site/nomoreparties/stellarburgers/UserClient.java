package site.nomoreparties.stellarburgers;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.StringUtils;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.*;
import static org.junit.Assert.*;

public class UserClient {

    private String getResponseDataByKey(ValidatableResponse response, int statusCode, String key) {
        return  response
                .assertThat()
                .statusCode(statusCode)
                .extract()
                .path(key).toString();
    }
    public ValidatableResponse createNewUser(NewUser newUser) {
        return given()
                .log().all()
                .baseUri(Constants.BASE_URI)
                .contentType(ContentType.JSON)
                .body(newUser)
                .and()
                .when()
                .post(Constants.REGISTRATION_PATH)
                .then().log().all();
    }

    public NewUserRegistrationInfo getResponseAboutNewUser(ValidatableResponse response) {
        return  response
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .body().as(NewUserRegistrationInfo.class);
    }

    public void deleteUser(String accessToken) {
        var token = StringUtils.substringAfter(accessToken, " ");

        ValidatableResponse response = given()
                .log().all()
                .baseUri(Constants.BASE_URI)
                .auth().oauth2(token)
                .when()
                .delete(Constants.DELETE_UPDATE_PATH)
                .then().log().all();

        checkDeletedUser(response);
    }

    public ValidatableResponse loginUser(UserLogin userLogin) {
        return given()
                .log().all()
                .baseUri(Constants.BASE_URI)
                .contentType(ContentType.JSON)
                .body(userLogin)
                .and()
                .when()
                .post(Constants.LOGIN_PATH)
                .then().log().all();
    }

    public UserLoginInfo getResponseAboutLogin(ValidatableResponse response) {
        return  response
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .body().as(UserLoginInfo.class);
    }

    public void logoutUser(String accessToken, String refreshToken) {
        var authToken = StringUtils.substringAfter(accessToken, " ");

        UserLogout userLogout = new UserLogout(refreshToken);

        ValidatableResponse response = given()
                .log().all()
                .baseUri(Constants.BASE_URI)
                .auth().oauth2(authToken)
                .body(userLogout)
                .when()
                .post(Constants.LOGOUT_PATH)
                .then().log().all();

        checkLogoutUser(response);
    }

    public ValidatableResponse changeUserWithAuth(UserNewData newData, String accessToken) {
        var token = StringUtils.substringAfter(accessToken, " ");

        return given()
                .log().all()
                .baseUri(Constants.BASE_URI)
                .auth().oauth2(token)
                .body(newData)
                .when()
                .patch(Constants.DELETE_UPDATE_PATH)
                .then().log().all();
    }

    public ValidatableResponse changeUserWithoutAuth(UserNewData newData) {
        return given()
                .log().all()
                .baseUri(Constants.BASE_URI)
                .body(newData)
                .when()
                .patch(Constants.DELETE_UPDATE_PATH)
                .then().log().all();
    }

    public UserModifiedData getResponseAboutModifiedData(ValidatableResponse response) {
        return  response
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .body().as(UserModifiedData.class);
    }







    public void checkCreatedUser(NewUserRegistrationInfo userInfo) {
        assertTrue(userInfo.isSuccess());
    }

    public void checkDeletedUser(ValidatableResponse response) {
        boolean isSuccess = Boolean.valueOf(getResponseDataByKey(response, HTTP_ACCEPTED, "success"));
        assertTrue(isSuccess);
    }

    public void checkDuplicateUser(ValidatableResponse response) {
        boolean isSuccess = Boolean.valueOf(getResponseDataByKey(response, HTTP_FORBIDDEN, "success"));
        assertFalse(isSuccess);

        String messageResponse = getResponseDataByKey(response, HTTP_FORBIDDEN, "message");
        assertEquals("Пользователь создан еще раз","User already exists", messageResponse);
    }

    public void checkCreatedUserWithoutParameter(ValidatableResponse response) {
        boolean isSuccess = Boolean.valueOf(getResponseDataByKey(response, HTTP_FORBIDDEN, "success"));
        assertFalse(isSuccess);

        String messageResponse = getResponseDataByKey(response, HTTP_FORBIDDEN, "message");
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
        boolean isSuccess = Boolean.valueOf(getResponseDataByKey(response, HTTP_UNAUTHORIZED, "success"));
        assertFalse(isSuccess);

        String messageResponse = getResponseDataByKey(response, HTTP_UNAUTHORIZED, "message");
        assertEquals("Несуществующий пользователь авторизован в системе",
                "email or password are incorrect", messageResponse);
    }

    public void checkLogoutUser(ValidatableResponse response) {
        boolean isSuccess = Boolean.valueOf(getResponseDataByKey(response, HTTP_OK, "success"));
        assertTrue(isSuccess);

        String messageResponse = getResponseDataByKey(response, HTTP_OK, "message");
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
        boolean isSuccess = Boolean.valueOf(getResponseDataByKey(response, HTTP_UNAUTHORIZED, "success"));
        assertFalse(isSuccess);

        String messageResponse = getResponseDataByKey(response, HTTP_UNAUTHORIZED, "message");
        assertEquals("Данные изменены без авторизации", "You should be authorised", messageResponse);
    }

    public void checkModifiedDataSameEmail(ValidatableResponse response) {
        boolean isSuccess = Boolean.valueOf(getResponseDataByKey(response, HTTP_FORBIDDEN, "success"));
        assertFalse(isSuccess);

        String messageResponse = getResponseDataByKey(response, HTTP_FORBIDDEN, "message");
        assertEquals("Данные изменены на существующий email",
                "User with such email already exists", messageResponse);
    }
}