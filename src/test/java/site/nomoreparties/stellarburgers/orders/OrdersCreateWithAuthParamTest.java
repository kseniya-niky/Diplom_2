package site.nomoreparties.stellarburgers.orders;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import site.nomoreparties.stellarburgers.ClientServices;
import site.nomoreparties.stellarburgers.Constants;
import site.nomoreparties.stellarburgers.ingredients.ListOfIngredients;
import site.nomoreparties.stellarburgers.users.*;

@RunWith(Parameterized.class)
public class OrdersCreateWithAuthParamTest {
    private String[] ingredients;
    private static OrderClient orderClient = new OrderClient();
    private static ClientServices clientServices = new ClientServices();
    private static ListOfIngredients listOfIngredients;
    private OrderChecks orderChecks = new OrderChecks();
    private static UserClient userClient = new UserClient();
    private static UserChecks userChecks = new UserChecks();
    private static NewUserRegistrationInfo userRegistrationInfo = new NewUserRegistrationInfo();

    public OrdersCreateWithAuthParamTest(String[] ingredients) {
        this.ingredients = ingredients;
    }

    static {
        ValidatableResponse responseData = orderClient.getListOfIngredients();
        listOfIngredients = orderClient.getResponseAboutListOfIngredients(responseData);
    }

    private static String[] getBurgerHash(ListOfIngredients listOfIngredients, String[] burger) {
        return clientServices.createListOfBurgerIngredients( listOfIngredients, burger);
    }

    @Parameterized.Parameters
    public static final Object[][] getListOfIngredients() {
        return new Object[][]{
                { getBurgerHash(listOfIngredients, Constants.BURGER_TWO_INGREDIENTS) },
                { getBurgerHash(listOfIngredients, Constants.BURGER_FIVE_INGREDIENTS) },
                { Constants.BURGER_WITH_DEFECT_INGREDIENTS },
                { Constants.BURGER_WITHOUT_INGREDIENTS },
                { Constants.BURGER_NULL }
        };
    }

    @BeforeClass
    public static void createUser() {
        NewUser newUser = NewUser.random();
        ValidatableResponse response = userClient.createNewUser(newUser);
        userRegistrationInfo = userClient.getResponseAboutNewUser(response);
        userChecks.checkCreatedUser(userRegistrationInfo, newUser);
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем с разным набором ингредиентов")
    public void createNewOrderWithAuthPositiveAndNegative() {
        OrderIngredients orderIngredients = new OrderIngredients(ingredients);
        ValidatableResponse response = orderClient.createNewOrderWithAuth(orderIngredients, userRegistrationInfo);
        OrderCreated orderCreated = orderClient.getResponseAboutCreatedOrder(response);
        orderChecks.checkCreatedOrderSuccess(orderCreated);
    }
    @AfterClass
    public static void deleteUser() {
        if(userRegistrationInfo.getAccessToken() != null) {
            if (!userRegistrationInfo.getAccessToken().isBlank()) {
                userClient.deleteUser(userRegistrationInfo);
            }
        }
    }
}