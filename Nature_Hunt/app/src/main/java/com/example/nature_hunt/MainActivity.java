package com.example.nature_hunt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nature_hunt.db.CloudDataRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;


    private static Context context;

    private static List<Hunt> searchList;
    ArrayList<HuntRecyclerItemModel> huntsInProgress;
    ArrayList<HuntRecyclerItemModel> huntsCompleted;
    HuntRecyclerAdapter huntsInProgressAdapter;
    HuntRecyclerAdapter huntsCompletedAdapter;

    private Integer[] stockTrailImages =
            {R.drawable.base_of_tree, R.drawable.leaves_against_sky, R.drawable.open_park,
                    R.drawable.park_with_bench, R.drawable.tree_canopy, R.drawable.tree_close_up,
                    R.drawable.worn_path, R.drawable.stock_trail};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_main);
        mTextMessage = findViewById(R.id.message);

        searchList = new ArrayList<>();
        LoadCloudDataRepository();
        AutoCompleteTextView textView = findViewById(R.id.HuntsSearchBar);
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
                Object item = parent.getItemAtPosition(position);
                if (item instanceof Hunt) {

                    Hunt hunt = (Hunt) item;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("hunt", hunt);
                    try {
                        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    HuntPreviewFrag previewFrag = HuntPreviewFrag.newInstance();
                    previewFrag.setArguments(bundle);
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.add(android.R.id.content, previewFrag)
                            .addToBackStack(null).commit();
                }
            }
        });

        huntsInProgress = new ArrayList<>();
        RecyclerView huntsInProgressView = findViewById(R.id.homepage_hunts_in_progress_recycler_view);
        huntsInProgressAdapter = new HuntRecyclerAdapter(this, huntsInProgress);
        huntsInProgressView.setAdapter(huntsInProgressAdapter);
        huntsInProgressView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        huntsInProgressView.addOnItemTouchListener(new RecyclerTouchListener(context, huntsInProgressView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                HuntRecyclerItemModel huntModel = huntsInProgressAdapter.getItem(position);
                HuntProgressTracker previewFrag = HuntProgressTracker.newInstance(huntModel.getHunt());
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(android.R.id.content, previewFrag)
                        .addToBackStack(null).commit();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        RecyclerView huntsCompletedView = findViewById(R.id.homepage_hunts_completed_recycler_view);
        huntsCompleted = new ArrayList<>();
        huntsCompletedAdapter = new HuntRecyclerAdapter(this, huntsCompleted);
        huntsCompletedView.setAdapter(huntsCompletedAdapter);
        huntsCompletedView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        huntsCompletedView.addOnItemTouchListener(new RecyclerTouchListener(context, huntsCompletedView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                HuntRecyclerItemModel huntModel = huntsCompletedAdapter.getItem(position);
                HuntProgressTracker previewFrag = HuntProgressTracker.newInstance(huntModel.getHunt());
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(android.R.id.content, previewFrag)
                        .addToBackStack(null).commit();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }


    private void LoadCloudDataRepository() {
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    if (!CloudDataRepository.isLoaded)
                    {
                        CloudDataRepository dataRepository = new CloudDataRepository(getApplicationContext());
                        dataRepository.LoadData();
                    }
                    final Map<Integer, Hunt> huntsMap = CloudDataRepository.huntsMap;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            searchList.clear();
                            searchList.addAll(huntsMap.values());
                            // Providing a new adapter will properly refresh the AutoComplete list
                            ArrayAdapter<Hunt> searchBarAdapter = new ArrayAdapter<Hunt>(
                                    MainActivity.this,
                                    android.R.layout.simple_dropdown_item_1line, searchList);
                            AutoCompleteTextView textView = findViewById(R.id.HuntsSearchBar);
                            textView.setAdapter(searchBarAdapter);

                            LoadHunts();
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

    public void addMapFragment(View view) {
        Intent mapActivity=new Intent(context, MapsActivity.class);
        startActivity(mapActivity);
    }

    private void LoadHunts()
    {
        RecyclerView huntsInProgressView = findViewById(R.id.homepage_hunts_in_progress_recycler_view);
        RecyclerView huntsCompletedView = findViewById(R.id.homepage_hunts_completed_recycler_view);
        for (Object huntId : App.getActiveHunts()) {
            Hunt hunt = CloudDataRepository.huntsMap.get(huntId);
            HuntRecyclerItemModel item = new HuntRecyclerItemModel();
            item.setName(hunt.name());
            item.setHunt(hunt);
            item.setImage_drawable(getDrawableID());

            List<Integer> speciesListFound =  App.getDB().getSpeciesFoundFromHunt(hunt.id());
            boolean completedHunt = true;
            for (Species species: hunt.speciesList()) {
                if (!speciesListFound.contains(species.id()))
                {
                    completedHunt = false;
                    break;
                }
            }
            if (completedHunt)
            {
                huntsCompleted.add(item);
            }
            else
            {
                huntsInProgress.add(item);
            }
        }
        huntsInProgressAdapter.setList(context, huntsInProgress);
        huntsInProgressView.setAdapter(huntsInProgressAdapter);

        huntsCompletedAdapter.setList(context, huntsCompleted);
        huntsCompletedView.setAdapter(huntsCompletedAdapter);
    }

    private int getDrawableID(){
        Random random = new Random();
        return stockTrailImages[random.nextInt(stockTrailImages.length)];
    }
}
