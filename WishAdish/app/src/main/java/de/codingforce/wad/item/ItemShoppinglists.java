package de.codingforce.wad.item;


import java.util.List;

public class ItemShoppinglists {
    private String id;

    private String name;

    private String done;

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
