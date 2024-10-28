package site.nomoreparties.stellarburgers.users;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.Constants;
import site.nomoreparties.stellarburgers.orders.OrderChecks;
import site.nomoreparties.stellarburgers.orders.OrderClient;
import site.nomoreparties.stellarburgers.orders.OrdersCustomer;
import site.nomoreparties.stellarburgers.users.UserChecks;
import site.nomoreparties.stellarburgers.users.UserClient;
import site.nomoreparties.stellarburgers.users.UserLogin;
import site.nomoreparties.stellarburgers.users.UserLoginInfo;

public class UserOrderListWithAuthTest {
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
    @DisplayName("Получение списка заказов с авторизацией пользователя")
    public void getListOfOrdersWithAuth() {
        ValidatableResponse responseOrder = orderClient.getOrdersWithAuth(accessToken);
        OrdersCustomer ordersCustomer = orderClient.getResponseAboutListOfOrders(responseOrder);
        orderChecks.checkOrdersWithAuth(ordersCustomer);
    }
}