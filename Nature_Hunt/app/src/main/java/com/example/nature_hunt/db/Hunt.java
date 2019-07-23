package com.example.nature_hunt.db;

public class Hunt {

    @com.google.gson.annotations.SerializedName("Id")
    private int m_id;

    @com.google.gson.annotations.SerializedName("Name")
    private String m_name;

    @com.google.gson.annotations.SerializedName("Location")
    private String m_location;

    @com.google.gson.annotations.SerializedName("Description")
    private String m_description;


    public int getId() {
        return m_id;
    }

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
