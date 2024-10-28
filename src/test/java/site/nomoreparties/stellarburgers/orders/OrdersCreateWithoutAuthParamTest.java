package site.nomoreparties.stellarburgers.orders;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import site.nomoreparties.stellarburgers.Constants;
import site.nomoreparties.stellarburgers.orders.OrderChecks;
import site.nomoreparties.stellarburgers.orders.OrderClient;
import site.nomoreparties.stellarburgers.orders.OrderCreated;
import site.nomoreparties.stellarburgers.orders.OrderIngredients;

@RunWith(Parameterized.class)
public class OrdersCreateWithoutAuthParamTest {
    private String[] ingredients;
    private OrderClient orderClient = new OrderClient();
    private OrderChecks orderChecks = new OrderChecks();

    public OrdersCreateWithoutAuthParamTest(String[] ingredients) {
        this.ingredients = ingredients;
    }

    @Parameterized.Parameters
    public static final Object[][] getListOfIngredients() {
        return new Object[][]{
                { Constants.BURGER_TWO_INGREDIENTS },
                { Constants.BURGER_FIVE_INGREDIENTS },
                { Constants.BURGER_WITH_DEFECT_INGREDIENTS },
                { Constants.BURGER_WITHOUT_INGREDIENTS },
                { Constants.BURGER_NULL }
        };
    }

    @Test
    @DisplayName("Создание заказа неавторизованным пользователем с разным набором ингредиентов")
    public void createNewOrderWithoutAuthPositiveAndNegative() {
        OrderIngredients orderIngredients = new OrderIngredients(ingredients);
        ValidatableResponse response = orderClient.createNewOrderWithoutAuth(orderIngredients);
        OrderCreated orderCreated = orderClient.getResponseAboutCreatedOrder(response);
        orderChecks.checkCreatedOrderSuccess(orderCreated);
    }
}