package site.nomoreparties.stellarburgers;

public class Constants {
    public static final String BASE_URI = "https://stellarburgers.nomoreparties.site/api/";
    public static final String REGISTRATION_PATH = "auth/register";
    public static final String DELETE_UPDATE_PATH = "auth/user";
    public static final String LOGIN_PATH = "auth/login";
    public static final String LOGOUT_PATH = "auth/logout";
    public static final String ORDERS_PATH = "orders";

    public static final String DEFAULT_EMAIL = "test@mail.doc";
    public static final String DEFAULT_PASSWORD = "548%*(nsdkjb";
    public static final String DEFAULT_NAME = "Stepan";

    public static final String NEW_EMAIL = "tst@tst.fjk.com";
    public static final String NEW_PASSWORD = "JGD754@#$";
    public static final String NEW_NAME = "tst user";

    public static final String[] BURGER_TWO_INGREDIENTS =  new String[] {"61c0c5a71d1f82001bdaaa79",
                                                                         "61c0c5a71d1f82001bdaaa6c"};

    public static final String[] BURGER_FIVE_INGREDIENTS =  new String[] {"61c0c5a71d1f82001bdaaa73",
                                                                         "61c0c5a71d1f82001bdaaa70",
                                                                         "61c0c5a71d1f82001bdaaa79",
                                                                         "61c0c5a71d1f82001bdaaa78",
                                                                         "61c0c5a71d1f82001bdaaa6d"};

    public static final String[] BURGER_WITH_DEFECT_INGREDIENTS =  new String[] {"DDD7c5a71d1f82001bdaaa79",
                                                                                 "HJU!c5a71d1f82001bdaaa6c"};

    public static final String[] BURGER_WITHOUT_INGREDIENTS = new String[] {};

    public static final String[] BURGER_NULL = null;
}