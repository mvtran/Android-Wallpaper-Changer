package com.example.michael.wallpaperchanger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class ScheduleRecyclerViewFragment extends Fragment {

    private static final String TAG = "From Fragment";

    private RecyclerView                    recyclerView;
    private RecyclerView.Adapter            adapter;
    private RecyclerView.LayoutManager      layoutManager;
    private OnFragmentInteractionListener   mListener;
    public  FloatingActionButton            fab;

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

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        ItemData itemsData[] = {
                new ItemData("4:20 AM", R.drawable.house),
                new ItemData("6:00 PM", R.drawable.person),
                new ItemData("5:55 PM", R.drawable.the_s)
        };

        WallpaperScheduleAdapter.OnItemClickListener listener = new WallpaperScheduleAdapter.OnItemClickListener() {
            @Override
            public void onClick(ItemData item) {
                ((MainActivity)getActivity()).onScheduleClicked(item.getText(), item.getImageURL());
            }
        };
        adapter = new WallpaperScheduleAdapter(itemsData, listener);
        recyclerView.setAdapter(adapter);
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

    private void loadFab() {
        if (fab == null)
            fab = (FloatingActionButton)getActivity().findViewById(R.id.fab);
        fab.show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onScheduleClicked(String time, int imageURL);
    }
}
