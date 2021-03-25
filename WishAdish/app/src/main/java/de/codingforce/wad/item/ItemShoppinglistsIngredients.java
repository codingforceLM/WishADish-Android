package de.codingforce.wad.item;

public class ItemShoppinglistsIngredients {
    String id;
    String name;
    String amount;
    String unit;
    String done;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setDone(String done) {
        this.done = done;
    }

    public String getAmount() {
        return amount;
    }

    public String getUnit() {
        return unit;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDone() {
        return done;
    }
}
