package com.example.nature_hunt;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HuntPreviewFrag extends DialogFragment {

    private SpeciesRecyclerAdapter adapter;
    private ArrayList<SpeciesRecyclerItemModel> models;
    RecyclerView recyclerView;
    private Hunt mHunt;
    private Button startHuntBtn;

    // Stock Images + Names to use as backups
    Integer[] stock_flower_ids = {
            R.drawable.daffodil, R.drawable.flower, R.drawable.hibiscus, R.drawable.maidenhair, R.drawable.maple, R.drawable.white_spruce
    };

    String[] stock_flower_names = {"Daffodil", "Sheep Laurel", "Hibiscus", "Maidenhair", "Maple", "White Spruce"};

    public static HuntPreviewFrag newInstance() {
        return new HuntPreviewFrag();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view =  inflater.inflate(R.layout.hunt_preview_fragment_2, container, false);
        try {
            mHunt = (Hunt) getArguments().getSerializable("hunt");
            Log.i("Bundle", mHunt.toString());
        } catch (Exception e){
            e.printStackTrace();
        }

        if (mHunt != null){
            // Set title data
            TextView title = (TextView) view.findViewById(R.id.hunt_title);
            title.setText(mHunt.name());

            // Set Hunt location
            TextView location = (TextView) view.findViewById(R.id.hunt_location);
            // If a location wasn't set, don't show anything
            if (mHunt.friendlyLocation() != null){
                Log.i("Location", mHunt.friendlyLocation());
                location.setVisibility(View.GONE);
            } else{
                title.setText(mHunt.friendlyLocation());
            }

            // Set hunt description
            TextView description = (TextView) view.findViewById(R.id.hunt_description);
            if (mHunt.description() == null){
                TextView description_title = (TextView) view.findViewById(R.id.hunt_description_title);
                description_title.setVisibility(View.GONE);
                description.setVisibility(View.GONE);
            } else{
                description.setText(mHunt.description());
            }
        }

        startHuntBtn = (Button) view.findViewById(R.id.hunt_start_btn);
        startHuntBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HuntProgressTracker tracker = HuntProgressTracker.newInstance(mHunt);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(android.R.id.content, tracker)
                        .commit();
            }
        });


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.dismiss_preview_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        recyclerView = (RecyclerView) view.findViewById(R.id.species_image_gallery);

        models = getData();
        adapter = new SpeciesRecyclerAdapter(getContext(), models);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        return view;
    }

<<<<<<< HEAD
    private ArrayList getData(){
        if (mHunt == null || mHunt.speciesList() == null || mHunt.speciesList().isEmpty()){
            ArrayList speciesList = new ArrayList<>();
=======
    private ArrayList<SpeciesRecyclerItemModel> getData() {

        if (mHunt == null || mHunt.speciesList() == null){
            ArrayList<SpeciesRecyclerItemModel> speciesList = new ArrayList<SpeciesRecyclerItemModel>();
>>>>>>> master
            for (int i = 0; i < stock_flower_ids.length; i++){
                SpeciesRecyclerItemModel model = new SpeciesRecyclerItemModel();
                model.setImage_drawable(stock_flower_ids[i]);
                model.setName(stock_flower_names[i]);
                speciesList.add(model);
            }
            return speciesList;
        }

        ArrayList<SpeciesRecyclerItemModel> speciesList = new ArrayList<SpeciesRecyclerItemModel>();
        for (Species species : mHunt.speciesList()) {
            SpeciesRecyclerItemModel speciesItem = new SpeciesRecyclerItemModel();
            speciesItem.setName(species.commonName());
            speciesItem.setImage_drawable(R.drawable.flower);
            speciesList.add(speciesItem);
        }
        return speciesList;
    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mViewModel = ViewModelProviders.of(this).get(HuntPreviewViewModel.class);
//        // TODO: Use the ViewModel
//    }
}
