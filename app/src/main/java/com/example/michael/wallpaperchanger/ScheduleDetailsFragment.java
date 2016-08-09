package com.example.michael.wallpaperchanger;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ScheduleDetailsFragment extends Fragment {
    // the fragment initialization parameters
    public static final String ARG_CHANGING_TIME = "com.example.michael.wallpaperchanger.changingTime";
    public static final String ARG_WALLPAPER = "com.example.michael.wallpaperchanger.currentWallpaper";

    private ImageView wallpaperImageView = null;
    private TextView timeTv;

    private String changingTime;
    private int currentWallpaperId = R.drawable.placeholder_wallpaper_full;

    private OnFragmentInteractionListener mListener;

    public ScheduleDetailsFragment() {
        // Required empty public constructor
    }

    public static ScheduleDetailsFragment newInstance(String time, int imageURL) {
        ScheduleDetailsFragment fragment = new ScheduleDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CHANGING_TIME, time);
        args.putInt(ARG_WALLPAPER, imageURL);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            changingTime = getArguments().getString(ARG_CHANGING_TIME);
            currentWallpaperId = getArguments().getInt(ARG_WALLPAPER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        changingTime = getArguments().getString(ARG_CHANGING_TIME);
        currentWallpaperId = getArguments().getInt(ARG_WALLPAPER);

        View root = inflater.inflate(R.layout.fragment_schedule_details, container, false);
        wallpaperImageView = (ImageView)root.findViewById(R.id.wallpaper_preview);
        timeTv = (TextView)root.findViewById(R.id.changing_time);

        timeTv.setText(changingTime);
        loadImage(currentWallpaperId);

        wallpaperImageView.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        Button confirmButton = (Button)root.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmSettings();
            }
        });
        return root;
    }

    public void confirmSettings() {
        Toast.makeText(getContext(), "To be implemented!", Toast.LENGTH_SHORT).show();
    }

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), ScheduleRecyclerViewFragment.PICK_IMAGE);
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

    public void loadImage(int imageURL) {
        if (wallpaperImageView != null)
            Picasso.with(getContext())
                    .load(imageURL)
                    .resize(500,500)
                    .into(wallpaperImageView);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentClicked();
    }
}
