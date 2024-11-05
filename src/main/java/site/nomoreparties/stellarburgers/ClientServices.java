package site.nomoreparties.stellarburgers;

import org.apache.commons.lang3.StringUtils;
import site.nomoreparties.stellarburgers.ingredients.Ingredient;
import site.nomoreparties.stellarburgers.ingredients.ListOfIngredients;

import java.util.ArrayList;
import java.util.HashMap;

public class ClientServices {
    public String trimAccessToken(String accessToken) {
        return StringUtils.substringAfter(accessToken, " ");
    }

    public String[] createListOfBurgerIngredients(ListOfIngredients listOfIngredients, String[] newBurger) {
        HashMap<String, String> buns = new HashMap<>();
        HashMap<String, String> sauces = new HashMap<>();
        HashMap<String, String> toppings = new HashMap<>();

        for (Ingredient ingredient : listOfIngredients.getData()) {
            switch (ingredient.getType()) {
                case "bun":
                    buns.put(ingredient.getName(), ingredient.get_id());
                    break;

                case "sauce":
                    sauces.put(ingredient.getName(), ingredient.get_id());
                    break;

                case "main":
                    toppings.put(ingredient.getName(), ingredient.get_id());
                    break;

                default:
                    break;
            }
        }

        ArrayList<String> burger = new ArrayList<>();
        for (String burgerIngredient : newBurger) {
            if (buns.get(burgerIngredient) != null) {
                burger.add(buns.get(burgerIngredient));
            } else if (sauces.get(burgerIngredient) != null) {
                burger.add(sauces.get(burgerIngredient));
            } else if (toppings.get(burgerIngredient) != null) {
                burger.add(toppings.get(burgerIngredient));
            }
        }

        return burger.toArray(new String[0]);
    }
}