package site.nomoreparties.stellarburgers;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.*;

public class UserClient {
    private ClientServices services = new ClientServices();

    public String getResponseDataByKey(ValidatableResponse response, String key) {
        return response.extract().path(key).toString();
    }

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

    public ValidatableResponse createNewUser(NewUser newUser) {
        return specificationWithoutAuth()
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

    public ValidatableResponse deleteUser(String accessToken) {
        String token = services.trimAccessToken(accessToken);
        return  specificationWithAuth(token)
                .when()
                .delete(Constants.DELETE_UPDATE_PATH)
                .then().log().all();
    }

    public ValidatableResponse loginUser(UserLogin userLogin) {
        return specificationWithoutAuth()
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

    public ValidatableResponse logoutUser(String accessToken, String refreshToken) {
        String authToken = services.trimAccessToken(accessToken);
        UserLogout userLogout = new UserLogout(refreshToken);

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

    public ValidatableResponse changeUserWithAuth(UserNewData newData, String accessToken) {
        String token = services.trimAccessToken(accessToken);
        return updateUser(specificationWithAuth(token), newData);
    }

    public ValidatableResponse changeUserWithoutAuth(UserNewData newData) {
        return updateUser(specificationWithoutAuth(), newData);
    }

    public UserModifiedData getResponseAboutModifiedData(ValidatableResponse response) {
        return  response
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .body().as(UserModifiedData.class);
    }
}