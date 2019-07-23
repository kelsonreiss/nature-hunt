package com.example.nature_hunt.db.local;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"huntId", "speciesId"})
public class HuntProgress {
    public int huntId;
    public int speciesId;
}
