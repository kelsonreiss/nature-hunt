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

public class HuntRecyclerAdapter extends RecyclerView.Adapter<HuntRecyclerAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<HuntRecyclerItemModel> huntList;

    public HuntRecyclerAdapter(Context context, ArrayList<HuntRecyclerItemModel> models){
        inflater = LayoutInflater.from(context);
        this.huntList = models;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.horizontal_recycler_item, parent, false );
        HuntRecyclerAdapter.ViewHolder holder = new HuntRecyclerAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(huntList.get(position).getImage_drawable());
        holder.textView.setText(huntList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return huntList.size();
    }

    public HuntRecyclerItemModel getItem(int position) {
        return huntList.get(position);
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
