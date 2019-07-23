package com.example.nature_hunt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import com.example.nature_hunt.db.DatabaseWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private Button seePreview;
    private FloatingActionButton camButton;

    static final int REQUEST_TAKE_PHOTO = 1;

    private static class PostParams {
        Context context;
        String filePath;

        PostParams(Context context, String filePath) {
            this.context = context;
            this.filePath = filePath;
        }
    }

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
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("hunt", hunt);

                    // TODO: Have HuntPreviewFrag accept selected hunt object
                    HuntPreviewFrag previewFrag = HuntPreviewFrag.newInstance();
                    previewFrag.setArguments(bundle);
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
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK){
            System.out.println("mCurrentPhotoPath" + mCurrentPhotoPath);
            try{
                PostParams postParams = new PostParams(this, mCurrentPhotoPath);
                new UploadFileTask().execute(postParams);
            }
            catch(Exception e){
                Toast toast = Toast.makeText(this, "Error sending post request", Toast.LENGTH_LONG);
                toast.show();
            }
        }
        else{
            Toast toast = Toast.makeText(this, "Picture was not taken", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    String mCurrentPhotoPath;

    public String postImage(String filepath) throws Exception {
        HttpURLConnection connection = null;
        File file = new File(filepath);
        FileInputStream fileInputStream = new FileInputStream(file);
        URL url = new URL("https://aiforearth.azure-api.net/species-recognition/v0.1/predict?predictMode=classifyOnly");
        connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod("POST");

        String apiKey = "949e7dc4318041af961810828dadcf05";
        connection.setRequestProperty("Content-Type","application/octet-stream");
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", apiKey);

        OutputStream outputStream = connection.getOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead = -1;

        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

//        System.out.println("Response message connection.getResponseMessage(): " + connection.getResponseMessage());

        int status = connection.getResponseCode();
        switch(status) {
            case (HttpURLConnection.HTTP_OK):
                System.out.println("Got a response");
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                connection.disconnect();
                fileInputStream.close();
                outputStream.flush();
                outputStream.close();

                System.out.println("response.toString(): " + response.toString());
                return response.toString();
            case (415):
                throw new Exception("Unsupported media type");
            case (413):
                throw new Exception("Request entity too large");
            default:
                throw new Exception("HttpURLConnection did not return HTTP_OK but " + status);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        getApplicationContext().getPackageName() + ".fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private class UploadFileTask extends AsyncTask<PostParams, Integer, String> {
        Context context;

        protected String doInBackground(PostParams... params) {
            context = params[0].context;
            String filePath = params[0].filePath;
            String result = null;
            try {
                result = postImage(filePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            publishProgress(100);

            return result;
        }

        // this is called whenever you call puhlishProgress(Integer), for example when updating a progressbar when downloading stuff
        protected void onProgressUpdate(Integer... progress) {
//            setProgressPercent(progress[0]);
        }

        protected void onPostExecute(String result) {
            System.out.println(result);
            String confidence = "";
            String species_common = "";
            try {
                JSONObject jObject = new JSONObject(result);
                JSONArray jArray = jObject.getJSONArray("predictions");
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jObject2 = jArray.getJSONObject(i);
                    confidence = jObject2.getString("confidence");
                    species_common = jObject2.getString("species_common");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast toast = Toast.makeText(context, "Confidence: " + confidence + "\n" + "Species Common: " + species_common, Toast.LENGTH_LONG);
            toast.show();
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
