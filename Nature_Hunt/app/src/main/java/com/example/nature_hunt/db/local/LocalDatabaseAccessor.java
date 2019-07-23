package com.example.nature_hunt.db.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public abstract class LocalDatabaseAccessor {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract void insert(HuntProgress huntProgress);

    @Ignore
    public void markSpeciesAsFound(int huntId, int speciesId) {
        HuntProgress huntProgress = new HuntProgress();
        huntProgress.huntId = huntId;
        huntProgress.speciesId = speciesId;

        insert(huntProgress);
    }

    @Query("DELETE FROM HuntProgress WHERE huntId == :huntId")
    public abstract void clearHuntProgress(int huntId);

    @Query("SELECT speciesId FROM HuntProgress WHERE huntId == :huntId")
    public abstract List<Integer> getSpeciesFoundFromHunt(int huntId);
}
