package site.nomoreparties.stellarburgers.users;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import site.nomoreparties.stellarburgers.orders.OrderChecks;
import site.nomoreparties.stellarburgers.orders.OrderClient;

public class UserOrderListWithoutAuthTest {
    private OrderClient orderClient = new OrderClient();
    private OrderChecks orderChecks = new OrderChecks();

    @Test
    @DisplayName("Получение списка заказов без авторизации пользователя")
    public void getListOfOrdersWithoutAuth() {
        ValidatableResponse response = orderClient.getOrdersWithoutAuth();
        orderChecks.checkOrdersWithoutAuth(response);
    }
}