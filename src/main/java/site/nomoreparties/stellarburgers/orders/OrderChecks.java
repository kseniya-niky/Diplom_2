package site.nomoreparties.stellarburgers.orders;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static java.net.HttpURLConnection.*;
import static org.junit.Assert.*;

public class OrderChecks {
    private OrderClient orderClient = new OrderClient();

    @Step("Проверка, что новый заказ создан успешно")
    public void checkCreatedOrderSuccess(OrderCreated orderCreated) {
        assertTrue("Заказ не создан", orderCreated.isSuccess());
        assertNotNull("Заказу не присвоен номер", orderCreated.getOrder().getNumber());
    }

    @Step("Проверка создания заказа с некорректными хэшами ингредиентов")
    public void checkCreateOrderWithIncorrectData(ValidatableResponse response) {
        orderClient.checkResponseHTTPStatus(response, HTTP_INTERNAL_ERROR);
    }

    @Step("Проверка создания заказа без ингредиентов")
    public void checkCreateOrderWithNullData(ValidatableResponse response) {
        orderClient.checkResponseHTTPStatus(response, HTTP_BAD_REQUEST);
        boolean isSuccess = Boolean.valueOf(orderClient.getResponseDataByKey(response, "success"));
        assertFalse(isSuccess);

        String messageResponse = orderClient.getResponseDataByKey(response, "message");
        assertEquals("Создан заказ без списка ингредиентов",
                "Ingredient ids must be provided", messageResponse);
    }

    @Step("Проверка создания заказа без авторизации")
    public void checkOrdersWithoutAuth(ValidatableResponse response) {
        orderClient.checkResponseHTTPStatus(response, HTTP_UNAUTHORIZED);
        boolean isSuccess = Boolean.valueOf(orderClient.getResponseDataByKey(response, "success"));
        assertFalse(isSuccess);

        String messageResponse = orderClient.getResponseDataByKey(response, "message");
        assertEquals("Получен список заказов без авторизации",
                "You should be authorised", messageResponse);
    }

    @Step("Проверка создания заказа с авторизацией")
    public void checkOrdersWithAuth(OrdersCustomer ordersCustomer) {
        assertTrue(ordersCustomer.isSuccess());
        assertNotEquals("У пользователя нет заказов", 0, ordersCustomer.getTotal());
        assertNotNull("Список заказов пользователя пуст", ordersCustomer.getOrders());
    }
}