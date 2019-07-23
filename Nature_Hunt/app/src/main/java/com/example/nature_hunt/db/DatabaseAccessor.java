package com.example.nature_hunt.db;

import android.content.Context;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;

public class DatabaseAccessor {
    private static final String DATABASE_URL = "https://naturehuntwebapp.azurewebsites.net";

    private MobileServiceClient m_client;

    public DatabaseAccessor(Context context) {
        try {
            m_client = new MobileServiceClient(DATABASE_URL, context);
        } catch (MalformedURLException ex) {
            Log.wtf("[NatureHunt] MalformedURLException", ex.getMessage());
        }
    }

    protected MobileServiceTable<Hunt> getHuntTable() {
        return m_client.getTable("Hunts", Hunt.class);
    }

    protected MobileServiceTable<Species> getSpeciesTable() {
        return m_client.getTable(Species.class);
    }

    protected MobileServiceTable<SpeciesPerHunt> getSpeciesPerHuntTable() {
        return m_client.getTable("HuntSpeciesMap", SpeciesPerHunt.class);
    }
}
