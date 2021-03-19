package de.codingforce.wad.item;

public class ItemIngredient {
    private String id;
    private String name;
    private String userID;

    public ItemIngredient(String name, String userID) {
        this.name = name;
        this.userID = userID;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUserID() {
        return userID;
    }
}
