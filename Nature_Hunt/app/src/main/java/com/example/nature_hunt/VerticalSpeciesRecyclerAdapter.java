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

public class VerticalSpeciesRecyclerAdapter extends RecyclerView.Adapter<VerticalSpeciesRecyclerAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<SpeciesRecyclerItemModel> speciesList;


    public VerticalSpeciesRecyclerAdapter(Context context, ArrayList<SpeciesRecyclerItemModel> models){
        inflater = LayoutInflater.from(context);
        this.speciesList = models;
    }

    @NonNull
    @Override
    public VerticalSpeciesRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.vertical_recycler_item,viewGroup, false );
        ViewHolder holder = new ViewHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(speciesList.get(position).getImage_drawable());
        holder.textView.setText(speciesList.get(position).getName());
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
            textView = (TextView) v.findViewById(R.id.vertical_text_view);
            imageView = (ImageView) v.findViewById(R.id.vertical_image_view);
        }
    }
}
