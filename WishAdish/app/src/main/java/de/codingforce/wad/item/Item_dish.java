package de.codingforce.wad.item;

import java.util.List;

public class Item_dish {
    String id;
    String name;
    private List<Item_shoppinglists_ingredients> ingredients;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Item_shoppinglists_ingredients> getIngredients() {
        return ingredients;
    }
}
