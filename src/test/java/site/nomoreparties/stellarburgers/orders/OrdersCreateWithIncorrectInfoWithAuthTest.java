package site.nomoreparties.stellarburgers.orders;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.Constants;
import site.nomoreparties.stellarburgers.users.*;

public class OrdersCreateWithIncorrectInfoWithAuthTest {
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
    @DisplayName("Создание заказа с неправильным хэшем ингредиентов авторизованным пользователем")
    public void createNewOrderWithIncorrectDataWithAuth() {
        OrderIngredients orderIngredients = new OrderIngredients(Constants.BURGER_WITH_DEFECT_INGREDIENTS);
        ValidatableResponse responseOrder = orderClient.createNewOrderWithAuth(orderIngredients, userRegistrationInfo);
        orderChecks.checkCreateOrderWithIncorrectData(responseOrder);
    }

    @Test
    @DisplayName("Создание заказа с пустым списком ингредиентов авторизованным пользователем")
    public void createNewOrderWithEmptyDataWithAuth() {
        OrderIngredients orderIngredients = new OrderIngredients(Constants.BURGER_WITHOUT_INGREDIENTS);
        ValidatableResponse responseOrder = orderClient.createNewOrderWithAuth(orderIngredients, userRegistrationInfo);
        orderChecks.checkCreateOrderWithNullData(responseOrder);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов авторизованным пользователем")
    public void createNewOrderWithNullDataWithAuth() {
        OrderIngredients orderIngredients = new OrderIngredients(Constants.BURGER_NULL);
        ValidatableResponse responseOrder = orderClient.createNewOrderWithAuth(orderIngredients, userRegistrationInfo);
        orderChecks.checkCreateOrderWithNullData(responseOrder);
    }
}