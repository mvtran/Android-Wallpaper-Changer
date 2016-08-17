package com.example.michael.wallpaperchanger;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.content.ContextCompat;

import com.squareup.picasso.Picasso;

public class ScheduleDetailsFragment extends Fragment {
    // the fragment initialization parameters
    static final String ARG_CHANGING_TIME = "com.example.michael.wallpaperchanger.changingTime";
    static final String ARG_WALLPAPER = "com.example.michael.wallpaperchanger.currentWallpaper";

    // request codes
    static final int PICK_IMAGE = 1;
    static final int PERMISSIONS_CODE = 2;

    static final String TAG = "ScheduleDetails";

    private ImageView wallpaperImageView = null;
    private TextView timeTv;
    private String changingTime;
    private Uri wallpaperImage;

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
        } else {
            returnToScheduleList();
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
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
        setTime(changingTime);
        loadImage(wallpaperImage);

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
                Toast.makeText(getContext(), "data is null", Toast.LENGTH_SHORT).show();
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

    // Seems to never get called for some reason
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doChooseImage();
                } else {
                    Toast.makeText(getContext(), "Permission denied to choose image :(", Toast.LENGTH_SHORT).show();
                }
            }
        }
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
        DialogFragment fragment = TimePickerFragment.newInstance(changingTime);
        fragment.show(getActivity().getSupportFragmentManager(), "timePicker");
    }

    //android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    public void doChooseImage() {
        Intent intent;
        // Can choose from documents and Google Drive
        if (Build.VERSION.SDK_INT >= 19)
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        // Can only choose from Gallery app
        else
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    public void chooseImage() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Determine if we need an explanation. This happens if the user has previously denied the request.
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(getContext(), "I need your permission yo", Toast.LENGTH_SHORT).show();
            } else {
                // No explanation needed, we can request the permission.
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_CODE);
            }
        } else { // Permission already granted
            doChooseImage();
        }
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
                    .resize(750,1000)
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
