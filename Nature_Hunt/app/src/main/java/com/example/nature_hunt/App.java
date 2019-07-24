package com.example.nature_hunt;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import com.example.nature_hunt.db.DatabaseWrapper;
import com.example.nature_hunt.db.local.LocalDatabase;
import com.example.nature_hunt.db.local.LocalDatabaseAccessor;

import java.util.ArrayList;
import java.util.Map;

public class App extends Application {
    private static ArrayList<Hunt> hunts;
    private static Map<Integer, Hunt> huntsMap;
    private static Context context;

    public App(){
    }

    public void onCreate() {
        super.onCreate();
        App.context = getApplicationContext();
        hunts = new ArrayList<>();
        PopulateHuntSearchList();
    }

    private void PopulateHuntSearchList() {
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    DatabaseWrapper dbWrapper = new DatabaseWrapper(getApplicationContext());
                    final Map<Integer, Hunt> huntsMap = dbWrapper.getHuntsMap();

                    new Runnable() {
                        @Override
                        public void run() {
                            for (Hunt hunt : huntsMap.values()) {
                                hunts.add(hunt);
                            }
                        }
                    };
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

    public static Context getAppContext() {
        return App.context;
    }

    public static Map<Integer, Hunt> getHuntsMap() {
        return huntsMap;
    }

    public static ArrayList<Hunt> getHuntsList() {
        return hunts;
    }
}
