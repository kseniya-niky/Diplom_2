package site.nomoreparties.stellarburgers;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.StringUtils;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.*;
import static org.junit.Assert.*;

public class OrderClient {

    private String getResponseDataByKey(ValidatableResponse response, int statusCode, String key) {
        return  response
                .assertThat()
                .statusCode(statusCode)
                .extract()
                .path(key).toString();
    }
    public ValidatableResponse createNewOrderWithoutAuth(OrderIngredients ingredients) {
        return given()
                .log().all()
                .baseUri(Constants.BASE_URI)
                .contentType(ContentType.JSON)
                .body(ingredients)
                .and()
                .when()
                .post(Constants.ORDERS_PATH)
                .then().log().all();
    }

    public ValidatableResponse createNewOrderWithAuth(OrderIngredients ingredients, String accessToken) {
        var token = StringUtils.substringAfter(accessToken, " ");
        return given()
                .log().all()
                .baseUri(Constants.BASE_URI)
                .auth().oauth2(token)
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
        return given()
                .log().all()
                .baseUri(Constants.BASE_URI)
                .and()
                .when()
                .get(Constants.ORDERS_PATH)
                .then().log().all();
    }

    public ValidatableResponse getOrdersWithAuth(String accessToken) {
        var token = StringUtils.substringAfter(accessToken, " ");
        return given()
                .log().all()
                .baseUri(Constants.BASE_URI)
                .auth().oauth2(token)
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












    public void checkCreatedOrderSuccess(OrderCreated orderCreated) {
        assertTrue("Заказ не создан", orderCreated.isSuccess());
        assertNotNull("Заказу не присвоен номер", orderCreated.getOrder().getNumber());
    }

    public void checkCreateOrderWithIncorrectData(ValidatableResponse response) {
        getResponseDataByKey(response, HTTP_INTERNAL_ERROR, "");
    }

    public void checkCreateOrderWithNullData(ValidatableResponse response) {
        boolean isSuccess = Boolean.valueOf(getResponseDataByKey(response, HTTP_BAD_REQUEST, "success"));
        assertFalse(isSuccess);

        String messageResponse = getResponseDataByKey(response, HTTP_BAD_REQUEST, "message");
        assertEquals("Создан заказ без списка ингредиентов",
                        "Ingredient ids must be provided", messageResponse);
    }

    public void checkOrdersWithoutAuth(ValidatableResponse response) {
        boolean isSuccess = Boolean.valueOf(getResponseDataByKey(response, HTTP_UNAUTHORIZED, "success"));
        assertFalse(isSuccess);

        String messageResponse = getResponseDataByKey(response, HTTP_UNAUTHORIZED, "message");
        assertEquals("Получен список заказов без авторизации",
                "You should be authorised", messageResponse);
    }


    public void checkOrdersWithAuth(OrdersCustomer ordersCustomer) {
        assertTrue(ordersCustomer.isSuccess());
        assertNotEquals("У пользователя нет заказов", 0, ordersCustomer.getTotal());
        assertNotNull("Список заказов пользователя пуст", ordersCustomer.getOrders());
    }
}