package com.example.nature_hunt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import com.example.nature_hunt.db.local.LocalDatabase;
import com.example.nature_hunt.db.local.LocalDatabaseAccessor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.example.nature_hunt.db.CloudDataRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;


    private static Context context;

    private static List<Hunt> searchList;
    private static Map<Integer, Species> speciesMap;

    private static LocalDatabase db;
    private static LocalDatabaseAccessor dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
//        PopulateSpeciesMap();
        setContentView(R.layout.activity_main);
        mTextMessage = findViewById(R.id.message);


        searchList = new ArrayList<>();
        LoadCloudDataRepository();
        ArrayAdapter<Hunt> searchBarAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, searchList);
        AutoCompleteTextView textView = findViewById(R.id.HuntsSearchBar);
        textView.setAdapter(searchBarAdapter);
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                Object item = parent.getItemAtPosition(position);
                if (item instanceof Hunt) {

                    Hunt hunt = (Hunt) item;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("hunt", hunt);
                    try {
                        InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    } catch (Exception e){
                        e.printStackTrace();
                    }

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

        // TODO: Populate both of these RecyclerViews with actual data from our databases
        RecyclerView huntsInProgressView = findViewById(R.id.homepage_hunts_in_progress_recycler_view);
        ArrayList<HuntRecyclerItemModel> huntsInProgress = new ArrayList<HuntRecyclerItemModel>();
        for (int i = 0; i < 5; i++) {
            HuntRecyclerItemModel item = new HuntRecyclerItemModel();
            item.setName("Hunt " + i);
            item.setImage_drawable(R.drawable.stock_trail);
            huntsInProgress.add(item);
        }
        HuntRecyclerAdapter huntsInProgressAdapter = new HuntRecyclerAdapter(this, huntsInProgress);
        huntsInProgressView.setAdapter(huntsInProgressAdapter);
        huntsInProgressView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        RecyclerView huntsCompletedView = findViewById(R.id.homepage_hunts_completed_recycler_view);
        ArrayList<HuntRecyclerItemModel> huntsCompleted = new ArrayList<HuntRecyclerItemModel>();
        for (int i = 0; i < 5; i++) {
            HuntRecyclerItemModel item = new HuntRecyclerItemModel();
            item.setName("Completed Hunt " + i);
            item.setImage_drawable(R.drawable.stock_trail);
            huntsCompleted.add(item);
        }
        HuntRecyclerAdapter huntsCompletedAdapter = new HuntRecyclerAdapter(this, huntsCompleted);
        huntsCompletedView.setAdapter(huntsCompletedAdapter);
        huntsCompletedView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }


    private void LoadCloudDataRepository() {
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    CloudDataRepository dataRepository = new CloudDataRepository(context);
                    dataRepository.LoadData();
                    final Map<Integer, Hunt> huntsMap = CloudDataRepository.huntsMap;
                    App.setSpeciesMap(CloudDataRepository.speciesMap);

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

    private void PopulateSpeciesMap() {
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    CloudDataRepository dataRepository = new CloudDataRepository(context);
                    dataRepository.LoadData();
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
