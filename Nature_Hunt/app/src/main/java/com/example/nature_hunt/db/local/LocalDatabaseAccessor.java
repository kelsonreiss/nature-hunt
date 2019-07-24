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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract void insert(ActiveHunts activeHunt);

    @Ignore
    public void markSpeciesAsFound(int huntId, int speciesId) {
        HuntProgress huntProgress = new HuntProgress();
        huntProgress.huntId = huntId;
        huntProgress.speciesId = speciesId;

        insert(huntProgress);
    }

    @Ignore
    public void startHunt(int huntId) {
        ActiveHunts hunt = new ActiveHunts();
        hunt.huntId = huntId;
        insert(hunt);
    }

    @Query("DELETE FROM HuntProgress WHERE huntId == :huntId")
    public abstract void clearHuntProgress(int huntId);

    @Query("SELECT huntId FROM ActiveHunts")
    public abstract List<Integer> getActiveHunts();

    @Query("SELECT speciesId FROM HuntProgress WHERE huntId == :huntId")
    public abstract List<Integer> getSpeciesFoundFromHunt(int huntId);
}
