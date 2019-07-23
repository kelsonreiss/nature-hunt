package com.example.nature_hunt;

/**
 * Class representing an individual species
 */
public class Species {

    // Id of the species
    public int id;

    // Scientific name of the species
    public String name;

    // Common name of the species
    public String commonName;

    // Kingdom name of the species
    public String kingdom;

    // Phylum of the species
    public String phylum;

    // Class of the species
    public String className;

    // Order of the species
    public String order;

    // Family of the species
    public String family;

    // Genus of the species
    public String genus;

    public Species(Integer id, String name, String commonName, String kingdom, String phylum,
                   String className, String order, String family, String genus)
    {
        this.id = id;
        this.name = name;
        this.commonName = commonName;
        this.className = className;
        this.kingdom = kingdom;
        this.phylum = phylum;
        this.order = order;
        this.family = family;
        this.genus = genus;
    }
}
