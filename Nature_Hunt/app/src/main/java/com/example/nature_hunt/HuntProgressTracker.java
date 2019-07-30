package com.example.nature_hunt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nature_hunt.db.local.LocalDatabaseAccessor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
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
import java.util.HashMap;
import java.util.List;


/**
 *
 */
public class HuntProgressTracker extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String HUNT_ARG = "hunt";
    private VerticalSpeciesRecyclerAdapter adapter;
    private ArrayList<SpeciesRecyclerItemModel> models;
    private MoreInfoDialogFrag moreInfoDialogFrag;
    RecyclerView recyclerView;
    LinearLayout progress_dialog;

    private FloatingActionButton camButton;
    private ProgressBar progressBar;
    private TextView progressText;

    static final int REQUEST_TAKE_PHOTO = 1;

    private static class PostParams {
        Context context;
        String filePath;

        PostParams(Context context, String filePath) {
            this.context = context;
            this.filePath = filePath;
        }
    }

    // Stock Images + Names to use as backups
    Integer[] stock_flower_ids = {
            R.drawable.daffodil, R.drawable.flower, R.drawable.hibiscus, R.drawable.maidenhair, R.drawable.maple, R.drawable.white_spruce
    };

    String[] stock_flower_names = {"Daffodil", "House Sparrow", "Hibiscus", "Maidenhair", "Maple", "White Spruce"};
    private Hunt mHunt;
    private List<Species> speciesList;
    private List<Integer> currentSpeciesFound;
    private HashMap<Integer, Species> speciesMap;
    private LocalDatabaseAccessor dbAccessor;

    private HuntProgressTracker() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HuntProgressTracker.
     */
    // TODO: Rename and change types and number of parameters
    public static HuntProgressTracker newInstance(Hunt hunt) {
        HuntProgressTracker fragment = new HuntProgressTracker();
        Bundle args = new Bundle();
        args.putSerializable(HUNT_ARG, hunt);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mHunt = (Hunt) getArguments().getSerializable(HUNT_ARG);
        }
        List<Species> speciesList = mHunt.speciesList();
        speciesMap = new HashMap();
        for(Species species : speciesList){
            speciesMap.put(species.id(), species);
        }
        HuntProgressTracker fragment = new HuntProgressTracker();
