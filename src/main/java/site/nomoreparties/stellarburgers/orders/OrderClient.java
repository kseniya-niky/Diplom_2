package site.nomoreparties.stellarburgers.orders;

import io.qameta.allure.Param;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import site.nomoreparties.stellarburgers.ClientServices;
import site.nomoreparties.stellarburgers.Constants;

import static io.qameta.allure.model.Parameter.Mode.HIDDEN;
import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.*;

public class OrderClient {
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

    @Step("Создание нового заказа без авторизации пользователя")
    public ValidatableResponse createNewOrderWithoutAuth(OrderIngredients ingredients) {
        return specificationWithoutAuth()
                .contentType(ContentType.JSON)
                .body(ingredients)
                .and()
                .when()
                .post(Constants.ORDERS_PATH)
                .then().log().all();
    }

    @Step("Создание нового заказа с авторизацией пользователя")
    public ValidatableResponse createNewOrderWithAuth(OrderIngredients ingredients,
                                                      @Param(mode=HIDDEN) String accessToken) {
        String token = services.trimAccessToken(accessToken);
        return specificationWithAuth(token)
                .contentType(ContentType.JSON)
                .body(ingredients)
                .and()
                .when()
                .post(Constants.ORDERS_PATH)
                .then().log().all();
    }

    @Step("Получение информации о созданном заказе")
    public OrderCreated getResponseAboutCreatedOrder(ValidatableResponse response) {
        return  response
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .body().as(OrderCreated.class);
    }

    @Step("Получение списка заказов для неавторизованного пользователя")
    public ValidatableResponse getOrdersWithoutAuth() {
        return specificationWithoutAuth()
                .and()
                .when()
                .get(Constants.ORDERS_PATH)
                .then().log().all();
    }

    @Step("Получение списка заказов для авторизованного пользователя")
    public ValidatableResponse getOrdersWithAuth(@Param(mode=HIDDEN) String accessToken) {
        String token = services.trimAccessToken(accessToken);
        return specificationWithAuth(token)
                .and()
                .when()
                .get(Constants.ORDERS_PATH)
                .then().log().all();
    }

    @Step("Получение успешного ответа о списке заказов")
    public OrdersCustomer getResponseAboutListOfOrders(ValidatableResponse response) {
        return  response
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .body().as(OrdersCustomer.class);
    }
}