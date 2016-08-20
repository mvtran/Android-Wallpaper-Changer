package com.example.michael.wallpaperchanger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;


public class ScheduleRecyclerViewFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    ItemTouchHelper itemTouchHelper;

    private static final String TAG = "blah";

    private OnFragmentInteractionListener mListener;
    public  FloatingActionButton fab;

    public ScheduleRecyclerViewFragment() {
        // Required empty public constructor
    }

    /**
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduleRecyclerViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleRecyclerViewFragment newInstance(String param1, String param2) {
        ScheduleRecyclerViewFragment fragment = new ScheduleRecyclerViewFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // Called when graphically initializing the view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule_recycler_view, container, false);
        loadFab();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.schedules_recycler_view);
        recyclerView.setHasFixedSize(true); // Set this to improve performance

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<ItemData> scheduleList = new ArrayList<>();
        populateScheduleList(scheduleList);

        WallpaperScheduleAdapter.OnItemClickListener listener = new WallpaperScheduleAdapter.OnItemClickListener() {
            @Override
            public void onClick(ItemData item) {
                // Passes these arguments to ScheduleDetailsFragment.newInstance(String time, String imageUriString)
                ((MainActivity)getActivity()).onScheduleClicked(item.getText(), item.getImageUri());
            }
        };
        adapter = new WallpaperScheduleAdapter(scheduleList, listener);
        recyclerView.setAdapter(adapter);

        setupItemTouchHelper();
        setUpAnimationDecoratorHelper();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // TODO: sort by time
    private void populateScheduleList(ArrayList<ItemData> items) {
        SharedPreferences prefs = getActivity().getSharedPreferences(MainActivity.ALARM_PREFS, 0);
        Map<String,?> all = prefs.getAll();
        Iterator it = all.entrySet().iterator();

        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            /*
                Key = schedule time as string in the format "schedule-X:XX [AM|PM]". this is where
                      we get the time
                Value = string representation of uri for image
            */
            String[] key = ((String)pair.getKey()).split("-");
            String time = key[1];
            Uri image = Uri.parse((String)pair.getValue());
            Log.d(TAG, "(" + time + ", " + image.toString() + ")");
            items.add(new ItemData(time, image));
            it.remove();
        }
    }

    private void loadFab() {
        if (fab == null)
            fab = (FloatingActionButton)getActivity().findViewById(R.id.fab);
        fab.show();
        // Temporary
        Button clear = (Button)getActivity().findViewById(R.id.temp_clear_button);
        clear.setVisibility(View.VISIBLE);
    }

    public interface OnFragmentInteractionListener {
        void onScheduleClicked(String time, Uri imageUri);
    }

    private void setupItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                xMark = ContextCompat.getDrawable(getContext(), R.drawable.castform);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int)getContext().getResources().getDimension(R.dimen.clear_margin);
                initiated = true;
            }

            @Override
            public boolean onMove(RecyclerView rv, RecyclerView.ViewHolder vh, RecyclerView.ViewHolder target) {
                Log.d(TAG, "onMove()");
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                WallpaperScheduleAdapter adapter = (WallpaperScheduleAdapter)recyclerView.getAdapter();
                if (adapter.isUndoOn() && adapter.isPendingRemoval(position))
                    return 0;
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                WallpaperScheduleAdapter theAdapter = (WallpaperScheduleAdapter)adapter;
                boolean undoOn = theAdapter.isUndoOn();
                if (undoOn)
                    theAdapter.pendingRemoval(swipedPosition);
                else
                    theAdapter.remove(swipedPosition);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // unknown why, gets called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1)
                    return;
                if (!initiated)
                    init();

                // Draw red background
                background.setBounds(itemView.getRight() + (int)dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // Draw X mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth(); // TODO: typo or not?

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    /* Setup ItemDecorator to draw red background in empty space while items
        are animating to their new positions after an item is removed.
     */
    private void setUpAnimationDecoratorHelper() {
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            Drawable background;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                initiated = true;
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                if (!initiated)
                    init();

                // Only if animation is in progress
                if (parent.getItemAnimator().isRunning()) {
                    View lastViewComingDown = null;
                    View firstViewComingUp = null;

                    int left = 0;
                    int right = parent.getWidth();

                    int top = 0;
                    int bottom = 0;

                    int childCount = parent.getLayoutManager().getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getLayoutManager().getChildAt(i);
                        if (child.getTranslationY() < 0) {
                            // view is coming down
                            lastViewComingDown = child;
                        } else if (child.getTranslationY() > 0) {
                            // view is coming up
                            if (firstViewComingUp == null)
                                firstViewComingUp = child;
                        }
                    }

                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        // views are coming down AND going up to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    } else if (lastViewComingDown != null) {
                        // views are going down to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = lastViewComingDown.getBottom();
                    } else if (firstViewComingUp != null) {
                        // views are coming up to fill the void
                        top = firstViewComingUp.getTop();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    }

                    background.setBounds(left, top, right, bottom);
                    background.draw(c);
                }
                super.onDraw(c, parent, state);
            }
        });
    }
}
