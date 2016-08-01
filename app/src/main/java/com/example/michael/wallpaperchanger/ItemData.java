package com.example.michael.wallpaperchanger;

public class ItemData {
    private String text;
    private int imageURL;

    public ItemData(String text, int imageURL) {
        this.text = text;
        this.imageURL = imageURL;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImageURL() {
        return imageURL;
    }

    public void setImageURL(int imageURL) {
        this.imageURL = imageURL;
    }
}
