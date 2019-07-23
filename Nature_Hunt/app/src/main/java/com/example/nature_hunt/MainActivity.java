package com.example.nature_hunt;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private Button seePreview;
    private FloatingActionButton camButton;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String imageFilePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextMessage = findViewById(R.id.message);
        camButton = findViewById(R.id.launch_cam_button);

        camButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        seePreview = findViewById(R.id.preview_button);
        seePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HuntPreviewFrag previewFrag = HuntPreviewFrag.newInstance();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(android.R.id.content, previewFrag)
                        .addToBackStack(null).commit();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE ) {
            if (resultCode == Activity.RESULT_OK){
                Bitmap imageBitmap = BitmapFactory.decodeFile(imageFilePath);
                //preview.setImageBitmap(imageBitmap);
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
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create File for photo
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e){
                e.printStackTrace();
            }

            if  (photoFile != null){
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.nature_hunt.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

}
