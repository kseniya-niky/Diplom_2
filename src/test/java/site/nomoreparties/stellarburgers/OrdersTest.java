package site.nomoreparties.stellarburgers;

import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

public class OrdersTest {
    private OrderClient orderClient = new OrderClient();
    private UserClient userClient = new UserClient();
    private OrderChecks orderChecks = new OrderChecks();
    private UserChecks userChecks = new UserChecks();

    private String accessToken;

    @Before
    public void loginUser() {
        UserLogin userLogin = new UserLogin(Constants.DEFAULT_EMAIL, Constants.DEFAULT_PASSWORD);
        ValidatableResponse response = userClient.loginUser(userLogin);
        UserLoginInfo userLoginInfo = userClient.getResponseAboutLogin(response);
        userChecks.checkLoginDefaultUser(userLoginInfo);
        accessToken = userLoginInfo.getAccessToken();
    }

    @Test
    public void createNewOrderWithIncorrectDataWithoutAuth() {
        OrderIngredients orderIngredients = new OrderIngredients(Constants.BURGER_WITH_DEFECT_INGREDIENTS);
        ValidatableResponse response = orderClient.createNewOrderWithoutAuth(orderIngredients);
        orderChecks.checkCreateOrderWithIncorrectData(response);
    }

    @Test
    public void createNewOrderWithIncorrectDataWithAuth() {
        OrderIngredients orderIngredients = new OrderIngredients(Constants.BURGER_WITH_DEFECT_INGREDIENTS);
        ValidatableResponse responseOrder = orderClient.createNewOrderWithAuth(orderIngredients, accessToken);
        orderChecks.checkCreateOrderWithIncorrectData(responseOrder);
    }

    @Test
    public void createNewOrderWithEmptyDataWithoutAuth() {
        OrderIngredients orderIngredients = new OrderIngredients(Constants.BURGER_WITHOUT_INGREDIENTS);
        ValidatableResponse response = orderClient.createNewOrderWithoutAuth(orderIngredients);
        orderChecks.checkCreateOrderWithNullData(response);
    }

    @Test
    public void createNewOrderWithEmptyDataWithAuth() {
        OrderIngredients orderIngredients = new OrderIngredients(Constants.BURGER_WITHOUT_INGREDIENTS);
        ValidatableResponse responseOrder = orderClient.createNewOrderWithAuth(orderIngredients, accessToken);
        orderChecks.checkCreateOrderWithNullData(responseOrder);
    }

    @Test
    public void createNewOrderWithNullDataWithoutAuth() {
        OrderIngredients orderIngredients = new OrderIngredients(Constants.BURGER_NULL);
        ValidatableResponse response = orderClient.createNewOrderWithoutAuth(orderIngredients);
        orderChecks.checkCreateOrderWithNullData(response);
    }

    @Test
    public void createNewOrderWithNullDataWithAuth() {
        OrderIngredients orderIngredients = new OrderIngredients(Constants.BURGER_NULL);
        ValidatableResponse responseOrder = orderClient.createNewOrderWithAuth(orderIngredients, accessToken);
        orderChecks.checkCreateOrderWithNullData(responseOrder);
    }

    @Test
    public void getListOfOrdersWithoutAuth() {
        ValidatableResponse response = orderClient.getOrdersWithoutAuth();
        orderChecks.checkOrdersWithoutAuth(response);
    }

    @Test
    public void getListOfOrdersWithAuth() {
        ValidatableResponse responseOrder = orderClient.getOrdersWithAuth(accessToken);
        OrdersCustomer ordersCustomer = orderClient.getResponseAboutListOfOrders(responseOrder);
        orderChecks.checkOrdersWithAuth(ordersCustomer);
    }
}