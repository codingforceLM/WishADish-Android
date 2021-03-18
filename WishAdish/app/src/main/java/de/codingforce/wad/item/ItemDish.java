package de.codingforce.wad.item;

import java.util.List;

public class ItemDish {
    String id;
    String name;
    private List<ItemShoppinglistsIngredients> ingredients;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<ItemShoppinglistsIngredients> getIngredients() {
        return ingredients;
    }
}
