package com.example.nature_hunt;

public class HuntRecyclerItemModel {
    private String name;
    private int image_drawable;
    private Hunt hunt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage_drawable() {
        return image_drawable;
    }

    public void setImage_drawable(int image_drawable) {
        this.image_drawable = image_drawable;
    }

    public Hunt getHunt() { return hunt; }

    public void setHunt(Hunt hunt) {
        this.hunt = hunt;
    }
}
