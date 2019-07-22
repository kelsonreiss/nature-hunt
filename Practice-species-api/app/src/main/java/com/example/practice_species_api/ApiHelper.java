package com.example.practice_species_api;

import android.net.Uri;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

// Had to change Gradle dependency to use legacy Apache http library
// TODO: modify URL call to conform with latest standard
// For now, use AI for Earth sample code
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

/**
 * Singleton class that handles logic for API call
 * Documentation for AI for Earth Species Classification API found at:
 * https://aiforearth.portal.azure-api.net/docs/services/5b183aa7d114573c1fd1d383/operations/classify
 */
public class ApiHelper {

    private static String apiKey = "949e7dc4318041af961810828dadcf05";
    private static ApiHelper mInstance = null;

    private ApiHelper(){
        // No fields to instantiate
    }

    public static ApiHelper getInstance(){
        if (mInstance == null)
            mInstance= new ApiHelper();

        return mInstance;
    }

    public ArrayList<String> getPreds(File image){

            // https://stackoverflow.com/questions/19167954/use-uri-builder-in-android-or-create-url-with-variables
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority("aiforearth,azure-api.net")
                    .appendPath("species-recognition")
                    .appendPath("v0.1")
                    .appendPath("predict")
                    // Number of results to return
                    .appendQueryParameter("topK", "1")
                    // Can be classifyOnly or classifyAndDetect
                    .appendQueryParameter("predictMode", "classifyOnly");

            try {

                // WORK ON THIS
//                URL mUrl = new URL(builder.build().toString());
//                HttpURLConnection connection = (HttpURLConnection) mUrl.openConnection();
//                connection.setRequestMethod("POST");
//                connection.setRequestProperty("Content-Type", "application/octet-stream");
//                connection.
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
            return null;
    }

}
