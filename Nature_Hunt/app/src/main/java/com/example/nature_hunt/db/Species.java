package com.example.nature_hunt.db;

public class Species {

    @com.google.gson.annotations.SerializedName("id")
    private int m_id;

    @com.google.gson.annotations.SerializedName("SpeciesId")
    private int m_speciesId;

    @com.google.gson.annotations.SerializedName("Name")
    private String m_scientificName;

    @com.google.gson.annotations.SerializedName("CommonName")
    private String m_commonName;

    @com.google.gson.annotations.SerializedName("Kingdom")
    private String m_kingdom;

    @com.google.gson.annotations.SerializedName("Phylum")
    private String m_phylum;

    @com.google.gson.annotations.SerializedName("Class")
    private String m_class;

    @com.google.gson.annotations.SerializedName("Order")
    private String m_order;

    @com.google.gson.annotations.SerializedName("Family")
    private String m_family;

    @com.google.gson.annotations.SerializedName("Genus")
    private String m_genus;


    public int getId() {
        return m_speciesId;
    }

    public String getScientificName() {
        return m_scientificName;
    }

    public String getCommonName() {
        return m_commonName;
    }

    public String getKingdom() {
        return m_kingdom;
    }

    public String getPhylum() {
        return m_phylum;
    }

    public String getTaxonomyClass() {
        // This has a different name because getClass() is already a Java method
        return m_class;
    }

    public String getOrder() {
        return m_order;
    }

    public String getFamily() {
        return m_family;
    }

    public String getGenus() {
        return m_genus;
    }
}