//        LocalDatabaseAccessor accessor = App.getDB();
        dbAccessor = App.getDB();
        currentSpeciesFound = dbAccessor.getSpeciesFoundFromHunt(mHunt.id());
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.hunt_progress_tracker, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.vertical_species_image_gallery);

        // Set info button to show more info about hunt
        moreInfoDialogFrag  = MoreInfoDialogFrag.getInstance(mHunt);
        ImageButton infoButton = (ImageButton) view.findViewById(R.id.more_info_prog_frag_btn);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager()
                        .beginTransaction();
                ft.add(moreInfoDialogFrag, "info dialog")
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Set back arrow to return to home screen
        ImageButton backButton = (ImageButton) view.findViewById(R.id.back_arrow_prog_frag);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // Set data fields based on hunt
        if (mHunt != null){
            // Set title
            TextView title = (TextView) view.findViewById(R.id.hunt_title_prog_frag);
            title.setText(mHunt.name());
        }
        models = getData();
        adapter = new VerticalSpeciesRecyclerAdapter(getContext(), models);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        progress_dialog = (LinearLayout) view.findViewById(R.id.api_call_progress_dialog);

        camButton = view.findViewById(R.id.launch_cam_button);

        camButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                markFound(4, 5);
                dispatchTakePictureIntent();
            }
        });


        // Progress Bar
        progressBar = view.findViewById(R.id.progressBar);
        progressText = view.findViewById(R.id.fraction_progress_text);
        updateProgressVisuals();
        return view;
    }

    private void updateProgressVisuals(){
        int numTotal = mHunt.speciesList().size();
        int numCurrent = currentSpeciesFound.size();

        if(null != progressBar){
            progressBar.setProgress(numCurrent);
            progressBar.setMax(numTotal);
        }
        if(null != progressText){
            progressText.setText(numCurrent + " / " + numTotal);
        }
    }

    private boolean validateResult(String commonName){
        if(!commonName.isEmpty()){
            ArrayList<Species> speciesList = mHunt.speciesList();
            for(int i = 0; i <= mHunt.speciesList().size(); i++){
                if(commonName.equals(speciesList.get(i).commonName().toLowerCase())){
                    App.getDB().markSpeciesAsFound(this.mHunt.id(), speciesList.get(i).id());
                }
                return true;
            }
        }
        return false;
    }

    private Species getSpeciesFromID(int id){
        return App.getSpeciesMap().get(id);
    }

    private boolean existsSpeciesInHunt(String commonName){
        for(Species species : this.speciesList){
            if(species.commonName() == commonName){
                return true;
            }
        }
        return false;
    }

    private ArrayList getData(){
        ArrayList speciesList = new ArrayList<>();
            for (int i = 0; i < stock_flower_ids.length; i++){
                SpeciesRecyclerItemModel model = new SpeciesRecyclerItemModel();
                model.setImage_drawable(stock_flower_ids[i]);
                model.setName(stock_flower_names[i]);
                speciesList.add(model);
            }
            return speciesList;
        //(TODO: Update this to return URL images for species)
//        if (mHunt == null || mHunt.speciesList() == null || mHunt.speciesList().isEmpty()){
//            ArrayList speciesList = new ArrayList<>();
//            for (int i = 0; i < stock_flower_ids.length; i++){
//                SpeciesRecyclerItemModel model = new SpeciesRecyclerItemModel();
//                model.setImage_drawable(stock_flower_ids[i]);
//                model.setName(stock_flower_names[i]);
//                speciesList.add(model);
//            }
//            return speciesList;
//        }
//
//        return mHunt.speciesList();
    }

    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            System.out.println("mCurrentPhotoPath" + mCurrentPhotoPath);
            try {
                HuntProgressTracker.PostParams postParams = new HuntProgressTracker.PostParams(getContext(), mCurrentPhotoPath);
                new UploadFileTask().execute(postParams);
            } catch (Exception e) {
                Toast toast = Toast.makeText(getContext(), "Error sending post request", Toast.LENGTH_LONG);
                toast.show();
            }
        } else {
            Toast toast = Toast.makeText(getContext(), "Picture was not taken", Toast.LENGTH_LONG);
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
        connection.setRequestProperty("Content-Type", "application/octet-stream");
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", apiKey);

        OutputStream outputStream = connection.getOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead = -1;

        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

//        System.out.println("Response message connection.getResponseMessage(): " + connection.getResponseMessage());

        int status = connection.getResponseCode();
        switch (status) {
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
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        getActivity().getApplicationContext().getPackageName() + ".fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private class UploadFileTask extends AsyncTask<HuntProgressTracker.PostParams, Integer, String> {
        Context context;

        protected String doInBackground(HuntProgressTracker.PostParams... params) {
            getActivity().runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    progress_dialog.setVisibility(View.VISIBLE);
                    progress_dialog.bringToFront();
                }
            });

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

            // Hide waiting dialog
            progress_dialog.setVisibility(View.INVISIBLE);

//          (TODO: Change to markChecked(species_common)
            // Validate returns a boolean to indicate success
//            boolean validResult = validate(species_common);
            boolean validResult = validateResult(species_common);
            System.out.println("validateResult: " + validResult);
            if(validResult) {
                updateProgressVisuals();
                markChecked(stock_flower_names[0]);
            }
            Toast toast = Toast.makeText(context, "Confidence: " + confidence + "\n" + "Species Common: " + species_common, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    // Mark the image icon as checked
    private void markChecked(String species_name){
        int pos = -1;

        // (TODO: Change this to mHunt.speciesList
        for (int i = 0; i < stock_flower_names.length; i++){
            if (species_name.equalsIgnoreCase(stock_flower_names[i])){
                pos = i;
            }
        }

        try {
            if (pos != -1){
                recyclerView.getChildAt(pos)
                        .findViewById(R.id.vertical_image_view)
                        .setForeground(ContextCompat.getDrawable(getContext(), R.drawable.checked_overlay));

            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
