package com.example.nature_hunt;

import android.util.Pair;

import java.util.ArrayList;

/**
 * Class to represent a single scavenger hunt
 */
public class Hunt {
    // Name of Hunt
    // i.e. "Green Loop Trail"
    private String name;

    // Easy to read location info
    // i.e. "Redwoods National Park, CA"
    private String friendly_location;

    // Pair representing latitude and longitude coordinates
    // Negative value represents South / West
    // i.e. <40.682932, -124.039570>
    private Pair<Double, Double> coords;

    // Optional
    // URL link to header image in blob
    private String header_image_url;

    // Arraylist of Species objects that must be found
    private ArrayList<Species> species_list;

    // Optional
    // Description / Overview of hunt
    private String description;

    public Hunt(String name, String friendly_location, Pair<Double, Double> coords, ArrayList<Species> species_list){
        this.name=name;
        this.friendly_location = friendly_location;
        this.coords = coords;
        this.species_list = species_list;
    }

    public Hunt(String name, String friendly_location, Pair<Double, Double> coords, ArrayList<Species> species_list, String header_image_url){
        this(name, friendly_location, coords, species_list);
        this.header_image_url = header_image_url;
    }

    public Hunt(String name, String friendly_location, Pair<Double, Double> coords, ArrayList<Species> species_list, String description, String header_image_url){
        this(name, friendly_location, coords, species_list);
        this.header_image_url = header_image_url;
        this.description = description;
    }


}
