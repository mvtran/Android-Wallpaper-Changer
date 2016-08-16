package com.example.michael.wallpaperchanger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
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
    public static final int PICK_IMAGE = 1; // request code for image chooser

    private ImageView wallpaperImageView = null;
    private TextView timeTv;

    private String changingTime;
    private Uri wallpaperImage;

    static final String TAG = "ScheduleDetails";

    private OnFragmentInteractionListener mListener;

    public ScheduleDetailsFragment() {
        // Required empty public constructor
    }

    public static ScheduleDetailsFragment newInstance(String time, String imageUriString) {
        ScheduleDetailsFragment fragment = new ScheduleDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CHANGING_TIME, time);
        args.putString(ARG_WALLPAPER, imageUriString);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            changingTime = getArguments().getString(ARG_CHANGING_TIME);
            wallpaperImage = Uri.parse(getArguments().getString(ARG_WALLPAPER));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the root view then get references to the needed views
        View root = inflater.inflate(R.layout.fragment_schedule_details, container, false);
        wallpaperImageView = (ImageView)root.findViewById(R.id.wallpaper_preview);
        timeTv = (TextView)root.findViewById(R.id.changing_time);
        Button confirmButton = (Button)root.findViewById(R.id.confirm_button);
        Button cropButton = (Button)root.findViewById(R.id.crop_button);

        // Fill in the Text/Image views
        getAndSetScheduleDetails(root);

        // Set listeners
        wallpaperImageView.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        timeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickTime();
            }
        });
        confirmButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmSettings();
            }
        });
        cropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crop();
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(getContext(), "ya done fucked up A-A-ron (data is null)", Toast.LENGTH_SHORT).show();
            } else {
                // TODO: what happens if you move the image's location?
                Uri selectedImage = data.getData();
                wallpaperImage = selectedImage;
                loadImage(selectedImage);
            }
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

    public void getAndSetScheduleDetails(View root) {
        changingTime = getArguments().getString(ARG_CHANGING_TIME);
        wallpaperImage = Uri.parse(getArguments().getString(ARG_WALLPAPER));
        setTime(changingTime);
        loadImage(wallpaperImage);
    }

    public void returnToScheduleList() {
        MainActivity activity = (MainActivity)getActivity();
        activity.showScheduleList();
    }

    public void confirmSettings() {
        SharedPreferences prefs = getActivity().getSharedPreferences(MainActivity.ALARM_PREFS, 0);
        SharedPreferences.Editor editor = prefs.edit();

        // TODO: check for duplicates while ignoring 12hour/24hour format
        String key = "schedule-"+changingTime;
        if (prefs.contains(key)) {
            Toast.makeText(getContext(), "Schedule for " + changingTime + " already exists", Toast.LENGTH_SHORT).show();
        } else {
            String imageUriString = wallpaperImage.toString();
            editor.putString(key, imageUriString);
            editor.apply();
            returnToScheduleList();
            Toast.makeText(getContext(), "Saved schedule for " + changingTime, Toast.LENGTH_SHORT).show();
        }
    }

    public void pickTime() {
        DialogFragment fragment = new TimePickerFragment();
        fragment.show(getActivity().getSupportFragmentManager(), "timePicker");
    }

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    public void setTime(String time) {
        changingTime = time;
        if (timeTv != null)
            timeTv.setText(time);
    }

    public void crop() {
        Toast.makeText(getContext(), "To be implemented!", Toast.LENGTH_SHORT).show();
    }

    public void loadImage(Uri imageUri) {
        if (wallpaperImageView != null)
            Picasso.with(getContext())
                    .load(imageUri)
                    .error(R.drawable.error)
                    .resize(500,500)
                    .into(wallpaperImageView);
    }

    public void loadImage(int imageId) {
        if (wallpaperImageView != null)
            Picasso.with(getContext())
                    .load(imageId)
                    .error(R.drawable.error)
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
