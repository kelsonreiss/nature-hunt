package com.example.nature_hunt;

import android.util.Pair;

import java.util.ArrayList;

/**
 * Class to represent a single scavenger hunt
 */
public class Hunt {

    // Id of the hunt
    public Integer id;

    // Name of Hunt
    // i.e. "Green Loop Trail"
    public String name;

    // Easy to read location info
    // i.e. "Redwoods National Park, CA"
    public String friendly_location;

    // Pair representing latitude and longitude coordinates
    // Negative value represents South / West
    // i.e. <40.682932, -124.039570>
    public Pair<Double, Double> coords;

    // Optional
    // URL link to header image in blob
    public String header_image_url;

    // Arraylist of Species objects that must be found
    public ArrayList<Species> species_list;

    // Optional
    // Description / Overview of hunt
    public String description;

    public Hunt(Integer id, String name, String friendly_location, ArrayList<Species> species_list){
        this.id = id;
        this.name=name;
        this.friendly_location = friendly_location;
        this.species_list = species_list;
    }

    public Hunt(Integer id, String name, String friendly_location, Pair<Double, Double> coords, ArrayList<Species> species_list){
        this(id, name, friendly_location, species_list);
        this.coords = coords;
    }

    public Hunt(Integer id, String name, String friendly_location, Pair<Double, Double> coords, ArrayList<Species> species_list, String header_image_url){
        this(id, name, friendly_location, coords, species_list);
        this.header_image_url = header_image_url;
    }

    public Hunt(Integer id, String name, String friendly_location, Pair<Double, Double> coords, ArrayList<Species> species_list, String description, String header_image_url){
        this(id, name, friendly_location, coords, species_list);
        this.header_image_url = header_image_url;
        this.description = description;
    }

    @Override
    public String toString() {
        return name + " - " + friendly_location;
    }

}
