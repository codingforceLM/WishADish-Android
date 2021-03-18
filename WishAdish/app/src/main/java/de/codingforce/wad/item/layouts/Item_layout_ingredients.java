package de.codingforce.wad.item.layouts;

public class Item_layout_ingredients {
    private String mText1;
    private String mText2;
    private boolean done;

    public Item_layout_ingredients(String mText1, String mText2, boolean done) {
        this.mText1 = mText1;
        this.mText2 = mText2;
        this.done = done;
    }

    public String getmText1() {
        return mText1;
    }

    public String getmText2() {
        return mText2;
    }

    public boolean isDone() {
        return done;
    }
}
