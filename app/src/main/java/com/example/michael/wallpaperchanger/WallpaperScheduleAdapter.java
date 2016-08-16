package com.example.michael.wallpaperchanger;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WallpaperScheduleAdapter extends RecyclerView.Adapter<WallpaperScheduleAdapter.ViewHolder> {

    public interface OnItemClickListener {void onClick(ItemData item);}

    private ArrayList<ItemData> items;
    private OnItemClickListener listener;

    // Constructor that takes as arguments the data that will be displayed as well as the listener
    public WallpaperScheduleAdapter(ArrayList<ItemData> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    /*
        The three mandatory methods to implement
     */
    // Create new Views (invoked by layoutmanager)
    @Override
    public WallpaperScheduleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        // Create a new View
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, null);
        return new ViewHolder(itemLayoutView);
    }

    // Called when actually filling in the contents inside the ViewHolder
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int pos) {
        viewHolder.bind(items.get(pos), listener);
    }

    // Return size of itemsData (invoked by layoutmanager)
    @Override
    public int getItemCount() {
        return items.size();
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

        // Get data from items[pos], then replace contents of the View with that ItemData
        public void bind(final ItemData item, final OnItemClickListener listener) {
            text.setText(item.getText());
            Picasso.with(itemView.getContext())
                    .load(item.getImageUri())
                    .error(R.drawable.error)
                    .resize(50,50)
                    .into(icon);

            // Set the onClick method of this view to be the onClick method implemented in
            // ScheduleRecyclerViewFragment.java when setting the adapter.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(item);
                }
            });
        }
    }
}
