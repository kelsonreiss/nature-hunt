package com.example.nature_hunt;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Image gallery to display list of species
 * https://javapapers.com/android/android-image-gallery-example-app-using-glide-library/
 */
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private ArrayList<ImageUrl> imageUrls;
    private Context context;

    public DataAdapter(Context context, ArrayList<ImageUrl> imageUrls){
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    /**
     * gets the image url from adapter and passes to Glide API to load the image
     *
     * @param viewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Glide.with(context)
                .load(R.mipmap.stock_trail)
                .into(viewHolder.img);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;

        public ViewHolder(View v){
            super(v);
            img = v.findViewById(R.id.species_preview_image);
        }
    }
}
