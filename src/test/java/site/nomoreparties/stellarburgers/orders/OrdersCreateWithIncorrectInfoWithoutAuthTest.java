package site.nomoreparties.stellarburgers.orders;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import site.nomoreparties.stellarburgers.Constants;
import site.nomoreparties.stellarburgers.orders.OrderChecks;
import site.nomoreparties.stellarburgers.orders.OrderClient;
import site.nomoreparties.stellarburgers.orders.OrderIngredients;

public class OrdersCreateWithIncorrectInfoWithoutAuthTest {
    private OrderClient orderClient = new OrderClient();
    private OrderChecks orderChecks = new OrderChecks();

    @Test
    @DisplayName("Создание заказа с неправильным хэшем ингредиентов неавторизованным пользователем")
    public void createNewOrderWithIncorrectDataWithoutAuth() {
        OrderIngredients orderIngredients = new OrderIngredients(Constants.BURGER_WITH_DEFECT_INGREDIENTS);
        ValidatableResponse response = orderClient.createNewOrderWithoutAuth(orderIngredients);
        orderChecks.checkCreateOrderWithIncorrectData(response);
    }

    @Test
    @DisplayName("Создание заказа с пустым списком ингредиентов неавторизованным пользователем")
    public void createNewOrderWithEmptyDataWithoutAuth() {
        OrderIngredients orderIngredients = new OrderIngredients(Constants.BURGER_WITHOUT_INGREDIENTS);
        ValidatableResponse response = orderClient.createNewOrderWithoutAuth(orderIngredients);
        orderChecks.checkCreateOrderWithNullData(response);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов неавторизованным пользователем")
    public void createNewOrderWithNullDataWithoutAuth() {
        OrderIngredients orderIngredients = new OrderIngredients(Constants.BURGER_NULL);
        ValidatableResponse response = orderClient.createNewOrderWithoutAuth(orderIngredients);
        orderChecks.checkCreateOrderWithNullData(response);
    }
}