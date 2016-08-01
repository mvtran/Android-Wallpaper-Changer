package com.example.michael.wallpaperchanger;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class WallpaperScheduleAdapter extends RecyclerView.Adapter<WallpaperScheduleAdapter.ViewHolder> {
    private ItemData[] itemsData;

    public WallpaperScheduleAdapter(ItemData[] itemsData) {
        this.itemsData = itemsData;
    }

    // The ViewHolder caches the Views we get from findViewById for each row's Views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public ImageView icon;
        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            text = (TextView) itemLayoutView.findViewById(R.id.item_title);
            icon = (ImageView) itemLayoutView.findViewById(R.id.item_icon);
        }
    }

    // Create new Views (invoked by layoutmanager)
    @Override
    public WallpaperScheduleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        // Create a new View
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                                            .inflate(R.layout.item_layout, parent);
        return new ViewHolder(itemLayoutView);
    }

    // Called when actually changing the contents inside the ViewHolder
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int pos) {
        // Get data from ItemData itemsData[pos], then replace contents
        // of the View with that ItemData
        viewHolder.text.setText(itemsData[pos].getText());
        viewHolder.icon.setImageResource(itemsData[pos].getImageURL());
    }

    // Return size of itemsData (invoked by layoutmanager)
    @Override
    public int getItemCount() {
        return itemsData.length;
    }
}
