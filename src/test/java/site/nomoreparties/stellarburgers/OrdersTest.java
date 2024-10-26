package site.nomoreparties.stellarburgers;

import io.restassured.response.ValidatableResponse;
import org.junit.Test;

public class OrdersTest {
    private OrderClient orderClient = new OrderClient();
    private UserClient userClient = new UserClient();
    private OrderChecks orderChecks = new OrderChecks();

    @Test
    public void createSuccessNewOrderWithoutAuth() {
        OrderIngredients orderIngredients =
                new OrderIngredients(new String[] {"61c0c5a71d1f82001bdaaa79", "61c0c5a71d1f82001bdaaa6c"});

        ValidatableResponse response = orderClient.createNewOrderWithoutAuth(orderIngredients);
        OrderCreated orderCreated = orderClient.getResponseAboutCreatedOrder(response);
        orderChecks.checkCreatedOrderSuccess(orderCreated);
    }

    @Test
    public void createSuccessNewOrderWithAuth() {
        UserLogin userLogin = new UserLogin(Constants.DEFAULT_EMAIL, Constants.DEFAULT_PASSWORD);
        ValidatableResponse response = userClient.loginUser(userLogin);
        UserLoginInfo userLoginInfo = userClient.getResponseAboutLogin(response);
        userClient.checkLoginDefaultUser(userLoginInfo);
        String accessToken = userLoginInfo.getAccessToken();

        OrderIngredients orderIngredients =
                new OrderIngredients(new String[] {"61c0c5a71d1f82001bdaaa79", "61c0c5a71d1f82001bdaaa6c"});

        ValidatableResponse orderResponse = orderClient.createNewOrderWithAuth(orderIngredients, accessToken);
        OrderCreated orderCreated = orderClient.getResponseAboutCreatedOrder(orderResponse);
        orderChecks.checkCreatedOrderSuccess(orderCreated);
    }

    @Test
    public void createNewOrderWithIncorrectDataWithoutAuth() {
        OrderIngredients orderIngredients =
                new OrderIngredients(new String[] {"DDD7c5a71d1f82001bdaaa79", "HJU!c5a71d1f82001bdaaa6c"});

        ValidatableResponse response = orderClient.createNewOrderWithoutAuth(orderIngredients);
        orderChecks.checkCreateOrderWithIncorrectData(response);
    }

    @Test
    public void createNewOrderWithIncorrectDataWithAuth() {
        UserLogin userLogin = new UserLogin(Constants.DEFAULT_EMAIL, Constants.DEFAULT_PASSWORD);
        ValidatableResponse response = userClient.loginUser(userLogin);
        UserLoginInfo userLoginInfo = userClient.getResponseAboutLogin(response);
        userClient.checkLoginDefaultUser(userLoginInfo);
        String accessToken = userLoginInfo.getAccessToken();

        OrderIngredients orderIngredients =
                new OrderIngredients(new String[] {"DDD7c5a71d1f82001bdaaa79", "HJU!c5a71d1f82001bdaaa6c"});

        ValidatableResponse responseOrder = orderClient.createNewOrderWithAuth(orderIngredients, accessToken);
        orderChecks.checkCreateOrderWithIncorrectData(responseOrder);
    }

    @Test
    public void createNewOrderWithEmptyDataWithoutAuth() {
        OrderIngredients orderIngredients = new OrderIngredients(new String[] {});

        ValidatableResponse response = orderClient.createNewOrderWithoutAuth(orderIngredients);
        orderChecks.checkCreateOrderWithNullData(response);
    }

    @Test
    public void createNewOrderWithEmptyDataWithAuth() {
        UserLogin userLogin = new UserLogin(Constants.DEFAULT_EMAIL, Constants.DEFAULT_PASSWORD);
        ValidatableResponse response = userClient.loginUser(userLogin);
        UserLoginInfo userLoginInfo = userClient.getResponseAboutLogin(response);
        userClient.checkLoginDefaultUser(userLoginInfo);
        String accessToken = userLoginInfo.getAccessToken();

        OrderIngredients orderIngredients = new OrderIngredients(new String[] {});

        ValidatableResponse responseOrder = orderClient.createNewOrderWithAuth(orderIngredients, accessToken);
        orderChecks.checkCreateOrderWithNullData(responseOrder);
    }

    @Test
    public void createNewOrderWithNullDataWithoutAuth() {
        OrderIngredients orderIngredients = new OrderIngredients(null);

        ValidatableResponse response = orderClient.createNewOrderWithoutAuth(orderIngredients);
        orderChecks.checkCreateOrderWithNullData(response);
    }

    @Test
    public void createNewOrderWithNullDataWithAuth() {
        UserLogin userLogin = new UserLogin(Constants.DEFAULT_EMAIL, Constants.DEFAULT_PASSWORD);
        ValidatableResponse response = userClient.loginUser(userLogin);
        UserLoginInfo userLoginInfo = userClient.getResponseAboutLogin(response);
        userClient.checkLoginDefaultUser(userLoginInfo);
        String accessToken = userLoginInfo.getAccessToken();

        OrderIngredients orderIngredients = new OrderIngredients(null);

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
        UserLogin userLogin = new UserLogin(Constants.DEFAULT_EMAIL, Constants.DEFAULT_PASSWORD);
        ValidatableResponse response = userClient.loginUser(userLogin);
        UserLoginInfo userLoginInfo = userClient.getResponseAboutLogin(response);
        userClient.checkLoginDefaultUser(userLoginInfo);
        String accessToken = userLoginInfo.getAccessToken();

        ValidatableResponse responseOrder = orderClient.getOrdersWithAuth(accessToken);
        OrdersCustomer ordersCustomer = orderClient.getResponseAboutListOfOrders(responseOrder);
        orderChecks.checkOrdersWithAuth(ordersCustomer);
    }
}