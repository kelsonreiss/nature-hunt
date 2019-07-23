package com.example.nature_hunt.db;

public class SpeciesPerHunt {

    @com.google.gson.annotations.SerializedName("HuntId")
    private int m_huntId;

    @com.google.gson.annotations.SerializedName("SpeciesId")
    private int m_speciesId;


    public int getHuntId() {
        return m_huntId;
    }

    public int getSpeciesId() {
        return m_speciesId;
    }
}
