package site.nomoreparties.stellarburgers.users;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.orders.OrderChecks;
import site.nomoreparties.stellarburgers.orders.OrderClient;
import site.nomoreparties.stellarburgers.orders.OrdersCustomer;

public class UserOrderListWithAuthTest {
    private OrderClient orderClient = new OrderClient();
    private UserClient userClient = new UserClient();
    private OrderChecks orderChecks = new OrderChecks();
    private UserChecks userChecks = new UserChecks();
    private NewUserRegistrationInfo userRegistrationInfo = new NewUserRegistrationInfo();

    @Before
    public void createUser() {
        NewUser newUser = NewUser.random();
        ValidatableResponse response = userClient.createNewUser(newUser);
        userRegistrationInfo = userClient.getResponseAboutNewUser(response);
        userChecks.checkCreatedUser(userRegistrationInfo, newUser);
    }

    @After
    public void deleteUser() {
        if(userRegistrationInfo.getAccessToken() != null) {
            if (!userRegistrationInfo.getAccessToken().isBlank()) {
                userClient.deleteUser(userRegistrationInfo);
            }
        }
    }

    @Test
    @DisplayName("Получение списка заказов с авторизацией пользователя")
    public void getListOfOrdersWithAuth() {
        ValidatableResponse responseOrder = orderClient.getOrdersWithAuth(userRegistrationInfo);
        OrdersCustomer ordersCustomer = orderClient.getResponseAboutListOfOrders(responseOrder);
        orderChecks.checkOrdersWithAuth(ordersCustomer);
    }
}