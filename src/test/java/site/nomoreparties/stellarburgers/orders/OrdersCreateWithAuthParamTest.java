package site.nomoreparties.stellarburgers.orders;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import site.nomoreparties.stellarburgers.Constants;
import site.nomoreparties.stellarburgers.orders.OrderChecks;
import site.nomoreparties.stellarburgers.orders.OrderClient;
import site.nomoreparties.stellarburgers.orders.OrderCreated;
import site.nomoreparties.stellarburgers.orders.OrderIngredients;
import site.nomoreparties.stellarburgers.users.UserChecks;
import site.nomoreparties.stellarburgers.users.UserClient;
import site.nomoreparties.stellarburgers.users.UserLogin;
import site.nomoreparties.stellarburgers.users.UserLoginInfo;

@RunWith(Parameterized.class)
public class OrdersCreateWithAuthParamTest {
    private String[] ingredients;
    private OrderClient orderClient = new OrderClient();
    private OrderChecks orderChecks = new OrderChecks();
    private static UserClient userClient = new UserClient();
    private static UserChecks userChecks = new UserChecks();
    private static String accessToken, refreshToken;

    public OrdersCreateWithAuthParamTest(String[] ingredients) {
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

    @BeforeClass
    public static void loginUser() {
        UserLogin userLogin = new UserLogin(Constants.DEFAULT_EMAIL, Constants.DEFAULT_PASSWORD);
        ValidatableResponse response = userClient.loginUser(userLogin);
        UserLoginInfo userLoginInfo = userClient.getResponseAboutLogin(response);
        userChecks.checkLoginDefaultUser(userLoginInfo);
        accessToken = userLoginInfo.getAccessToken();
        refreshToken = userLoginInfo.getRefreshToken();
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем с разным набором ингредиентов")
    public void createNewOrderWithAuthPositiveAndNegative() {
        OrderIngredients orderIngredients = new OrderIngredients(ingredients);
        ValidatableResponse response = orderClient.createNewOrderWithAuth(orderIngredients, accessToken);
        OrderCreated orderCreated = orderClient.getResponseAboutCreatedOrder(response);
        orderChecks.checkCreatedOrderSuccess(orderCreated);
    }

    @AfterClass
    public static void logoutUser() {
        if(!refreshToken.isBlank()) {
            ValidatableResponse response = userClient.logoutUser(accessToken, refreshToken);
            userChecks.checkLogoutUser(response);
        }
    }
}