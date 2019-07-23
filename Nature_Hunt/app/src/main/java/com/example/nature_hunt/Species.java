package com.example.nature_hunt;

/**
 * Class representing an individual species
 */
public class Species {

    // Id of the species
    private int m_id;
    public Integer id() {
        return m_id;
    }

    // Scientific name of the species
    private String m_name;
    public String name() {
        return m_name;
    }

    // Common name of the species
    private String m_commonName;
    public String commonName() {
        return m_commonName;
    }

    // Kingdom name of the species
    private String m_kingdom;
    public String kingdom() {
        return m_kingdom;
    }

    // Phylum of the species
    private String m_phylum;
    public String phylum() {
        return m_phylum;
    }

    // Class of the species
    private String m_className;
    public String className() {
        return m_className;
    }

    // Order of the species
    private String m_order;
    public String order() {
        return m_order;
    }

    // Family of the species
    private String m_family;
    public String family() {
        return m_family;
    }

    // Genus of the species
    private String m_genus;
    public String genus() {
        return m_genus;
    }

    public Species(Integer id, String name, String commonName, String kingdom, String phylum,
                   String className, String order, String family, String genus)
    {
        this.m_id = id;
        this.m_name = name;
        this.m_commonName = commonName;
        this.m_className = className;
        this.m_kingdom = kingdom;
        this.m_phylum = phylum;
        this.m_order = order;
        this.m_family = family;
        this.m_genus = genus;
    }
}
