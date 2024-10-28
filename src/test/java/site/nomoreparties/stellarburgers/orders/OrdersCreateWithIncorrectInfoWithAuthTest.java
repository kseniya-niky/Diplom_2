package site.nomoreparties.stellarburgers.orders;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.Constants;
import site.nomoreparties.stellarburgers.orders.OrderChecks;
import site.nomoreparties.stellarburgers.orders.OrderClient;
import site.nomoreparties.stellarburgers.orders.OrderIngredients;
import site.nomoreparties.stellarburgers.users.UserChecks;
import site.nomoreparties.stellarburgers.users.UserClient;
import site.nomoreparties.stellarburgers.users.UserLogin;
import site.nomoreparties.stellarburgers.users.UserLoginInfo;

public class OrdersCreateWithIncorrectInfoWithAuthTest {
    private OrderClient orderClient = new OrderClient();
    private UserClient userClient = new UserClient();
    private OrderChecks orderChecks = new OrderChecks();
    private UserChecks userChecks = new UserChecks();

    private String accessToken, refreshToken;

    @Before
    public void loginUser() {
        UserLogin userLogin = new UserLogin(Constants.DEFAULT_EMAIL, Constants.DEFAULT_PASSWORD);
        ValidatableResponse response = userClient.loginUser(userLogin);
        UserLoginInfo userLoginInfo = userClient.getResponseAboutLogin(response);
        userChecks.checkLoginDefaultUser(userLoginInfo);
        accessToken = userLoginInfo.getAccessToken();
        refreshToken = userLoginInfo.getRefreshToken();
    }

    @After
    public void logoutUser() {
        if(refreshToken != null) {
            if (!refreshToken.isBlank()) {
                ValidatableResponse response = userClient.logoutUser(accessToken, refreshToken);
                userChecks.checkLogoutUser(response);
            }
        }
    }

    @Test
    @DisplayName("Создание заказа с неправильным хэшем ингредиентов авторизованным пользователем")
    public void createNewOrderWithIncorrectDataWithAuth() {
        OrderIngredients orderIngredients = new OrderIngredients(Constants.BURGER_WITH_DEFECT_INGREDIENTS);
        ValidatableResponse responseOrder = orderClient.createNewOrderWithAuth(orderIngredients, accessToken);
        orderChecks.checkCreateOrderWithIncorrectData(responseOrder);
    }

    @Test
    @DisplayName("Создание заказа с пустым списком ингредиентов авторизованным пользователем")
    public void createNewOrderWithEmptyDataWithAuth() {
        OrderIngredients orderIngredients = new OrderIngredients(Constants.BURGER_WITHOUT_INGREDIENTS);
        ValidatableResponse responseOrder = orderClient.createNewOrderWithAuth(orderIngredients, accessToken);
        orderChecks.checkCreateOrderWithNullData(responseOrder);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов авторизованным пользователем")
    public void createNewOrderWithNullDataWithAuth() {
        OrderIngredients orderIngredients = new OrderIngredients(Constants.BURGER_NULL);
        ValidatableResponse responseOrder = orderClient.createNewOrderWithAuth(orderIngredients, accessToken);
        orderChecks.checkCreateOrderWithNullData(responseOrder);
    }
}