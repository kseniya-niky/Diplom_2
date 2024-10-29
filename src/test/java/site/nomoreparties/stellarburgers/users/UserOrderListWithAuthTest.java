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

public class UserOrderListWithAuthTest {
    private OrderClient orderClient = new OrderClient();
    private UserClient userClient = new UserClient();
    private OrderChecks orderChecks = new OrderChecks();
    private UserChecks userChecks = new UserChecks();

    private UserLoginInfo userLoginInfo;

    @Before
    public void loginUser() {
        UserLogin userLogin = new UserLogin(Constants.DEFAULT_EMAIL, Constants.DEFAULT_PASSWORD);
        ValidatableResponse response = userClient.loginUser(userLogin);
        userLoginInfo = userClient.getResponseAboutLogin(response);
        userChecks.checkLoginDefaultUser(userLoginInfo);
    }

    @After
    public void logoutUser() {
        if(userLoginInfo.getRefreshToken() != null) {
            if (!userLoginInfo.getRefreshToken().isBlank()) {
                ValidatableResponse response = userClient.logoutUser(userLoginInfo);
                userChecks.checkLogoutUser(response);
            }
        }
    }

    @Test
    @DisplayName("Получение списка заказов с авторизацией пользователя")
    public void getListOfOrdersWithAuth() {
        ValidatableResponse responseOrder = orderClient.getOrdersWithAuth(userLoginInfo);
        OrdersCustomer ordersCustomer = orderClient.getResponseAboutListOfOrders(responseOrder);
        orderChecks.checkOrdersWithAuth(ordersCustomer);
    }
}