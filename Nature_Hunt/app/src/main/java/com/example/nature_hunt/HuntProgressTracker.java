package com.example.nature_hunt;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;


/**
 *
 */
public class HuntProgressTracker extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String HUNT_ARG = "hunt";
    private VerticalSpeciesRecyclerAdapter adapter;
    private ArrayList<SpeciesRecyclerItemModel> models;
    private MoreInfoDialogFrag moreInfoDialogFrag;
    RecyclerView recyclerView;

    // Stock Images + Names to use as backups
    Integer[] stock_flower_ids = {
            R.drawable.daffodil, R.drawable.flower, R.drawable.hibiscus, R.drawable.maidenhair, R.drawable.maple, R.drawable.white_spruce
    };

    String[] stock_flower_names = {"Daffodil", "Sheep Laurel", "Hibiscus", "Maidenhair", "Maple", "White Spruce"};


    private Hunt mHunt;

    private HuntProgressTracker() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HuntProgressTracker.
     */
    // TODO: Rename and change types and number of parameters
    public static HuntProgressTracker newInstance(Hunt hunt) {
        HuntProgressTracker fragment = new HuntProgressTracker();
        Bundle args = new Bundle();
        args.putSerializable(HUNT_ARG, hunt);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mHunt = (Hunt) getArguments().getSerializable(HUNT_ARG);
        }

        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.hunt_progress_tracker, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.vertical_species_image_gallery);

        // Set info button to show more info about hunt
        moreInfoDialogFrag  = MoreInfoDialogFrag.getInstance(mHunt);
        ImageButton infoButton = (ImageButton) view.findViewById(R.id.more_info_prog_frag_btn);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager()
                        .beginTransaction();
                ft.add(moreInfoDialogFrag, "info dialog")
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Set back arrow to return to home screen
        ImageButton backButton = (ImageButton) view.findViewById(R.id.back_arrow_prog_frag);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // Set data fields based on hunt
        if (mHunt != null){
            // Set title
            TextView title = (TextView) view.findViewById(R.id.hunt_title_prog_frag);
            title.setText(mHunt.name());
        }
        models = getData();
        adapter = new VerticalSpeciesRecyclerAdapter(getContext(), models);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        return view;
    }

    private ArrayList getData(){
        if (mHunt == null || mHunt.speciesList() == null || mHunt.speciesList().isEmpty()){
            ArrayList speciesList = new ArrayList<>();
            for (int i = 0; i < stock_flower_ids.length; i++){
                SpeciesRecyclerItemModel model = new SpeciesRecyclerItemModel();
                model.setImage_drawable(stock_flower_ids[i]);
                model.setName(stock_flower_names[i]);
                speciesList.add(model);
            }
            return speciesList;
        }

        return mHunt.speciesList();
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
