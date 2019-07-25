package com.example.nature_hunt;

import android.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;

import static java.lang.Double.parseDouble;

/**
 * Class to represent a single scavenger hunt
 * Make it Serializable to pass between fragments
 */
public class Hunt implements Serializable {

    // Id of the hunt
    private Integer m_id;
    public Integer id() {
        return m_id;
    }

    // Name of Hunt
    // i.e. "Green Loop Trail"
    private String m_name;
    public String name() {
        return m_name;
    }

    // Easy to read location info
    // i.e. "Redwoods National Park, CA"
    private String m_friendlyLocation;
    public String friendlyLocation() {
        return m_friendlyLocation;
    }

    // Pair representing latitude and longitude coordinates
    // Negative value represents South / West
    // i.e. <40.682932, -124.039570>
    private String m_coords;
    public String coords() {
        return m_coords;
    }

    // Optional
    // URL link to header image in blob
    private String m_header_image_url;
    public String header_image_url() {
        return m_header_image_url;
    }

    // Arraylist of Species objects that must be found
    private ArrayList<Species> m_speciesList;
    public ArrayList<Species> speciesList() {
        return m_speciesList;
    }

    // Optional
    // Description / Overview of hunt
    private String m_description;
    public String description() {
        return m_description;
    }

    public Hunt(Integer id, String name, String friendly_location, ArrayList<Species> species_list){
        this.m_id = id;
        this.m_name=name;
        this.m_friendlyLocation = friendly_location;
        this.m_speciesList = species_list;
    }

    public Hunt(Integer id, String name, String friendly_location, String coords, ArrayList<Species> species_list){
        this(id, name, friendly_location, species_list);
        this.m_coords = coords;
    }

    public Hunt(Integer id, String name, String friendly_location, String coords, ArrayList<Species> species_list, String header_image_url){
        this(id, name, friendly_location, coords, species_list);
        this.m_header_image_url = header_image_url;
    }

    public Hunt(Integer id, String name, String friendly_location, String coords, ArrayList<Species> species_list, String description, String header_image_url){
        this(id, name, friendly_location, coords, species_list, header_image_url);
        this.m_description = description;
    }

    @Override
    public String toString() {
        return m_name + " - " + m_friendlyLocation;
    }

}
