package com.example.nature_hunt.db.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {HuntProgress.class}, version = 1)
public abstract class LocalDatabase extends RoomDatabase {

    public abstract LocalDatabaseAccessor getAccessor();
}
