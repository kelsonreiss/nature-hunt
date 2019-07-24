package com.example.nature_hunt.db;

import android.content.Context;

import com.example.nature_hunt.Species;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.MobileServiceList;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CloudDataRepository {

    private  CloudDatabaseAccessor m_dbAccessor;

    public static Map<Integer, com.example.nature_hunt.Hunt> huntsMap;
    public static Map<Integer, ArrayList<Species>> huntSpeciesMap;
    public static Map<Integer, Species> speciesMap;

    public CloudDataRepository(Context context)
    {
        m_dbAccessor = new CloudDatabaseAccessor(context);
    }

    public void LoadData() throws InterruptedException, ExecutionException, MobileServiceException {
        LoadSpeciesMap();
        LoadHuntSpeciesMap();
        LoadHuntsMap();
    }

    private void LoadHuntsMap() throws MobileServiceException, ExecutionException, InterruptedException {

        // Get the DB records from the Hunts DB table
        MobileServiceTable<Hunt> huntTable = m_dbAccessor.getHuntTable();
        MobileServiceList<Hunt> huntsDB = huntTable.execute().get();

        // Loop through all the DB records to populate the Hunts map
        huntsMap = new HashMap<>();
        for (Hunt huntDB: huntsDB ) {
            Integer huntId = huntDB.getId();
            com.example.nature_hunt.Hunt huntBO = new com.example.nature_hunt.Hunt(
                    huntId,
                    huntDB.getName(),
                    huntDB.getLocation(),
                    huntSpeciesMap.get(huntId)
            );
            huntsMap.put(huntId, huntBO);
        }
    }

    private void LoadHuntSpeciesMap() throws MobileServiceException, ExecutionException, InterruptedException {

        // Get the DB records from the HuntSpeciesMap table
        MobileServiceTable<SpeciesPerHunt> huntSpeciesMapTable = m_dbAccessor.getSpeciesPerHuntTable();
        MobileServiceList<SpeciesPerHunt> huntsSpeciesMapDB = huntSpeciesMapTable.execute().get();

        // Loop through all the records to populate the hunt species map
        huntSpeciesMap = new HashMap<>();

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
    }

    private void LoadSpeciesMap() throws MobileServiceException, ExecutionException, InterruptedException {
        // Get the DB records from the HuntSpeciesMap table
        MobileServiceTable<com.example.nature_hunt.db.Species> speciesTable = m_dbAccessor.getSpeciesTable();
        MobileServiceList<com.example.nature_hunt.db.Species> speciesDBList = speciesTable.execute().get();

        // Loop through all the records to populate the hunt species map
        speciesMap = new HashMap<>();

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
    }
}
