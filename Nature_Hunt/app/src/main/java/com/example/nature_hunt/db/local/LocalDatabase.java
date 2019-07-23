package com.example.nature_hunt.db.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ActiveHunts.class, HuntProgress.class}, version = 2)
public abstract class LocalDatabase extends RoomDatabase {

    public abstract LocalDatabaseAccessor getAccessor();
}
