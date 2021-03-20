package de.codingforce.wad.item;

public class ItemDishIngredients {
    private String id;
    private String amount;
    private String unit;

    public ItemDishIngredients(String id, String amount, String unit) {
        this.id = id;
        this.amount = amount;
        this.unit = unit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
