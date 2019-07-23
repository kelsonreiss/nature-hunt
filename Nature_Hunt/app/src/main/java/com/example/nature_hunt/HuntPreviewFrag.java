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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HuntPreviewFrag extends DialogFragment {

    private HuntPreviewViewModel mViewModel;
    private SpeciesRecyclerAdapter adapter;
    private ArrayList<SpeciesRecyclerItemModel> models;
    GridView gridView;
    private ImageView imageView;
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    private Hunt mHunt;

    Integer[] imageIDs = {
            R.mipmap.stock_trail,
            R.mipmap.stock_trail,
            R.mipmap.stock_trail
    };

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
        } catch (Exception e){
            e.printStackTrace();
        }

        if (mHunt != null){
            // Set title data
            TextView title = (TextView) view.findViewById(R.id.hunt_title);
            title.setText(mHunt.name());
        }
        
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.dismiss_preview_button);

        // Made dismiss button transparent
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



//        gridView = (GridView) view.findViewById(R.id.hunt_preview_image_grid);
//        gridView.setAdapter(new ImageAdapterGridView(getActivity()));
//        Toolbar toolbar = view.findViewById(R.id.hunt_preview_toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_home_black_24dp);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });

        return view;
    }

    private ArrayList getData(){

        ArrayList speciesList = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            SpeciesRecyclerItemModel model = new SpeciesRecyclerItemModel();
            model.setImage_drawable(R.drawable.flower);
            model.setName("Flower");
            speciesList.add(model);
        }
        return speciesList;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HuntPreviewViewModel.class);
        // TODO: Use the ViewModel
    }

    public class ImageAdapterGridView extends BaseAdapter{
        private Context mContext;

        public ImageAdapterGridView(Context c){
            mContext = c;
        }

        @Override
        public int getCount() {
            return imageIDs.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView mImageView;

            if (convertView == null) {
                mImageView = new ImageView(mContext);
                mImageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

//                mImageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mImageView.setPadding(16, 16, 16, 16);
            } else {
                mImageView = (ImageView) convertView;
            }
            mImageView.setImageResource(imageIDs[position]);
            return mImageView;
        }
    }
}
