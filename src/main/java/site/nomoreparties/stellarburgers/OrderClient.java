package site.nomoreparties.stellarburgers;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.*;

public class OrderClient {
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

    public ValidatableResponse createNewOrderWithoutAuth(OrderIngredients ingredients) {
        return specificationWithoutAuth()
                .contentType(ContentType.JSON)
                .body(ingredients)
                .and()
                .when()
                .post(Constants.ORDERS_PATH)
                .then().log().all();
    }

    public ValidatableResponse createNewOrderWithAuth(OrderIngredients ingredients, String accessToken) {
        String token = services.trimAccessToken(accessToken);
        return specificationWithAuth(token)
                .contentType(ContentType.JSON)
                .body(ingredients)
                .and()
                .when()
                .post(Constants.ORDERS_PATH)
                .then().log().all();
    }

    public OrderCreated getResponseAboutCreatedOrder(ValidatableResponse response) {
        return  response
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .body().as(OrderCreated.class);
    }

    public ValidatableResponse getOrdersWithoutAuth() {
        return specificationWithoutAuth()
                .and()
                .when()
                .get(Constants.ORDERS_PATH)
                .then().log().all();
    }

    public ValidatableResponse getOrdersWithAuth(String accessToken) {
        String token = services.trimAccessToken(accessToken);
        return specificationWithAuth(token)
                .and()
                .when()
                .get(Constants.ORDERS_PATH)
                .then().log().all();
    }

    public OrdersCustomer getResponseAboutListOfOrders(ValidatableResponse response) {
        return  response
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .body().as(OrdersCustomer.class);
    }
}