package com.example.michael.wallpaperchanger;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScheduleRecyclerViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScheduleRecyclerViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleRecyclerViewFragment extends Fragment {

    private static final String TAG = "From Fragment";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private OnFragmentInteractionListener mListener;

    // Required empty public constructor
    public ScheduleRecyclerViewFragment() {
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
        Log.d(TAG, "inside onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_schedule_recycler_view, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.schedules_recycler_view);
        recyclerView.setHasFixedSize(true); // Set this to improve performance

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        ItemData itemsData[] = {
                new ItemData("a house", R.drawable.house),
                new ItemData("a person", R.drawable.person),
                new ItemData("WHOA", R.drawable.the_s)
        };

        adapter = new WallpaperScheduleAdapter(itemsData);
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int position) {
        if (mListener != null) {
            mListener.onScheduleClicked(position);
        }
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
        // TODO: Update argument type and name
        void onScheduleClicked(int position);
    }
}
