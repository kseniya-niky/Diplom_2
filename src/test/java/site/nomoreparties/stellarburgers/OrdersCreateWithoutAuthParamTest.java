package site.nomoreparties.stellarburgers;

import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

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
    public void createNewOrderWithoutAuthPositiveAndNegative() {
        OrderIngredients orderIngredients = new OrderIngredients(ingredients);
        ValidatableResponse response = orderClient.createNewOrderWithoutAuth(orderIngredients);
        OrderCreated orderCreated = orderClient.getResponseAboutCreatedOrder(response);
        orderChecks.checkCreatedOrderSuccess(orderCreated);
    }
}