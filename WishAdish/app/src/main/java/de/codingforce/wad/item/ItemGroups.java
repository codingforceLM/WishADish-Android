package de.codingforce.wad.item;

import java.util.List;

public class ItemGroups {
    private String id;
    private String title;
    private String creation;
    private List<ItemGroup> user;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCreation() {
        return creation;
    }

    public List<ItemGroup> getUser() {
        return user;
    }
}
