package com.example.nature_hunt.db;

import android.content.Context;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;

public class CloudDatabaseAccessor {
    private static final String DATABASE_URL = "https://naturehuntwebapp.azurewebsites.net";

    protected MobileServiceClient m_client;

    public CloudDatabaseAccessor(Context context) {
        try {
            m_client = new MobileServiceClient(DATABASE_URL, context);
        } catch (MalformedURLException ex) {
            Log.wtf("[NatureHunt] MalformedURLException", ex.getMessage());
        }
    }

    public MobileServiceTable<Hunt> getHuntTable() {
        return m_client.getTable("Hunts", Hunt.class);
    }

    public MobileServiceTable<Species> getSpeciesTable() {
        return m_client.getTable("Species_SUBSET", Species.class);
    }

    public MobileServiceTable<SpeciesPerHunt> getSpeciesPerHuntTable() {
        return m_client.getTable("HuntSpeciesMap_SUBSET", SpeciesPerHunt.class);
    }
}
