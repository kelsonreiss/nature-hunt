package com.example.practice_species_api;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class to manage taking photos in-app
 * Uses Android documentation
 * https://developer.android.com/training/camera/photobasics#java
 */
public class CamHelperFragment extends Fragment {

    private CamHelperViewModel mViewModel;
    private Button launchCamBtn;
    private ImageView preview;
    private String imageFilePath = "";

    static final int REQUEST_IMAGE_CAPTURE = 1;

    public static CamHelperFragment newInstance() {
        return new CamHelperFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.cam_helper_fragment, container, false);
        launchCamBtn = view.findViewById(R.id.launchCamBtnID);
        preview = view.findViewById(R.id.previewID);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CamHelperViewModel.class);
        // TODO: Use the ViewMode
        launchCamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
                mViewModel.launchCam();
            }
        });
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create File for photo
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e){
                e.printStackTrace();
            }

            if  (photoFile != null){
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.practice_species_api.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE ) {
            if (resultCode == Activity.RESULT_OK){
                Bitmap imageBitmap = BitmapFactory.decodeFile(imageFilePath);
                preview.setImageBitmap(imageBitmap);
            } else if (resultCode == Activity.RESULT_CANCELED){
                // HANDLE USER CANCELLED ACTION
            }

        }
    }

    private File createImageFile() throws IOException{
        // Create file name for each image based on timestamp
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+time+"_";
        File image = null;
        try {
            // Use getExternalStoragePublicDirectory if want to export photos to other apps
            File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            image = File.createTempFile(
                    imageFileName, /* prefix */
                    ".jpg", /* suffix */
                    storageDir /* directory */
            );
            imageFilePath = image.getAbsolutePath();
        } catch (Exception e){
            e.printStackTrace();
        }
        return image;
    }
}
