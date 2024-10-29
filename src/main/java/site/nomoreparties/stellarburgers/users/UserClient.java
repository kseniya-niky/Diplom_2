package site.nomoreparties.stellarburgers.users;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import site.nomoreparties.stellarburgers.*;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.*;

public class UserClient {
    private ClientServices services = new ClientServices();

    public String getResponseDataByKey(ValidatableResponse response, String key) {
        return response.extract().path(key).toString();
    }

    @Step("Проверка статуса ответа")
    public void checkResponseHTTPStatus(ValidatableResponse response, int statusCode) {
        response.assertThat().statusCode(statusCode);
    }

    private RequestSpecification specificationWithoutAuth() {
        return given().log().all()
                .baseUri(Constants.BASE_URI);
    }

    private RequestSpecification specificationWithAuth(String token) {
        return given().log().all()
                .baseUri(Constants.BASE_URI)
                .auth().oauth2(token);
    }

    @Step("Создание нового пользователя")
    public ValidatableResponse createNewUser(NewUser newUser) {
        return specificationWithoutAuth()
                .contentType(ContentType.JSON)
                .body(newUser)
                .and()
                .when()
                .post(Constants.REGISTRATION_PATH)
                .then().log().all();
    }

    @Step("Получение информации о созданном новом пользователе")
    public NewUserRegistrationInfo getResponseAboutNewUser(ValidatableResponse response) {
        return  response
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .body().as(NewUserRegistrationInfo.class);
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(NewUserRegistrationInfo info) {
        String token = services.trimAccessToken(info.getAccessToken());
        return  specificationWithAuth(token)
                .when()
                .delete(Constants.DELETE_UPDATE_PATH)
                .then().log().all();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse loginUser(UserLogin userLogin) {
        return specificationWithoutAuth()
                .contentType(ContentType.JSON)
                .body(userLogin)
                .and()
                .when()
                .post(Constants.LOGIN_PATH)
                .then().log().all();
    }

    @Step("Получение информации об авторизации пользователя")
    public UserLoginInfo getResponseAboutLogin(ValidatableResponse response) {
        return  response
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .body().as(UserLoginInfo.class);
    }

    @Step("Выход пользователя из системы")
    public ValidatableResponse logoutUser(UserLoginInfo info) {
        String authToken = services.trimAccessToken(info.getAccessToken());
        UserLogout userLogout = new UserLogout(info.getRefreshToken());

         return specificationWithAuth(authToken)
                .contentType(ContentType.JSON)
                .body(userLogout)
                .when()
                .post(Constants.LOGOUT_PATH)
                .then().log().all();
    }

    private ValidatableResponse updateUser(RequestSpecification specification, UserNewData newData) {
        return specification
                .contentType(ContentType.JSON)
                .body(newData)
                .when()
                .patch(Constants.DELETE_UPDATE_PATH)
                .then().log().all();
    }

    @Step("Обновление данных авторизованного пользователя")
    public ValidatableResponse changeUserWithAuth(UserNewData newData,
                                                  NewUserRegistrationInfo info) {
        String token = services.trimAccessToken(info.getAccessToken());
        return updateUser(specificationWithAuth(token), newData);
    }

    @Step("Обновление данных неавторизованного пользователя")
    public ValidatableResponse changeUserWithoutAuth(UserNewData newData) {
        return updateUser(specificationWithoutAuth(), newData);
    }

    @Step("Получение информации об изменении данных пользователя")
    public UserModifiedData getResponseAboutModifiedData(ValidatableResponse response) {
        return  response
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .body().as(UserModifiedData.class);
    }
}