package com.example.nature_hunt;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class HuntPreviewFrag extends DialogFragment {

    private HuntPreviewViewModel mViewModel;
    GridView gridView;
    private ImageView imageView;
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;

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
        imageView = (ImageView) view.findViewById(R.id.species_preview_image);
        recyclerView = (RecyclerView) view.findViewById(R.id.species_image_gallery);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        ArrayList imageUrls = getData();
        DataAdapter dataAdapter = new DataAdapter(getContext(), imageUrls);
        recyclerView.setAdapter(dataAdapter);


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
        String urlString[] = {
                "url1",
                "url2",
                "url3"
        };
        ArrayList imageUrlList = new ArrayList<>();
        for (int i = 0; i < urlString.length; i++){
            ImageUrl imageUrl = new ImageUrl();
            imageUrl.setImageUrl(urlString[i]);
            imageUrlList.add(imageUrl);
        }
        return imageUrlList;
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
