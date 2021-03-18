package de.codingforce.wad.item;

import java.io.Serializable;

public class ItemIngredients implements Serializable {

    private String ingredient;
    private String amount;
    private String unit;

    public ItemIngredients() {
    }

    public ItemIngredients(String ingredient, String amount, String unit) {
        this.ingredient = ingredient;
        this.amount = amount;
        this.unit = unit;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
