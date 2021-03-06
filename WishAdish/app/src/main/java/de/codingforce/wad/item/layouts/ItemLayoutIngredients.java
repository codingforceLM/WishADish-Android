package de.codingforce.wad.item.layouts;

public class ItemLayoutIngredients {
    private String mText1;
    private String mText2;
    private boolean done;

    public ItemLayoutIngredients(String mText1, String mText2, boolean done) {
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

    public void setmText1(String mText1) {
        this.mText1 = mText1;
    }

    public void setmText2(String mText2) {
        this.mText2 = mText2;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
