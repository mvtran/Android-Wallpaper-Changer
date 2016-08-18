package com.example.michael.wallpaperchanger;

import android.net.Uri;
import android.util.Log;

/*
 * A class that represents a schedule.
 */
public class ItemData {
    private String prefKey;
    private String text;
    private int imageId; // android resource id, e.g. R.drawable.picture
    private Uri imageUri;

    public ItemData(String text, int imageId) {
        this.text = text;
        this.imageId = imageId;
        this.prefKey = Utility.timeAsPrefKey(text);
    }

    public ItemData(String text, Uri imageUri) {
        this.text = text;
        this.imageUri = imageUri;
        this.prefKey = Utility.timeAsPrefKey(text);
    }

    public ItemData(String text, String imageUriString) {
        this.text = text;
        this.imageUri = Uri.parse(imageUriString);
        this.prefKey = Utility.timeAsPrefKey(text);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getPrefKey() { return prefKey; }
}
