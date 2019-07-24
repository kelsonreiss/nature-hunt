package com.example.nature_hunt;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import androidx.room.Room;

import com.example.nature_hunt.db.CloudDataRepository;
import com.example.nature_hunt.db.local.LocalDatabase;
import com.example.nature_hunt.db.local.LocalDatabaseAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class App extends Application {
    private static ArrayList<Hunt> hunts;
    private static Context context;
    private static LocalDatabase db;
    private static LocalDatabaseAccessor dao;
    private static Map<Integer, Species> speciesMap;
    private static ArrayList<Hunt> huntsInProgress;
    private static List<Integer> activeHunts;

    public App(){
    }

    public void onCreate() {
        super.onCreate();
        App.context = getApplicationContext();
        hunts = new ArrayList<>();
        db = Room.databaseBuilder(context, LocalDatabase.class, "hunts.db").allowMainThreadQueries().build();
        dao = db.getAccessor();
        CloudDataRepository dataRepository = new CloudDataRepository(context);
        activeHunts = dao.getActiveHunts();
        System.out.println("onCreate from App");
    }

    public static Context getAppContext() {
        return App.context;
    }

    public static void addHunt(Hunt hunt) {
        hunts.add(hunt);
    }

    public static ArrayList<Hunt> getHuntsList() {
        return hunts;
    }

    public static LocalDatabaseAccessor getDB(){
        return dao;
    }

    public static void setSpeciesMap(Map<Integer, Species> map){
        speciesMap = map;
    }

    public static Map<Integer, Species> getSpeciesMap(){
        return speciesMap;
    }

    public static List getActiveHunts() { return dao.getActiveHunts(); }

    public static void startHunt(Integer huntId) { dao.startHunt(huntId); }
}
