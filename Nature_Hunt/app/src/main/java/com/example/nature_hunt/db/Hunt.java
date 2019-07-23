package com.example.nature_hunt.db;

public class Hunt {

    @com.google.gson.annotations.SerializedName("id")
    private int m_id;

    @com.google.gson.annotations.SerializedName("HuntId")
    private int m_huntId;

    @com.google.gson.annotations.SerializedName("Name")
    private String m_name;

    @com.google.gson.annotations.SerializedName("Location")
    private String m_location;

    @com.google.gson.annotations.SerializedName("Description")
    private String m_description;


    public int getId() { return m_huntId; }

    public String getName() {
        return m_name;
    }

    public String getLocation() {
        return m_location;
    }

    public String getDescription() {
        return m_description;
    }
}
