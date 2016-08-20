package com.example.michael.wallpaperchanger;

import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class WallpaperScheduleAdapter extends RecyclerView.Adapter<WallpaperScheduleAdapter.ViewHolder> {

    private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3 sec

    public interface OnItemClickListener {void onClick(ItemData item);}

    private ArrayList<ItemData> items;
    private ArrayList<ItemData> itemsPendingRemoval;
    private OnItemClickListener listener;

    boolean undoOn = true;

    private Handler handler = new Handler();
    HashMap<ItemData, Runnable> pendingRunnables = new HashMap<>();

    // Constructor that takes as arguments the data that will be displayed as well as the listener
    public WallpaperScheduleAdapter(ArrayList<ItemData> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
        this.itemsPendingRemoval = new ArrayList<>();
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
        final ItemData item = items.get(pos);

        if (itemsPendingRemoval.contains(item)) {
            // Show the "undo" state of the row
            viewHolder.itemView.setBackgroundColor(Color.RED);
            viewHolder.text.setVisibility(View.GONE);
            viewHolder.undoButton.setVisibility(View.VISIBLE);
            viewHolder.undoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // User wants to undo, so cancel the pending task
                    Runnable pendingRemovalRunnable = pendingRunnables.get(item);
                    pendingRunnables.remove(item);
                    if (pendingRemovalRunnable != null) handler.removeCallbacks(pendingRemovalRunnable);
                    itemsPendingRemoval.remove(item);
                    // Rebind the row to "normal" state
                    notifyItemChanged(items.indexOf(item));
                }
            });
        } else {
            // "normal" state
            viewHolder.showNormalState(items.get(pos), listener);
        }
        //viewHolder.bind(items.get(pos), listener);
    }

    // Return size of itemsData (invoked by layoutmanager)
    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setUndoOn(boolean undoOn) {
        this.undoOn = undoOn;
    }

    public boolean isUndoOn() {
        return undoOn;
    }

    public void pendingRemoval(int position) {
        System.out.println("pendingRemoval("+position+")");
        final ItemData item = items.get(position);
        if (!itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.add(item);
            // Rebind row to "undo" state
            notifyItemChanged(position);
            // Create, store, and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(items.indexOf(item));
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(item, pendingRemovalRunnable);
        }
    }

    public void remove(int position) {
        System.out.println("remove("+position+")");
        ItemData item = items.get(position);
        if (itemsPendingRemoval.contains(item))
            itemsPendingRemoval.remove(item);

        if (items.contains(item)) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    public boolean isPendingRemoval(int position) {
        ItemData item = items.get(position);
        return itemsPendingRemoval.contains(item);
    }

    // The ViewHolder caches the Views we get from findViewById for each row's Views
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView text;
        public ImageView icon;
        public Button undoButton;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            text = (TextView) itemLayoutView.findViewById(R.id.item_title);
            icon = (ImageView) itemLayoutView.findViewById(R.id.item_icon);
            undoButton = (Button) itemLayoutView.findViewById(R.id.undo_button);
        }

        // Get data from items[pos], then replace contents of the View with that item
        public void showNormalState(final ItemData item, final OnItemClickListener listener) {
            itemView.setBackgroundColor(Color.WHITE);
            text.setVisibility(View.VISIBLE);
            text.setText(item.getText());
            undoButton.setVisibility(View.GONE);
            undoButton.setOnClickListener(null);

            Picasso.Builder builder = new Picasso.Builder(itemView.getContext());
            builder.listener(new Picasso.Listener()
            {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                {
                    exception.printStackTrace();
                }
            });
            builder.build()
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
