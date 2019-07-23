package com.example.nature_hunt.db;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.nature_hunt.Species;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class DatabaseWrapper  extends DatabaseAccessor{

    public DatabaseWrapper(Context context)
    {
        super(context);
    }

    public Map<Integer, com.example.nature_hunt.Hunt> getHuntsMap() throws MobileServiceException, ExecutionException, InterruptedException {

        // Get the DB records from the Hunts DB table
        MobileServiceTable<Hunt> huntTable = getHuntTable();
        MobileServiceList<Hunt> huntsDB = huntTable.execute().get();

        // Get the HuntSpeciesMap to get species list corresponding to each hunt
        // Map<Integer, ArrayList<Species>> huntSpeciesMap = getHuntSpeciesMap();

        // Loop through all the DB records to populate the Hunts map
        Map<Integer, com.example.nature_hunt.Hunt> huntsMap = new HashMap<>();
        for (Hunt huntDB: huntsDB ) {
            Integer huntId = huntDB.getId();
            com.example.nature_hunt.Hunt huntBO = new com.example.nature_hunt.Hunt(
                    huntId,
                    huntDB.getName(),
                    huntDB.getLocation(),
                    new ArrayList<Species>()
                    //huntSpeciesMap.get(huntId)
            );
            huntsMap.put(huntId, huntBO);
        }
        return huntsMap;
    }

    public Map<Integer, ArrayList<Species>> getHuntSpeciesMap() throws MobileServiceException, ExecutionException, InterruptedException {

        // Get the DB records from the HuntSpeciesMap table
        MobileServiceTable<SpeciesPerHunt> huntSpeciesMapTable = getSpeciesPerHuntTable();
        MobileServiceList<SpeciesPerHunt> huntsSpeciesMapDB = huntSpeciesMapTable.execute().get();

        // Get SpeciesMap to join with this result
        Map<Integer, Species> speciesMap = getSpeciesMap();

        // Loop through all the records to populate the hunt species map
        Map<Integer, ArrayList<Species>> huntSpeciesMap = new HashMap<>();

        for (SpeciesPerHunt speciesPerHunt: huntsSpeciesMapDB)
        {
            Integer huntId = speciesPerHunt.getHuntId();
            Integer speciesId = speciesPerHunt.getSpeciesId();
            Species species = speciesMap.get(speciesId);

            if (huntSpeciesMap.containsKey(huntId))
            {
                huntSpeciesMap.get(huntId).add(species);
            }
            else
            {
                ArrayList<Species> speciesList = new ArrayList<>();
                speciesList.add(species);
                huntSpeciesMap.put(huntId, speciesList);
            }
        }
        return  huntSpeciesMap;
    }

    public Map<Integer, Species> getSpeciesMap() throws MobileServiceException, ExecutionException, InterruptedException {
        // Get the DB records from the HuntSpeciesMap table
        MobileServiceTable<com.example.nature_hunt.db.Species> speciesTable = getSpeciesTable();
        MobileServiceList<com.example.nature_hunt.db.Species> speciesDBList = speciesTable.execute().get();

        // Loop through all the records to populate the hunt species map
        Map<Integer, Species> speciesMap = new HashMap<>();

        for (com.example.nature_hunt.db.Species speciesDB: speciesDBList ) {
            Species species = new Species(
                    speciesDB.getId(),
                    speciesDB.getScientificName(),
                    speciesDB.getCommonName(),
                    speciesDB.getKingdom(),
                    speciesDB.getPhylum(),
                    speciesDB.getTaxonomyClass(),
                    speciesDB.getOrder(),
                    speciesDB.getFamily(),
                    speciesDB.getGenus()
                    );
            speciesMap.put(speciesDB.getId(), species);
        }

        return  speciesMap;
    }
}
