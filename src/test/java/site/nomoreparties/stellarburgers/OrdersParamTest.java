package site.nomoreparties.stellarburgers;

import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class OrdersParamTest {
    private String[] ingredients;
    private OrderClient orderClient = new OrderClient();
    private OrderChecks orderChecks = new OrderChecks();

    public OrdersParamTest(String[] ingredients) {
        this.ingredients = ingredients;
    }

    @Parameterized.Parameters
    public static final Object[][] getListOfIngredients() {
        return new Object[][]{
                { new String[] {"61c0c5a71d1f82001bdaaa79", "61c0c5a71d1f82001bdaaa6c"}},
                { new String[] {"61c0c5a71d1f82001bdaaa73", "61c0c5a71d1f82001bdaaa70",
                        "61c0c5a71d1f82001bdaaa79",
                        "61c0c5a71d1f82001bdaaa78",
                        "61c0c5a71d1f82001bdaaa6d"}},
                { new String[] {"DDD7c5a71d1f82001bdaaa79", "HJU!c5a71d1f82001bdaaa6c"}},
                { new String[] {}},
                { null }
        };
    }

    @Test
    public void createNewOrder() {
        OrderIngredients orderIngredients = new OrderIngredients(ingredients);
        ValidatableResponse response = orderClient.createNewOrderWithoutAuth(orderIngredients);
        OrderCreated orderCreated = orderClient.getResponseAboutCreatedOrder(response);
        orderChecks.checkCreatedOrderSuccess(orderCreated);
    }
}