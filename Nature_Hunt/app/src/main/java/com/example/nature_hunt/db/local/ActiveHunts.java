package com.example.nature_hunt.db.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ActiveHunts {

    @PrimaryKey
    public int huntId;
}
