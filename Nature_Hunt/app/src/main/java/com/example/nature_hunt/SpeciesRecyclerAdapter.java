package com.example.nature_hunt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SpeciesRecyclerAdapter extends RecyclerView.Adapter<SpeciesRecyclerAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<SpeciesRecyclerItemModel> speciesList;


    public SpeciesRecyclerAdapter(Context context, ArrayList<SpeciesRecyclerItemModel> models){
        inflater = LayoutInflater.from(context);
        this.speciesList = models;
    }

    @NonNull
    @Override
    public SpeciesRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.horizontal_recycler_item,viewGroup, false );
        ViewHolder holder = new ViewHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.imageView.setImageResource(speciesList.get(i).getImage_drawable());
        viewHolder.textView.setText(speciesList.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return speciesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;

        public ViewHolder(View v){
            super(v);
            textView = (TextView) v.findViewById(R.id.horizontal_text_view);
            imageView = (ImageView) v.findViewById(R.id.horizontal_image_view);
        }
    }
}
