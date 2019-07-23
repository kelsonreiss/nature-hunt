package com.example.nature_hunt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.example.nature_hunt.db.DatabaseWrapper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private Button seePreview;
    private FloatingActionButton camButton;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String imageFilePath = "";

    private static Context context;

    private static List<Hunt> searchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();

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

        searchList = new ArrayList<>();
        PopulateHuntSearchList();
        ArrayAdapter<Hunt> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, searchList);
        AutoCompleteTextView textView = findViewById(R.id.HuntsSearchBar);
        textView.setAdapter(adapter);
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                Object item = parent.getItemAtPosition(position);
                if (item instanceof Hunt){
                    Hunt hunt=(Hunt) item;

                    // TODO: Have HuntPreviewFrag accept selected hunt object
                    HuntPreviewFrag previewFrag = HuntPreviewFrag.newInstance();
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.add(android.R.id.content, previewFrag)
                            .addToBackStack(null).commit();
                }
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

    private void PopulateHuntSearchList() {

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    DatabaseWrapper dbWrapper = new DatabaseWrapper(context);
                    final Map<Integer, Hunt> huntsMap = dbWrapper.getHuntsMap();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (Hunt hunt : huntsMap.values()) {
                                searchList.add(hunt);
                            }
                        }
                    });
                } catch (final Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        runAsyncTask(task);
    }

    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }
}
